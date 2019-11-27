-- Add Accounts table

-- !Ups
CREATE TABLE accounts (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255),
  account_type VARCHAR(255)
);

-- !Downs
DROP TABLE accounts;
