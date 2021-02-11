# kakao-pay 머니 뿌리기


## 핵심 문제해결 전략
일반적인 REST API 서버 구조로 요구사항 구현이 가능해, Spring Boot, JPA, MySQL 등을 활용해 REST API 서버를 구현했습니다.


## 프로젝트의 구성

controller 
  dto
    dto files
  controller files

Controller, Service, Persistence 세개의 Layer 로 최상위 패키지를 구성했습니다. 역활은 다음과 같습니다.
* Controller : 클라이언트의 요청을 받아 Service Layer 의 비즈니스 로직을 호출하고, 결과를 돌려줍니다.
* Service : 비즈니스 로직을 수행합니다. 





2.서버 실행

3.테스트 방법

3.1. Swagger

3.2. Junit

4. 기타

4.1. 에러코드
