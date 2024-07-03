package org.onap.sdnc.apps.ms.sanitytest.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.onap.ccsdk.apps.services.RestException;
import org.onap.ccsdk.apps.services.SvcLogicFactory;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
import org.onap.ccsdk.sli.core.sli.SvcLogicLoader;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicServiceBase;
import org.onap.sdnc.apps.ms.sanitytest.swagger.OperationsApi;
import org.onap.sdnc.apps.ms.sanitytest.swagger.model.SanitytestApiInputBodyparam;
import org.onap.sdnc.apps.ms.sanitytest.swagger.model.SanitytestApiRequestInformation;
import org.onap.sdnc.apps.ms.sanitytest.swagger.model.SanitytestApiResponseField;
import org.onap.sdnc.apps.ms.sanitytest.swagger.model.SanitytestApiResponseFields;
import org.onap.sdnc.apps.ms.sanitytest.swagger.model.SanitytestApiResultEnumeration;
import org.onap.sdnc.apps.ms.sanitytest.swagger.model.SanitytestApiServiceConfigurationOperation;
import org.onap.sdnc.apps.ms.sanitytest.swagger.model.SanitytestApiTestListItem;
import org.onap.sdnc.apps.ms.sanitytest.swagger.model.SanitytestApiTestNameEnumeration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@ComponentScan(basePackages = { "org.onap.sdnc.apps.ms.sanitytest.*", "org.onap.ccsdk.apps.services" })
@Import(value = SvcLogicFactory.class)
public class SanityTestOperationsApi implements OperationsApi 
{

    private static final String MODULE_NAME = "SANITYTEST-API";

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    protected SvcLogicServiceBase svc;

    @Autowired
    protected SvcLogicLoader svcLogicLoader;

    private static class Iso8601Util {

        private static TimeZone timeZone = TimeZone.getTimeZone("UTC");
        private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        private Iso8601Util() {
        }

        static {
            dateFormat.setTimeZone(timeZone);
        }

        private static String now() {
            return dateFormat.format(new Date());
        }
    }

    @org.springframework.beans.factory.annotation.Autowired
    public SanityTestOperationsApi(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }


    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<SanitytestApiServiceConfigurationOperation> operationsSANITYTESTAPIserviceConfigurationOperationPost(
            @Valid SanitytestApiInputBodyparam input) throws RestException {
        final String svcOperation = "service-configuration-operation";
        SanitytestApiRequestInformation reqInfo = input.getInput().getRequestInformation();
        List<SanitytestApiTestListItem> testList = reqInfo.getTestList();
        SanitytestApiServiceConfigurationOperation retval = new SanitytestApiServiceConfigurationOperation();
        SanitytestApiResponseFields resp = new SanitytestApiResponseFields();


        SvcLogicContext ctxIn = new SvcLogicContext();


        // Add input to SvcLogicContext
        try {
            ctxIn.mergeJson(svcOperation + "-input", objectMapper.writeValueAsString(input.getInput()));
        } catch (JsonProcessingException e) {
            log.error("Caught exception trying to save input to SvcLogicContext", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Call DG
        try {
            // Any of these can throw a nullpointer exception
            // execute should only throw a SvcLogicException
            SvcLogicContext ctxOut = svc.execute(MODULE_NAME, svcOperation, null, "sync", ctxIn);
            Properties respProps = ctxOut.toProperties();

            String errCode = respProps.getProperty("error-code", "200");

            if ("200".equals(errCode)) {
                // DG returns success - return test results.


                int testcount = Integer.parseInt(respProps.getProperty("service-configuration-operation-output.response-information.response-test-list_length"));
                for (int i = 0; i < testcount; i++) {
                    SanitytestApiResponseField respItem = new SanitytestApiResponseField();
                    SanitytestApiTestListItem testItem = testList.get(i);
                    respItem.setResponseTestNumber(testItem.getTestNumber());
                    String testName = respProps.getProperty("service-configuration-operation-output.response-information.response-test-list["
					    + i + "].response-test-name");
                    respItem.setResponseTestName(SanitytestApiTestNameEnumeration.fromValue(testName));
                    respItem.setStartTime(respProps.getProperty("service-configuration-operation-output.response-information.response-test-list["
                        + i + "].start-time"));
                    respItem.setEndTime(respProps.getProperty("service-configuration-operation-output.response-information.response-test-list["
                        + i + "].end-time"));
                    respItem.setElapsedTime(respProps.getProperty("service-configuration-operation-output.response-information.response-test-list["
                            + i + "].elapsed-time"));
                    respItem.setErrorMessage(respProps.getProperty("service-configuration-operation-output.response-information.response-test-list["
                            + i + "].error-message"));
                    respItem.setResult(SanitytestApiResultEnumeration.fromValue(respProps.getProperty("service-configuration-operation-output.response-information.response-test-list["
                            + i + "].result")));
                    resp.addResponseInformationItem(respItem);
                }
                log.info("Returned SUCCESS for " + svcOperation);
                retval.setOutput(resp);
                return new ResponseEntity<> (retval, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (NullPointerException npe) {
            log.error("Caught NPE", npe);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SvcLogicException e) {
            log.error("Caught SvcLogicException", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    
}
