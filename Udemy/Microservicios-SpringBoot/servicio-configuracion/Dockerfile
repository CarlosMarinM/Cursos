FROM openjdk:11
VOLUME /tmp
EXPOSE 8888
ADD ./build/libs/servicio-configuracion-0.0.1-SNAPSHOT.jar config-server.jar
ENTRYPOINT ["java","-jar","/config-server.jar"]