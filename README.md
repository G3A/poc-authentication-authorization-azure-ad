

# PoC Autenticación y Autorización con Spring Boot Y Microsoft Entra ID para backend to backend


https://learn.microsoft.com/en-us/azure/active-directory-b2c/client-credentials-grant-flow?pivots=b2c-user-flow

1. Crear Aplicación

Nombre: app-apirest

click en registrar

2. Exponer una API

modificaremos el valor original que aparece al dar click en agregar desde: URI de id. de aplicación Agregar

por:

URI de id. de aplicación: api://app-apirest


click en guardar

3. Roles de Aplicación

Creemos un par de roles

Click en: Crear rol de aplicación

Role1:
Display name: Read User
Seleccionar: Applications
Value: readuser
Description: Read User Details

Click en Aplicar

Role2:
Display name: Update User
Seleccionar: Applications
Value: updateuser
Description: Update User Details

Click en Aplicar

4. Manifiesto

Para verificar los roles creados.

5. Registrar una aplicación cliente

Desde la miga de pan, dar click en Registros de aplicaciones o Click en: Registrar una aplicación

Click en: Nuevo registro

Nombre: app-cliente

click en: Registrar

6. Modificar el manifest

cambiar el valor de:

accessTokenAcceptedVersion: null
por
accessTokenAcceptedVersion: 2

click en Guardar


7. Permisos de API

Click en: Permisos de API
Click en: Agregar un permiso
Click en: API usadas en mi organización
Seleccionar: app-apirest (creada en el punto 1)
Seleccionar: readuser y updateuser
click en: Agregar permisos
Click en: Conceder consentimiento de administrador para [reemplazar por organización] S.A.Seleccionar



8. Obtener un token de acceso
   19:13 video-> https://www.youtube.com/watch?v=hiBeW4-hrfE

https://login.microsoftonline.com/<tenant-name>/oauth2/v2.0/token

Reemplazar tenant-name con el nombre de la subscripción al hacer click en el piñon dentado, directorio y copiar el valor de la columna dominio para poder reemplazar este valor.

quedando de la siguiente forma:

https://login.microsoftonline.com/[reemplazar por dominio]/oauth2/v2.0/token

Seleccionar Body

Seleccionar x-www-form-urlencoded


especificar estos parámetros

Key	Value
grant_type	client_credentials
client_id	The Client ID from the Step 2 Register an application.
client_secret	The Client secret value from Step 2.1 Create a client secret.
scope	The Application ID URI from Step 1.1 Define web API roles (scopes) and .default. For example https://contoso.onmicrosoft.com/api/.default, or https://contoso.onmicrosoft.com/00001111-aaaa-2222-bbbb-3333cccc4444/.default.


Para el valor del scope: ingresar a Microsoft Entra ID

Click en: Registro de Aplicaciones

Seleccionar la pestaña: Todas las aplicaciones

Buscar por el nombre: app-apirest

Click en: Exponer una API

Copiar: URI de id. de aplicación
api://app-apirest


Agreguele al final de la url: /.default

quedando de la siguiente forma

scope: api://app-apirest/.default



Para el valor de client_secret: ingresar a Microsoft Entra ID

Click en: Registro de Aplicaciones

Seleccionar la pestaña: Todas las aplicaciones

Buscar por el nombre: app-cliente

Click en certificados y secretos

Click en: Nuevo secreto de cliente

Nombrarlo con: v1

click en: Agregar

copiar el valor de la columna valor y pegarlo en el valor del atributo client_secret del postman






Para el valor de client_id: ingresar a Microsoft Entra ID

Click en: Información general

Copiar el valor de: Id. de aplicación (Cliente) y pegarlo en el valor del atributo client_id del postman


9. Consumir el token

POST /[reemplazar por dominio]/oauth2/v2.0/token HTTP/1.1
Host: login.microsoftonline.com
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials&client_id=<reemplazar>&client_secret=<reemplazar>&scope=<reemplazar>

10. Poner las siguientes propiedades en el proyecto Spring Boot

spring:
cloud:
azure:
active-directory:
profile:
tenant-id: <your-Microsoft-Entra-tenant-ID>
credential:
client-id: <your-application-ID-of-app-apirest>
app-id-uri: <your-application-ID-URI-of-app-apirest>




11. Marcar los métodos con @PreAuthorize


@PreAuthorize("hasAuthority('APPROLE_readuser')")

y

@PreAuthorize("hasAuthority('APPROLE_updateuser')")

12. Probar el consumo utilizando el token generado del punto 9

GET /user HTTP/1.1
Host: localhost:8080
Authorization: Bearer <reemplazar>


Debe arrojar: Access Granted : Read User Details	
