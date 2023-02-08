package th.co.prior.training.spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import th.co.prior.training.spring.entity.EmployeeEntity;
import th.co.prior.training.spring.model.EmployeeDepartmentModel;
import th.co.prior.training.spring.model.EmployeeModel;
import th.co.prior.training.spring.model.ResponseModel;
import th.co.prior.training.spring.repository.EmployeeNativeRepository;
import th.co.prior.training.spring.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private EmployeeNativeRepository employeeNativeRepository;

    public EmployeeService(EmployeeRepository employeeRepository
            , EmployeeNativeRepository employeeNativeRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeNativeRepository = employeeNativeRepository;
    }

    public ResponseModel<List<EmployeeModel>> getEmployee(String firstName) {
        ResponseModel<List<EmployeeModel>> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
            List<EmployeeEntity> employeeEntityList = this.employeeRepository.findByFirstName(firstName);
            List<EmployeeModel> datas = new ArrayList<>();
            for (EmployeeEntity x: employeeEntityList) {
                EmployeeModel y = new EmployeeModel();
                y.setGender(x.getGender());
                y.setLastName(x.getLastName());
                y.setFirstName(x.getFirstName());

                datas.add(y);
            }
            result.setData(datas);
        } catch (Exception e){
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResponseModel<List<EmployeeDepartmentModel>> getEmployee2(String firstName) {
        ResponseModel<List<EmployeeDepartmentModel>> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
            List<EmployeeDepartmentModel> employeeEntityList
                    = this.employeeNativeRepository.findEmployeeAndDepartment(firstName);
            result.setData(employeeEntityList);
        } catch (Exception e){
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResponseModel<List<EmployeeDepartmentModel>> getEmployeeByCriteria(EmployeeDepartmentModel employeeDepartmentModel) {
        ResponseModel<List<EmployeeDepartmentModel>> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
            List<EmployeeDepartmentModel> employeeEntityList
                    = this.employeeNativeRepository.findEmployeeAndDepartment(employeeDepartmentModel);
            result.setData(employeeEntityList);
        } catch (Exception e){
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
