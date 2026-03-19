CREATE TABLE user_table (
    ldap_login VARCHAR2(50) PRIMARY KEY,
    name       VARCHAR2(100),
    surname    VARCHAR2(100)
);

INSERT INTO user_table (ldap_login, name, surname) VALUES ('o-001', 'Anna', 'Dark');
INSERT INTO user_table (ldap_login, name, surname) VALUES ('o-002', 'Mark', 'Goldberg');
INSERT INTO user_table (ldap_login, name, surname) VALUES ('o-003', 'John', 'Bad');
INSERT INTO user_table (ldap_login, name, surname) VALUES ('o-004', 'Anton', 'Vetrov');
INSERT INTO user_table (ldap_login, name, surname) VALUES ('o-005', 'Ondu', 'The Fifth');

COMMIT;
