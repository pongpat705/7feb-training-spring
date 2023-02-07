package th.co.prior.training.spring.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.co.prior.training.spring.model.EmployeeDepartmentModel;
import th.co.prior.training.spring.repository.EmployeeNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "employeeNativeRepository")
public class EmployeeNativeRepositoryImpl implements EmployeeNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public EmployeeNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<EmployeeDepartmentModel> findEmployeeAndDepartment(String firstName) {

        String sql = "select e.emp_no\n" +
                " ,e.birth_date\n" +
                " ,e.first_name\n" +
                " ,e.last_name\n" +
                " ,e.gender\n" +
                " ,e.hire_date, d.dept_name \n" +
                " from employees e \n" +
                " inner join dept_emp de on de.emp_no  = e.emp_no \n" +
                " inner join departments d on de.dept_no  = d.dept_no \n" +
                " where 1=1\n" +
                " and e.first_name  = ? \n";

        return this.jdbcTemplate.query(sql, new RowMapper<EmployeeDepartmentModel>() {
            @Override
            public EmployeeDepartmentModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                EmployeeDepartmentModel x = new EmployeeDepartmentModel();
                int cols = 1;
                x.setEmpNo(rs.getInt(cols++));
                x.setBirthDate(rs.getDate(cols++).toLocalDate());
                x.setFirstName(rs.getString(cols++));
                x.setLastName(rs.getString(cols++));
                x.setGender(rs.getString(cols++));
                x.setHireDate(rs.getDate(cols++).toLocalDate());
                x.setDeptName(rs.getString(cols++));
                return x;
            }
        }, firstName);
    }

    @Override
    public List<EmployeeDepartmentModel> findEmployeeAndDepartment(EmployeeDepartmentModel employeeDepartmentModel) {

        List<Object> params = new ArrayList<>();

        String sql = "select e.emp_no\n" +
                " ,e.birth_date\n" +
                " ,e.first_name\n" +
                " ,e.last_name\n" +
                " ,e.gender\n" +
                " ,e.hire_date, d.dept_name \n" +
                " from employees e \n" +
                " inner join dept_emp de on de.emp_no  = e.emp_no \n" +
                " inner join departments d on de.dept_no  = d.dept_no \n" +
                " where 1=1\n" ;
        if(!StringUtils.isEmpty(employeeDepartmentModel.getFirstName())){
            sql += " and e.first_name = ?";
            params.add(employeeDepartmentModel.getFirstName());
        }
        if(!StringUtils.isEmpty(employeeDepartmentModel.getLastName())){
            sql += " and e.last_name = ?";
            params.add(employeeDepartmentModel.getLastName());
        }
        if(!StringUtils.isEmpty(employeeDepartmentModel.getDeptName())){
            sql += " and d.dept_name = ?";
            params.add(employeeDepartmentModel.getDeptName());
        }

        sql += " limit 10";

        return this.jdbcTemplate.query(sql, new RowMapper<EmployeeDepartmentModel>() {
            @Override
            public EmployeeDepartmentModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                EmployeeDepartmentModel x = new EmployeeDepartmentModel();
                int cols = 1;
                x.setEmpNo(rs.getInt(cols++));
                x.setBirthDate(rs.getDate(cols++).toLocalDate());
                x.setFirstName(rs.getString(cols++));
                x.setLastName(rs.getString(cols++));
                x.setGender(rs.getString(cols++));
                x.setHireDate(rs.getDate(cols++).toLocalDate());
                x.setDeptName(rs.getString(cols++));
                return x;
            }
        }, params.toArray());
    }
}
