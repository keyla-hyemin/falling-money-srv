# FALLING-MONEY-SRV

## 개요

* 머니 뿌리기, 받기, 뿌리기 건에 대한 정보(이력) 조회를 수행한다.

### Naming 관련

* FallingMoney는 '머니 뿌리기'의 서비스 도메인과 역할을 나타낸다.

## 핵심 전략
* 토큰 및 뿌리기 데이터 관리를 위해 외부 캐시(Redis)를 이용한다.
* Redis 서버의 접속 정보는 application.yml 설정으로 관리한다.
* Redis의 TTL 설정으로 토큰 만료시간을 관리한다.
* 각 서비스별 비즈니스 로직은 서비스 클래스에서 수행한다.

