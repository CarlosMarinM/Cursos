FROM openjdk:11
VOLUME /tmp
ADD ./build/libs/servicio-productos-0.0.1-SNAPSHOT.jar product-server.jar
CMD sleep 30 && java -jar product-server.jar