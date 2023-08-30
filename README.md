## 🔧 SSL for local development

Use the following instead:

```yml
  ssl:
    build:
      dockerfile_inline: |
        FROM twalter/minica:latest
        RUN echo 'minica "$@" && cd $2 && mv cert.pem fullchain.pem && mv key.pem privkey.pem' > /output/rename.sh
        ENTRYPOINT ["/bin/sh", "/output/rename.sh"]
    volumes:
      - ssl_certificates:/output/www.localhost
    command: --domains www.localhost
```