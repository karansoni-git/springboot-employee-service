package in.kp.main.repositories;

import in.kp.main.configs.TestContainerConfig;
import in.kp.main.entities.Employee;
import in.kp.main.entities.enums.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


//@SpringBootTest
@Import(TestContainerConfig.class)
@DataJpaTest // this will use the h2 database by default for testing.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

//    @DisplayName("find by name")
//    @ParameterizedTest
//    @CsvSource({
//            "Karan soni",
//            "purn",
//    })
//    void findByName(String name) {
//        Employee employee = employeeRepository.findByName(name);
//        Assertions.assertThat(employee.getName()).isEqualTo(name);
//        System.out.println(employee);
//    }

    private Employee employee;

    @BeforeEach
    void setUpEmployee() {
        employee = Employee.builder()
                .name("karan")
                .email("karan@gmail.com")
                .password("Karan@2026")
                .age(21)
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void findByName_success_test() {
        //arrange , give
        employeeRepository.save(employee);

        //act , when
        Employee emp = employeeRepository.findByName("karan");

        //assert, then
        Assertions.assertThat(emp).isNotNull();
        Assertions.assertThat(emp.getName()).isEqualTo(employee.getName());

        System.out.println(emp);
    }

    @Test
    void findByName_fail_test() {
        //arrange , give
        String name = "shazam";

        //act , when
        Employee emp = employeeRepository.findByName("shazam");

        //assert, then
        Assertions.assertThat(emp).isNull();
    }
}

/*
=> @SpringBootTest: Used to create an application context and load the full application for integration tests. Useful in Integration Testing.

=> @DataJpaTest: Used to test JPA repositories, configuring an in-memory database for the test. Useful in Unit Testing Service Layer and Persistence Layer.
    -> @DataJpaTest is tailored to test JPA components like repositories. It configures an in-memory database, sets up Spring Data JPA repositories, and scans for JPA entities.
       This makes it ideal for testing repository methods and their interactions with the database.
    -> By default, @DataJpaTest runs each test within a transaction, which is rolled back after the test completes.
       This ensures tests do not affect each other and provides a clean state for each test

=> @TestConfiguration: Used to define extra beans or configurations for tests.

=> @WebMvcTest: Used for testing Spring MVC controllers. It initializes only the web layer and not the entire context. Useful in Unit Testing Controller layer

=> @AutoConfigureTestDatabase: Used to replace the actual database with an embedded database during tests.
    -> Use the AutoConfiureTestDatabase Annotation to configure the test database
    -> @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) :
        -> ANY: Replace the DataSource bean whether it was auto-configured or manually defined.
        -> NONE: Don't replace the application default DataSource.
        -> AUTO_CONFIGURED: Only replace the DataSource if it was auto-configured
*/