package in.kp.main.services;

import in.kp.main.dtos.EmployeeDTO;
import in.kp.main.entities.Employee;
import in.kp.main.repositories.EmployeeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServices {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();
        if (!employeeList.isEmpty()) {
            return employeeList.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).toList();
        }
        return Collections.emptyList();
    }


    public EmployeeDTO getEmpById(Long empId) {
        Employee employee = employeeRepository.findById(empId).orElse(null);
        if (employee != null) {
            return modelMapper.map(employee, EmployeeDTO.class);
        }
        return null;
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO employeeDTO) {
        try {
            Employee emp = employeeRepository.findByEmail(employeeDTO.getEmail());
            if (emp == null) {
                Employee employee = modelMapper.map(employeeDTO, Employee.class);
                Employee savedEmployee = employeeRepository.save(employee);
                return modelMapper.map(savedEmployee, EmployeeDTO.class);
            }
            throw new Exception("USER IS ALREADY PRESENT IN DATABASE!");
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean deleteEmpById(Long empId) {
        boolean isExists = employeeRepository.existsById(empId);
        if (isExists) employeeRepository.deleteById(empId);
        return isExists;
    }

    public EmployeeDTO updateEmployeeDetailsById(Long empId, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employeeDTO.getName() != null && !employeeDTO.getName().equals(employee.getName())) {
            employee.setName(employeeDTO.getName());
        }
        if (employeeDTO.getEmail() != null && !employeeDTO.getEmail().equals(employee.getEmail())) {
            employee.setEmail(employeeDTO.getEmail());
        }
        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().equals(employee.getPassword())) {
            employee.setPassword(employeeDTO.getPassword());
        }
        if (employeeDTO.getAge() != null && !employeeDTO.getAge().equals(employee.getAge())) {
            employee.setAge(employeeDTO.getAge());
        }
        if (employeeDTO.getRole() != null && !employeeDTO.getRole().equals(employee.getRole())) {
            employee.setRole(employeeDTO.getRole());
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, EmployeeDTO.class);
    }

    public EmployeeDTO updateFullEmployeeDetailsById(Long empId, @Valid EmployeeDTO employeeDTO) {
        employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee updatedEmployee = employeeRepository.save(modelMapper.map(employeeDTO, Employee.class));
        return modelMapper.map(updatedEmployee, EmployeeDTO.class);
    }
}
