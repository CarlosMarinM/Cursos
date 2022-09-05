FROM openjdk:11
VOLUME /tmp
EXPOSE 8002
ADD ./build/libs/servicio-item-0.0.1-SNAPSHOT.jar item-server.jar
CMD sleep 30 && java -jar item-server.jar