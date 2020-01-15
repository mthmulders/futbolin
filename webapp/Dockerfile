FROM open-liberty:19.0.0.9-javaee8-java11

ARG WAR_FILE

COPY src/main/docker/server.xml /opt/ol/wlp/usr/servers/defaultServer/server.xml
COPY target/${WAR_FILE} /tmp/app.war