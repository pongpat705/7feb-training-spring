package th.co.prior.training.spring.controller.rest;

import org.springframework.web.bind.annotation.*;
import th.co.prior.training.spring.model.EmployeeDepartmentModel;
import th.co.prior.training.spring.model.EmployeeModel;
import th.co.prior.training.spring.model.ResponseModel;
import th.co.prior.training.spring.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppRestController {

    private EmployeeService employeeeService;

    public AppRestController(EmployeeService employeeeService) {
        this.employeeeService = employeeeService;
    }

    @GetMapping("/employee/{firstName}")
    public ResponseModel<List<EmployeeModel>> getEmployee(@PathVariable String firstName){
        return this.employeeeService.getEmployee(firstName);
    }

    @PostMapping("/employee")
    public ResponseModel<List<EmployeeDepartmentModel>> getEmployee2(@RequestBody EmployeeDepartmentModel employeeDepartmentModel){
        return this.employeeeService.getEmployeeByCriteria(employeeDepartmentModel);
    }
}
