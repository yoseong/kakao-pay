# kakao-pay 머니 뿌리기


## 핵심 문제해결 전략
일반적인 REST API 서버 구조로 요구사항 구현이 가능해 Spring Boot, JPA, MySQL 등을 활용해 REST API 서버를 구현했습니다.

## 패키지 구성
Controller, Service, Persistence 세개의 계층으로 최상위 패키지가 구성되고, 그 하위에 DTO, Repository, Entity 패키지가 있습니다.

![Alt text](https://raw.githubusercontent.com/yoseong/kakao-pay/master/img/pakage-hierarchy.PNG)

각 패키지의 역활은 다음과 같습니다. 
* Controller : 클라이언트의 요청을 받아 Service 계층의 비즈니스 로직을 호출하고 결과를 돌려줍니다.
  * DTO : 클라이언트 및 계층간 데이터 교환을 위한 객체, 이 프로젝트에서는 주로 API Request/Response Body 를 위해 사용됩니다.
* Service : 비즈니스 로직을 수행하며 이를 위해 Persistence 계층을 통해 데이터를 변경/생성/조회 합니다.
* Persistence : 데이터베이스에 접근해 데이터를 처리합니다. 
  * Repository : 데이터베이스 접근을 위한 인터페이스 입니다.
  * Entity : 데이터베이스 테이블의 데이터를 가지고 있는 객체입니다. 

## 서버 실행 방법
다음 명령어를 사용해 서버를 실행 합니다. 실행파일 위치는 target 폴더 안에 있습니다.
> **java -jar kakao-pay-0.0.1-SNAPSHOT.jar**

### 주의 사항
* Mysql 이 먼저 실행되고 있어야 합니다.
* kakao-pay 데이터베이스가 생성되어 있어야 합니다. 
* 계정 및 암호 : root/root


## API 테스트 방법

### Swagger UI
주소 : http://localhost:8080/swagger-ui.html (로컬에서 실행하는 경우)

요구사항에 명시된 3개의 API 를 테스트 할 수 있습니다.

![Alt text](https://raw.githubusercontent.com/yoseong/kakao-pay/master/img/swagger-ui.PNG)

### 에러 코드 및 설명
#### 뿌리기 API
* **101** : 금액이 1원 미만 이거나 천만원 초과 (최대값은 임의로 설정했습니다.)
* **102** : 인원이 1명 미만 이거나 100명 초과 (최대값은 임의로 설정했습니다.)
* **103** : 금액이 인원수보다 적음
* **104** : 중복된 토큰 (재시도 필요)
#### 받기 API
* **201** : 잘못된 토큰
* **202** : 자신이 뿌리기한 건은 자신이 받을 수 없음
* **203** : 뿌리기가 호출된 대화방과 다른 대화방
* **204** : 10분 초과되어 만료됨
* **205** : 같은 사용자가 두번 이상 호출함
* **206** : 모두 할당됨
#### 조회 API
* **301** : 잘못된 토큰 
* **302** : 다른 사용자의 뿌리기건 
## 유닛 테스트 
JUnit을 사용해 유닛테스트를 작성했습니다. 코드 커버리지는 95.1% 입니다.
![Alt text](https://raw.githubusercontent.com/yoseong/kakao-pay/master/img/code-coverage.PNG)


## 기타
* 테스트 편의를 위해 토큰은 암호화 해서 전달하지 않았습니다.
* 낮은 확률이지만 중복 토큰(서로 다른 뿌리기에 같은 토큰)이 가능합니다. 이때 에러를 전달하도록 구현했습니다. 
* 실제 환경에서는 토큰 자리수를 늘일 필요가 있을것 같습니다. 

