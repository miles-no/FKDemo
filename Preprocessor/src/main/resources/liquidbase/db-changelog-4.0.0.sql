--changeset arpita:1 context:prod
Insert into IM_BRAND_CONFIG (ID,BRAND,USE_EA_BARCODE,AGREEMENT_NUMBER,SERVICELEVEL,PREFIX_KID,KONTONUMMER,DESCRIPTION,POSTCODE,CITY,NATIONALID,REGION) values (5,'SEAS','0','0703','2','235','15060204400','Sodvin Energi og Fiber','','Hollaveien 2','7200','Kyrksæterøra');
Insert into IM_TRANSACTION_GROUP (ID,NAME,BRAND,TYPE,DESCRIPTION) values (6,'Diverse','SEAS',null,null);

--Adding New Transaction Categories for SEAS brand.
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (187,'Debit','Fakturagebyr bedrift','DI;Fakturagebyr');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (188,'Debit','Kraftkjøp/Fastledd','FT;Kraftkjøp/Fastledd');
Insert into IM_TRANSACTION_CATEGORY (ID,TYPE,DESCRIPTION,CATEGORY) values (189,'Debit','Leie av solcelle','FT;Leie av solcelle');

-- Adding transaction categories in Diverse Group for SEAS brand 
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (265,6,19);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (266,6,20);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (267,6,37);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (268,6,44);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (269,6,57);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (270,6,58);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (271,6,69);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (272,6,70);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (273,6,77);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (274,6,79);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (275,6,83);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (276,6,86);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (277,6,87);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (278,6,91);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (279,6,92);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (280,6,94);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (281,6,95);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (282,6,98);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (283,6,100);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (284,6,127);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (285,6,128);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (286,6,135);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (287,6,137);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (288,6,138);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (289,6,139);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (290,6,140);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (291,6,144);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (292,6,145);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (293,6,146);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (294,6,147);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (295,6,148);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (296,6,150);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (297,6,155);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (298,6,157);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (299,6,160);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (300,6,176);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (301,6,187);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (302,6,188);
  Insert into IM_TRANSACTION_GRP_CATEGORY (ID,TG_ID,TC_ID) values (303,6,189);
