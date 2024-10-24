# 🏍️ WaguWagu Delivery Rider Server

### 👤 Manager: Jinho Jo

## <br>📃 Core Features
### Management of delivery riders, records, and requests

- Management of delivery rider information
- Retrieve detailed delivery records
- Assign delivery requests after validating delivery rider qualifications
- Save and retrieve real-time locations of delivery riders
- Calculate distances between two points using latitude and longitude

## <br>🏷️ Project Link
https://github.com/WAGUWAGUUU/WAGUWAGU

## <br>⚙️ Key skills

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
   
## <br>🧾 API Documentation (with Swagger API)<br><br>

<img width="1071" alt="image" src="https://github.com/user-attachments/assets/79203c19-0e0c-4164-b0c8-c549cd63ff2d">
<img width="1082" alt="image" src="https://github.com/user-attachments/assets/0cb0abf2-15af-49e9-9226-945731e9a22f">

<br><br>
## 🧾 Data Flow Diagram<br><br>
<img width="839" alt="image" src="https://github.com/user-attachments/assets/6cf60977-d629-45f1-b93b-d206ef220b57"><br>
<img width="845" alt="image" src="https://github.com/user-attachments/assets/e00e47e9-047b-4077-ac90-d73307a5ba3c"><br>
<img width="845" alt="image" src="https://github.com/user-attachments/assets/3f992d04-efe7-45a2-81d3-5f2edeb797e9">


## 🔗 ERD<br>
<img width="886" alt="image" src="https://github.com/user-attachments/assets/ce980faf-a0e8-43d1-93fb-6c5c3fe2e94e">

##  <br>🔧 Performance Improvements

<img width="956" alt="image" src="https://github.com/user-attachments/assets/83f2b723-533c-46b7-818c-2deb3186d69e">




##  <br>🔧 Troubleshooting

**1. Duplicate data occurs when checking detailed delivery records by date<br><br>**
> * Cause: Multiple delivery records exist in a single day, leading to duplicate data.<br><br>
> * Solution: Structure with a parent table for dates and a child table for detailed records (OneToMany relationship).

<br>

**2. Encountering -bash: export: 'postgres-user=root': not a valid identifier error when running export postgres-user=root in a Linux environment.<br><br>**
> * Cause: The dash (-) cannot be used in environment variable names in Linux; use an underscore (_) instead.<br><br>
> * Solution: Use export postgres_user=root, and verify with echo $postgres_user to confirm it returns root.

<br>

**3. When setting environment variables in the Application.yaml file, having a space between the braces and the variable name (e.g., ${ POSTGRES-USER }) prevents the variable from being injected.<br><br>**
> * Cause: YAML files are space-sensitive<br><br>
> * Solution: Use ${POSTGRES-USER} to ensure proper injection of environment variable values during deployment with docker run or Kubernetes env section.

<br>

**4. Jenkins pipeline stuck in infinite loading<br><br>**
> * Phenomenon: : ```Started by GitHub push by jinho9482 [Pipeline] 
Start of Pipeline [Pipeline] 
node Still waiting to schedule task Waiting for next available executor```
<br><br>
> * Cause: Insufficient disk space in the /var/jenkins_home folder within the Jenkins Docker container.<br><br>
> * Temporary Solution: Manually trigger the pipeline to run.<br><br> <img width="1226" alt="image" src="https://github.com/user-attachments/assets/3ca8f4b0-43df-4608-b076-5a22a810530b"><br><br>
> <img width="1226" alt="image" src="https://github.com/user-attachments/assets/3ca8f4b0-43df-4608-b076-5a22a810530b"><br><br>
> * Permanent Solution: Configure the pipeline to delete logs from the folder after execution using the "Workspace Cleanup" plugin.

<br>

**5.  All API requests coming into Ingress (e.g., /api/v1/riders, /api/v1/orders) are redirected to the path: / in ingress-controller.yaml.<br><br>**
> * Cause: The root path (/) is set first, causing all API requests starting with / to be sent to the service linked with / <br><br>
> * Solution: Move the root path to the end.
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
>           // Move root path to the end    
>           - path: / 
>             pathType: Prefix
>             backend:
>               service:
>                 name: wgwg-store-front
>                 port:
>                   number: 80
>```

<br>

**6. Poor HTTPS communication between the app and AWS EKS cluster server (unresolved)<br><br>**
> * Phenomenon: HTTPS communication works well between web and server, but fails between app and server.<br><br>
> * Findings: Axios network error occurs @app (no error code), and no logs appear in nginx with the SSL certificate.<br><br>
> * Suspected Cause: The web browser can authenticate, but the Android device lacks a certificate to authenticate that certificate.<br><br>
> * Action Taken: Directly included the certificate in the Android XML file.<br><br>
> * Result: This configuration blocks HTTP communication (OAuth authentication), preventing progress.<br><br>
> * Current Status: Communication via HTTP.<br><br>
> * Future Action: Analyze additional causes to restore HTTPS communication.<br><br>
> * Note: Current network flow requires consolidation of two existing nginx load balancers into one. 
> <img width="1145" alt="image" src="https://github.com/user-attachments/assets/f2a32a48-d519-4f32-af1f-8865a7572ea6">
