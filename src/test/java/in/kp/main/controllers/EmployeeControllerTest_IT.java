package in.kp.main.controllers;

import in.kp.main.configs.TestContainerConfig;
import in.kp.main.dtos.EmployeeDTO;
import in.kp.main.entities.Employee;
import in.kp.main.entities.enums.Role;
import in.kp.main.repositories.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "10000")
@Import(TestContainerConfig.class)
public class EmployeeControllerTest_IT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private ModelMapper modelMapper;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .name("Karan soni")
                .email("karan@gmail.com")
                .password("Karan@2026")
                .age(21)
                .role(Role.ADMIN)
                .build();

        employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
    }

    @Test
    @DisplayName("GetAllEmployees")
    void getAllEmployees_whenEmployeesExist_returnEmployeeDTOList() {
        employeeRepository.save(employee);
        List<EmployeeDTO> employeeDTOList = webTestClient.get()
                .uri("/employee/all-employees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(employeeDTOList).isNotEmpty();

        employeeDTOList.forEach(System.out::println);
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("GetAllEmployees(fail case)")
    void getAllEmployees_whenEmployeesAreNotPresent_returnEmptyList() {
        employeeRepository.deleteAll();
        List<EmployeeDTO> responseBody = webTestClient.get()
                .uri("/employee/all-employees")
                .exchange()
                .expectStatus().isNoContent()
                .expectBodyList(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isEmpty();

        System.out.println(responseBody);
    }

    @Test
    @DisplayName("getEmpById")
    void testGetEmpById_WhenEmployeeIdIsPresent_ThenReturnEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);
        EmployeeDTO responseBody = webTestClient.get()
                .uri("/employee/emp/{empId}", savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(savedEmployee.getId());

        System.out.println(responseBody);
        employeeRepository.deleteAll();
    }


    @Test
    @DisplayName("getEmpById(fail case)")
    void testGetEmpById_WhenEmployeeIdNotPresent_ThenReturnNotFound() {
        employeeRepository.deleteAll();
        webTestClient.get()
                .uri("employee/emp/{empId}", 2L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .isEmpty();
    }

    @Test
    @DisplayName("createNewEmployee")
    void testCreateNewEmployee_WhenEmployeeIsNotPresentAndInserted_ThenReturnEmployeeDTO() {
        EmployeeDTO responseBody = webTestClient.post()
                .uri("/employee")
                .bodyValue(employeeDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getName()).isEqualTo(employee.getName());
        Assertions.assertThat(responseBody.getEmail()).isEqualTo(employee.getEmail());
        Assertions.assertThat(responseBody.getPassword()).isEqualTo(employee.getPassword());
        Assertions.assertThat(responseBody.getAge()).isEqualTo(employee.getAge());
        Assertions.assertThat(responseBody.getRole()).isEqualTo(employee.getRole());

        System.out.println(responseBody);

        employeeRepository.deleteAll();
    }


    @Test
    @DisplayName("createNewEmployee(fail case)")
    void testCreateNewEmployee_WhenEmployeeIsAlreadyPresent_ThenReturnBadRequest() {
        Employee savedEmployee = employeeRepository.save(employee);
        webTestClient.post()
                .uri("/employee")
                .bodyValue(savedEmployee)
                .exchange()
                .expectStatus().isBadRequest();

        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("deleteEmpById")
    void testDeleteEmpById_WhenEmployeeIdIsPresent_ThenReturnSuccessMsg() {
        Employee savedEmployee = employeeRepository.save(employee);
        String responseBody = webTestClient.delete()
                .uri("/employee/emp/{empId}", savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isEqualTo("SUCCESSFULLY DELETED");

        System.out.println(responseBody);
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("deleteEmpById(fail case)")
    void testDeleteEmpById_WhenEmployeeIdNotIsPresent_ThenReturnSuccessMsg() {
        Long id = 999L;
        webTestClient.delete()
                .uri("/employee/emp/{empId}", id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo("NO EMPLOYEE FOUND WITH ID : " + id);
    }

    @Test
    @DisplayName("updateEmployeeDetailsById")
    void testUpdateEmployeeDetailsById_WhenIdAndNewDetailsAreValid_ThenReturnNewUpdatedEmployeeDTO() {
        Employee savedEmployee = employeeRepository.save(employee);

        EmployeeDTO newEmployeeDTO = EmployeeDTO.builder()
                .name("shazam soni")
                .email("shazam@gmail.com")
                .password("Shazam@2026")
                .build();

        EmployeeDTO responseBody = webTestClient.patch()
                .uri("/employee/update/emp/{empId}", savedEmployee.getId())
                .bodyValue(newEmployeeDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getName()).isEqualTo(newEmployeeDTO.getName());
        Assertions.assertThat(responseBody.getEmail()).isEqualTo(newEmployeeDTO.getEmail());
        Assertions.assertThat(responseBody.getPassword()).isEqualTo(newEmployeeDTO.getPassword());

        System.out.println(responseBody);
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("updateEmployeeDetailsById(fail case)")
    void testUpdateEmployeeDetailsById_WhenIdAndNewDetailsAreNotValid_ThenReturnBadRequest() {
        EmployeeDTO newEmployeeDTO = EmployeeDTO.builder()
                .id(13L)
                .name("shazam soni")
                .email("shazam@gmail.com")
                .password("Shazam@2026")
                .build();

        webTestClient.patch()
                .uri("/employee/update/emp/{empId}", newEmployeeDTO.getId())
                .bodyValue(newEmployeeDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("updateFullEmployeeDetailsById")
    void testUpdateFullEmployeeDetailsById_WhenEmployeeIdAndDetailsAreValid_ThenReturnEmployeeDTO() {
        Employee savedEmployee = employeeRepository.save(employee);

        EmployeeDTO newEmployeeDTO = EmployeeDTO.builder()
                .name("shazam soni")
                .email("shazam@gmail.com")
                .password("Shazam@2026")
                .age(19)
                .role(Role.USER)
                .build();

        EmployeeDTO responseBody = webTestClient.put()
                .uri("/employee/update/emp/{empId}", savedEmployee.getId())
                .bodyValue(newEmployeeDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getName()).isEqualTo(newEmployeeDTO.getName());
        Assertions.assertThat(responseBody.getEmail()).isEqualTo(newEmployeeDTO.getEmail());
        Assertions.assertThat(responseBody.getPassword()).isEqualTo(newEmployeeDTO.getPassword());
        Assertions.assertThat(responseBody.getAge()).isEqualTo(newEmployeeDTO.getAge());
        Assertions.assertThat(responseBody.getRole()).isEqualTo(newEmployeeDTO.getRole());

        System.out.println(responseBody);
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("updateFullEmployeeDetailsById(fail case)")
    void testUpdateFullEmployeeDetailsById_WhenEmployeeIdNotValid_ThenReturnNotFoundException() {
        Long invalidEmployeeId = 13L;
        EmployeeDTO newEmployeeDTO = EmployeeDTO.builder()
                .name("shazam soni")
                .email("shazam@gmail.com")
                .password("Shazam@2026")
                .age(33)
                .role(Role.USER)
                .build();

        webTestClient.put()
                .uri("/employee/update/emp/{empId}", invalidEmployeeId)
                .bodyValue(newEmployeeDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

}
