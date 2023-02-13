package th.co.prior.training.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.training.spring.entity.EmployeeEntity;

import java.util.List;

@Repository
public interface EmployeeRepository
        extends
        JpaRepository<EmployeeEntity, Integer>
        , PagingAndSortingRepository<EmployeeEntity, Integer> {
    List<EmployeeEntity> findByFirstName(String firstName);
}
