package th.co.prior.training.spring.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDepartmentModel {
    private Integer empNo;
    private LocalDate birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate hireDate;
    private String deptName;
}
