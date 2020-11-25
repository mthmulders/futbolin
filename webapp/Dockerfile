FROM openliberty/open-liberty:20.0.0.12-kernel-slim-java11-openj9

COPY src/main/liberty/config/server.xml /opt/ol/wlp/usr/servers/defaultServer/server.xml
RUN configure.sh
COPY target/keystore.p12 /tmp/keystore.p12
COPY target/futbolin-webapp.war /opt/ol/wlp/usr/servers/defaultServer/apps/futbolin-webapp.war
