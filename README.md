# Library App - Publisher Management Prototype

Java Spring Boot ile geliştirilmiş yayınevi yönetimi prototipi.

## İçindekiler

- [Özellikler](#-özellikler)
- [Teknolojiler](#-teknolojiler)
- [Proje Yapısı](#-proje-yapısı)
- [Kurulum](#-kurulum)
- [API Endpoints](#-api-endpoints)
- [Örnek İstekler](#-örnek-i̇stekler)

---

## Özellikler

| Özellik | Açıklama |
|---------|----------|
| **CRUD İşlemleri** | Kitap ekleme, güncelleme, silme ve listeleme |
| **Yayınevi Yönetimi** | Otomatik yayınevi oluşturma/bulma |
| **Yazar Yönetimi** | Kitapla birlikte yazar kaydı |
| **Stream Filtreleme** | Başlık prefix'ine göre filtreleme |
| **JPA Query** | Tarih bazlı kitap sorgulama |
| **Google Books API** | Feign Client ile harici API entegrasyonu |
---

##  Teknolojiler

| Teknoloji         | Versiyon |
|-------------------|----------|
| Java              | 21       |
| Spring Boot       | 3.3.5    |
| PostgreSQL        | 16       |
| Springdoc OpenAPI | 2.6.0    |
| H2                | -        |

---

## Proje Yapısı

```
src/main/java/com/mindtech/library/
├── client/                 
│   └── GoogleBooksClient.java
├── config/                 
│   └── OpenApiConfig.java
├── controller/             
│   ├── AuthorController.java
│   ├── BookController.java
│   └── PublisherController.java
├── dto/
│   ├── request/            
│   │   └── BookRequest.java
│   └── response/           
│       ├── AuthorResponse.java
│       ├── BookResponse.java
│       ├── GoogleBookResponse.java
│       ├── GoogleBooksApiResponse.java
│       ├── PagedResponse.java
│       ├── PublisherResponse.java
│       └── PublisherWithBooksResponse.java
├── entity/                 
│   ├── Author.java
│   ├── BaseEntity.java
│   ├── Book.java
│   └── Publisher.java
├── exception/              
│   ├── custom/
│   │   ├── DuplicateResourceException.java
│   │   └── ResourceNotFoundException.java
│   ├── ErrorCode.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
├── mapper/                 
│   ├── AuthorMapper.java
│   ├── BookMapper.java
│   ├── GoogleBookMapper.java
│   └── PublisherMapper.java
├── repository/             
│   ├── AuthorRepository.java
│   ├── BookRepository.java
│   └── PublisherRepository.java
├── service/                
│   ├── AuthorService.java
│   ├── BookService.java
│   ├── GoogleBooksService.java
│   └── PublisherService.java
└── LibraryApplication.java
```

---

## Kurulum

### Gereksinimler
- Docker & Docker Compose

### Docker ile Çalıştırma

```bash
git clone https://github.com/emirhanusta/LibraryApp
cd library-app

docker-compose up -d
```


### Erişim URL'leri

| Servis | URL |
|--------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |

---

##  API Endpoints

### Books

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/api/books` | Tüm kitapları listele (paginated) |
| `GET` | `/api/books/{id}` | ID ile kitap getir |
| `POST` | `/api/books` | Yeni kitap oluştur |
| `PUT` | `/api/books/{id}` | Kitap güncelle |
| `DELETE` | `/api/books/{id}` | Kitap sil |
| `GET` | `/api/books/by-title-prefix?prefix=A` | Başlığa göre filtrele (Stream) |
| `GET` | `/api/books/published-after?date=2023-01-01` | Tarihe göre filtrele (JPA) |
| `GET` | `/api/books/search/google?query=Clean+Code` | Google Books'ta ara |

### Publishers

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/api/publishers` | Tüm yayınevlerini listele |
| `GET` | `/api/publishers/with-books?count=2` | Yayınevlerini kitaplarıyla listele |

### Authors

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| `GET` | `/api/authors` | Tüm yazarları listele |

---

### Hata Yanıt Formatı

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "errorCode": "ERR001",
  "error": "Resource not found",
  "message": "Book not found with id: 999",
  "path": "/api/books/999"
}
```

### Validasyon Hatası Yanıt Formatı

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation failed",
  "message": "Invalid request parameters",
  "path": "/api/books",
  "fieldErrors": [
    {
      "field": "title",
      "message": "Title is required"
    },
    {
      "field": "isbn13",
      "message": "ISBN13 must be exactly 13 characters"
    }
  ]
}
```

---

## Örnek İstekler

### Kitap Oluşturma

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "price": 45.00,
    "isbn13": "9780132350884",
    "publisherName": "Prentice Hall",
    "authorNameSurname": "Robert C. Martin",
    "publicationDate": "2008-08-01"
  }'
```

**Başarılı Yanıt (201 Created):**
```json
{
  "id": 1,
  "title": "Clean Code",
  "price": 45.00,
  "isbn13": "9780132350884",
  "publisherName": "Prentice Hall",
  "authorNameSurname": "Robert C. Martin",
  "publicationDate": "2008-08-01"
}
```

### Kitapları Listeleme

```bash
curl "http://localhost:8080/api/books?page=0&size=10&sort=title,asc"
```

**Yanıt:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Clean Code",
      "price": 45.00,
      "isbn13": "9780132350884",
      "publisherName": "Prentice Hall",
      "authorNameSurname": "Robert C. Martin",
      "publicationDate": "2008-08-01"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

### Google Books API ile Arama

```bash
curl "http://localhost:8080/api/books/search/google?query=Effective+Java"
```

**Yanıt:**
```json
[
  {
    "title": "Effective Java",
    "price": 1431.8,
    "isbn13": "9780134686042",
    "publisherName": "Addison-Wesley Professional",
    "authorNameSurname": "Joshua Bloch"
  }
]
```

### 2023'ten Sonra Yayınlanan Kitaplar

```bash
curl "http://localhost:8080/api/books/published-after?date=2023-01-01&page=0&size=20"
```

### 'A' ile Başlayan Kitaplar (Stream)

```bash
curl "http://localhost:8080/api/books/by-title-prefix?prefix=A"
```

---

## Veritabanı Şeması

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│  publisher   │       │     book     │       │    author    │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │       │ id (PK)      │       │ id (PK)      │
│ publisher_   │◄──────│ publisher_id │       │ author_name_ │
│   name       │   1:N │   (FK)       │   1:1 │   surname    │
│ created_at   │       │ title        │──────►│ book_id (FK) │
│ updated_at   │       │ price        │       │ created_at   │
└──────────────┘       │ isbn13       │       │ updated_at   │
                       │ publication_ │       └──────────────┘
                       │   date       │
                       │ created_at   │
                       │ updated_at   │
                       └──────────────┘
```

---


### Ortam Değişkenleri

| Değişken | Varsayılan | Açıklama |
|----------|------------|----------|
| `SPRING_DATASOURCE_URL` | jdbc:postgresql://localhost:5432/librarydb | DB URL |
| `SPRING_DATASOURCE_USERNAME` | library | DB kullanıcı |
| `SPRING_DATASOURCE_PASSWORD` | library123 | DB şifre |

---
