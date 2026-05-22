package in.kp.main.controllers;


import in.kp.main.dtos.EmployeeDTO;
import in.kp.main.services.EmployeeServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {


    private final EmployeeServices employeeServices;

    @GetMapping("/all-employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employeeDTOList = employeeServices.getAllEmployees();
        if (!employeeDTOList.isEmpty()) {
            return new ResponseEntity<>(employeeDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<>(employeeDTOList, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/emp/{empId}")
    public ResponseEntity<EmployeeDTO> getEmpById(@PathVariable Long empId) {
        EmployeeDTO employeeDTO = employeeServices.getEmpById(empId);
        if (employeeDTO != null) {
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createNewEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        EmployeeDTO newEmployee = employeeServices.createNewEmployee(employeeDTO);
        if (newEmployee != null) {
            return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/update/emp/{empId}")
    public ResponseEntity<EmployeeDTO> updateEmployeeDetailsById(@PathVariable Long empId, @RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO updatedEmployeeDTO = employeeServices.updateEmployeeDetailsById(empId, employeeDTO);
            return new ResponseEntity<>(updatedEmployeeDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/emp/{empId}")
    public ResponseEntity<EmployeeDTO> updateFullEmployeeDetailsById(@PathVariable Long empId, @RequestBody @Valid EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO updatedEmployeeDTO = employeeServices.updateFullEmployeeDetailsById(empId, employeeDTO);
            return new ResponseEntity<>(updatedEmployeeDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/emp/{empId}")
    public ResponseEntity<?> deleteEmpById(@PathVariable Long empId) {
        boolean isDeleted = employeeServices.deleteEmpById(empId);
        if (isDeleted) {
            return new ResponseEntity<>("SUCCESSFULLY DELETED", HttpStatus.OK);
        }
        return new ResponseEntity<>("NO EMPLOYEE FOUND WITH ID : " + empId, HttpStatus.NOT_FOUND);
    }
}
