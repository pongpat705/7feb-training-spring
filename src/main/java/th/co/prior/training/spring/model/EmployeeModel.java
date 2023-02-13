package th.co.prior.training.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class EmployeeModel {

    @JsonIgnoreProperties
    private String firstName;
    private String lastName;
    private String gender;

}
