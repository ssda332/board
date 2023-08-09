/*insert into TB_MEM (MEM_ID, MEM_USERID, MEM_PASSWORD, MEM_NICKNAME, MEM_ACTIVATED) values (MEM_ID_SEQ.NEXTVAL, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);
insert into TB_MEM (MEM_ID, MEM_USERID, MEM_PASSWORD, MEM_NICKNAME, MEM_ACTIVATED) values (MEM_ID_SEQ.NEXTVAL, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 1);

insert into TB_AUTH (AUTH_NAME) values ('ROLE_USER');
insert into TB_AUTH (AUTH_NAME) values ('ROLE_ADMIN');

insert into TB_MEM_AUTH (MEM_ID, AUTH_NAME) values (1, 'ROLE_USER');
insert into TB_MEM_AUTH (MEM_ID, AUTH_NAME) values (1, 'ROLE_ADMIN');
insert into TB_MEM_AUTH (MEM_ID, AUTH_NAME) values (2, 'ROLE_USER');*/