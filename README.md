# My Board
게시판 웹 사이트 개발 프로젝트
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
- Oracle

### Infra
- AWS EC2
- AWS S3
- AWS Route53

* * *
## 핵심 요구사항
- MVC 프레임워크 기반 웹 어플리케이션 서버 구현
- Spring Security와 JWT을 이용한 인증/인가 처리
- REST 기반의 서비스 API 구현
- Junit5 기반 단위 테스트 구현

* * *
## 기능
- 회원가입, 로그인, OAuth2 로그인(google)
- 관리자 메뉴(유저, 카테고리, 게시글, 댓글)
- 게시판, 댓글 CRUD(글 열람, 글쓰기, 댓글달기)

* * *
## 시스템 아키텍처

![시스템 아키텍쳐](https://github.com/ssda332/board/assets/82029665/2c6c8a37-6e7c-40f2-95fa-f2b126faff29)


* * *
## ERD
![스크린샷 2024-02-10 195027](https://github.com/ssda332/board/assets/82029665/3839310a-7cfe-4503-bbec-c2565ca4057f)

* * *
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

