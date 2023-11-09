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

### DB
- Oracle

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
- 

* * *
## WAS 아키텍처

(사진)
WAS와 DB 서버만을 둔 MPA 방식의 구조로 설계하였습니다. 추후 클라이언트 서버를 따로 둘 것을 염두하여
REST API 통신 방식으로 설계하였습니다.

* * *
## ERD
(사진)

* * *
## 시퀀스 다이어그램


