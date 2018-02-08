--liquibase formatted sql
--changeset arpita:1 context:prod

  CREATE TABLE IM_GRID_LINE(GRID_LINE_ID NUMBER, GRID_LINE_NME VARCHAR2(50),CONSTRAINT PK_IM_GRID_LINE PRIMARY KEY (GRID_LINE_ID));

  create sequence IM_GRID_LINE_SEQ
  start with 1
  increment by 1
  nocache
  nocycle;
  alter table IM_GRID_LINE add CONSTRAINT IMGL_LINE_U unique (GRID_LINE_NME);

  CREATE TABLE IM_GRID_GROUP  (GROUP_ID NUMBER,GRID_GROUP_NAME  VARCHAR2(255 BYTE),GRID_CONFIG_ID NUMBER ,CONSTRAINT PK_IM_GRID_GROUP PRIMARY KEY (GROUP_ID));
  create sequence IM_GRID_GROUP_SEQ
  start with 1
  increment by 1
  nocache
  nocycle;
  ALTER TABLE IM_GRID_GROUP ADD CONSTRAINT IMGG_GROUP_U UNIQUE (GRID_GROUP_NAME);
  ALTER TABLE im_GRID_GROUP add constraint FK_IM_GRP_CONFIG_ID foreign key(GRID_CONFIG_ID)
  references IM_GRID_CONFIG (ID);


   CREATE TABLE IM_GROUP_GRID_LINE
     (ID NUMBER, GRID_LINE_ID NUMBER,GRID_GROUP_ID NUMBER,CONSTRAINT PK_IM_GROUP_GRID_LINE PRIMARY KEY (ID));

      create sequence im_group_grid_line_seq
        start with 1
        increment by 1
        nocache
        nocycle;

    ALTER TABLE IM_GROUP_GRID_LINE add constraint FK_IM_GRP_GRID_LINE_ID foreign key(GRID_LINE_ID)
      references IM_GRID_LINE (GRID_LINE_ID);
    ALTER TABLE IM_GROUP_GRID_LINE add constraint FK_IM_GRP_GRID_GRP_ID foreign key(GRID_GROUP_ID)
          references IM_GRID_GROUP (GROUP_ID);


--changeset arpita:2

      CREATE TABLE IM_ATTACHMENT_CONFIG
       (ID NUMBER,ATTACHMENT_NAME VARCHAR2(20 BYTE));

      create sequence IM_ATTACHMENT_CONFIG_SEQ
      start with 1
      increment by 1
      nocache
      nocycle;


    CREATE TABLE IM_ATTACHMENT (ATTACHMENT_ID NUMBER, ATTACHMENT_TYPE VARCHAR2(20 BYTE),ATTACHMENT_CONTENT CLOB, BRAND VARCHAR2(20 BYTE), ATTACHMENT_CONFIG_ID NUMBER,	FILE_TYPE VARCHAR2(20 BYTE));
    create sequence IM_ATTACHMENT_SEQ
      start with 1
      increment by 1
      nocache
      nocycle;


    CREATE TABLE IM_BLANKET_NUMBER (BLANKET_NUMBER  VARCHAR2(30 BYTE), ID NUMBER, DATE_OF_ACTIVATION DATE,MODE_OF_GENERATION CHAR(1 BYTE),STATUS NUMBER(1,0));
    CREATE SEQUENCE  IM_BLANKET_NUMBER_SEQ
    start with 1
    INCREMENT BY 1
     NOCACHE
     NOCYCLE ;

Insert into IM_CONFIG (NAME,VALUE) values ('blanketnumber.validity.period.months','3');
Insert into IM_CONFIG (NAME,VALUE) values ('read.attachment.from.db','false');

alter table im_statement add credit_limit number;

alter table im_statement add ATTACHMENT_CONFIG_ID number;

--changeset arpita:3
--Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (123,'Credit','Penger tilbake fra Fjordkraft Netthandel','FT;Penger tilbake fra Fjordkraft Netthandel');
--Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (106,2,123);
