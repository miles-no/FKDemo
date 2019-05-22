--liquibase formatted sql
--changeset arpita:1 context:prod,dev
--added create_tms and update_tms in config files
alter table IM_CONFIG add UPDATE_TIME Timestamp;
alter table IM_CONFIG add CREATE_TIME Timestamp;
alter table IM_ATTACHMENT add CREATE_TIME Timestamp;
alter table IM_ATTACHMENT add UPDATE_TIME Timestamp;
alter table IM_ATTACHMENT_CONFIG add CREATE_TIME Timestamp;
alter table IM_ATTACHMENT_CONFIG add UPDATE_TIME Timestamp;
alter table IM_BRAND_CONFIG add  CREATE_TIME timestamp;
alter table IM_BRAND_CONFIG add  UPDATE_TIME timestamp;
alter table IM_GRID_CONFIG add CREATE_TIME Timestamp;
alter table IM_GRID_CONFIG add UPDATE_TIME Timestamp;
alter table IM_GRID_GROUP add CREATE_TIME Timestamp;
alter table IM_GRID_GROUP add UPDATE_TIME Timestamp;
alter table IM_LAYOUT_CONTENT add CREATE_TIME Timestamp;
alter table IM_LAYOUT_CONTENT add  UPDATE_TIME Timestamp;
alter table IM_LAYOUT_RULE add  CREATE_TIME Timestamp;
alter table IM_LAYOUT_RULE add  UPDATE_TIME Timestamp;
alter table IM_LAYOUT_RULE_MAP add CREATE_TIME Timestamp;
alter table IM_LAYOUT_RULE_MAP add UPDATE_TIME Timestamp;
alter table IM_RULE_ATTRIBUTES add CREATE_TIME Timestamp;
alter table IM_RULE_ATTRIBUTES add UPDATE_TIME Timestamp;
alter table IM_TRANSACTION_CATEGORY add CREATE_TIME Timestamp;
alter table IM_TRANSACTION_CATEGORY add UPDATE_TIME Timestamp;
alter table IM_TRANSACTION_GROUP add  CREATE_TIME Timestamp;
alter table IM_TRANSACTION_GROUP add  UPDATE_TIME Timestamp;
alter table IM_TRANSACTION_GRP_CATEGORY add  CREATE_TIME Timestamp;
alter table IM_TRANSACTION_GRP_CATEGORY add UPDATE_TIME Timestamp;

--changeset arpita:2 context:prod,dev
--added NORGESNETT AS grid in IM_GRID_CONFIG
 --Insert into IM_GRID_CONFIG (ID,GRID_NAME,EMAIL,PHONE,GRID_LABEL) values (113,'NORGESNETT AS','norgesnett@norgesnett.no','21492506','Norgesnett AS');
 --Insert into IM_GRID_CONFIG (ID,GRID_NAME,EMAIL,PHONE,GRID_LABEL) values (114,'ENEAS ENERGY','post@eneasenergy.no','32242230','Eneas Energy');

--changeset arpita:3 context:prod,dev
--added config to delete invoice pdfs
Insert into IM_CONFIG (NAME,VALUE) values ('delete.invoice.pdf.before.no.of.days','7');

--changeset arpita:4 context:prod,dev
 alter table im_audit_log add LEGAL_PART_CLASS VARCHAR2(50);

-- changeset bhavik:5 context:prod,dev
Insert into IM_CONFIG (NAME,VALUE) values ('delete.statement.payload.before.days','7');
Insert into IM_CONFIG (NAME,VALUE) values ('transferfile.update.job.active','YES');

-- changeset arpita:6 context:prod,dev
Insert into IM_CONFIG (NAME,VALUE) values ('summary.page.with.greater.or.equal.meter','4');

-- changeset arpita:7 context:prod,dev
Insert into IM_GRID_CONFIG (ID,GRID_NAME,EMAIL,PHONE,GRID_LABEL) values (115,'SVORKA ENERGI NETT AS','nettkunde@svorka.no','71659100','Svorka Energi Nett AS');

-- changeset arpita:8 context:prod,dev
---------------------Insert script for New BRAND : VEAS
Insert into IM_BRAND_CONFIG (ID,BRAND,USE_EA_BARCODE,AGREEMENT_NUMBER,SERVICELEVEL,PREFIX_KID,KONTONUMMER,DESCRIPTION,POSTCODE,CITY,NATIONALID,REGION) values (7,'VEAS','0','0703','2','235','15060204400','Sodvin Energi og Fiber',null,'Hollaveien 2','7200','Kyrksæterøra');
Insert into IM_TRANSACTION_GROUP (ID,NAME,BRAND,TYPE,DESCRIPTION) values (8,'Diverse','VEAS',null,null);

--Adding New Transaction Categories for VEAS brand.
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (191,'Debit','Flatgebyr Rabatt','DI;Rabatt jevn betaling');

Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (358,8,19);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (359,8,34);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (360,8,37);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (361,8,40);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (362,8,42);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (363,8,44);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (364,8,57);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (365,8,58);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (366,8,60);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (367,8,77);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (368,8,90);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (369,8,91);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (370,8,92);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (371,8,94);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (372,8,95);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (373,8,98);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (374,8,127);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (375,8,128);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (376,8,131);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (377,8,135);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (378,8,136);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (379,8,137);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (380,8,138);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (381,8,139);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (382,8,140);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (383,8,144);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (384,8,145);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (385,8,146);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (386,8,147);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (387,8,148);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (388,8,150);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (389,8,154);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (390,8,155);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (391,8,157);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (392,8,158);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (393,8,160);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (394,8,161);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (395,8,168);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (396,8,175);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (397,8,176);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (398,8,187);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (399,8,188);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (400,8,189);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (401,8,190);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (402,8,191);

-- changeset arpita:9 context:prod,dev
---------------------Added 4 new columns in IM_STATEMENT----------------------
ALTER TABLE IM_STATEMENT ADD "NO_OF_METER" NUMBER;
ALTER TABLE IM_STATEMENT ADD "ACCOUNT_INVOICE_LAYOUT" VARCHAR2(40 BYTE);
ALTER TABLE IM_STATEMENT ADD "EHF_ATTACHMENT" CHAR(1 BYTE);
ALTER TABLE IM_STATEMENT ADD "E2B_ATTACHMENT" CHAR(1 BYTE);

--Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (403,1,180);