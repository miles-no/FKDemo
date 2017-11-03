--liquibase formatted sql
--changeset bhavik:1 context:dev,aws
CREATE TABLE TRANSFERFILE
   (	TRANSFERTYPE VARCHAR2(20),
	BRAND VARCHAR2(10),
	FILENAME VARCHAR2(255),
	CREATED TIMESTAMP (6), 
	EKBATCHJOBID NUMBER, 
	TRANSFERSTATUS VARCHAR2(20),
	STATUSUPDATED TIMESTAMP (6), 
	FILESIZE NUMBER,
	FILESTORED TIMESTAMP (6), 
	FILECONTENT CLOB, 
	UPLOADSTARTED TIMESTAMP (6), 
	UPLOADENDED TIMESTAMP (6), 
	EHF NUMBER(1,0), 
	COMPELLOUPLOADSTARTED TIMESTAMP (6), 
	COMPELLOUPLOADENDED TIMESTAMP (6), 
	IMSTATUS VARCHAR2(20),
	TRANSFERREDBYTES NUMBER,
  CONSTRAINT PK_TRANSFERFILE PRIMARY KEY (TRANSFERTYPE, BRAND, FILENAME));

 CREATE TABLE SEGMENTFILE
   (	ID NUMBER(10,0),
	FILENAME VARCHAR2(255),
	FILETYPE VARCHAR2(20),
	FILECONTENT CLOB,
	BRAND VARCHAR2(30) NOT NULL ENABLE,
	CUSTOMERTYPE VARCHAR2(10),
	STROMAVTALE VARCHAR2(255),
	NETOWNERID NUMBER,
	JEVNSTROM NUMBER(1,0),
	AVTALEGIRO NUMBER(1,0),
	FORSTEFAKTURA NUMBER(1,0),
	OPPHOR NUMBER(1,0),
	UPLOADED TIMESTAMP (6),
	CHANGED TIMESTAMP (6),
	CHANGEDBY VARCHAR2(32),
	ORIGFILENAME VARCHAR2(255),
	TARIFF VARCHAR2(10),
	 CONSTRAINT PK_SEGMENTFILE PRIMARY KEY (ID));

	   CREATE TABLE SEGMENTCONTROLFILERESULT
   (	ID NUMBER,
	FROMDATE DATE,
	BRAND VARCHAR2(30 ),
	CUSTOMERTYPE VARCHAR2(1 ),
	ACCOUNTNO VARCHAR2(10 ),
	ATTACH_FILENAME VARCHAR2(255 ),
	CAMPAIGN_FILENAME VARCHAR2(255 ),
	ID_ATTACH NUMBER(10,0),
	ID_CAMPAIGN NUMBER(10,0),
	 CONSTRAINT PK_SEGMENTCONTROLFILERESULT PRIMARY KEY (ID));

--changeset bhavik:1.1 context:dev,aws
CREATE TABLE IM_SYSTEM_BATCH_INPUT
    (ID NUMBER NOT NULL ENABLE,
     FILENAME VARCHAR2(255),
     STATUS VARCHAR2(20),
     BRAND VARCHAR2(10),
     CREATE_TIME TIMESTAMP (6),
     UPDATE_TIME TIMESTAMP (6),
     TRANSFERTYPE VARCHAR2(20) NOT NULL ENABLE,
     CONSTRAINT PK_IM_SYSTEM_BATCH_INPUT PRIMARY KEY (ID),
     CONSTRAINT FK_IM_SYSTEM_BATCH_INPUT FOREIGN KEY (TRANSFERTYPE,BRAND,FILENAME) REFERENCES TRANSFERFILE (TRANSFERTYPE,BRAND,FILENAME));

--changeset bhavik:1.2 context:fjordkraftdev,fjordkrafttest,prod
CREATE TABLE IM_SYSTEM_BATCH_INPUT
    (ID NUMBER NOT NULL ENABLE,
     FILENAME VARCHAR2(255),
     STATUS VARCHAR2(20),
     BRAND VARCHAR2(10),
     CREATE_TIME TIMESTAMP (6),
     UPDATE_TIME TIMESTAMP (6),
     TRANSFERTYPE VARCHAR2(20) NOT NULL ENABLE,
     CONSTRAINT PK_IM_SYSTEM_BATCH_INPUT PRIMARY KEY (ID));--,
     --CONSTRAINT FK_IM_SYSTEM_BATCH_INPUT FOREIGN KEY (TRANSFERTYPE,BRAND,FILENAME) REFERENCES eacprod.TRANSFERFILE (TRANSFERTYPE,BRAND,FILENAME));

--changeset bhavik:2

    CREATE TABLE IM_JOB
    (ID VARCHAR2(255) NOT NULL,
     OBID NUMBER,
     NAME VARCHAR2(255),
     JOBCLASS VARCHAR2(255),
     MANUALALLOWED CHAR(1),
     EDITALLOWED CHAR(1),
     SCHEDULE VARCHAR2(255),
     STATUS VARCHAR2(255),
     CONSTRAINT PK_IM_JOB PRIMARY KEY(ID));

    CREATE TABLE IM_CONFIG
    (NAME VARCHAR2(255) NOT NULL ENABLE,
     VALUE VARCHAR2(255),
     CONSTRAINT PK_IM_CONFIG PRIMARY KEY (NAME));

    CREATE TABLE IM_BRAND_CONFIG
    (ID NUMBER NOT NULL ENABLE,
     BRAND VARCHAR2(20),
     USE_EA_BARCODE CHAR(1),
     AGREEMENT_NUMBER VARCHAR2(5),
     SERVICELEVEL CHAR(1),
     PREFIX_KID VARCHAR2(3),
     KONTONUMMER VARCHAR2(20),
     CONSTRAINT PK_IM_BRAND_CONFIG PRIMARY KEY (ID));


    CREATE TABLE IM_GRID_CONFIG
    (ID NUMBER NOT NULL ENABLE,
     GRID_NAME VARCHAR2(50),
     EMAIL VARCHAR2(100),
     PHONE VARCHAR2(25),
     CONSTRAINT PK_IM_GRID_CONFIG PRIMARY KEY (ID));


    CREATE TABLE IM_STATEMENT
    (ID NUMBER NOT NULL ENABLE,
     SI_ID NUMBER,
     STATUS VARCHAR2(100),
     STATEMENT_ID VARCHAR2(255),
     STATEMENT_TYPE VARCHAR2(50),
     INVOICE_NUMBER VARCHAR2(50),
     CUSTOMER_ID VARCHAR2(10),
     ACCOUNT_NUMBER VARCHAR2(10),
     CREATE_TIME TIMESTAMP (6),
     UPDATE_TIME TIMESTAMP (6),
     PDF_ATTACHMENT NUMBER,
     CITY VARCHAR2(50),
     VERSION NUMBER,
     DISTRIBUTION_METHOD VARCHAR2(50),
     AMOUNT NUMBER(20,5),
     INVOICE_DATE DATE,
     DUE_DATE DATE,
     LAYOUT_ID NUMBER,
     CONSTRAINT PK_IM_STATEMENT PRIMARY KEY (ID));


    CREATE TABLE IM_INVOICE_PDFS
    (ID NUMBER NOT NULL ENABLE,
     STATEMENT_ID NUMBER,
     TYPE VARCHAR2(255),
     PAYLOAD BLOB,
     CONSTRAINT PK_IM_INVOICE_PDFS PRIMARY KEY (ID),
     CONSTRAINT FK_IM_INVOICE_PDFS FOREIGN KEY (STATEMENT_ID) REFERENCES IM_STATEMENT(ID) )
     nologging
     LOB("PAYLOAD") STORE AS SECUREFILE compress_im_invoice_pdfs_lob (
      compress high
     );



    CREATE TABLE IM_STATEMENT_PAYLOAD
    (ID NUMBER NOT NULL ENABLE,
     STATEMENT_ID NUMBER,
     PAYLOAD CLOB,
     CONSTRAINT PK_IM_STATEMENT_PAYLOAD PRIMARY KEY (ID))
      nologging
     LOB("PAYLOAD") STORE AS SECUREFILE comp_im_stmt_payload_lob (
      compress high
     );



    CREATE TABLE IM_TRANSACTION_GROUP
    (ID NUMBER NOT NULL ENABLE,
     NAME VARCHAR2(255),
     LABEL VARCHAR2(255),
     TRANSACTION_CATEGORY VARCHAR2(255),
     BRAND VARCHAR2(20),
     CONSTRAINT PK_IM_TRANSACTION_GROUP PRIMARY KEY (ID));


--changeset bhavik:3

    create sequence IM_BRAND_CONFIG_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_GRID_CONFIG_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_SYSTEM_BATCH_INPUT_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_STATEMENT_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_INVOICE_PDFS_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_STATEMENT_PAYLOAD_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;


    create sequence IM_TRANSACTION_GROUP_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

--changeset hemlatha:4

     CREATE TABLE IM_RULE_ATTRIBUTES
      (	ID NUMBER,
    NAME VARCHAR2(100),
    TYPE VARCHAR2(50),
    FIELD_MAPPING VARCHAR2(500),
    CONSTRAINT PK_IM_LAYOUT_CONFIG PRIMARY KEY (ID));


    CREATE TABLE IM_LAYOUT (ID NUMBER, NAME VARCHAR2(100), DESCRIPTION VARCHAR2(500), CREATE_TIME TIMESTAMP, UPDATE_TIME TIMESTAMP,
    CONSTRAINT PK_IM_LAYOUT PRIMARY KEY(ID));

    CREATE TABLE IM_LAYOUT_CONTENT(ID NUMBER, LAYOUT_ID NUMBER, FILE_CONTENT CLOB, VERSION NUMBER DEFAULT 1, ACTIVE CHAR(1) DEFAULT 0,
    CONSTRAINT PK_IM_LAYOUT_CONTENT PRIMARY KEY(ID),
    CONSTRAINT FK_IM_LAYOUT_CONTENT FOREIGN KEY(LAYOUT_ID) REFERENCES IM_LAYOUT(ID) ON DELETE CASCADE);

    CREATE TABLE IM_LAYOUT_RULE (ID NUMBER, BRAND VARCHAR2(50), LAYOUT_ID NUMBER,
    CONSTRAINT PK_IM_LAYOUT_RULE PRIMARY KEY(ID),
    CONSTRAINT FK_IM_LAYOUT_RULE FOREIGN KEY (LAYOUT_ID) REFERENCES IM_LAYOUT(ID));

    CREATE TABLE IM_LAYOUT_RULE_MAP (ID NUMBER, RULE_ID NUMBER, NAME VARCHAR2(100), OPERATION VARCHAR2(100), VALUE VARCHAR2(100),
    CONSTRAINT PK_IM_LAYOUT_RULE_MAP PRIMARY KEY(ID),
    CONSTRAINT FK_IM_LAYOUT_RULE_MAP FOREIGN KEY (RULE_ID) REFERENCES IM_LAYOUT_RULE(ID));


--changeset hemlatha:5
    create sequence IM_RULE_ATTRIBUTES_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_LAYOUT_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_LAYOUT_CONTENT_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_LAYOUT_RULE_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

    create sequence IM_LAYOUT_RULE_MAP_SEQ
    start with 1
    increment by 1
    nocache
    nocycle;

--changeset hemlatha:6 context:dev,aws

Insert into IM_CONFIG (NAME,VALUE) values ('base.destination.dir','E:/XMLTOPDF/ExampleCode/Output1/');
Insert into IM_CONFIG (NAME,VALUE) values ('generated.pdf.folder.name','pdf_generated');
Insert into IM_CONFIG (NAME,VALUE) values ('attachment.pdf.folder.name','pdf_attached');
Insert into IM_CONFIG (NAME,VALUE) values ('processedxml.folder.name','processed_xml');
Insert into IM_CONFIG (NAME,VALUE) values ('birt.log.path','E:/FuelKraft/invoice_manager/logs/birt.log');
Insert into IM_CONFIG (NAME,VALUE) values ('fjordkraft.custom.fonts.path','E:/FuelKraft/font/TrueType_FjorkraftNeoSans');
Insert into IM_CONFIG (NAME,VALUE) values ('birt.layout.file.path','E:/FuelKraft/invoice_manager');
Insert into IM_CONFIG (NAME,VALUE) values ('num.thread.preprocess','35');
Insert into IM_CONFIG (NAME,VALUE) values ('num.thread.pdf.generator','35');
Insert into IM_CONFIG (NAME,VALUE) values ('num.thread.file.splitter','5');
Insert into IM_CONFIG (NAME,VALUE) values ('generated.invoice.folder.name','invoice_generated');
Insert into IM_CONFIG (NAME,VALUE) values ('control.file.dir','E:/FuelKraft/invoice_manager/controlfile');
Insert into IM_CONFIG (NAME,VALUE) values ('control.file.name','TKAS_p_304.pdf');
Insert into IM_CONFIG (NAME,VALUE) values ('campaign.file.path','E:/FuelKraft/invoice_manager/controlfile/FKAS_p_68.jpg');
Insert into IM_CONFIG (NAME,VALUE) values ('birt.resource.path','E:/FuelKraft/invoice_manager_new/invoice_manager/src/main/resources/Layout');

--changeset hemlatha:7 context:dev,aws,fjordkrafttest

Insert into IM_BRAND_CONFIG (ID,BRAND,USE_EA_BARCODE,AGREEMENT_NUMBER,SERVICELEVEL,PREFIX_KID,KONTONUMMER) values (1,'FKAS','1','0700','0','000',null);
Insert into IM_BRAND_CONFIG (ID,BRAND,USE_EA_BARCODE,AGREEMENT_NUMBER,SERVICELEVEL,PREFIX_KID,KONTONUMMER) values (2,'TKAS','1','0701','1','111',null);

--changeset hemlatha:8

CREATE TABLE IM_AUDIT_LOG
(	ID NUMBER NOT NULL ENABLE,
ACTION_ON_TYPE VARCHAR2(30 BYTE),
ACTION_ON_ID NUMBER,
ACTION VARCHAR2(40 BYTE),
USERNAME VARCHAR2(128 BYTE),
MSG CLOB,
DATETIME TIMESTAMP,
LOG_TYPE VARCHAR2(50),
CONSTRAINT PK_IM_AUDIT_LOG PRIMARY KEY (ID));

create sequence IM_AUDIT_LOG_SEQ
start with 1
increment by 1
nocache
nocycle;

--changeset hemlatha:9
alter table im_rule_attributes add options varchar2(500);


--changeset bhavik:9 context:dev,aws,fjordkraftdev,fjordkrafttest
Insert into IM_RULE_ATTRIBUTES (ID,NAME,TYPE,FIELD_MAPPING,OPTIONS) values (25,'Distribution Method','STRING','distributionMethod','EHF, NOT EHF');
Insert into IM_RULE_ATTRIBUTES (ID,NAME,TYPE,FIELD_MAPPING,OPTIONS) values (23,'Account Category','STRING','accountCategory.code','TPM, NOT TPM');
Insert into IM_RULE_ATTRIBUTES (ID,NAME,TYPE,FIELD_MAPPING,OPTIONS) values (26,'Credit Limit','INTEGER','creditLimit',null);
Insert into IM_RULE_ATTRIBUTES (ID,NAME,TYPE,FIELD_MAPPING,OPTIONS) values (22,'Legal Part Class','STRING','legalPartClass','Individual,Organization');
Insert into IM_RULE_ATTRIBUTES (ID,NAME,TYPE,FIELD_MAPPING,OPTIONS) values (1,'Pincode of area','STRING','postcode',null);

--changeset hemlatha:10
alter table im_system_batch_input add NUM_OF_RECORDS NUMBER;

--changeset hemalatha:11
alter table IM_BRAND_CONFIG add DESCRIPTION varchar2(100);
alter table IM_BRAND_CONFIG add POSTCODE varchar2(10);
alter table IM_BRAND_CONFIG add CITY varchar2(50);
alter table IM_BRAND_CONFIG add NATIONALID varchar2(10);
alter table IM_BRAND_CONFIG add REGION varchar2(50);

create table im_transaction_grp_category(id number, TG_ID number, transaction_category varchar2(255),
constraint PK_IM_TRANSACTION_GRP_CATEGORY primary key (id),
constraint FK_IM_TRANSACTION_GRP_CATEGORY foreign key (tg_id)
references IM_TRANSACTION_GROUP(id));
alter table im_transaction_group drop column transaction_category;

create sequence im_transactn_grp_category_seq
start with 1
increment by 1
nocache
nocycle;

--changeset hemalatha:12
CREATE TABLE IM_TRANSACTION_CATEGORY(ID NUMBER, TYPE VARCHAR2(50), DESCRIPTION VARCHAR2(100), CATEGORY VARCHAR2(250),
CONSTRAINT PK_IM_TRANSACTION_CATEGORY PRIMARY KEY(ID));

create sequence IM_TRANSACTION_CATEGORY_SEQ
start with 1
increment by 1
nocache
nocycle;

alter table im_transaction_grp_category drop column transaction_category;
alter table im_transaction_grp_category add TC_ID NUMBER;

alter table im_transaction_grp_category add constraint FK_IM_TRNCTN_GRP_CATEGORY_TC foreign key(TC_ID)
references IM_TRANSACTION_CATEGORY(ID);

alter table im_transaction_group drop column label;
alter table im_transaction_group add type varchar2(50);
alter table im_transaction_group add description varchar2(250);


--changeset bhavik:13
ALTER TABLE IM_BRAND_CONFIG ADD CONSTRAINT IMBC_BRAND_U UNIQUE (BRAND);
ALTER TABLE IM_GRID_CONFIG ADD CONSTRAINT IMGC_GRID_NAME_U UNIQUE (GRID_NAME);
ALTER TABLE IM_LAYOUT ADD CONSTRAINT IML_NAME_U UNIQUE (NAME);
ALTER TABLE IM_RULE_ATTRIBUTES ADD CONSTRAINT IMRA_NAME_U UNIQUE (NAME);
ALTER TABLE IM_TRANSACTION_GROUP ADD CONSTRAINT IMTG_NAME_BRAND_U UNIQUE (NAME,BRAND);
ALTER TABLE IM_TRANSACTION_GRP_CATEGORY ADD CONSTRAINT IMTGC_TG_TC_U UNIQUE (TG_ID,TC_ID);
ALTER TABLE IM_TRANSACTION_CATEGORY ADD CONSTRAINT IMTC_CAT_TYPE_DESC_U UNIQUE (CATEGORY,TYPE,description);

--changeset hemalatha:14
alter table IM_GRID_CONFIG add GRID_LABEL VARCHAR2(50);

