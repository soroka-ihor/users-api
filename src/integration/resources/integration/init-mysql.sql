CREATE TABLE users (
    user_id    VARCHAR(50) PRIMARY KEY,
    login      VARCHAR(50) NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100)
);

INSERT INTO users (user_id, login, first_name, last_name) VALUES ('m-001', 'mysql_user1', 'MySQL', 'One');
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('m-002', 'mysql_user2', 'MySQL', 'Two');
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('m-003', 'mysql_user3', 'MySQL', 'Three');
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('m-004', 'mysql_user4', 'MySQL', 'Four');
INSERT INTO users (user_id, login, first_name, last_name) VALUES ('m-005', 'mysql_user5', 'MySQL', 'Five');
