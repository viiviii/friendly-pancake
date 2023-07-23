# 로컬에서 minikube로 사용하기

> ⚠️ macOS m1 환경 기준으로 작성됨

<br>


## ⬇️ 설치

- [minikube 설치](https://minikube.sigs.k8s.io/docs/start/)

<br>


## 🛠️ 설정

```bash
# minikube 시작
minikube start

# ingress 활성화
minikube addons enable ingress

# ingress 생성
kubectl apply -f k8s/ingress.yaml

# database secret 생성
DATABASE_URL=데이터베이스_주소 DATABASE_USERNAME=사용자 DATABASE_PASSWORD=비밀번호 \
envsubst < k8s/secret-database.yaml.template | kubectl apply -f -
```

<br>


## 🏃🏻‍♀️ 사용

### 필수

```bash
# ingress 서비스 터널 시작
minikube tunnel
```

* OS 비밀번호까지 입력 완료해야 함
* 사용하는 동안 터미널 유지해야 함

### 선택

```bash
# dashboard ui 사용
minikube dashboard
```

* 사용하는 동안 터미널 유지해야 함

<br>


## 🔥 제거

```bash
kubectl delete service/api-service
kubectl delete deployment/api-deployment
kubectl delete ingress/pancake-ingress
kubectl delete secret/database-secret
```

or 

```bash
minikube delete
```
