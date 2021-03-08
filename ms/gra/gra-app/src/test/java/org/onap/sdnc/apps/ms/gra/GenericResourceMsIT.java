package org.onap.sdnc.apps.ms.gra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

public class GenericResourceMsIT {
    @Test
    public void healthcheckTest() throws ClientProtocolException, IOException {
        String graPort = System.getenv("GRA_PORT");
        if ((graPort == null) || (graPort.length() == 0)) {
            graPort = "8080";
        }

        String testUrl = "http://localhost:" + graPort + "/restconf/operations/SLI-API:healthcheck/";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postCmd = new HttpPost(testUrl);
        postCmd.addHeader("Content-Type", "application/json");
        
        CloseableHttpResponse resp = client.execute(postCmd);
        assertEquals(200, resp.getStatusLine().getStatusCode());
    }
}
