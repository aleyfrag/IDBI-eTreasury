# IDBI-eTreasury

Spring Boot 4.0.2 Maven application targeting Java 25, with an embedded LDAP directory and the supplied IDBI e-Treasury frontend.

## Run

Prerequisites: JDK 25 and Maven 3.9+.

```bash
mvn spring-boot:run
```

Open <http://localhost:8080> and sign in with the JSP-backed login view:

- Employee ID: `int12991`
- Password: `treasury123`

The development LDAP directory listens on `127.0.0.1:8389` and is seeded from `src/main/resources/ldap/users.ldif`. Plaintext demo credentials are for local development only; production should use the bank's managed LDAP/LDAPS service and externalized secrets.

## Application logs

Logs are written to `logs/idbi-etreasury.log` relative to the project directory. Archived logs are retained under `logs/archive` for 30 days. Override the directory when needed:

```bash
APP_LOG_PATH=/var/log/idbi-etreasury mvn spring-boot:run
```
