-- Clean up test data from the database
-- Run this in your MariaDB client or H2 console

-- Delete the test user
DELETE FROM a1_user WHERE email = 'testuser@example.com';

-- Delete any other test data (adjust as needed)
DELETE FROM a1_user WHERE name LIKE '%Test%' OR email LIKE '%test%' OR email LIKE '%example.com%';

-- Show remaining users
SELECT id, name, email, country, utype FROM a1_user;