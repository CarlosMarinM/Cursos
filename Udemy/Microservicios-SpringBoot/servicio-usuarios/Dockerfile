FROM openjdk:11
VOLUME /tmp
ADD ./build/libs/servicio-usuarios-0.0.1-SNAPSHOT.jar user-server.jar
CMD sleep 30 && java -jar user-server.jar