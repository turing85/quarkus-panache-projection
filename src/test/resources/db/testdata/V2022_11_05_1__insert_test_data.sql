INSERT INTO public.data(id, name)
  VALUES (0, 'foo'), (1, 'bar'), (2, 'baz');

ALTER SEQUENCE public.data__seq__id
  RESTART WITH 3;