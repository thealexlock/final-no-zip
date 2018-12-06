CREATE TABLE public.customerProjects(
  job_id SERIAL PRIMARY KEY NOT NULL,
  customer_name VARCHAR(24),
  date VARCHAR(24),
  phone_number VARCHAR(24),
  street_address VARCHAR(24),
  city VARCHAR(24),
  state VARCHAR(24),
  zipcode VARCHAR(24),
  cost VARCHAR(24),
  description VARCHAR(300)
);