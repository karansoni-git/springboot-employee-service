CREATE TABLE department
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    department_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_department PRIMARY KEY (id)
);

ALTER TABLE employees
    ADD department_id BIGINT;

ALTER TABLE department
    ADD CONSTRAINT uc_department_department_name UNIQUE (department_name);

ALTER TABLE employees
    ADD CONSTRAINT FK_EMPLOYEES_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES department (id);