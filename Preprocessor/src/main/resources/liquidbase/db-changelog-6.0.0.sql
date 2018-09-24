--liquibase formatted sql
--changeset arpita:1 context:prod
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

--changeset arpita:2 context:prod
--added NORGESNETT AS grid in IM_GRID_CONFIG
 --Insert into IM_GRID_CONFIG (ID,GRID_NAME,EMAIL,PHONE,GRID_LABEL) values (113,'NORGESNETT AS','norgesnett@norgesnett.no','21492506','Norgesnett AS');
 --Insert into IM_GRID_CONFIG (ID,GRID_NAME,EMAIL,PHONE,GRID_LABEL) values (114,'ENEAS ENERGY','post@eneasenergy.no','32242230','Eneas Energy');

--changeset arpita:3 context:prod
--added config to delete invoice pdfs
Insert into IM_CONFIG (NAME,VALUE) values ('delete.invoice.pdf.before.no.of.days','7');

--changeset arpita:4 context:prod
 alter table im_audit_log add LEGAL_PART_CLASS VARCHAR2(50);

-- changeset bhavik:5 context:prod
Insert into IM_CONFIG (NAME,VALUE) values ('delete.statement.payload.before.days','7');
Insert into IM_CONFIG (NAME,VALUE) values ('transferfile.update.job.active','YES');

-- changeset arpita:6 context:prod
Insert into IM_CONFIG (NAME,VALUE) values ('summary.page.with.greater.or.equal.meter','4');

-- changeset arpita:7 context:prod
Insert into IM_GRID_CONFIG (ID,GRID_NAME,EMAIL,PHONE,GRID_LABEL) values (115,'SVORKA ENERGI NETT AS','nettkunde@svorka.no','71659100','Svorka Energi Nett AS');
