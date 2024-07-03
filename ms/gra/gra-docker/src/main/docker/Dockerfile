# Prepare stage for multistage image build
## START OF STAGE0 ##
FROM @base.image.name@:@base.image.version@ AS stage0


ENV LOG_PATH /var/log/onap/sdnc
ENV SDNC_CONFIG_DIR /opt/sdnc/gra/config
ENV TRUSTSTORE @truststore.file@
ENV JAVA_SECURITY_DIR /opt/java/openjdk/lib/security
USER root

# copy deliverables to opt
COPY opt /opt

# Install  certificates
RUN keytool -importkeystore -srckeystore ${SDNC_CONFIG_DIR}/${TRUSTSTORE} -srcstorepass changeit -destkeystore ${JAVA_SECURITY_DIR}/cacerts -deststorepass changeit -noprompt

# Copy any certs
COPY *.md *.pem /etc/ssl/certs/

# Install certs
RUN update-ca-certificates
## END OF STAGE0 ##


FROM @base.image.name@:@base.image.version@

MAINTAINER SDNC Team (onap-sdnc@lists.onap.org)

ENV PROJECT_VERSION @project.version@
ENV SDNC_HOME /opt/sdnc/gra
ENV SDNC_CONFIG_DIR /opt/sdnc/gra/config
ENV GRA_JAR @sdnc.gra.jar@
ENV SDNC_UID @sdnc.uid@
ENV SVCLOGIC_PROPERTIES /opt/sdnc/gra/config/svclogic.properties
ENV SVCLOGIC_DIR /opt/sdnc/gra/service-logic/graphs
ENV LOG_PATH /var/log/onap/sdnc

USER root


RUN echo "http_proxy = $http_proxy" && echo "https_proxy = $https_proxy" && echo "no_proxy = $no_proxy" &&  echo "HTTP_PROXY = $HTTP_PROXY" &&  echo "HTTPS_PROXY = $HTTPS_PROXY" && echo "NO_PROXY = $NO_PROXY"

# Install sudo,  IP utilities and openjdk 11
RUN apk update && apk --no-cache add bash curl mysql-client sudo iputils openssl 

# Enable wheel group
RUN sed -e 's/# %wheel ALL=(ALL) NOPASSWD: ALL/%wheel ALL=(ALL) NOPASSWD: ALL/g' -i /etc/sudoers

# Create sdnc user
RUN addgroup -g ${SDNC_UID} -S sdnc && adduser -u ${SDNC_UID} -G sdnc -S sdnc
RUN addgroup sdnc wheel

COPY --from=stage0 --chown=sdnc:sdnc /opt /opt
RUN mkdir -p ${LOG_PATH} && chown sdnc:sdnc ${LOG_PATH}
RUN chmod +x ${SDNC_HOME}/bin/*.sh


USER sdnc
WORKDIR ${SDNC_HOME}
ENTRYPOINT /opt/sdnc/gra/bin/startGra.sh
EXPOSE 8443 8080

