package in.kp.main.repositories;

import in.kp.main.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee , Long> {
    Employee findByName(String name);
    Employee findByEmail(String email);
}
