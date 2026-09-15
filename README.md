This project is a demonstration of how to use Java with PostgreSQL using Hibernate/JPA and Liquibase

**JPA (Jakarta Persistence API)** is a Java specification for object-relational mapping, and **Hibernate** is its most widely used implementation. Entities are annotated with `@Entity`/`@Id`/`@OneToMany`/`@ManyToOne`, and Spring Data JPA repositories (`JpaRepository`) provide CRUD and query methods derived from the entity model.
* The project connects to PostgreSQL with Liquibase, which owns the schema; Hibernate is configured with `ddl-auto: validate` so it never alters the schema itself.
* During testing, it connects to H2 in-memory database with test data to run unit tests.

## Security with Auth0 Integration
This project uses Auth0 for Machine-to-Machine (M2M) authentication and authorization. Auth0 issues a JWT (JSON Web Token) to clients, which is then used to access protected API endpoints.

**How it works:**
1. The client application authenticates with Auth0 using its client credentials (client ID and secret).
2. Auth0 returns a JWT access token if the credentials are valid.
3. The client includes this JWT in the `Authorization: Bearer <token>` header of each API request.
4. The backend validates the JWT signature and claims (such as audience and issuer) to ensure the request is authenticated and authorized.

**Key points:**
- Only clients with valid Auth0 credentials can obtain a JWT and access the API.
- JWT tokens are validated on every request for authenticity and required scopes/roles.
- Sensitive endpoints are protected using security filters that check for valid JWTs.


**Example request:**
```http
GET /api/customers
Authorization: Bearer <your-jwt-token>
```

The following screenshots illustrate the different access levels provided by each client.

### Custom API
![auth0 demo app](src/main/resources/static/demo-app.png)

### Client with READ Access
![client read access](src/main/resources/static/client-read-access.png)

### Client with ADMIN Access
![client admin  access](src/main/resources/static/client-admin-access.png)

## Hibernate / Spring Data JPA

Hibernate is the JPA provider used for all database access in this project. It handles common tasks such as:

* Opening and closing database connections/sessions
* Translating entity CRUD operations and JPQL/derived queries into SQL
* Managing the persistence context (dirty checking, identity map, cascading)
* Handling exceptions and translating them into Spring's DataAccessException hierarchy

Repositories are `JpaRepository<Entity, Id>` interfaces (see `repository/`), and entities declare real relationships with an explicit fetch strategy:

* `Customer.orders`, `Orders.orderItems`, `Orders.payments`, `Products.orderItems` are all **LAZY** — only queried when the collection is actually accessed, avoiding unnecessary joins/queries.
* `OrderItems.product` is the one **EAGER** relationship, since product details (name/price) are needed almost every time an order item is read.

Because `spring.jpa.open-in-view` is disabled, any lazy collection that needs to be returned to a client is explicitly initialized (e.g. `order.getOrderItems().size()`) inside a `@Transactional` service method, before the Hibernate session closes.

## Transactions/Rollbacks

When deleting a Customer that has related orders with payments, we use @Transactional annotation, which is used to execute a block of code within a database transaction. If any exception is thrown inside the block, the transaction is rolled back automatically; otherwise, it is committed. 

Most of these cascading deletes are driven by `cascade = CascadeType.ALL` + `orphanRemoval = true` on the parent side of a relationship (e.g. `Customer.orders`, `Orders.orderItems`, `Orders.payments`), so deleting the parent entity cascades down automatically instead of requiring manual, separate delete calls per table.
* See example: src/main/java/com/vcarrin87/jdbc_example/services/CustomerService.java #deleteCustomer()
* See example: src/main/java/com/vcarrin87/jdbc_example/services/OrdersService.java #deleteOrders()
* See example: src/main/java/com/vcarrin87/jdbc_example/services/ProductsService.java #deleteProduct()

## Models explained

* **Customer**: Represents the person placing the order. Stores details such as customer ID, name, contact information, and address. Each order is linked to a specific customer.
* **Product**: Represents an item available for purchase. Contains information like product ID, name, description, price, and current inventory level. Products are referenced in OrderItems.
* **Payment**: Records how an order was paid for. Includes payment ID, order ID, payment method (e.g., credit card, PayPal), payment status, and transaction details. Each payment is linked to a specific order.
* **Order**: Represents the overall purchase (who, when, total, etc.).
* **OrderItems**: Represents each product in the order (productId, quantity, price, etc.).
- When a customer places an order, they usually buy multiple products. Each product is an OrderItem linked to the main Order via its ID. You need to record which products (and how many) are part of the order. This is done by inserting each OrderItem into the order_items table, using the new order’s ID.
* **Inventory**: You must decrease the stock for each product sold.

We combine all of these in OrdersService #placeOrder()

### Swagger-UI

Swagger UI provides a web-based interface to visualize and interact with the API's endpoints.
To access Swagger UI:
1. Start the application server.
2. Open a web browser.
3. Navigate to the Swagger UI URL, typically in the format: `http://<server_address>:8091/swagger-ui/index.html`.
4. Use the interface to explore available API endpoints, view request/response schemas, and test API calls.

![swagger-ui](src/main/resources/static/swagger-ui.png)

### Database ER Diagram
![er-diagram](src/main/resources/static/er-diagram.png)

## Testing
### Run all tests
```bash
    ./mvnw test
```

### Run single test suite
```bash
    ./mvnw -Dtest=<Test_Name> test
```

### Run single test
```bash
    ./mvnw -Dtest=<Test_Name>#<method_name> test
```