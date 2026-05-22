package in.kp.main.dtos;

import in.kp.main.entities.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "NAME FIELD CAN NOT BE BLANK")
    @Size(min = 2, max = 30, message = "LENGTH MUST BE GREATER THAN 2 AND LESS THAN 30")
    private String name;

    @NotBlank(message = "EMAIL FIELD CAN NOT BE BLANK")
    @Email(message = "EMAIL MUST BE VALID")
    private String email;

    @NotBlank(message = "PASSWORD FIELD CAN NOT BE BLANK")
    private String password;

    @NotNull(message = "AGE FIELD CAN NOT BE NULL")
    @Min(value = 18, message = "AGE MUST BE GREATER THAN 18 ")
    @Max(value = 45, message = "AGE MUST BE LESS THAN 45")
    private Integer age;

    @NotNull(message = "ROLE FIELD CAN NOR BE NULL")
    private Role role;

}
