# E-Commerce Backend API

API RESTful para un sistema de e-commerce desarrollada con Spring Boot, que incluye gestión de productos, carritos de compra, órdenes, pagos, autenticación y autorización basada en roles y permisos.

## Postman collection: 
https://www.postman.com/abrilsar03/workspace/my-workspace/collection/39717677-4fd1b35d-f4ca-41a3-a99d-5ff50e602a62?action=share&creator=39717677&active-environment=39717677-45fb816e-ea12-4a47-8088-a28b37d21228

## Hosted
- On render Api y Postgres

## Heath Check: https://backend-java-ecommerce.onrender.com/api/v1/healt-check/ping

## 📋 Tabla de Contenidos

- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitectura](#-arquitectura)
- [Componentes del Sistema](#-componentes-del-sistema)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración Local](#-instalación-y-configuración-local)
- [Despliegue en GCP](#-despliegue-en-gcp)
- [Ejecución de Pruebas](#-ejecución-de-pruebas)
- [Uso de la API](#-uso-de-la-api)
- [Estructura del Proyecto](#-estructura-del-proyecto)

## 🛠 Stack Tecnológico

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.4.5** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Spring Boot Actuator** - Monitoreo y métricas
- **JWT (JJWT 0.12.6)** - Tokens de autenticación
- **Flyway** - Migraciones de base de datos
- **Lombok** - Reducción de boilerplate

### Base de Datos
- **PostgreSQL 18** - Base de datos relacional

### Herramientas de Desarrollo
- **Gradle 8.14.3** - Gestión de dependencias y build
- **JUnit 5** - Framework de testing
- **Mockito** - Mocking para tests
- **Docker & Docker Compose** - Containerización

### Infraestructura
- **Docker** - Containerización
- **Google Cloud Platform (GCP)** - Despliegue en la nube

## 🏗 Arquitectura

### Patrón Arquitectónico
El sistema sigue una **arquitectura en capas (Layered Architecture)** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────┐
│         Controllers Layer                │  ← REST API Endpoints
├─────────────────────────────────────────┤
│         Services Layer                   │  ← Lógica de Negocio
├─────────────────────────────────────────┤
│         Repositories Layer               │  ← Acceso a Datos
├─────────────────────────────────────────┤
│         Entities Layer                   │  ← Modelo de Dominio
└─────────────────────────────────────────┘
```

### Flujo de Petición

1. **Request** → Filtros de seguridad (API Key / JWT)
2. **Controller** → Valida entrada y delega a Service
3. **Service** → Ejecuta lógica de negocio y valida reglas
4. **Repository** → Accede a la base de datos
5. **Response** → DTO transformado y retornado

### Seguridad

El sistema implementa **autenticación dual**:

1. **API Key Authentication** - Para servicios externos (tokenización)
2. **JWT Authentication** - Para usuarios (clientes y administradores)

**Autorización basada en roles y permisos:**
- Roles: `ADMIN`, `CLIENT`
- Permisos granulares: `PRODUCT:READ`, `PRODUCT:CREATE`, `PRODUCT:UPDATE`, etc.

## 🧩 Componentes del Sistema

### 1. **Módulo de Autenticación y Autorización**
- **AuthService**: Registro y login de usuarios
- **JwtAuthFilter**: Filtro para validar tokens JWT
- **ApiKeyAuth**: Filtro para validar API keys
- **AuthorityUtils**: Conversión de roles y permisos a autoridades Spring Security

### 2. **Módulo de Productos**
- **ProductService**: Gestión de productos (CRUD, búsqueda)
- **ProductSpecifications**: Especificaciones JPA para filtros avanzados
- **Filtros**: Por nombre, SKU, precio, stock, estado activo
- **Visibilidad**: Restricción por stock mínimo configurable

### 3. **Módulo de Carrito de Compras**
- **CartService**: Gestión de carritos activos
- **Validaciones**: Stock disponible, productos activos
- **Cálculo de totales**: Subtotal, impuestos, total

### 4. **Módulo de Órdenes y Pagos**
- **OrderService**: Creación y gestión de órdenes
- **Proceso de pago**: Simulación con reintentos configurables
- **Estados**: PENDING_PAYMENT, PAID, FAILED, CANCELLED
- **Validaciones**: Estado de orden antes de pagar

### 5. **Módulo de Tokenización**
- **TokenizationService**: Tokenización segura de tarjetas
- **Fingerprint HMAC**: Identificación única de tarjetas
- **Validación Luhn**: Verificación de números de tarjeta
- **Detección de marca**: VISA, Mastercard, AMEX

### 6. **Módulo de Logging y Auditoría**
- **EventLogService**: Logging asíncrono de eventos del sistema
- **SearchLogService**: Registro de búsquedas de productos
- **Niveles**: INFO, WARN, ERROR
- **Eventos**: Creación de productos, búsquedas, pagos, etc.

### 7. **Módulo de Configuración del Sistema**
- **SystemParamService**: Gestión de parámetros configurables
- **Parámetros**: Stock mínimo, probabilidades, reintentos
- **Tipos**: String, Int, Double, Boolean

### 8. **Módulo de Usuarios**
- **UserService**: Gestión de perfiles de usuario
- **Roles y Permisos**: Sistema RBAC (Role-Based Access Control)

## 📦 Requisitos Previos

- **Java 21** o superior
- **Gradle 8.14.3** o superior (incluido en el proyecto)
- **PostgreSQL 18** o superior
- **Docker** y **Docker Compose** (opcional, para desarrollo local)
- **Git** para clonar el repositorio

## 🚀 Instalación y Configuración Local

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd Backend-Java-Ecommerce
```

### 2. Configurar Variables de Entorno

Crear un archivo `.env` en la raíz del proyecto con las siguientes variables:

```env
# Base de Datos
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_NAME=ecommerce_db
DATABASE_USER=postgres
DATABASE_PASSWORD=postgres

# JWT
JWT_SECRET=tu-secret-key-super-segura-minimo-256-bits
jwt.expires=10800

# Security
SECURITY_USER_NAME=admin
SECURITY_USER_PASSWORD=admin123

# CORS
APP_CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173

# Tokenization
FINGERPRINT-SECRET=tu-fingerprint-secret-key-super-segura
```

### 3. Configurar Base de Datos

#### Opción A: Usando Docker Compose

```bash
docker-compose up -d postgres
```

#### Opción B: PostgreSQL Local

```bash

createdb ecommerce_db

psql -U postgres
CREATE DATABASE ecommerce_db;
```

### 4. Ejecutar Migraciones

Las migraciones de Flyway se ejecutan automáticamente al iniciar la aplicación. Si necesitas ejecutarlas manualmente:

```bash
./gradlew flywayMigrate
```

### 5. Compilar y Ejecutar

```bash
# Compilar
./gradlew build

# Ejecutar
./gradlew bootRun
```

O usando el JAR:

```bash
./gradlew bootJar
java -jar build/libs/*.jar
```

La aplicación estará disponible en: `http://localhost:3000/api/v1`

### 6. Verificar que Funciona

```bash
# Health check
curl http://localhost:3000/api/v1/health-check
```


### Ejecutar Pruebas Específicas

```bash
# Pruebas de un servicio específico
./gradlew test --tests "com.ecommerce.api.services.ProductServiceTest"

# Pruebas de utilidades
./gradlew test --tests "com.ecommerce.api.utils.*"
```

### Ver Reportes de Pruebas

Los reportes HTML se generan en:
```
build/reports/tests/test/index.html
```

### Cobertura de Código

```bash
# Con plugin de cobertura (si está configurado)
./gradlew test jacocoTestReport
```

## 📡 Uso de la API

### Base URL

```
http://localhost:3000/api/v1
```

### Autenticación

#### Para Usuarios (JWT)

1. **Registro de Usuario**
```bash
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

2. **Login**
```bash
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

# Respuesta incluye accessToken
```

3. **Usar Token en Requests**
```bash
GET /products/current-user/paginate
Authorization: Bearer <accessToken>
```

#### Para Servicios Externos (API Key)

```bash
POST /tokenization/cards
X-API-KEY: api_xxxxxxxxxxxxx
Content-Type: application/json

{
  "pan": "4111111111111111",
  "cvv": "123",
  "expMonth": 12,
  "expYear": 2025
}
```

### Endpoints Principales

#### Productos

```bash
# Listar productos públicos (sin autenticación)
GET /products?page=1&size=10&name=laptop

# Búsqueda para clientes autenticados
GET /products/current-user/paginate?page=1&size=10

# Ver producto específico
GET /products/current-user/{id}

# Crear producto (ADMIN)
POST /products/
Authorization: Bearer <token>

# Actualizar producto (ADMIN)
PATCH /products/{id}
Authorization: Bearer <token>
```

#### Carrito

```bash
# Ver carrito activo
GET /cart
Authorization: Bearer <token>

# Agregar items
PATCH /cart/add-items
Authorization: Bearer <token>
Content-Type: application/json

{
  "items": [
    {
      "productId": "uuid-del-producto",
      "quantity": 2
    }
  ]
}

# Actualizar cantidades
PATCH /cart/edit-items
Authorization: Bearer <token>

# Remover items
PATCH /cart/remove-items
Authorization: Bearer <token>
```

#### Órdenes

```bash
# Crear orden (checkout)
POST /orders/checkout
Authorization: Bearer <token>
Content-Type: application/json

{
  "shippingAddress": "123 Main St",
  "cardToken": "tok_xxxxxxxxxxxxx"
}

# Ver orden
GET /orders/{id}
Authorization: Bearer <token>
```

### Colecciones de API

#### Postman

1. Importar colección:
   - Crear nueva colección en Postman

2. Ejemplos de requests:
   - `POST {{baseUrl}}/auth/register`
   - `POST {{baseUrl}}/auth/login` → Guardar token en variable
   - `GET {{baseUrl}}/products?page=1&size=10`

### Ejemplo Completo de Flujo

```bash
# 1. Registrar usuario
curl -X POST http://localhost:3000/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"test123","firstName":"Test","lastName":"User"}'

# 2. Login
TOKEN=$(curl -X POST http://localhost:3000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"test123"}' \
  | jq -r '.accessToken')

# 3. Listar productos
curl -X GET "http://localhost:3000/api/v1/products?page=1&size=10" \
  -H "Authorization: Bearer $TOKEN"

# 4. Agregar al carrito
curl -X PATCH http://localhost:3000/api/v1/cart/add-items \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"items":[{"productId":"uuid-producto","quantity":1}]}'

# 5. Ver carrito
curl -X GET http://localhost:3000/api/v1/cart \
  -H "Authorization: Bearer $TOKEN"
```

## 📁 Estructura del Proyecto

```
Backend-Java-Ecommerce/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/api/
│   │   │   ├── config/          # Configuraciones (Security, Async, Mail)
│   │   │   ├── controllers/     # REST Controllers
│   │   │   ├── services/        # Lógica de negocio
│   │   │   ├── repositories/    # Acceso a datos (JPA)
│   │   │   ├── entities/        # Entidades JPA
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── enums/           # Enumeraciones
│   │   │   ├── exceptions/      # Manejo de excepciones
│   │   │   ├── filters/         # Filtros HTTP (JWT, RequestId)
│   │   │   ├── security/        # Configuración de seguridad
│   │   │   ├── utils/           # Utilidades
│   │   │   └── model/           # Modelos de dominio
│   │   └── resources/
│   │       ├── application.properties
│   │       └── database/
│   │           ├── migrations/  # Migraciones Flyway
│   │           └── seeds/      # Datos iniciales
│   └── test/                    # Pruebas unitarias
├── build.gradle                 # Configuración Gradle
├── docker-compose.yml           # Docker Compose para desarrollo
├── Dockerfile                    # Imagen Docker
└── README.md                     # Este archivo
```

## 🔒 Seguridad

- **JWT Tokens**: Tokens firmados con HMAC-SHA256
- **API Keys**: Autenticación para servicios externos
- **BCrypt**: Hash de contraseñas
- **CORS**: Configurado para orígenes permitidos
- **Validación**: Validación de entrada con Jakarta Validation
- **SQL Injection**: Prevenido con JPA/Hibernate
- **XSS**: Prevenido con serialización JSON segura

## 📝 Notas Adicionales

- Los productos inactivos o con stock menor al mínimo configurable no son visibles públicamente
- El sistema de pagos es una simulación (no procesa pagos reales)
- Los tokens de tarjeta se generan de forma única y se reutilizan para la misma tarjeta (mismo fingerprint)

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto es privado y propietario.
