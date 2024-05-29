## 🔓 속성

- application.properties

```
# [필수]
# tmdb api
# https://developer.themoviedb.org/docs/authentication-application#bearer-token
api.tmdb.token=$ACCESS_TOKEN

# [선택]
# postgres
spring.datasource.url=jdbc:postgresql://$HOST:$PORT/
spring.datasource.username=$USER_NAME
spring.datasource.password=$PASSWORD
```

<br>

## 🧪 파일 구성 예시

> 🔗 https://docs.spring.io/spring-boot/docs/3.2.5/reference/html/features.html#features.external-config.files

### 단일 파일

```
config/
  application.properties
```

### 파일 여러 개

```
config/
  postgres/
    application.properties
  tmdb/
    application.properties
```