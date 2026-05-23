package in.kp.main.entities;

import in.kp.main.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_name", nullable = false)
    private String name;

    @Column(name = "employee_email", nullable = false)
    private String email;

    @Column(name = "employee_password", nullable = false)
    private String password;

    @Column(name = "employee_age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_role", nullable = false)
    private Role role;

    @ManyToOne
    private Department department;
}
