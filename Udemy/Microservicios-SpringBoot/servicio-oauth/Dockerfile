FROM openjdk:11
VOLUME /tmp
EXPOSE 9100
ADD ./build/libs/servicio-oauth-0.0.1-SNAPSHOT.jar oauth-server.jar
CMD sleep 30 && java -jar oauth-server.jar