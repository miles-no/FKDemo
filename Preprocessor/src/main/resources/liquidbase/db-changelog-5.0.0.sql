--liquibase formatted sql
--changeset arpita:1 context:dev,prod
--Added new column to check the legal part class in PDFGenerator.
--alter table im_statement add LEGAL_PART_CLASS VARCHAR2(100);
Insert into IM_ATTACHMENT_CONFIG (ID,ATTACHMENT_NAME) values (4,'ORGANIZATION');

--changeset arpita:2 context:dev,prod
-- Add transction category "Rabatt Prismatch" in FKAS Rabatter
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (305,2,185);

--changeset arpita:3 context:dev,prod
--New Brand SVAS configuration inserts.
Insert into IM_BRAND_CONFIG (ID,BRAND,USE_EA_BARCODE,AGREEMENT_NUMBER,SERVICELEVEL,PREFIX_KID,KONTONUMMER,DESCRIPTION,POSTCODE,CITY,REGION,NATIONALID) values (6,'SVAS','0','0704','4','333','15060796855','SvorkaEnergi AS Avd.Kraft',null,'Svartvassvegen 6','Surnadal','6650');
Insert into IM_TRANSACTION_GROUP (ID,NAME,BRAND,TYPE,DESCRIPTION) values (7,'Diverse','SVAS',null,null);

--Existing transaction Categories in SVAS brand for Diverse group
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (306,7,19);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (307,7,34);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (308,7,37);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (309,7,40);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (310,7,42);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (311,7,44);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (312,7,47);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (313,7,57);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (314,7,58);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (315,7,60);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (316,7,69);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (317,7,70);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (318,7,72);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (319,7,77);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (320,7,79);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (321,7,87);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (322,7,88);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (323,7,90);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (324,7,91);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (325,7,92);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (326,7,94);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (327,7,95);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (328,7,96);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (329,7,98);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (330,7,104);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (331,7,106);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (332,7,113);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (333,7,127);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (334,7,128);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (335,7,131);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (336,7,135);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (337,7,136);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (338,7,137);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (339,7,138);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (340,7,139);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (341,7,140);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (342,7,144);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (343,7,145);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (344,7,146);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (345,7,147);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (346,7,148);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (347,7,150);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (348,7,155);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (349,7,158);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (350,7,159);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (351,7,160);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (352,7,175);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (353,7,176);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (354,7,187);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (355,7,188);
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (356,7,189);
--Adding new transaction Category and into group
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (190,'Debit','Purregebyr Lindorff','DI;Purregebyr');
Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (357,7,190);

--changeset arpita:4 context:dev,prod
--Brand TKAS name is changed from 'Trondheim Kraft AS' to 'TrøndelagKraft AS'.
--update IM_brand_config set description = 'TrøndelagKraft AS' where brand = 'TKAS';