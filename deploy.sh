#!/bin/bash

K8S_DEPLOYMENT_TEMPLATE="k8s/deployment.yaml.template"
DOCKER_IMAGE_NAME="api"
VERSION="$1"

function check_input_version() {
  if [[ -z $VERSION ]]; then
    echo "❗❗❗️ 필수 입력 값인 version 누락, 예시) sh deploy.sh 1.0.0 ❗❗❗"
    exit 1
  fi
}

function build_jar() {
  ./gradlew clean build
  check_success_or_exit $? "jar 빌드"
}

function build_docker_image_in_local() {
  eval "$(minikube docker-env)"
  docker build --tag "$DOCKER_IMAGE_NAME:$VERSION" .
  check_success_or_exit $? "도커 이미지 빌드"
}

function change_image_version() {
  DOCKER_IMAGE=$DOCKER_IMAGE_NAME DOCKER_IMAGE_VERSION=$VERSION \
  envsubst < "$K8S_DEPLOYMENT_TEMPLATE" | kubectl apply -f -
  check_success_or_exit $? "Deployment image 새 버전으로 적용"
}

function check_success_or_exit() {
  if [[ $1 -eq 0 ]]; then
    echo "✅ $2 성공"
  else
    echo "❌ $2 실패"
    exit 1
  fi
}

echo "🚀🚀🚀 시작 ver=$VERSION"

# 배포 준비
check_input_version

build_jar
build_docker_image_in_local

# 배포 시작

change_image_version

echo "🚀🚀🚀 완료"