insert into TB_MEM (MEM_ID, MEM_USERID, MEM_PASSWORD, MEM_NICKNAME, MEM_ACTIVATED, MEM_REG_DATE, MEM_UPT_DATE) values (1, 'adminTest', 'password', 'adminTest', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into TB_MEM (MEM_ID, MEM_USERID, MEM_PASSWORD, MEM_NICKNAME, MEM_ACTIVATED, MEM_REG_DATE, MEM_UPT_DATE) values (2, 'userTest', 'password', 'userTest', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

insert into TB_AUTH (AUTH_NAME) values ('ROLE_USER');
insert into TB_AUTH (AUTH_NAME) values ('ROLE_ADMIN');

insert into TB_MEM_AUTH (MEM_ID, AUTH_NAME) values (1, 'ROLE_USER');
insert into TB_MEM_AUTH (MEM_ID, AUTH_NAME) values (1, 'ROLE_ADMIN');
insert into TB_MEM_AUTH (MEM_ID, AUTH_NAME) values (2, 'ROLE_USER');

insert into TB_ARTICLE_CTG (CTG_ID, CTG_TITLE, CTG_HIERACHY, CTG_REG_DATE, CTG_UPT_DATE, CTG_SORT, CTG_PRT_ID, CTG_ACTIVATED) values ('test1', '부모카테고리1', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, null, 1);
insert into TB_ARTICLE_CTG (CTG_ID, CTG_TITLE, CTG_HIERACHY, CTG_REG_DATE, CTG_UPT_DATE, CTG_SORT, CTG_PRT_ID, CTG_ACTIVATED) values ('test2', '자식카테고리1-1', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'test1', 1);
insert into TB_ARTICLE_CTG (CTG_ID, CTG_TITLE, CTG_HIERACHY, CTG_REG_DATE, CTG_UPT_DATE, CTG_SORT, CTG_PRT_ID, CTG_ACTIVATED) values ('test3', '자식카테고리1-2', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 'test1', 1);
insert into TB_ARTICLE_CTG (CTG_ID, CTG_TITLE, CTG_HIERACHY, CTG_REG_DATE, CTG_UPT_DATE, CTG_SORT, CTG_PRT_ID, CTG_ACTIVATED) values ('test4', '자식카테고리1-3', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, 'test1', 1);
insert into TB_ARTICLE_CTG (CTG_ID, CTG_TITLE, CTG_HIERACHY, CTG_REG_DATE, CTG_UPT_DATE, CTG_SORT, CTG_PRT_ID, CTG_ACTIVATED) values ('test5', '부모카테고리2', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, null, 1);
insert into TB_ARTICLE_CTG (CTG_ID, CTG_TITLE, CTG_HIERACHY, CTG_REG_DATE, CTG_UPT_DATE, CTG_SORT, CTG_PRT_ID, CTG_ACTIVATED) values ('test6', '자식의자식카테고리1-3-1', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 'test4', 1);
insert into TB_ARTICLE_CTG (CTG_ID, CTG_TITLE, CTG_HIERACHY, CTG_REG_DATE, CTG_UPT_DATE, CTG_SORT, CTG_PRT_ID, CTG_ACTIVATED) values ('test7', '자식의자식카테고리1-3-2', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 'test4', 1);
insert into TB_ARTICLE_CTG (CTG_ID, CTG_TITLE, CTG_HIERACHY, CTG_REG_DATE, CTG_UPT_DATE, CTG_SORT, CTG_PRT_ID, CTG_ACTIVATED) values ('test8', '부모카테고리3', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3, null, 1);

insert into TB_ARTICLE (ATC_NUM, ATC_TITLE, ATC_CONTENT, ATC_WRITER, ATC_ACTIVATED, CTG_ID, ATC_REG_DATE, ATC_UPT_DATE, ATC_VIEWS) values ('test1', '테스트제목1', '테스트내용1', 1, 1, 'test2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
insert into TB_ARTICLE (ATC_NUM, ATC_TITLE, ATC_CONTENT, ATC_WRITER, ATC_ACTIVATED, CTG_ID, ATC_REG_DATE, ATC_UPT_DATE, ATC_VIEWS) values ('test2', '테스트제목2', '테스트내용2', 1, 1, 'test2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
insert into TB_ARTICLE (ATC_NUM, ATC_TITLE, ATC_CONTENT, ATC_WRITER, ATC_ACTIVATED, CTG_ID, ATC_REG_DATE, ATC_UPT_DATE, ATC_VIEWS) values ('test3', '테스트제목3', '테스트내용3', 1, 1, 'test2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

