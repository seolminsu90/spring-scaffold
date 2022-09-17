## Spring boot 기반 기본 프로젝트

Spring 최신 버전 (2.7) 기준으로 이것 저것 만들어봄

- 기본 골격
- Spring security rest + jwt
- Route datasource
- Xa Atomikos Transaction
- Mybatis 기반 프로젝트
- SPA oauth login 예시

### SPA Oauth 연동 흐름 정리 []는 제 3자의 리소스

SPA의 경우 외부 Oauth 연동 방식이 조금 달라 예시로 개발
```
1. Frontend -> [Oauth Server]
- 로그인 과정을 통해 AuthorizationCode 인가 코드를 발급받는다.(대체로 수명이 짧거나 1회성으로발급)
- *Frontend의 별도 oauth 로그인 처리용 빈 페이지를 redirect_url로 등록한다.

2. Frontend -> Backend
- AuthorizationCode를 백엔드로 전달한다.

3. Backend -> [Oauth Server]
- AuthorizationCode를 이용하여 AccessToken 을 가져온다. 이때 redirect_url은 1에서 설정한 Frontend의 리다이렉트이다.(검증)
- AccessToken를 이용하여 Oauth Server그룹의 [Resource(유저 정보 등)] 을 가져온다.
- [Resource(유저 정보)]를 가지고 서비스 전용 로그인 처리 및 권한 등을 담은 JWT를 발급한다.

4. Backend -> Frontend
- 생성한 JWT를 프론트엔드로 전달한다.
- 설정한 oauth 처리용 빈 페이지에서 JWT관련 저장 처리 후 홈 페이지로 location 이동한다.

Frontend
- 별도의 JWT 저장 및 api에 사용
```

기타 더 필요한 것들 수시로 추가
