CREATE TABLE employees
(
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    employee_name     VARCHAR(255) NOT NULL,
    employee_email    VARCHAR(255) NOT NULL,
    employee_password VARCHAR(255) NOT NULL,
    employee_age      INT          NOT NULL,
    employee_role     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);