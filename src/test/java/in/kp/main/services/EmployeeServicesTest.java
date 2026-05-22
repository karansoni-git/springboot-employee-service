package in.kp.main.services;

import in.kp.main.dtos.EmployeeDTO;
import in.kp.main.entities.Employee;
import in.kp.main.entities.enums.Role;
import in.kp.main.repositories.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServicesTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServices employeeServices;

    private Employee mockEmployee;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        mockEmployee = Employee.builder()
                .id(1L)
                .name("karan")
                .email("karan@gmail.com")
                .password("Karan@2026")
                .age(21)
                .role(Role.ADMIN)
                .build();

        employeeDTO = modelMapper.map(mockEmployee, EmployeeDTO.class);

    }

    @Test
    @DisplayName("getEmpById")
    void testGetEmpById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDTO() {

        //assign
        Long id = mockEmployee.getId();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));

        //act
        EmployeeDTO employeeDTO = employeeServices.getEmpById(id);

        //assert
        Assertions.assertThat(employeeDTO.getId()).isEqualTo(id);
        Assertions.assertThat(employeeDTO.getEmail()).isEqualTo(mockEmployee.getEmail());

//        verify(employeeRepository,times(1)).findById(id);
//        verify(employeeRepository, atLeast(1)).findById(id);
//        verify(employeeRepository, atLeastOnce()).findById(id);
//        verify(employeeRepository,atMost(2)).findById(id);
//        verify(employeeRepository, atMostOnce()).findById(id);
//        verify(employeeRepository, only()).findById(id);

        System.out.println(employeeDTO);
    }

    @Test
    @DisplayName("getEmpById(fail case)")
    void testGetEmpById_WhenEmployeeIdIsNotPresent_ThenReturnNull() {

        //assign
        Long id = 2L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        //act
        EmployeeDTO empById = employeeServices.getEmpById(id);

        //assert
        Assertions.assertThat(empById).isNull();

        verify(employeeRepository).findById(id);
    }

    @Test
    @DisplayName("createNewEmployee")
    void testCreateNewEmployee_WhenEmployeeDTOIsValid_ThenReturnEmployeeDTO() {

        //assign
        String email = mockEmployee.getEmail();
        when(employeeRepository.findByEmail(email)).thenReturn(null);
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        //act
        EmployeeDTO savedEmployeeDTO = employeeServices.createNewEmployee(employeeDTO);

        //assert
        Assertions.assertThat(savedEmployeeDTO).isNotNull();
        Assertions.assertThat(savedEmployeeDTO.getEmail()).isEqualTo(mockEmployee.getEmail());

        verify(employeeRepository).findByEmail(email);
        verify(employeeRepository).save(any(Employee.class));
        System.out.println(savedEmployeeDTO);
    }

    @Test
    @DisplayName("createNewEmployee(fail case)")
    void testCreateNewEmployee_WhenEmployeeIsAlreadyPresent_ThenReturnNull() {

        //assign
        String email = mockEmployee.getEmail();
        when(employeeRepository.findByEmail(email)).thenReturn(mockEmployee);

        //act
        EmployeeDTO savedEmployeeDTO = employeeServices.createNewEmployee(employeeDTO);

        //assert
        Assertions.assertThat(savedEmployeeDTO).isNull();
        verify(employeeRepository).findByEmail(email);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("getAllEmployees")
    void testGetAllEmployees_WhenEmployeeExists_ThenReturnListOfEmployeeDTOs() {
        //assign
        when(employeeRepository.findAll()).thenReturn(List.of(mockEmployee));
        //act

        List<EmployeeDTO> allEmployees = employeeServices.getAllEmployees();

        //assert
        Assertions.assertThat(allEmployees).isNotEmpty();

        verify(employeeRepository).findAll();
    }

    @Test
    @DisplayName("getAllEmployees(fail case)")
    void testGetAllEmployees_WhenNoEmployeesExists_ThenReturnEmptyList() {
        //assign
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        //act
        List<EmployeeDTO> allEmployees = employeeServices.getAllEmployees();

        //assert
        Assertions.assertThat(allEmployees).isEmpty();

        verify(employeeRepository).findAll();
    }

    @Test
    @DisplayName("deleteEmpById")
    void testDeleteEmpById_WhenEmployeeIdIsPresent_ThenReturnTrue() {

        //assign
        Long id = mockEmployee.getId();
        when(employeeRepository.existsById(id)).thenReturn(true);

        //act
        boolean isDeleted = employeeServices.deleteEmpById(id);

        //assert
        Assertions.assertThat(isDeleted).isTrue();
        verify(employeeRepository).existsById(id);
        verify(employeeRepository).deleteById(id);

        System.out.println("Is Employee Deleted : " + isDeleted);
    }

    @Test
    @DisplayName("deleteEmpById(fail case)")
    void testDeleteEmpById_WhenEmployeeIdIsNotPresent_ThenReturnFalse() {
        //assign
        Long id = mockEmployee.getId();
        when(employeeRepository.existsById(id)).thenReturn(false);

        //act
        boolean isDeleted = employeeServices.deleteEmpById(id);

        //assert
        Assertions.assertThat(isDeleted).isFalse();

        verify(employeeRepository).existsById(id);

        System.out.println("Is Employee Deleted : " + isDeleted);
    }

    //updateEmployeeDetailsById methods test cases
    @Test
    @DisplayName("updateEmployeeDetailsById")
    void testUpdateEmployeeDetailsById_WhenEmployeeIsPresent_ThenReturnUpdatedEmployeeDTO() {
        //assign
        Long id = mockEmployee.getId();
        Employee employee = Employee.builder()
                .id(2L)
                .name("shazam soni")
                .email("shazam@gmail.com")
                .password("shazam@2026")
                .age(18)
                .role(Role.USER)
                .build();

        EmployeeDTO updatedEmployeeDTO = modelMapper.map(employee, EmployeeDTO.class);

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(mockEmployee)).thenReturn(employee);

        //act
        EmployeeDTO updated = employeeServices.updateEmployeeDetailsById(id, updatedEmployeeDTO);

        //assert
        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getName()).isNotEqualTo(employeeDTO.getName());
        System.out.println(updated);
    }

    @Test
    @DisplayName("updateEmployeeDetailsById(fail case 1)")
    void testUpdateEmployeeDetailsById_WhenEmployeeIsNotPresent_ThenThrowError() {
        //assign
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        //act & assert
        Assertions.assertThatThrownBy(() -> employeeServices.updateEmployeeDetailsById(id, employeeDTO)).isInstanceOf(RuntimeException.class).hasMessage("Employee not found");
        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("updateEmployeeDetailsById(fail case 2)")
    void testUpdateEmployeeDetailsById_WhenEmployeeIsPresent_ButDetailsIsNull() {
        //assign
        Long id = 1L;
        EmployeeDTO employee = EmployeeDTO.builder()
                .id(null)
                .name(null)
                .email(null)
                .password(null)
                .age(null)
                .role(null)
                .build();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        //act
        EmployeeDTO updatedDTO = employeeServices.updateEmployeeDetailsById(id, employee);

        //assert
        Assertions.assertThat(updatedDTO).isNotNull();
        Assertions.assertThat(updatedDTO.getName()).isEqualTo(mockEmployee.getName());
        Assertions.assertThat(updatedDTO.getEmail()).isEqualTo(mockEmployee.getEmail());
        Assertions.assertThat(updatedDTO.getPassword()).isEqualTo(mockEmployee.getPassword());
        Assertions.assertThat(updatedDTO.getAge()).isEqualTo(mockEmployee.getAge());
        Assertions.assertThat(updatedDTO.getRole()).isEqualTo(mockEmployee.getRole());

        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("updateEmployeeDetailsById(fail case 3)")
    void testUpdateEmployeeDetailsById_WhenEmployeeIsPresent_ButDetailsIsSame() {
        //assign
        Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(mockEmployee)).thenReturn(mockEmployee);

        //act
        EmployeeDTO updated = employeeServices.updateEmployeeDetailsById(id, employeeDTO);

        //assert
        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getName()).isEqualTo(mockEmployee.getName());
        Assertions.assertThat(updated.getEmail()).isEqualTo(mockEmployee.getEmail());
        Assertions.assertThat(updated.getPassword()).isEqualTo(mockEmployee.getPassword());
        Assertions.assertThat(updated.getAge()).isEqualTo(mockEmployee.getAge());
        Assertions.assertThat(updated.getRole()).isEqualTo(mockEmployee.getRole());

        System.out.println(updated);
    }

    @Test
    @DisplayName("updateFullEmployeeDetailsById")
    void testUpdateFullEmployeeDetailsById_WhenEmployeeIdAndDetailsAreValid_ThenReturnEmployeeDTO() {
        //assign
        EmployeeDTO newEmployeeForUpdate = EmployeeDTO.builder()
                .id(1L)
                .name("amrish puri")
                .email("amrish@gmail.com")
                .password("oyyMeAmrishpuri")
                .age(88)
                .role(Role.ADMIN)
                .build();

        Employee employee = modelMapper.map(newEmployeeForUpdate, Employee.class);

        when(employeeRepository.findById(mockEmployee.getId())).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        //act
        EmployeeDTO updated = employeeServices.updateFullEmployeeDetailsById(mockEmployee.getId(), newEmployeeForUpdate);

        //assert
        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getName()).isEqualTo(newEmployeeForUpdate.getName());
        Assertions.assertThat(updated.getEmail()).isEqualTo(newEmployeeForUpdate.getEmail());
        Assertions.assertThat(updated.getPassword()).isEqualTo(newEmployeeForUpdate.getPassword());
        Assertions.assertThat(updated.getAge()).isEqualTo(newEmployeeForUpdate.getAge());
        Assertions.assertThat(updated.getRole()).isEqualTo(newEmployeeForUpdate.getRole());

        System.out.println(updated);
    }

    @Test
    @DisplayName("updateFullEmployeeDetailsById")
    void testUpdateFullEmployeeDetailsById_WhenEmployeeIdAndDetailsAreNotValid_ThenThrowException() {
        //assign
        EmployeeDTO newEmployeeForUpdate = EmployeeDTO.builder()
                .id(1L)
                .name("amrish puri")
                .email("amrish@gmail.com")
                .password("oyyMeAmrishpuri")
                .age(88)
                .role(Role.ADMIN)
                .build();

        when(employeeRepository.findById(newEmployeeForUpdate.getId())).thenReturn(Optional.empty());

        //act & assert
        Assertions.assertThatThrownBy(() -> employeeServices.updateFullEmployeeDetailsById(newEmployeeForUpdate.getId(), newEmployeeForUpdate))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee not found");

        verify(employeeRepository).findById(newEmployeeForUpdate.getId());
        verify(employeeRepository, never()).save(any(Employee.class));

    }
}

/*
=> Mockito is useful for testing components in isolation without relying on their actual implementations or external dependencies.
=> Mockito can be used for:
    -> Mocking: Creating mock objects to simulate the behavior of real objects.
    -> Stubbing: Defining the behavior of mock objects when specific methods are called.
    -> Verification: Checking if certain methods were called on mock objects

=> Mockito Methods
    1.Creating Mock
        with @Mock Annotation
        with Mockito.mock(Classname)
    2. Stubbing Mock
        when(T methodCall): Specifies a method call to be stubbed.
        thenReturn(T value): Sets a return value for a stubbed method call.
        thenThrow(Throwable... throwables): Throws an exception when a stubbed method call is made
    3. Verifying Methods
        verify(T mock): Verifies that a method was called on a mock.
        verify(T mock, VerificationMode mode): Verifies that a method was called with a specific verification mode (e.g., times, never).

=> VerificationMode modes:
    -> times(int wantedNumberOfInvocations): Verifies the exact number of invocations.
    -> never(): Verifies that a method was never called.
    -> atLeastOnce(): Verifies that a method was called at least once.
    -> atLeast(int minNumberOfInvocations): at least called x times
    -> atMost(int maxNumberOfInvocations): at most called x times
    -> only(): Verifies that no other method was called on the mock.
*/

