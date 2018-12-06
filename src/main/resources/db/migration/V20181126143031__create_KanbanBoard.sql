CREATE TABLE public.kanbanBoard(
  job_id SERIAL PRIMARY KEY NOT NULL,
  preProduction VARCHAR(75),
  production VARCHAR(75),
  closeOut VARCHAR(75),
  archived VARCHAR(75)
);