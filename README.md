# Gestion de Taxis - Parcial 2

Aplicacion de microservicios con autenticacion JWT, roles `ADMIN` y `USER`, dos servicios funcionales (`taxi-service` y `trip-service`), dos motores de base de datos (MySQL y PostgreSQL), frontend Angular y contenedorizacion completa.

## Servicios

- `api-gateway` (`8080`): punto unico de entrada para el backend.
- `auth-service` (`8081`): registro, login y perfil con JWT. Usa PostgreSQL.
- `taxi-service` (`8082`): CRUD de taxis. Usa MySQL.
- `trip-service` (`8083`): CRUD de carreras/viajes. Usa PostgreSQL.
- `frontend` (`4300`): cliente Angular con tema Gestion de Taxis.

## Formato modular

Los microservicios backend tambien estan declarados como modulos Maven:

```text
gestion-taxis
└── backend
    ├── auth-service
    ├── api-gateway
    ├── taxi-service
    └── trip-service
```

Puedes compilar todos los microservicios juntos desde la raiz con:

```bash
mvn -q -DskipTests package
```

O desde `backend` con:

```bash
mvn -q -DskipTests package
```

## Ejecutar con Docker

```bash
docker compose up --build
```

Frontend: http://localhost:4300

Usuario administrador inicial:

- Email: `admin@taxis.com`
- Password: `admin123`

Tambien puedes registrar usuarios nuevos desde la interfaz.

## Despliegue gratuito del backend

El archivo `render.yaml` permite crear el backend como Blueprint gratuito:

- `taxis-api-gateway`: Web Service Free en Render.
- `taxis-auth-service`: Web Service Free en Render.
- `taxis-taxi-service`: Web Service Free en Render.
- `taxis-trip-service`: Web Service Free en Render.
- `taxis-postgres-db`: PostgreSQL Free en Render.
- MySQL externo gratuito en Aiven Free Tier.

### MySQL gratis recomendado

Render no ofrece MySQL administrado gratuito. La opcion gratuita recomendada es Aiven for MySQL Free Tier:

- Gratis sin tarjeta.
- 1 GB de almacenamiento.
- Sin limite de 30 dias.
- MySQL administrado.

Pasos para crear MySQL:

1. Crear cuenta en Aiven.
2. Crear servicio `MySQL`.
3. Elegir plan `Free`.
4. Copiar los datos de conexion:
   - host
   - port
   - database
   - user
   - password

Cuando Render cree el Blueprint, pedira estas variables del `taxi-service`:

```text
MYSQL_HOST
MYSQL_PORT
MYSQL_DATABASE
MYSQL_USER
MYSQL_PASSWORD
```

Para Aiven, `MYSQL_SSL_MODE` ya queda configurado como `REQUIRED`.

Pasos:

1. Entrar a Render.
2. Crear un nuevo Blueprint.
3. Seleccionar el repositorio `EmersonAngel/GestionTaxis`.
4. Completar las variables `MYSQL_*` con los datos de Aiven.
5. Revisar que todos los Web Services esten en plan `Free`.
6. Aplicar el Blueprint.

Endpoint principal del backend:

```text
https://taxis-api-gateway.onrender.com
```

Rutas por el Gateway:

```text
/api/auth/**
/api/taxis/**
/api/trips/**
/health
```

Importante: Render Free Postgres expira a los 30 dias y solo permite una base Free activa por workspace. Para una entrega academica funciona, pero no debe usarse como produccion.

## Despliegue gratuito del frontend

El frontend queda preparado como Static Site Free en Render dentro del mismo `render.yaml`:

- Servicio: `taxis-frontend`
- Build command: `cd frontend && npm install && npm run build`
- Publish directory: `./frontend/dist/gestion-taxis-frontend/browser`
- Rewrite SPA: `/* -> /index.html`

La aplicacion Angular consume el backend mediante el API Gateway:

```text
https://taxis-api-gateway.onrender.com
```

En local usa automaticamente:

```text
http://localhost:8080
```

Si Render genera otra URL para el gateway, puedes abrir la consola del navegador y guardar una URL distinta:

```js
localStorage.setItem('apiBaseUrl', 'https://URL-REAL-DEL-GATEWAY.onrender.com')
```

Luego recarga la pagina.

## Requisitos cubiertos

- API REST con microservicios independientes.
- JWT con registro, login y roles.
- Dos servicios funcionales del tema Gestion de Taxis.
- Persistencia en MySQL y PostgreSQL.
- Arquitectura por capas: Controller, Service, Repository, Model y DTO.
- Dockerfile por microservicio backend y frontend.
- `docker-compose.yml` para levantar backend, frontend y bases de datos.
