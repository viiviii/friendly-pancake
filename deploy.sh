#!/bin/bash

PORT="8080"
IMAGE_NAME="api"
NETWORK_NAME="api-net"
NGINX_CONTAINER_NAME="nginx1"
UPSTREAM_FILE="/etc/nginx/conf.d/docker-api.conf"
CONTAINER_SHUTDOWN_TIMEOUT=30
HEALTH_CHECK_MAX_RETRIES=5
HEALTH_CHECK_RETRY_DELAY=5
VERSION="$1"

# 입력받은 버전 확인
if [[ -z $VERSION ]]; then
  echo "❗️ version argument 없음, 예시) sh deploy.sh 1.0.1"
  exit 1
fi

# jar 파일 build
./gradlew clean build
echo "✅ jar 파일 build"

# 도커 이미지 빌드
if docker build --tag "$IMAGE_NAME:$VERSION" .; then
  echo "✅ 도커 이미지 빌드"
else
  echo "❌ 도커 이미지 빌드"
  exit 1
fi

# 배포할 환경 확인
current="green"
target="blue"
containers=$(docker network inspect $NETWORK_NAME --format '{{range .Containers}}{{.Name}} {{end}}')
if [[ $containers =~ "blue" ]]; then
  current="blue"
  target="green"
fi
echo "👉 이번에 배포할 환경은 $target"

# 백업용으로 보관중이던 배포 컨테이너 제거
if docker rm "$(docker ps -aq --filter status=exited --filter name="^/${target}")"; then
  echo "✅ 백업용으로 보관중이던 배포 컨테이너 제거"
else
  echo "❌ 백업용으로 보관중이던 배포 컨테이너 제거"
  exit 1
fi

# 배포 이미지로 새로운 배포 컨테이너 생성 및 실행
if docker run -d --name $target --network $NETWORK_NAME "$IMAGE_NAME:$VERSION"; then
  echo "✅ 배포할 도커 컨테이너 생성 및 실행 - $IMAGE_NAME:$VERSION"
else
  echo "❌ 배포할 도커 컨테이너 생성 및 실행"
  exit 1
fi

# 배포 컨테이너 헬스 체크
for retries in $(seq $HEALTH_CHECK_MAX_RETRIES); do
  if docker exec $NGINX_CONTAINER_NAME curl -s $target:$PORT/api/contents >/dev/null; then
    echo "✅ 배포할 환경인 $target health check"
    break
  fi

  if [[ $retries -eq $HEALTH_CHECK_MAX_RETRIES ]]; then
    echo "❌ 배포할 환경인 $target health check"
    exit 1
  fi

  echo "...${HEALTH_CHECK_RETRY_DELAY}초 후 health check 재시도"
  sleep $HEALTH_CHECK_RETRY_DELAY
done

# Nginx 배포 환경을 바라보도록 변경
if docker exec $NGINX_CONTAINER_NAME sed -i "s/server $current:$PORT;/server $target:$PORT;/g" $UPSTREAM_FILE; then
  echo "✅ Nginx 설정 변경"
else
  echo "❌ Nginx 설정 변경"
  exit 1
fi

# Nginx 새로운 설정 적용
if docker exec $NGINX_CONTAINER_NAME nginx -t; then
  docker exec $NGINX_CONTAINER_NAME nginx -s reload
  echo "✅ Nginx 설정 적용"
else
  echo "❌ Nginx 설정 적용"
  exit 1
fi

# 기존 환경 종료
if docker stop --time $CONTAINER_SHUTDOWN_TIMEOUT $current >/dev/null; then
  echo "✅ 기존 환경인 $current 종료"
else
  echo "❌ 기존 환경인 $current 종료"
  exit 1
fi
