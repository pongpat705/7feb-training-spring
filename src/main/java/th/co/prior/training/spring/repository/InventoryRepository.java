package th.co.prior.training.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.training.spring.entity.InventoryEntity;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {

    Optional<InventoryEntity> findByInventoryIdAndIsDelete(Integer inventoryId, String isDeleted);
}
