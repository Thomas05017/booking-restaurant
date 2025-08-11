# 🍽️ Restaurant Booking System

Un sistema completo per la gestione delle prenotazioni di un ristorante, sviluppato con Spring Boot e MySQL.

## 📋 Indice

- [Caratteristiche](#-caratteristiche)
- [Tecnologie](#-tecnologie)
- [Prerequisiti](#-prerequisiti)
- [Installazione](#-installazione)
- [Configurazione](#-configurazione)
- [Utilizzo](#-utilizzo)
- [API Endpoints](#-api-endpoints)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Struttura del Progetto](#-struttura-del-progetto)
- [Contribuire](#-contribuire)

## 🚀 Caratteristiche

### Gestione Utenti
- ✅ Registrazione e autenticazione utenti
- ✅ Sistema di ruoli (ADMIN/USER)
- ✅ Password sicure con BCrypt hashing
- ✅ Autenticazione HTTP Basic

### Gestione Prenotazioni
- ✅ Creazione prenotazioni con validazioni business
- ✅ Aggiornamento e cancellazione prenotazioni
- ✅ Controllo disponibilità automatico
- ✅ Validazioni orari apertura (12:00-15:00, 19:00-23:00)
- ✅ Chiusura domenicale
- ✅ Prenotazioni fino a 60 giorni in anticipo
- ✅ Slot da 30 minuti

### Caratteristiche Tecniche
- ✅ API RESTful
- ✅ Validazione multi-layer
- ✅ Gestione errori centralizzata
- ✅ Health monitoring
- ✅ Containerizzazione Docker
- ✅ Database migrations con Flyway

## 🛠️ Tecnologie

- **Backend**: Java 17, Spring Boot 3.5.3
- **Database**: MySQL 8.0
- **Security**: Spring Security
- **ORM**: Hibernate/JPA
- **Migration**: Flyway
- **Build**: Maven 3.9.10
- **Testing**: JUnit 5, Mockito, Testcontainers
- **Containerization**: Docker, Docker Compose

## 📋 Prerequisiti

- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Git

## 🔧 Installazione

### 1. Clone del Repository
```bash
git clone https://github.com/your-username/restaurant-booking-system.git
cd restaurant-booking-system
```

### 2. Setup Ambiente di Sviluppo
```bash
# Genera password per MySQL (opzionale)
chmod +x setup-dev.sh
./setup-dev.sh

# Avvia l'ambiente di sviluppo
docker-compose -f docker-compose.dev.yml up -d
```

### 3. Verifica Installazione
```bash
# Verifica health dell'applicazione
curl http://localhost:8080/actuator/health

# Accesso PhpMyAdmin (dev only)
# http://localhost:8081
# Server: mysql
# Username: user
# Password: userpassword
```

## ⚙️ Configurazione

### Variabili d'Ambiente

#### Sviluppo
```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/ristorante_db
SPRING_DATASOURCE_USERNAME: user
SPRING_DATASOURCE_PASSWORD: userpassword
```

#### Produzione
```yaml
SPRING_PROFILES_ACTIVE: prod
SPRING_DATASOURCE_PASSWORD_FILE: /run/secrets/mysql_password
MYSQL_ROOT_PASSWORD_FILE: /run/secrets/mysql_root_password
```

### Setup Secrets (Produzione)
```bash
# Crea secrets Docker
echo "your-secure-password" | docker secret create mysql_password -
echo "your-root-password" | docker secret create mysql_root_password -

# Deploy produzione
docker-compose -f docker-compose.prod.yml up -d
```

## 🎯 Utilizzo

### Utente Admin di Default
```
Username: admin
Password: admin123
```

### Registrazione Nuovo Utente
```bash
curl -X POST http://localhost:8080/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

### Creazione Prenotazione
```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic $(echo -n 'johndoe:password123' | base64)" \
  -d '{
    "userId": 2,
    "date": "2025-08-15",
    "time": "20:00",
    "numberOfGuests": 4
  }'
```

## 📚 API Endpoints

### 👥 Utenti

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/users/register` | Registrazione utente | No |
| GET | `/users` | Lista utenti | Yes |
| GET | `/users/{id}` | Dettagli utente | Yes |
| DELETE | `/users/{id}` | Elimina utente | Yes |

### 📅 Prenotazioni

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/bookings` | Lista prenotazioni | Yes |
| POST | `/bookings` | Crea prenotazione | Yes |
| GET | `/bookings/{id}` | Dettagli prenotazione | Yes |
| PUT | `/bookings/{id}` | Aggiorna prenotazione | Yes |
| DELETE | `/bookings/{id}` | Cancella prenotazione | Yes |
| GET | `/bookings/user/{userId}` | Prenotazioni per utente | Yes |
| GET | `/bookings/date/{date}` | Prenotazioni per data | Yes |

### 🏥 Monitoring

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/actuator/health` | Health check | No |

### Esempi di Request/Response

#### Creazione Prenotazione
**Request:**
```json
POST /bookings
{
  "userId": 1,
  "date": "2025-08-15",
  "time": "20:00",
  "numberOfGuests": 4
}
```

**Response:**
```json
{
  "id": 1,
  "date": "2025-08-15",
  "time": "20:00",
  "numberOfGuests": 4,
  "username": "johndoe",
  "status": "CONFIRMED"
}
```

#### Gestione Errori
```json
{
  "timestamp": "2025-08-11T10:30:00",
  "status": 400,
  "error": "Invalid Booking",
  "message": "Il ristorante è chiuso la domenica",
  "path": "/bookings"
}
```

## 🧪 Testing

### Esecuzione Test
```bash
# Unit test
mvn test

# Test con coverage
mvn test jacoco:report

# Test specifico
mvn test -Dtest=BookingServiceTest
```

### Test Categories
- **Unit Tests**: Service e Controller layer
- **Integration Tests**: Database e repository
- **Debug Tests**: Troubleshooting autenticazione

### Test Database
I test utilizzano Testcontainers per database isolati:
```java
@Testcontainers
@SpringBootTest
class BookingIntegrationTest {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");
}
```

## 🚀 Deployment

### Ambiente di Sviluppo
```bash
# Avvia con hot reload
docker-compose -f docker-compose.dev.yml up -d

# Logs dell'applicazione
docker-compose -f docker-compose.dev.yml logs -f app
```

### Ambiente di Produzione
```bash
# Build immagine production
docker build -t restaurant-booking:latest .

# Deploy con secrets
docker-compose -f docker-compose.prod.yml up -d

# Monitoring
docker-compose -f docker-compose.prod.yml ps
docker-compose -f docker-compose.prod.yml logs -f
```

### Health Checks
```bash
# Verifica database
docker-compose exec mysql mysqladmin ping -h localhost -u user -p

# Verifica applicazione
curl http://localhost:8080/actuator/health
```

## 📁 Struttura del Progetto

```
src/
├── main/
│   ├── java/com/myrestaurant/
│   │   ├── BookingApplication.java
│   │   ├── booking/                 # Dominio Prenotazioni
│   │   │   ├── controller/
│   │   │   │   └── BookingController.java
│   │   │   ├── dto/
│   │   │   │   ├── BookingDTO.java
│   │   │   │   ├── BookingResponseDTO.java
│   │   │   │   └── BookingUpdateDTO.java
│   │   │   ├── model/
│   │   │   │   └── Booking.java
│   │   │   ├── repository/
│   │   │   │   └── BookingRepository.java
│   │   │   ├── service/
│   │   │   │   └── BookingService.java
│   │   │   ├── util/
│   │   │   │   ├── DateTimeUtil.java
│   │   │   │   └── ValidationUtil.java
│   │   │   └── validator/
│   │   │       └── BookingValidator.java
│   │   ├── user/                    # Dominio Utenti
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── config/
│   │   └── exception/               # Gestione Errori
│   │       ├── GlobalExceptionHandler.java
│   │       ├── BookingNotFoundException.java
│   │       ├── InvalidBookingException.java
│   │       └── UserNotFoundException.java
│   └── resources/
│       ├── application.properties
│       └── db/migration/           # Flyway Migrations
├── test/                          # Test Suite
│   └── java/com/myrestaurant/
└── docker/                        # Docker Configuration
    ├── Dockerfile
    ├── docker-compose.dev.yml
    ├── docker-compose.prod.yml
    └── nginx.conf
```

## 📋 Regole Business

### Orari Ristorante
- **Pranzo**: 12:00 - 15:00
- **Cena**: 19:00 - 23:00
- **Chiuso**: Domenica e orario 15:00-19:00

### Vincoli Prenotazioni
- Numero ospiti: 1-20 persone
- Anticipo: Minimo 1 ora
- Periodo: Massimo 60 giorni in anticipo
- Slot: Ogni 30 minuti (12:00, 12:30, 13:00, etc.)

### Validazioni
- Username: 3-20 caratteri alfanumerici + underscore
- Password: Minimo 6 caratteri
- Date: Solo future o presente
- Orari: Solo negli slot consentiti

## 🔒 Sicurezza

### Autenticazione
- HTTP Basic Authentication
- Password hashing con BCrypt
- Session management sicuro

### Autorizzazione
- Role-based access control
- ADMIN: Accesso completo
- USER: Operazioni base prenotazioni

### Protezione Dati
- Validazione input su tutti gli endpoint
- SQL injection protection (JPA/Hibernate)
- Password mai esposte in JSON response

## 🐛 Troubleshooting

### Problemi Comuni

#### Database Connection Failed
```bash
# Verifica stato containers
docker-compose ps

# Restart database
docker-compose restart mysql

# Check logs
docker-compose logs mysql
```

#### Autenticazione Fallita
```bash
# Reset password admin
docker-compose exec app java -jar app.jar --spring.profiles.active=test

# Verifica utente nel DB
docker-compose exec mysql mysql -u root -p ristorante_db
SELECT username, password FROM user;
```

#### Migration Failed
```bash
# Repair Flyway
docker-compose exec app java -jar app.jar --spring.flyway.repair=true

# Baseline
docker-compose exec app java -jar app.jar --spring.flyway.baseline-on-migrate=true
```

### Logs Utili
```bash
# Application logs
docker-compose logs -f app

# Database logs  
docker-compose logs -f mysql

# Tutti i servizi
docker-compose logs -f
```

## 📈 Performance

### Ottimizzazioni Implementate
- Connection pooling HikariCP
- Indici database ottimizzati
- Lazy loading JPA
- Query specializzate per casi d'uso frequenti

### Monitoring
- Spring Boot Actuator metrics
- Database health checks
- Application performance indicators
