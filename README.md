# ProductService

Микросервис каталога товаров. Управляет товарами, их вариациями (SKU), категориями и атрибутами. Поддерживает полнотекстовый поиск через Elasticsearch и кэширование в Redis. Принимает Feign-вызовы от CartService и OrderService, слушает Kafka-события об отменах позиций заказа.

- **Порт:** 8082
- **Имя в Eureka:** `PRODUCT-SERVICE`
- **Java:** 17
- **Spring Boot:** 3.4.4

---

## Содержание

- [Назначение](#назначение)
- [API эндпоинты](#api-эндпоинты)
- [Модель данных](#модель-данных)
- [Kafka-интеграция](#kafka-интеграция)
- [Feign-клиент (входящие)](#feign-клиент-входящие)
- [Переменные окружения](#переменные-окружения)
- [Сборка и запуск](#сборка-и-запуск)
- [Тесты](#тесты)

---

## Назначение

- Хранит товары (`Product`) и их вариации (`ProductVariation`) в PostgreSQL.
- Индексирует данные в Elasticsearch для полнотекстового поиска (`/api/search/{text}`).
- Кэширует результаты в Redis (`spring.cache.type: redis`).
- Предоставляет эндпоинт `/api/products/checkAndReserve` для резервирования товаров при создании заказа.
- Обрабатывает Kafka-событие `order.item.cancelled` для снятия резерва при отмене позиции.

---

## API эндпоинты

Доступны через Gateway: `http://localhost:8080`. Пометка `[AUTH]` — требуется JWT-токен.

### Товары — `/api/products`

| Метод  | Путь                                      | Auth          | Описание                                         |
|--------|-------------------------------------------|---------------|--------------------------------------------------|
| GET    | `/api/products`                           | —             | Список всех товаров (пагинация через `Pageable`) |
| GET    | `/api/products/{id}`                      | —             | Товар по UUID                                    |
| POST   | `/api/products`                           | [AUTH]        | Создать товар                                    |
| DELETE | `/api/products/{id}`                      | [AUTH]        | Удалить товар по UUID                            |
| GET    | `/api/products/category/{id}`             | —             | Вариации товаров по категории (пагинация)        |

### Вариации товаров

| Метод  | Путь                                          | Auth   | Описание                                         |
|--------|-----------------------------------------------|--------|--------------------------------------------------|
| GET    | `/api/products/variations`                    | —      | Все вариации (пагинация)                         |
| POST   | `/api/products/variationsByIds`               | —      | Вариации по списку UUID (используется Feign)     |
| GET    | `/api/products/{productId}/variations`        | —      | Вариации конкретного товара (пагинация)          |
| POST   | `/api/products/{productId}/variations`        | [AUTH] | Добавить вариацию к товару                       |
| PUT    | `/api/products/variations/{variationId}`      | [AUTH] | Обновить вариацию                                |
| DELETE | `/api/products/variations/{variationId}`      | [AUTH] | Удалить вариацию                                 |
| POST   | `/api/products/checkAndReserve`               | [AUTH] | Проверить наличие и зарезервировать позиции      |

### Категории — `/api/categories`

| Метод  | Путь                    | Auth   | Описание                                     |
|--------|-------------------------|--------|----------------------------------------------|
| GET    | `/api/categories`       | —      | Все категории в виде дерева                  |
| POST   | `/api/categories`       | [AUTH] | Создать категорию                            |
| PUT    | `/api/categories/{id}`  | [AUTH] | Обновить категорию (возвращает поддерево)    |
| DELETE | `/api/categories/{id}`  | [AUTH] | Удалить категорию                            |

### Атрибуты — `/api/attributes`

| Метод  | Путь                      | Auth                    | Описание             |
|--------|---------------------------|-------------------------|----------------------|
| GET    | `/api/attributes`         | —                       | Список всех атрибутов|
| GET    | `/api/attributes/{id}`    | —                       | Атрибут по UUID      |
| POST   | `/api/attributes`         | [AUTH] `ROLE_ADMIN`     | Создать атрибут      |
| PUT    | `/api/attributes/{id}`    | [AUTH] `ROLE_ADMIN`     | Обновить атрибут     |
| DELETE | `/api/attributes/{id}`    | [AUTH] `ROLE_ADMIN`     | Удалить атрибут      |

### Поиск — `/api/search`

| Метод | Путь                         | Описание                                              |
|-------|------------------------------|-------------------------------------------------------|
| GET   | `/api/search/{searchText}`   | Полнотекстовый поиск по Elasticsearch (пагинация)    |

---

## Модель данных

| Сущность          | Описание                                               |
|-------------------|--------------------------------------------------------|
| `Product`         | Основная карточка товара (название, продавец, категория)|
| `ProductVariation`| Конкретный SKU (цена, количество на складе, атрибуты) |
| `Category`        | Иерархия категорий                                     |
| `Attribute`       | Типы атрибутов (цвет, размер и т.п.)                  |
| `AttributeValue`  | Значение атрибута для конкретной вариации              |
| `ProductDoc`      | Elasticsearch-документ для поиска                     |

Схема управляется Flyway (`classpath:db/migration`).

---

## Kafka-интеграция

**Консьюмер:** топик `order.item.cancelled`, группа `product-service-group`

При получении события `OrderItemCancelledEvent` снимает резерв с соответствующей вариации товара.

---

## Feign-клиент (входящие)

ProductService не вызывает другие сервисы по Feign. Он сам является **целью** Feign-вызовов:

| Caller       | Метод | Эндпоинт                           | Назначение                              |
|--------------|-------|------------------------------------|-----------------------------------------|
| CartService  | POST  | `/api/products/variationsByIds`    | Получить данные вариаций для корзины    |
| OrderService | POST  | `/api/products/checkAndReserve`    | Проверить наличие и зарезервировать     |

---

## Переменные окружения

| Переменная                  | По умолчанию (dev)                                 | Описание                     |
|-----------------------------|----------------------------------------------------|------------------------------|
| `DB_URL`                    | `jdbc:postgresql://localhost:5434/product_db`      | JDBC-URL PostgreSQL          |
| `DB_USERNAME`               | `user`                                             | Пользователь БД              |
| `DB_PASSWORD`               | `password`                                         | Пароль БД                    |
| `JWT_SECRET`                | `nTDmGYqtvLfDCptgzwG+xKGtXV/JHL4fHKJrxK9tHdI=`   | Ключ проверки JWT            |
| `KAFKA_BOOTSTRAP_SERVERS`   | `http://localhost:9092`                            | Адрес Kafka-брокера          |
| `REDIS_HOST`                | `localhost`                                        | Хост Redis                   |
| `REDIS_PORT`                | `6379`                                             | Порт Redis                   |
| `SPRING_ELASTICSEARCH_URIS` | не задан в application.yml                        | URI Elasticsearch (задаётся в Docker Compose как `http://elasticsearch:9200`) |
| `EUREKA_URL`                | `http://localhost:8761/eureka/`                    | Адрес Eureka Discovery       |

---

## Сборка и запуск

### Через Docker Compose

```bash
cd MarketPlaceProject
docker compose -f docker-compose.dev.yml up --build product-service product-db redis elasticsearch -d
```

### Локально из исходников

Требования: JDK 17, PostgreSQL 15 (база `product_db`), Redis, Elasticsearch 9.x, Kafka.

```bash
cd ProductService
SPRING_ELASTICSEARCH_URIS=http://localhost:9200 \
KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
./gradlew bootRun
```

### Сборка JAR

```bash
cd ProductService
./gradlew build
```

---

## Тесты

```bash
cd ProductService
./gradlew test
```

Тестовые классы в `src/test/java/ru/projects/product_service/`:
- `service/ProductServiceTest.java`
- `service/CategoryServiceTest.java`
- `service/AttributeServiceTest.java`
- `mapper/ProductMapperTest.java`
- `mapper/VariationMapperTest.java`

Health-check endpoint: `GET http://localhost:8082/actuator/health`
