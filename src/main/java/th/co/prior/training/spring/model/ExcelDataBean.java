package th.co.prior.training.spring.model;


import lombok.Data;

import java.util.List;


@Data
public class ExcelDataBean {
    private List<Object> values;
    private List<Class> types;
}
