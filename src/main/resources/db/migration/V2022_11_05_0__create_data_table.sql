CREATE TABLE public.data (
  id BIGINT CONSTRAINT upload__pk__id PRIMARY KEY,
  name VARCHAR(127) CONSTRAINT upload__not_null__file_name NOT NULL
);

CREATE SEQUENCE public.data__seq__id INCREMENT BY 1 OWNED BY public.data.id;

ALTER TABLE public.data
  ALTER COLUMN id SET DEFAULT nextval('public.data__seq__id');