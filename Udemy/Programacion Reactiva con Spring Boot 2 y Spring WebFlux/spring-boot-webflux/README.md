# Iniciar MongoDB con Docker

1. Descargar la imagen de mongo y correrla en el puerto 27017:

    ```bash
    docker run -d -p 27017:27017 --name mydatabase mongo:latest
    ```

2. Entrar a al contenedor ejecutado de MongoDB:

    ```bash
    docker exec -it mydatabase mongosh
    ```

3. Crear la bbase de datos web-flux:

    ```bash
    use web-flux
    ```