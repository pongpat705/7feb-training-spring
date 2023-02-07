package th.co.prior.training.spring.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseModel<T> {
    private String code;
    private String description;
    private List<ErrorModel> errors = new ArrayList<>();
    private T data;
}
