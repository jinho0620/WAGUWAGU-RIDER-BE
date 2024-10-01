# 🏍️ 와구와구 배달 기사 서버

### 👤 담당자 : 조진호

## <br>📃 핵심 기능
### 배달 기사, 내역, 요청 건 관리 / 배달 기사 실시간 위치 관리 및 거리 계산

- 배달 기사 정보 관리
- 배달 상세 내역 가져오기
- 배달 기사 자격 검증 후 배달 요청 건 할당
- 배달 기사 실시간 위치 저장 및 가져오기
- 두 지점 위,경도로 사이의 거리 계산

## <br>🏷️ 전체 프로젝트 링크
https://github.com/WAGUWAGUUU/WAGUWAGU

## <br>⚙️ 기술스택

### ✔️ Server Framework
![Spring-Boot](https://img.shields.io/badge/spring--boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)

### ✔️ Database
![postgresql](https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![redis](https://img.shields.io/badge/redis-FF4438?style=for-the-badge&logo=redis&logoColor=white)

### ✔️ Message Broker  
![apachekafka](https://img.shields.io/badge/apachekafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)

### ✔️ Deployment  
![amazoneks](https://img.shields.io/badge/amazoneks-232F3E?style=for-the-badge&logo=amazoneks&logoColor=white)
![googlecloud](https://img.shields.io/badge/googlecloud-4285F4?style=for-the-badge&logo=googlecloud&logoColor=white)

### ✔️ Container & Container orchestration
![docker](https://img.shields.io/badge/docker-496ED?style=for-the-badge&logo=docker&logoColor=white)
![kubernetes](https://img.shields.io/badge/kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)
![helm](https://img.shields.io/badge/helm-0F1689?style=for-the-badge&logo=helm&logoColor=white)

### ✔️ CI/CD  
![jenkins](https://img.shields.io/badge/jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)
   
## <br>🧾 API 명세서 (with Swagger API)<br><br>

<img width="1085" alt="image" src="https://github.com/user-attachments/assets/4ebb2ae2-7bbb-41c7-ac1b-38759efd342b">
<img width="1085" alt="image" src="https://github.com/user-attachments/assets/a584ba9c-c81a-42cb-a5ac-0a80ab9a693c">

<br><br>
## 🧾 데이터 흐름도<br><br>
<img width="839" alt="image" src="https://github.com/user-attachments/assets/6cf60977-d629-45f1-b93b-d206ef220b57"><br>
<img width="845" alt="image" src="https://github.com/user-attachments/assets/e00e47e9-047b-4077-ac90-d73307a5ba3c"><br>
<img width="845" alt="image" src="https://github.com/user-attachments/assets/3f992d04-efe7-45a2-81d3-5f2edeb797e9">


## 🔗 ERD<br>
<img width="886" alt="image" src="https://github.com/user-attachments/assets/ce980faf-a0e8-43d1-93fb-6c5c3fe2e94e">

##  <br>🔧 성능 개선 사항

<img width="956" alt="image" src="https://github.com/user-attachments/assets/83f2b723-533c-46b7-818c-2deb3186d69e">




##  <br>🔧 트러블 슈팅

**1. 날짜별 및 일별 상세 배달 내역을 확인할 때 중복 데이터 발생<br><br>**
> * 원인 : 하루에 여러 건의 배달 내역이 존재하기 때문에 중복 데이터가 발생한다.<br><br>
> * 해결 : 날짜를 갖는 부모 테이블과 상세 내역을 갖는 자식 테이블(OneToMany 관계)로 구성

<br>

**2. Linux 환경에서 ```export postgres-user=root```를 하면 ```-bash: export: `postgres-user=root': not a valid identifier```와 같은 에러가 뜬다.<br><br>**
> * 원인 : linux 환경에서 환경 변수이름에 - (dash)를 쓸 수 없다. 대신 _ (underscore)를 써야 한다.<br><br>
> * 해결 : ```export postgres_user=root``` 후 ```echo $postgres_user``` 를 하면 root로 제대로 뜬다.

<br>

**3. Application.yaml file에서 환경 변수 설정 시 ${ POSTGRES-USER }와 같이 중괄호와 변수명 사이에 space가 있으면 환경 변수 주입이 되지 않는다.<br><br>**
> * 원인 : Yaml file은 space가 있고 없음을 구분한다. (space-sensitive) <br><br>
> * 해결 : ${POSTGRES-USER} 로 진행해야 배포할 때 docker run 혹은 kubernetes의 env 항목의 환경 변수 값이 제대로 주입된다.

<br>

**4. Jenkins pipeline 무한 로딩 발생<br><br>**
> * 현상 : ```Started by GitHub push by jinho9482 [Pipeline] 
Start of Pipeline [Pipeline] 
node Still waiting to schedule task Waiting for next available executor```
<br><br>
> * 원인 : /var/jenkins_home 폴더에 용량 부족 @ Jenkins docker container <br><br>
> * 임시 해결 : 아래 버튼을 눌러 강제로 pipeline을 실행 시킨다.<br><br>
> <img width="1226" alt="image" src="https://github.com/user-attachments/assets/3ca8f4b0-43df-4608-b076-5a22a810530b"><br><br>
> * 근본 해결 : pipeline 실행 후 폴더의 log들을 지우도록 설정한다. "Workspace Cleanup" plugin 사용

<br>

**5. Ingress에 들어온 모든 api 요청 (ex. /api/v1/riders, /api/v1/orders)이 path : / 로 설정된 곳으로 이동한다. @ ingress-controller.yaml<br><br>**
> * 원인 : root path ( / ) 이 제일 처음 설정되어 있어 /로 시작하는 모든 api 요청은 / 와 연결된 service로 보내진다.<br><br>
> * 해결 : root path를 제일 뒤로 보냄
>```yaml
> spec:
>   ingressClassName: nginx
>   rules:
>     - http:
>         paths:
>           - path: /api/v1/orders
>             pathType: Prefix
>             backend:
>               service:
>                 name: order-waguwagu-order
>                 port:
>                   number: 8080
>           - path: /api/v1/auth
>             pathType: Prefix
>             backend:
>               service:
>                 name: wgwg-auth-server
>                 port:
>                   number: 8080
>           // root path를 제일 뒤로    
>           - path: / 
>             pathType: Prefix
>             backend:
>               service:
>                 name: wgwg-store-front
>                 port:
>                   number: 80
>```

<br>

**6. App과 AWS EKS cluster server 간 https 통신 불량 (미해결)<br><br>**
> * 현상 : web ~ server간 https 통신은 잘 되나, app ~ server 간 통신 불가<br><br>
> * 확인 내용 : Axios network error 발생 @app (Error code 없음)SSL 인증서를 가지고 있는 nginx 에도 log가 찍히지 않음<br><br>
> * 추측 원인 : Web browser에서는 인증이 되나, Android device에서는 해당 인증서를 인증할 인증서가 없음<br><br>
> * 취한 Action : Android xml 파일에 직접 인증서를 넣음<br><br>
> * 결과  : 해당 설정이 http 통신 (OAuth 인증)을 막아 진행 불가<br><br>
> * 현재 상황 : http로 통신<br><br>
> * 추후 Action : 추가 원인 분석을 통해 https로 원복 예정<br><br>
> * 참고 : 현재 network flow (nginx load balancer가 2개 존재하여 1개로 합칠 필요 있음)
> <img width="1145" alt="image" src="https://github.com/user-attachments/assets/f2a32a48-d519-4f32-af1f-8865a7572ea6">

