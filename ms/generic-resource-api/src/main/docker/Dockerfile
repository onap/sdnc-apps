# Prepare stage for multistage image build
## START OF STAGE0 ##
FROM @base.image.name@:@ccsdk.docker.version@ AS stage0

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV LOG_PATH /var/log/onap/sdnc


# copy deliverables to opt
COPY opt /opt

## END OF STAGE0 ##


FROM @base.image.name@:@ccsdk.docker.version@

MAINTAINER SDNC Team (onap-sdnc@lists.onap.org)

ENV PROJECT_VERSION @project.version@
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV SDNC_HOME /opt/onap/sdnc
ENV SDNC_CONFIG_DIR /opt/onap/sdnc/config
ENV GRA_JAR @sdnc.gra.jar@
ENV SVCLOGIC_PROPERTIES /opt/onap/sdnc/config/svclogic.properties
ENV SVCLOGIC_DIR /opt/onap/sdnc/svclogic/graphs
ENV LOG_PATH /var/log/onap/sdnc

# Install sudo and IP utilities
RUN apk update && apk --no-cache add sudo iputils openssl


# Enable wheel group
RUN sed -e 's/# %wheel ALL=(ALL) NOPASSWD: ALL/%wheel ALL=(ALL) NOPASSWD: ALL/g' -i /etc/sudoers

# Create sdnc user
RUN addgroup -S sdnc && adduser -S sdnc -G sdnc
RUN addgroup sdnc wheel

COPY --from=stage0 --chown=sdnc:sdnc /opt /opt
RUN mkdir -p ${LOG_PATH} && chown sdnc:sdnc ${LOG_PATH}
RUN chmod +x ${SDNC_HOME}/bin/*.sh


USER sdnc
WORKDIR ${SDNC_HOME}
ENTRYPOINT /opt/onap/sdnc/bin/startGra.sh
EXPOSE 8443 8080
