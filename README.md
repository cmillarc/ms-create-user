# Documentación del Proyecto Create User

Este documento detalla cómo compilar y ejecutar el proyecto localmente con base de datos H2.

## Requisitos
- Java 17
- Maven 3.8.6

## Pasos para la compilación y ejecución

### 1-Clonar el repositorio
Usa el comando git para clonar el repositorio a tu máquina local.

```sh
git clone https://github.com/cmillarc/prueba-usuarios.git
```

### 2-Compilar el proyecto
Navega hasta el directorio del proyecto y utiliza Maven para compilarlo.

```sh
cd .../ms-create-user
mvn clean install
```

### 3-Ejecutar la aplicación
Usa el siguiente comando para ejecutar la aplicación:
```sh
mvn spring-boot:run
```
## Documentación de la API
La documentación de la API está disponible en http://localhost:8080/swagger-ui.html.

### Creación de usuarios
Para crear un usuario, se puede utilizar el siguiente ejemplo en el endpoint de creación de usuarios en Swagger 
(POST /users):

```sh
{
    "name": "Juan Rodriguez",
    "email": "juanro@driguz.org",
    "password": "hunter2",
    "phones": [
        {
        "number": "1234567",
        "cityCode": "1",
        "countryCode": "57"
        }
    ]
}
```

### Error controlado

Se valida que el correo no exista, devuelve el siguiente error.

```sh
{
"email": "El correo juanro@driguz.org ya está registrado"
}
```

### Ejemplos de validaciones

Valida datos de entrada, en caso de enviar todo vacio devuelve el siguiente error Code 400 Bad Request.

```sh
{
  "password": "Password is mandatory",
  "phones[0].cityCode": "no debe estar vacío",
  "phones[0].number": "no debe estar vacío",
  "name": "Name must be between 3 and 20 characters",
  "phones[0].countryCode": "no debe estar vacío",
  "email": "no debe estar vacío"
}
```
en caso de enviar una contraseña o correo que no cumpla con el patrón de validación, devuelve el siguiente error.
```sh
{
  "password": "Password must contain at least one lowercase letter and one number",
  "email": "Email should be valid"
}
```
