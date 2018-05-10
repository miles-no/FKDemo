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

--changeset bhavik:4
  ALTER TABLE IM_BLANKET_NUMBER ADD LAST_UPDATED DATE;

--changeset arpita:5  context:prod
--Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (124,'Debit','Hjemmelading','DI;Hjemmelading');
--Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (107,1,124);

--Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (125,'Debit','Papirfaktura','DI;Papirfaktura');
--Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (108,1,125);

--Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (126,'Debit','Fastledd','FT;Fastledd');
--Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (109,1,126);


--changeset arpita:6  context:prod
--update im_grid_config set PHONE = '76112501' where ID= 106;

--changeset arpita:7 context:prod
Insert into IM_BRAND_CONFIG (ID,BRAND,USE_EA_BARCODE,AGREEMENT_NUMBER,SERVICELEVEL,PREFIX_KID,KONTONUMMER,DESCRIPTION,POSTCODE,CITY,NATIONALID,REGION) values (3,'VKAS','0','0702','2','222','15038103560','VesterlskraftStrm','103','Sortland','8401','Sortland');
Insert into IM_TRANSACTION_GROUP (ID,NAME,BRAND,TYPE,DESCRIPTION) values (3,'Diverse','VKAS',null,null);
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (127,'Debit','Flatgebyr','DI;Jevn betaling');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (128,'Debit','Flatgebyr bedrift','DI;Full kontroll');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (129,'Credit','Avsetning tap','NA;Avsetning tap');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (130,'Credit','Avskrivning','DI;Avskrivning');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (131,'Credit','EL-sertifikat','FT;EL-sertifikat');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (132,'Credit','Faktura Dragefossen Kraftanlegg','KR;Strøm fra Dragefossen Kraftanlegg');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (133,'Credit','Faktura fra Fjernvarme','NE;Fjernvarme');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (134,'Credit','Faktura fra netteier','NE;Nettleie fra netteier');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (135,'Credit','Fakturagebyr bedrift  Kredit CHARGE','DI;Rabatt fakturagebyr');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (136,'Credit','Fakturagebyr privat Kredit CHARGE','DI;Rabatt fakturagebyr');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (137,'Credit','Flatgebyr Rabatt CHARGE','DI;Rabatt jevn betaling');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (138,'Credit','Kjøp av kraftproduksjon','FT;Kjøp av kraftproduksjon');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (139,'Credit','Kompensasjon bruddgebyr','DI;Kompensasjon bruddgebyr');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (140,'Credit','Kompensasjon forsinket oppstart','DI;Kompensasjon forsinket oppstart');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (141,'Credit','Kreditnota Dragefossen Kraftanlegg','KN;Kreditnota fra Dragefossen Kraftanlegg');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (142,'Credit','Kreditnota fra Fjernvarme','NE;Fjernvarme');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (143,'Credit','Kreditnota fra netteier','NE;Kreditnota fra netteier');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (144,'Credit','Rabatt 100% fornøyd','DI;Rabatt 100% fornøyd');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (145,'Credit','Rabatt forsinkelsesrenter','DI;Rabatt forsinkelsesrenter');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (146,'Credit','Rabatt purregebyr','DI;Rabatt purregebyr');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (147,'Credit','Rabatt strømforsikring','DI;Rabatt strømforsikring');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (148,'Credit','Rabatt Strømpris','DI;Rabatt strømpris');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (149,'Credit','Reversering MVA','DI;MVA - høy sats');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (150,'Credit','Skadeoppgjør strømforsikring','DI;Skadeoppgjør strømforsikring');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (151,'Debit','Faktura Dragefossen Kraftanlegg','KR;Strøm fra Dragefossen Kraftanlegg');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (152,'Debit','Faktura fra Fjernvarme','NE;Fjernvarme');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (153,'Debit','Faktura fra netteier','NE;Nettleie fra netteier');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (154,'Debit','Forsinkelsesrente Lindorff','DI;Forsinkelsesrente');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (155,'Debit','Kjøp av varmepumpe','DI;Kjøp av varmepumpe');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (156,'Credit','Manuell innbetaling','DI;Manuell innbetaling');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (157,'Debit','Unspecified debit','DI;Overført skyldig beløp');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (158,'Credit','Unspecified credit','DI;Overført tilgodebeløp');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (159,'Credit','Vatkjøp','DI;Vatkjøp');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (160,'Debit','Ulykkesforsikring','FT;Ulykkesforsikring');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (161,'Credit','Rabatt eksisterande krav','IB; Rabatt eksisterande krav');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (162,'Credit','Avtalegiro','IB;Avtalegiro');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (163,'Credit','Bankgiro innbetaling','IB;Bankgiro innbetaling');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (164,'Credit','Collection Account to ongoing account','IB;Innbetaling fra Kredinor');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (165,'Credit','Innbetaling Kredinor','IB;Innbetaling Kredinor');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (166,'Credit','Innbetaling Lindorff','IB;Innbetaling Lindorff');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (167,'Credit','Kontant innskudd','IB;Kontant innskudd');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (168,'Debit','Bankgiro innbetaling','IB;Negative bankgiro innbetaling');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (169,'Credit','OCR PAYMENT KREDINOR','Innbetaling fra Kredinor OCR');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (170,'Debit','Excessive collection payment transfer','NA;Excessive collection payment transfer');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (171,'Credit','Innbetalt avskrevet tap','NA;innbetalt avskrevet tap');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (172,'Credit','Transfer negative or positive amounts','Transfer from one account to another');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (173,'Debit','Manuell overföring','UB;Manuell utbetaling');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (174,'Debit','Utbetaling - Telepay','UB;Utbetaling - Telepay');
insert into im_transaction_category (id,TYPE,DESCRIPTION,category) values (175,'Credit','Bankgebyr dekket av AP', 'DI;Bankgebyrer og omkostn');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (176,'Debit','Fakturagebyr privat','DI;Tillegg for papirfaktura');



Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (110,3,127);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (111,3,128);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (112,3,129);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (113,3,130);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (114,3,131);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (115,3,132);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (116,3,133);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (117,3,134);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (118,3,135);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (119,3,136);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (120,3,137);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (121,3,138);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (122,3,139);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (123,3,140);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (124,3,141);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (125,3,142);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (126,3,143);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (127,3,144);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (128,3,145);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (129,3,146);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (130,3,147);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (131,3,148);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (132,3,149);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (133,3,150);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (134,3,151);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (135,3,152);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (136,3,153);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (137,3,154);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (138,3,155);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (139,3,156);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (140,3,157);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (141,3,158);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (142,3,159);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (143,3,160);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (144,3,161);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (145,3,162);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (146,3,163);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (147,3,164);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (148,3,165);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (149,3,166);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (150,3,167);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (151,3,168);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (152,3,169);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (153,3,170);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (154,3,171);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (155,3,172);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (156,3,173);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (157,3,174);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (158,3,175);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (159,3,176);
--Existing Transaction Category to new Brand
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (160,3,19);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (161,3,37);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (162,3,42);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (163,3,44);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (164,3,54);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (165,3,77);
--Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (166,3,19);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (166,3,92);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (167,3,70);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (168,3,79);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (169,3,69);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (170,3,98);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (171,3,97);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (172,3,96);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (173,3,86);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (174,3,7);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (175,3,104);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (176,3,109);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (177,3,110);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (178,3,105);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (179,3,120);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (180,3,90);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (181,3,84);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (182,3,62);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (183,3,87);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (184,3,100);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (185,3,101);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (186,3,72);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (187,3,85);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (188,3,76);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (189,3,94);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (190,3,57);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (191,3,91);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (192,3,95);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (193,3,65);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (194,3,4);

--changeset arpita:8 context:prod
--Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (177,'Debit','Hjemmelading','FT;Hjemmelading');
--Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (195,1,177);


--changeset arpita:9 context:prod
Insert into IM_BRAND_CONFIG (ID,BRAND,USE_EA_BARCODE,AGREEMENT_NUMBER,SERVICELEVEL,PREFIX_KID,KONTONUMMER,DESCRIPTION,POSTCODE,CITY,NATIONALID,REGION) values (4,'TKAS','1','0701','1','111','15032873747','Sluppenvegen17B7030Trondheim',null,'Trondheim','7037','Norway');
Insert into IM_TRANSACTION_GROUP (ID,NAME,BRAND,TYPE,DESCRIPTION) values (4,'Diverse','TKAS',null,null);
Insert into IM_TRANSACTION_GROUP (ID,NAME,BRAND,TYPE,DESCRIPTION) values (5,'Rabatter','TKAS',null,null);
-- Diverse group for TKAS
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (178,'Debit','Penger tilbake fra Trondheim Kraft Netthandel','FT;Penger tilbake fra Trondheim Kraft Netthandel');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (179,'Debit','Vervepremie fra Trondheim Kraft','FT;Vervepremie fra Trondheim Kraft');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (180,'Debit','Betalingsutsettelse','DI;Betalingsutsettelse');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (181,'Debit','Avskrivning','DI;Avskrivning');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (182,'Debit','Kompensasjon bruddgebyr','EK;Kompensasjon bruddgebyr');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (183,'Debit','Kompensasjon forsinket oppstart','EK;Kompensasjon forsinket oppstart');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (184,'Debit','Korrigering av strømpris','EK;Korrigering av strømpris');

-- Rabatter group for TKAS
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (185,'Debit','Rabatt Prismatch','DI;Rabatt Prismatch');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (186,'Debit','Rabatt oppgjørsadministrasjon','DI;Rabatt oppgjørsadministrasjon');
--Existing transaction Category to be added in TKAS Diverse Group.
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (196,4,1);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (197,4,2);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (198,4,5);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (199,4,6);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (200,4,7);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (201,4,8);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (202,4,11);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (203,4,20);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (204,4,35);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (205,4,38);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (206,4,39);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (207,4,42);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (208,4,43);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (209,4,44);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (210,4,45);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (211,4,46);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (212,4,47);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (213,4,51);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (214,4,52);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (215,4,54);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (216,4,56);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (217,4,57);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (218,4,58);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (219,4,60);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (220,4,69);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (221,4,70);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (222,4,72);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (223,4,73);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (224,4,74);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (225,4,75);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (226,4,77);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (227,4,79);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (228,4,82);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (229,4,87);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (230,4,88);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (231,4,90);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (232,4,91);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (233,4,92);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (234,4,94);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (235,4,95);
-- Adding new Transaction Category to TKAS  Diverse group.
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (236,4,178);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (237,4,179);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (238,4,180);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (239,4,181);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (240,4,182);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (241,4,183);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (242,4,184);
--Existing transaction category to TKAS Rabbatter group.
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (243,5,1);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (244,5,2);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (245,5,12);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (246,5,13);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (247,5,14);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (248,5,15);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (249,5,16);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (250,5,17);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (251,5,19);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (252,5,21);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (253,5,22);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (254,5,23);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (255,5,24);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (256,5,25);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (257,5,26);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (258,5,28);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (259,5,31);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (260,5,32);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (261,5,33);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (262,5,41);
--Adding new transaction category to TKAS Rabatter group.
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (263,5,185);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (264,5,186);
