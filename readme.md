## Spring boot 기반 기본 프로젝트

### 구조
```
Project
└─src
  └─main
     ├─java
     │  └─com.admin.tool
     │     ├─api
     │     │  ├─admin
     │     │  ├─event
     │     │  ├─guild
     │     │  ├─shop
     │     │  └─user
     │     │      ├─controller
     │     │      ├─dao
     │     │      └─service
     │     └─common
     │         ├─aop
     │         │  ├─annotation
     │         │  └─aspect
     │         ├─config
     │         │  ├─db
     │         │  ├─exception
     │         │  ├─security
     │         │  └─web
     │         ├─model
     │         └─util
     └─resources
         ├─mapper
         │  ├─admin
         │  └─user
         └─message
```

### 개선
- 다중 데이터소스 처리 및 매퍼 공용화 처리
- 공용 익셉션 처리
- 내장 톰캣
- 