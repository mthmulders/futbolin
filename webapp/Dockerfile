FROM openliberty/open-liberty:20.0.0.6-kernel-java11-openj9-ubi

COPY src/main/liberty/config/server.xml /opt/ol/wlp/usr/servers/defaultServer/server.xml
RUN configure.sh
COPY target/keystore.p12 /tmp/keystore.p12
COPY target/futbolin-webapp.war /opt/ol/wlp/usr/servers/defaultServer/apps/futbolin-webapp.war
