/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  ahill
 * Created: Aug 10, 2017
 */


-- Table: public.kore

-- DROP TABLE public.kore;

CREATE SEQUENCE public.kore_id
    INCREMENT 1
    START 1500
    MINVALUE 1
    MAXVALUE 100000
    CACHE 1;

ALTER SEQUENCE public.kore_id
    OWNER TO hnkhxfwxmhwpvq;

CREATE SEQUENCE public.owner_id
    INCREMENT 1
    START 150
    MINVALUE 1
    MAXVALUE 1000
    CACHE 1;

ALTER SEQUENCE public.owner_id
    OWNER TO hnkhxfwxmhwpvq;

CREATE TABLE public.kore
(
    id bigint NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    breed text COLLATE pg_catalog."default",
    pic text COLLATE pg_catalog."default",
    CONSTRAINT kore_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.kore
    OWNER to hnkhxfwxmhwpvq;