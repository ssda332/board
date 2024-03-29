# My Board
게시판 웹 사이트 개발 프로젝트

Link : https://yjboard.site
* * *

## 사용 기술
### Framework / Library
- Java 11
- SpringBoot 2.7.14
- SpringBoot Security
- Spring Data JPA
- Mybatis
- Thymeleaf
- Javascript
- Jquery

### DB
- Oracle Database 19c

### Infra
- AWS EC2
- AWS S3
- AWS Route53
- AWS CodeDeploy
- Github Actions
- Oracle Cloud Database

* * *
## 핵심 요구사항
- MVC 프레임워크 기반 웹 어플리케이션 서버 구현
- Spring Security와 JWT을 이용한 인증/인가 처리
- REST 기반의 서비스 API 구현
- Junit5 기반 단위 테스트 구현
- GitHub Actions, AWS CodeDeploy를 활용한 CI/CD 구현

* * *
## 기능
- 회원가입, 로그인, OAuth2 로그인(google)
- 관리자 메뉴(유저, 카테고리, 게시글, 댓글)
- 게시판, 댓글 CRUD(글 열람, 글쓰기, 댓글달기)

* * *
## 시스템 아키텍처

![system architecture](https://github.com/ssda332/board/assets/82029665/5d80bd89-0a00-4ab6-b736-57336655a0b0)



* * *
## ERD
![스크린샷 2024-02-10 195027](https://github.com/ssda332/board/assets/82029665/3839310a-7cfe-4503-bbec-c2565ca4057f)

* * *
## 변경 사항
### CI/CD
빌드 및 배포 자동화를 Github Actions와 AWS CodeDeploy, S3를 이용해 구현하였습니다.  
원격 저장소인 Github에 코드 변경사항을 브랜치에 push하는 이벤트가 발생하면 워크플로우가 발생합니다.
서버(runner)에서 워크플로우를 실행해 빌드 후 S3에 업로드, 그리고 업로드된 빌드 파일을 AWS CodeDeploy를 사용하여 배포합니다.
설정정보 민감한 내용 처리를 위해 개발환경에 따른 설정정보 파일를 분리하였습니다.

[[CI/CD 관련 자세한 내용]](https://yjboard.site/article/16)

[[워크플로우 파일]](https://github.com/ssda332/board/blob/2/.github/workflows/build-gradle.yml)  
[[appspec.yml 파일]](https://github.com/ssda332/board/blob/2/appspec.yml)  
[[빌드 스크립트 파일]](https://github.com/ssda332/board/tree/2/scripts)

![image](https://github.com/ssda332/board/assets/82029665/7d2ad7ac-d5a8-467d-aa1e-91cc86535694)
![image](https://github.com/ssda332/board/assets/82029665/c1cea2e3-a502-4431-ba08-b6f9c7c1bc55)

### **트러블 슈팅 - JWT 토큰 위치**

- 보안상 문제로 Refresh Token의 저장 위치를 Cookie로 변경하였습니다.

[트러블 슈팅 - JWT 토큰 위치 설명 링크](https://yjboard.site/article/14)

## 핵심 기능 설명
### Spring Security와 JWT을 이용한 인증/인가 처리

![JWT 인증 drawio](https://github.com/ssda332/board/assets/82029665/4d41118c-58f2-44c5-82e9-12c4978e3f28)

Spring Security와 JWT을 이옹해서 인증/인가 처리를 하였습니다.
Seurity Filter단에서 JWT를 처리하는 Custom Filter를 만들고, 만료와 권한 인증 실패 등
예외 발생시 응답 메세지에 상태코드를 주도록 구현했습니다.

Access Token 만료시 재발급 요청으로 Refresh Token을 보내고 Access Token 만료 시간을 짧게 가져가서 탈취될 경우를 대비하였습니다.
DB에 Refresh Token을 저장해서 검증시 오직 하나의 Refresh Token만 유효하도록 구현하였습니다.

### 소셜 로그인 (Google)
![소셜 로그인 시퀀스](https://github.com/ssda332/board/assets/82029665/e5fe0cbb-04ff-4dd6-83e7-cd32c153f5a2)

Google 서버와 통신하여 유저 정보를 가져오고, 해당 정보와 토큰을 만들어 소셜 로그인을 구현하였습니다.



### 게시판 CRUD 기능 (계층형 댓글, 계층형 카테고리)
![스크린샷 2023-12-05 164750](https://github.com/ssda332/board/assets/82029665/fe11c35c-2bb7-41c0-95ee-861edc24b147)
![스크린샷 2023-12-05 165205](https://github.com/ssda332/board/assets/82029665/604cea61-5fc4-4930-abfa-d75d8e625d3b)
![스크린샷 2023-12-05 165226](https://github.com/ssda332/board/assets/82029665/f57d7d1c-9592-4181-acb4-6af6ca575082)
![스크린샷 2023-12-06 134511](https://github.com/ssda332/board/assets/82029665/da565147-f5a6-4bd2-a773-2613ef254eb3)

기본적인 게시판의 CRUD 기능을 구현하였습니다.
- 게시글은 페이징 처리로 일정량의 게시글 데이터만 조회되게 했습니다.
- 댓글과 카테고리를 순환관계 엔티티로 설계하고 계층형 질의를 통해 대댓글과 상위,하위 카테고리를 구현하였습니다

### aws S3에 이미지 업로드
![글 작성 시퀀스 drawio](https://github.com/ssda332/board/assets/82029665/9c16d191-7e70-41d5-841f-68651629e783)

게시글에서 이미지 업로드시 임시 파일로 저장하고, 게시글이 등록되었을 때
이미지 원본 파일을 저장합니다. 임시 파일은 S3 버킷 내에서 수명 주기 규칙을 주어 일정 시간이
지나면 삭제돼서 이미지가 사용되지 않는 파일이 계속 쌓이지 않도록 하였습니다.

### **웹 애플리케이션의 도메인 및 보안 설정**

SSL 인증서를 받고 HTTPS 보안 프로토콜을 적용하였습니다. 또한, 도메인은 가비아에서 구매하였고, AWS Route 53을 활용해 해당 도메인과 웹 애플리케이션을 연결하는 작업을 완료하였습니다.

