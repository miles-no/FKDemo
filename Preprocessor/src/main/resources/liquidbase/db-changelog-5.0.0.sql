--changeset arpita:1 context:prod
--Added new column to check the legal part class in PDFGenerator.
alter table im_statement add LEGAL_PART_CLASS VARCHAR2(100 BYTE);