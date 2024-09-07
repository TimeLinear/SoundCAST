--계정생성
--CREATE USER C##SOUNDCAST IDENTIFIED BY SOUNDCAST;
--GRANT CONNECT, RESOURCE, UNLIMITED TABLESPACE TO C##SOUNDCAST;


---------------------------------------------------------------------------------
DROP SEQUENCE SEQ_SONG_NO;
DROP SEQUENCE SEQ_SONG_FILE_NO;
DROP SEQUENCE SEQ_SONG_IMAGE_NO;

DROP SEQUENCE SEQ_MEMBER_NO;
DROP SEQUENCE SEQ_MEMBER_BANNER_NO;
DROP SEQUENCE SEQ_MEMBER_PROFILE_IMAGE_NO;
DROP SEQUENCE SEQ_COMMENT_NO;
DROP SEQUENCE SEQ_REPORT_NO;

CREATE SEQUENCE SEQ_SONG_NO ;
CREATE SEQUENCE SEQ_SONG_FILE_NO ;
CREATE SEQUENCE SEQ_SONG_IMAGE_NO ;

CREATE SEQUENCE SEQ_MEMBER_NO ;
CREATE SEQUENCE SEQ_MEMBER_BANNER_NO ;
CREATE SEQUENCE SEQ_MEMBER_PROFILE_IMAGE_NO ;
CREATE SEQUENCE SEQ_COMMENT_NO ;
CREATE SEQUENCE SEQ_REPORT_NO ;
---------------------------------------------------------------------------------

DROP TABLE DOWNLOAD_COUNT;
DROP TABLE DOWNLOAD;
DROP TABLE FOLLOW;
DROP TABLE "COMMENT";
DROP TABLE REPORT;

DROP TABLE SONG;
DROP TABLE SONG_IMAGE;
DROP TABLE SONG_FILE;
DROP TABLE SONG_PATH;
DROP TABLE SONG_IMAGE_PATH;

DROP TABLE MOOD;
DROP TABLE GENRE;

DROP TABLE MEMBER;
DROP TABLE MEMBER_GRADE;
DROP TABLE MEMBER_SOCIAL;
DROP TABLE SOCIAL_CATEGORY;
DROP TABLE MEMBER_BANNER;
DROP TABLE PROFILE_IMAGE;

--멤버

CREATE TABLE PROFILE_IMAGE (
	PROFILE_IMAGE_NO    NUMBER,
	PROFILE_IMAGE_PATH  VARCHAR2(255) NOT NULL,
    
    CONSTRAINT PK_PROFILE_IMAGE_PIN PRIMARY KEY (PROFILE_IMAGE_NO),
    CONSTRAINT UQ_PROFILE_IMAGE_PIP UNIQUE(PROFILE_IMAGE_PATH)
);

CREATE TABLE MEMBER_BANNER (
	MEMBER_BANNER_NO	NUMBER,
	MEMBER_BANNER_PATH	VARCHAR2(255) NOT NULL,
    
    CONSTRAINT PK_MEMBER_BANNER_MBN PRIMARY KEY (MEMBER_BANNER_NO),
    CONSTRAINT UQ_MEMBER_BANNER_MBP UNIQUE(MEMBER_BANNER_PATH)
    
);


CREATE TABLE MEMBER_GRADE (
	MEMBER_GRADE_NO NUMBER,
	MEMBER_GRADE_NAME	VARCHAR2(15) NOT NULL,
    
    CONSTRAINT PK_MEMBER_GRADE_MGN PRIMARY KEY (MEMBER_GRADE_NO)
);

COMMENT ON COLUMN MEMBER_GRADE.MEMBER_GRADE_NO IS '0 - 관리자, 1 - 사용자';


CREATE TABLE MEMBER (
	MEMBER_NO	NUMBER,
	MEMBER_PROFILE_IMAGE_NO	NUMBER	NOT NULL,
	MEMBER_BANNER_NO	NUMBER	DEFAULT 0	NOT NULL,
	MEMBER_GRADE_NO	NUMBER	DEFAULT 1	NOT NULL,
	MEMBER_NICKNAME	VARCHAR2(15 CHAR)	NOT NULL,
	MEMBER_EMAIL	VARCHAR2(50 CHAR),
	MEMBER_INTRODUCE	VARCHAR2(100),
	MEMBER_CREATE_DATE	DATE	DEFAULT SYSDATE	NOT NULL,
	MEMBER_MODIFY_DATE	DATE	DEFAULT SYSDATE	NOT NULL,
	MEMBER_STATUS	CHAR(1 CHAR)	DEFAULT 'Y',
    
    CONSTRAINT FK_MEMBER_MPIN FOREIGN KEY (MEMBER_PROFILE_IMAGE_NO) REFERENCES PROFILE_IMAGE (PROFILE_IMAGE_NO),
    CONSTRAINT FK_MEMBER_MBN FOREIGN KEY (MEMBER_BANNER_NO) REFERENCES MEMBER_BANNER (MEMBER_BANNER_NO),
    CONSTRAINT FK_MEMBER_MGN FOREIGN KEY (MEMBER_GRADE_NO) REFERENCES MEMBER_GRADE (MEMBER_GRADE_NO),
    CONSTRAINT PK_MEMBER_MN PRIMARY KEY (MEMBER_NO),
    CONSTRAINT UQ_MEMBER_ME UNIQUE(MEMBER_EMAIL)
);


CREATE TABLE MEMBER_SOCIAL (
    MEMBER_SOCIAL_MEMBER_NO NUMBER,
    MEMBER_SOCIAL_SOCIAL_NAME VARCHAR2(50) NOT NULL,
    MEMBER_SOCIAL_SOCIAL_ID VARCHAR2(200) NOT NULL,
    
    CONSTRAINT PK_MEMBER_SOCIAL_MSMN PRIMARY KEY(MEMBER_SOCIAL_MEMBER_NO),
    CONSTRAINT UQ_MEMBER_SOCIAL_MSSI UNIQUE(MEMBER_SOCIAL_SOCIAL_ID),
    CONSTRAINT UQ_MEMBER_SOCIAL_MSSN UNIQUE(MEMBER_SOCIAL_SOCIAL_NAME)
);


CREATE TABLE FOLLOW (
	FOLLOW_MEMBER_NO	NUMBER,
	FOLLOW_TARGET_MEMBER_NO	NUMBER,
    
    CONSTRAINT FK_FOLLOW_FMN FOREIGN KEY (FOLLOW_MEMBER_NO) REFERENCES MEMBER (MEMBER_NO),
    CONSTRAINT FK_FOLLOW_FTMN FOREIGN KEY (FOLLOW_TARGET_MEMBER_NO) REFERENCES MEMBER (MEMBER_NO),
    CONSTRAINT PK_FOLLOW_FMN_FTN PRIMARY KEY(FOLLOW_MEMBER_NO, FOLLOW_TARGET_MEMBER_NO)
   
);


CREATE TABLE "COMMENT" (
	COMMENT_NO	NUMBER,
	COMMENT_WRITER_MEMBER_NO	NUMBER	NOT NULL,
	COMMENT_MEMBER_NO	NUMBER NOT NULL,
	COMMENT_TEXT	VARCHAR2(100),
	COMMENT_CREATE_DATE	DATE	DEFAULT SYSDATE	NOT NULL,
	COMMENT_MODIFY_DATE	DATE	DEFAULT SYSDATE	NOT NULL,
    
    CONSTRAINT FK_COMMENT_CWUN FOREIGN KEY (COMMENT_WRITER_MEMBER_NO) REFERENCES MEMBER (MEMBER_NO),
    CONSTRAINT PK_COMMENT_CN PRIMARY KEY (COMMENT_NO)
    
);


-- 음원

CREATE TABLE MOOD (
	MOOD_NO	NUMBER,
	MOOD_NAME	VARCHAR2(30)    NOT NULL,
    CONSTRAINT PK_MOOD_MN PRIMARY KEY (MOOD_NO),
    CONSTRAINT UQ_MOOD_MNAME UNIQUE (MOOD_NAME)
);

COMMENT ON COLUMN MOOD.MOOD_NO IS '1부터 시작';

CREATE TABLE GENRE (
	GENRE_NO	NUMBER	NOT NULL,
	GENRE_NAME	VARCHAR2(30)	NOT NULL,
    CONSTRAINT PK_GENRE_GN PRIMARY KEY (GENRE_NO),
    CONSTRAINT UQ_GENRE_GNAME UNIQUE (GENRE_NAME)
);

COMMENT ON COLUMN GENRE.GENRE_NO IS '1부터 시작';

CREATE TABLE SONG_IMAGE_PATH (
	SONG_IMAGE_PATH_NO	NUMBER	NOT NULL,
	SONG_IMAGE_PATH_NAME	VARCHAR2(225)	NULL,
    
    CONSTRAINT PK_IMAGE_PATH_SIPN PRIMARY KEY (SONG_IMAGE_PATH_NO)
);


CREATE TABLE SONG_IMAGE (
	SONG_IMAGE_NO	NUMBER,
	SONG_IMAGE_PATH_NO  NUMBER	NOT NULL,
	SONG_IMAGE_NAME	VARCHAR2(40)	NOT NULL,
    
    CONSTRAINT FK_SONG_IMAGE_SIPN FOREIGN KEY (SONG_IMAGE_PATH_NO) REFERENCES SONG_IMAGE_PATH (SONG_IMAGE_PATH_NO),
    CONSTRAINT PK_SONG_IMAGE_SINO PRIMARY KEY (SONG_IMAGE_NO),
    CONSTRAINT UQ_SONG_IMAGE_SINAME UNIQUE(SONG_IMAGE_NAME)
);


CREATE TABLE SONG_PATH (
	SONG_PATH_NO	NUMBER,
	SONG_PATH_NAME	VARCHAR2(225) NOT NULL,
    
    CONSTRAINT PK_SONG_PATH_FPN PRIMARY KEY (SONG_PATH_NO)
);
COMMENT ON COLUMN SONG_PATH.SONG_PATH_NAME IS '윈도우 경로명 최대 길이 - 파일명 최대 길이';



CREATE TABLE SONG_FILE (
	SONG_FILE_NO	NUMBER,
	SONG_FILE_SONG_PATH_NO	NUMBER		NOT NULL,
	SONG_FILE_CHANGE_NAME	VARCHAR2(40)		NOT NULL,
	SONG_FILE_ORIGIN_NAME	VARCHAR2(150)		NOT NULL,
    
    CONSTRAINT FK_SONG_FILE_SFPN FOREIGN KEY (SONG_FILE_SONG_PATH_NO) REFERENCES SONG_PATH (SONG_PATH_NO),
    CONSTRAINT PK_SONG_FILE_SFN PRIMARY KEY (SONG_FILE_NO),
    CONSTRAINT UQ_SONG_SFCN UNIQUE(SONG_FILE_CHANGE_NAME)
);


ALTER TABLE SONG ADD SONG_DURATION NUMBER;

CREATE TABLE SONG (
	SONG_NO	NUMBER,
	SONG_MEMBER_NO	NUMBER		NOT NULL,
	SONG_MOOD_NO	NUMBER		NOT NULL,
	SONG_GENRE_NO	NUMBER		NOT NULL,
	SONG_IMAGE_NO	NUMBER	DEFAULT 0	NOT NULL,
	SONG_FILE_NO	NUMBER		NOT NULL,
    SONG_PLACE_NO   NUMBER  DEFAULT 1   NOT NULL,
	SONG_TITLE	VARCHAR2(150)		NOT NULL,
    SONG_DURATION   NUMBER  NOT NULL,
	SONG_LICENSE	VARCHAR2(300),
	SONG_DETAIL	VARCHAR2(300),
	SONG_CREATE_DATE	DATE	DEFAULT SYSDATE	NOT NULL,
	SONG_MODIFY_DATE	DATE	DEFAULT SYSDATE	NOT NULL,
	SONG_STATUS	CHAR(1)	DEFAULT 'Y'	NOT NULL,
    
    CONSTRAINT FK_SONG_MN FOREIGN KEY (SONG_MEMBER_NO) REFERENCES MEMBER (MEMBER_NO),
    CONSTRAINT FK_SONG_SMN FOREIGN KEY (SONG_MOOD_NO) REFERENCES MOOD (MOOD_NO),
    CONSTRAINT FK_SONG_SGN FOREIGN KEY (SONG_GENRE_NO) REFERENCES GENRE (GENRE_NO),
    CONSTRAINT FK_SONG_SIN FOREIGN KEY (SONG_IMAGE_NO) REFERENCES SONG_IMAGE (SONG_IMAGE_NO),
    CONSTRAINT FK_SONG_SFN FOREIGN KEY (SONG_FILE_NO) REFERENCES SONG_FILE (SONG_FILE_NO),
    CONSTRAINT PK_SONG_SN PRIMARY KEY (SONG_NO)
    
);



--다운로드

CREATE TABLE DOWNLOAD (
	DOWNLOAD_SONG_NO	NUMBER  NOT NULL,
	DOWNLOAD_MEMBER_NO	NUMBER	NOT NULL,
	DOWNLOAD_DATE	DATE	DEFAULT SYSDATE	NOT NULL,
    CONSTRAINT FK_DOWNLOAD_DSN FOREIGN KEY (DOWNLOAD_SONG_NO) REFERENCES SONG (SONG_NO),
    CONSTRAINT FK_DOWNLOAD_DMN FOREIGN KEY (DOWNLOAD_MEMBER_NO) REFERENCES MEMBER (MEMBER_NO)
);

CREATE TABLE DOWNLOAD_COUNT (
	DOWNLOAD_COUNT_SONG_NO	NUMBER,
	DOWNLOAD_COUNT_NUMBER	NUMBER DEFAULT 0 NOT NULL,
   
    CONSTRAINT FK_DOWNLOAD_COUNT_DCSN FOREIGN KEY (DOWNLOAD_COUNT_SONG_NO) REFERENCES SONG (SONG_NO),
    CONSTRAINT PK_DOWNLOAD_COUNT_DCSN PRIMARY KEY (DOWNLOAD_COUNT_SONG_NO)
);

--신고
ALTER TABLE REPORT ADD REPORT_STATUS CHAR(1CHAR) DEFAULT 'N' NOT NULL;

CREATE TABLE REPORT (
	REPORT_NO	NUMBER,
	REPORT_SONG_NO	NUMBER	NOT NULL,
	REPORT_MEMBER_NO	NUMBER	NOT NULL,
	REPORT_TEXT	VARCHAR2(100),
	REPORT_DATE	DATE	DEFAULT SYSDATE	NOT NULL,
    REPORT_STATUS CHAR(1CHAR) DEFAULT 'N' NOT NULL,
    
    CONSTRAINT FK_REPORT_RSN FOREIGN KEY (REPORT_SONG_NO) REFERENCES SONG (SONG_NO),
     CONSTRAINT FK_REPORT_RMN FOREIGN KEY (REPORT_MEMBER_NO) REFERENCES MEMBER (MEMBER_NO),
     CONSTRAINT PK_REPORT_RN PRIMARY KEY (REPORT_NO)
);
