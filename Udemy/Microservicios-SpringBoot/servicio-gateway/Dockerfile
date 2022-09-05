FROM openjdk:11
VOLUME /tmp
EXPOSE 8090
ADD ./build/libs/servicio-gateway-0.0.1-SNAPSHOT.jar gateway-server.jar
CMD sleep 40 && java -jar gateway-server.jar