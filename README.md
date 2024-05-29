## 🔧 For local development

docker compose up 시 필요한 것들

### (필수) 환경 변수 구성

in-memory db로 실행하는 예시

```
export spring_datasource_properties="\
spring.datasource.url=jdbc:h2:mem:test
spring.datasource.username=sa
spring.datasource.password=
"

export tmdb_api_properties="\
api.tmdb.token=$ACCESS_TOKEN
"
```



###  (선택) SSL

```yml
ssl:
  image: twalter/minica:latest
  volumes:
    - ssl_certificates:/output/www.localhost
  command: --domains www.localhost
```
