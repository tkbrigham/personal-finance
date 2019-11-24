-- Add Users table

-- !Ups
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255),
  password VARCHAR(255)
);

-- !Downs
DROP TABLE users;
