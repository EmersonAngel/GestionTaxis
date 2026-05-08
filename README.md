# Gestion de Taxis - Parcial 2

Aplicacion de microservicios con autenticacion JWT, roles `ADMIN` y `USER`, dos servicios funcionales (`taxi-service` y `trip-service`), dos motores de base de datos (MySQL y PostgreSQL), frontend Angular y contenedorizacion completa.

## Servicios

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

## Requisitos cubiertos

- API REST con microservicios independientes.
- JWT con registro, login y roles.
- Dos servicios funcionales del tema Gestion de Taxis.
- Persistencia en MySQL y PostgreSQL.
- Arquitectura por capas: Controller, Service, Repository, Model y DTO.
- Dockerfile por microservicio backend y frontend.
- `docker-compose.yml` para levantar backend, frontend y bases de datos.
