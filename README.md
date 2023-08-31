## 🔧 SSL for local development

Use the following instead:

```yml
ssl:
  image: twalter/minica:latest
  volumes:
    - ssl_certificates:/output/www.localhost
  command: --domains www.localhost
```