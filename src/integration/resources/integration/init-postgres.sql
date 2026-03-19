CREATE TABLE users (
    user_id    VARCHAR(50) PRIMARY KEY,
    login      VARCHAR(50) NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100)
);

INSERT INTO users (user_id, login, first_name, last_name) VALUES ('p-001', 'pg_user1', 'Postgres', 'One');
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('p-002', 'pg_user2', 'Postgres', 'Two');
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('p-003', 'pg_user3', 'Postgres', 'Three');
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('p-004', 'pg_user4', 'Postgres', 'Four');
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('p-005', 'pg_user5', 'Postgres', 'Five');
