package th.co.prior.training.spring.repository;

import th.co.prior.training.spring.model.EmployeeDepartmentModel;

import java.util.List;

public interface EmployeeNativeRepository {

    List<EmployeeDepartmentModel> findEmployeeAndDepartment(String firstName);

    List<EmployeeDepartmentModel> findEmployeeAndDepartment(EmployeeDepartmentModel employeeDepartmentModel);
}
