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

function check_input_version() {
  if [[ -z $VERSION ]]; then
    echo "❗❗❗️ 필수 입력 값인 version 누락, 예시) sh deploy.sh 1.0.0 ❗❗❗"
    exit 1
  fi
}

function is_blue_running() {
  local container_names
  container_names=$(docker network inspect $NETWORK_NAME --format '{{range .Containers}}{{.Name}} {{end}}')

  [[ $container_names =~ "blue" ]]
}

function build_jar() {
  ./gradlew clean build
  check_success_or_exit $? "jar 빌드"
}

function build_docker_image() {
  docker build --tag "$IMAGE_NAME:$VERSION" .
  check_success_or_exit $? "도커 이미지 빌드"
}

function remove_container() {
  docker rm "$(docker ps -aq --filter status=exited --filter name="^/${1}$")"
  check_success_or_exit $? "백업용으로 보관중이던 컨테이너 제거"
}

function run_container() {
  docker run -d --name "$1" --network $NETWORK_NAME "$IMAGE_NAME:$VERSION"
  check_success_or_exit $? "도커 컨테이너 생성 및 실행"
}

# TODO: 헬스체크
function health_check() {
  for retries in $(seq $HEALTH_CHECK_MAX_RETRIES); do
    if docker exec $NGINX_CONTAINER_NAME curl -s "$1:$PORT/api/contents" >/dev/null; then
      echo "✅ 새 버전 health check 성공"
      return
    fi

    if [[ $retries -eq $HEALTH_CHECK_MAX_RETRIES ]]; then
      echo "❌ 새 버전 health check 실패"
      exit 1
    fi

    echo "...health check ${HEALTH_CHECK_RETRY_DELAY}초 후 재시도 ($retries)"
    sleep $HEALTH_CHECK_RETRY_DELAY
  done
}

function change_nginx_endpoint() {
  docker exec $NGINX_CONTAINER_NAME sed -i "s/$1:$PORT/$2:$PORT/" $UPSTREAM_FILE
  check_success_or_exit $? "Nginx 새 버전을 바라보도록 설정 변경"
}

function reload_nginx_config() {
  docker exec $NGINX_CONTAINER_NAME nginx -s reload
  check_success_or_exit $? "Nginx 설정 적용"
}

function stop_container() {
  docker stop --time $CONTAINER_SHUTDOWN_TIMEOUT "$1" >/dev/null
  check_success_or_exit $? "기존 버전 종료"
}

function check_success_or_exit() {
  if [[ $1 -eq 0 ]]; then
    echo "✅ $2 성공"
  else
    echo "❌ $2 실패"
    exit 1
  fi
}

# 배포 준비
check_input_version

build_jar
build_docker_image

if is_blue_running; then
  current="blue"
  target="green"
else
  current="green"
  target="blue"
fi

# 배포 시작
echo "🚀🚀🚀 시작, 기존 버전[$current] ➡️ 새 버전[$target]"

remove_container $target
run_container $target
health_check $target
change_nginx_endpoint $current $target
reload_nginx_config
stop_container $current

echo "🚀🚀🚀 완료"