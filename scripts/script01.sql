
CREATE SEQUENCE documento_id_seq
	MINVALUE 1
    MAXVALUE 99999999
    START WITH 1
    INCREMENT BY 1
    CACHE 1000;

CREATE TABLE DOCUMENTO(
   ID SMALLINT NOT NULL DEFAULT nextval('documento_id_seq'),
   RESULTADO          TEXT    NOT NULL,
   CAMINHO_LOGICO     CHAR(1000),
   ARMARIO            TEXT,    
   GAVETA             INT,     
   PASTA              CHAR(1000),
   NUMERO_DOCUMENTO   CHAR(10), 
   NUMERO_FOLHA       INT     NOT NULL    
);

commit;




