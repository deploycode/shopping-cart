# Shopping cart | Indra challenge
Code challenge desarrollado para la posición de Java Developer en Indra

# Requerimientos del Sistema
Para ejecutar este proyecto, asegúrate de tener los siguientes requisitos instalados y configurados en tu sistema:

#### 1. Java 17 o superior

#### 2. Apache Maven 3.8 o superior

#### 3. PostgreSQL 12 o superior
- **Configuración Requerida**:
    - Crea una base de datos para el proyecto:
      ```sql
      CREATE DATABASE shopping_cart_db;
      ```
    - Configura el archivo `application.properties` con tus credenciales de PostgreSQL:
      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/shopping_cart_db
      spring.datasource.username=tu_usuario
      spring.datasource.password=tu_contraseña
      ```
# Iniciar el proyecto
Para ejecutar este proyecto ejecutar alguno de los siguientes comandos en la raiz del proyecto:

Opcion 1 con maven:
```sql
mvn spring-boot:run;
```
Opción 2 generando el jar
```sql
java -jar target/shopping-cart-0.0.1-SNAPSHOT.jar
```
Ir al navegador web
```sql
http://localhost:8080/swagger-ui.html
```

# En caso de error
### Error por puerto 8080 ocupado
En caso de tener ocupado el puerto 8080 debe configurar otro en el archivo application.properties
```sql
# application.properties
server.port=PUERTO_LIBRE
```
### Error generación automática de tablas
El sistema generará automáticamente las tablas del aplicativo en la base de datos configurada en los properties del aplicativo, si presenta problemas comentar ésta linea y ejecutar los scripts de creación de tablas
```sql
# application.properties
# spring.jpa.hibernate.ddl-auto=update
```

```sql
-- Crear tabla de usuarios
CREATE TABLE users (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255) UNIQUE NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de productos
CREATE TABLE products (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  unit_price NUMERIC(10, 2) NOT NULL
);

-- Crear tabla de carritos
CREATE TABLE carts (
   id SERIAL PRIMARY KEY,
   user_id INTEGER NOT NULL,
   total NUMERIC(10, 2) DEFAULT 0.0,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Crear tabla de items del carrito
CREATE TABLE cart_items (
    id SERIAL PRIMARY KEY,
    cart_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

```
## Scripts de Datos Iniciales

Para poblar la base de datos con datos iniciales, puedes ejecutar los siguientes scripts SQL en PostgreSQL:

```sql
-- Insertar usuarios
INSERT INTO users (id, name, email, created_at) VALUES
(1, 'Carlos', 'carlos@example.com', NOW()),
(2, 'Ernesto', 'ernesto@example.com', NOW()),
(3, 'Gabriel', 'gabriel@example.com', NOW());

-- Insertar productos
INSERT INTO products (id, name, description, unit_price) VALUES
(1, 'Laptop', 'High-end laptop', 1500.00),
(2, 'Smartphone', 'Latest smartphone model', 800.00),
(3, 'Headphones', 'Noise-cancelling headphones', 200.00);

-- Insertar carritos
INSERT INTO carts (id, user_id, total, created_at) VALUES
(1, 1, 0.0, NOW()),  -- Carrito para Carlos
(2, 2, 0.0, NOW());  -- Carrito para Ernesto

-- Insertar items en los carritos
INSERT INTO cart_items (id, cart_id, product_id, quantity, subtotal) VALUES
(1, 1, 1, 1, 1500.00),  -- Carlos tiene una laptop en el carrito
(2, 1, 2, 1, 800.00),   -- Carlos tiene un smartphone en el carrito
(3, 2, 3, 1, 200.00);   -- Ernesto tiene unos audífonos en el carrito

-- Actualizar el total en cada carrito
UPDATE carts SET total = (SELECT SUM(subtotal) 
FROM cart_items WHERE cart_id = carts.id) WHERE id IN (1, 2);
```

# DOCUMENTACION
## Modelo entidad relación
![cart_items.png](cart_items.png)

## Postman
[Indra Challenge.postman_collection.json](Indra%20Challenge.postman_collection.json)
