## 🐳 Using build API Docker image

```bash
docker pull ghcr.io/viiviii/friendly-pancake-api:latest
```

### Example

Runs as h2 in-memory DB:

```bash
docker run -d -p 8080:8080 ghcr.io/viiviii/friendly-pancake-api:latest
```

If want to set up:

```bash
docker run -d -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://192.168.10.192:8098/ \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=1234 \
  ghcr.io/viiviii/friendly-pancake-api:latest
```

## 🗑️ Cleanup when removing a deployment

### Remove runner

> Settings → Actions → Runners → ec2 → Remove runner


(if needed) Clean up `~/actions-runner/_work`, docker image on ec2

### Remove Environment

> Settings → Environments → api-production → Delete