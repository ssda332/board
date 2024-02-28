CREATE TABLE TB_AUTH (
                         AUTH_NAME VARCHAR(50) NOT NULL,
                         PRIMARY KEY (AUTH_NAME)
);

CREATE TABLE TB_MEM (
                        MEM_ID VARCHAR(200) NOT NULL,
                        MEM_USERID VARCHAR(200) NOT NULL,
                        MEM_PASSWORD VARCHAR(200),
                        MEM_NICKNAME VARCHAR(50),
                        MEM_ACTIVATED INTEGER,
                        MEM_REG_DATE DATE,
                        MEM_UPT_DATE DATE,
                        PRIMARY KEY (MEM_ID)
);

CREATE TABLE TB_MEM_AUTH (
                             MEM_ID VARCHAR(200) NOT NULL,
                             AUTH_NAME VARCHAR(50) NOT NULL,
                             PRIMARY KEY (MEM_ID, AUTH_NAME),
                             FOREIGN KEY (MEM_ID) REFERENCES TB_MEM (MEM_ID)
);

CREATE TABLE TB_REF_TKN (
                            MEM_ID VARCHAR(200) NOT NULL,
                            REF_TKN VARCHAR(400),
                            PRIMARY KEY (MEM_ID),
                            FOREIGN KEY (MEM_ID) REFERENCES TB_MEM (MEM_ID)
);



CREATE TABLE TB_ARTICLE (
                            ATC_NUM VARCHAR(50) NOT NULL,
                            ATC_TITLE VARCHAR(200),
                            ATC_CONTENT CLOB,
                            ATC_WRITER VARCHAR(200) NOT NULL,
                            ATC_ACTIVATED INTEGER,
                            CTG_ID VARCHAR(50) NOT NULL,
                            ATC_REG_DATE DATE,
                            ATC_UPT_DATE DATE,
                            ATC_VIEWS INTEGER DEFAULT 0 NOT NULL,
                            PRIMARY KEY (ATC_NUM)
);

CREATE TABLE TB_ARTICLE_CTG (
                                CTG_ID VARCHAR(50) NOT NULL,
                                CTG_TITLE VARCHAR(200),
                                CTG_HIERACHY INTEGER,
                                CTG_REG_DATE DATE,
                                CTG_UPT_DATE DATE,
                                CTG_SORT INTEGER,
                                CTG_PRT_ID VARCHAR(50),
                                CTG_ACTIVATED INTEGER,
                                PRIMARY KEY (CTG_ID)
);

CREATE TABLE TB_COMMENT (
                            CMT_NUM VARCHAR(50) NOT NULL,
                            CMT_CONTENT VARCHAR(200) NOT NULL,
                            CMT_HIERACHY INTEGER NOT NULL,
                            CMT_ACTIVATED INTEGER NOT NULL,
                            CMT_REG_DATE DATE NOT NULL,
                            CMT_UPT_DATE DATE NOT NULL,
                            CMT_PRT_NUM VARCHAR(50),
                            MEM_ID VARCHAR(200) NOT NULL,
                            ATC_NUM VARCHAR(50) NOT NULL,
                            PRIMARY KEY (CMT_NUM),
                            FOREIGN KEY (MEM_ID) REFERENCES TB_MEM (MEM_ID),
                            FOREIGN KEY (ATC_NUM) REFERENCES TB_ARTICLE (ATC_NUM)
);

CREATE TABLE TB_ARTICLE_IMG (
                                IMG_URL VARCHAR(200) NOT NULL,
                                ATC_NUM VARCHAR(50) NOT NULL,
                                PRIMARY KEY (IMG_URL, ATC_NUM),
                                FOREIGN KEY (ATC_NUM) REFERENCES TB_ARTICLE (ATC_NUM)
);

CREATE SEQUENCE ATC_NUM_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE CMT_NUM_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE CTG_ID_SEQ START WITH 1 INCREMENT BY 1 CACHE 20;
CREATE SEQUENCE MEM_ID_SEQ START WITH 1 INCREMENT BY 1 CACHE 20;
CREATE SEQUENCE TOKEN_ID_SEQ START WITH 1 INCREMENT BY 1 CACHE 20;