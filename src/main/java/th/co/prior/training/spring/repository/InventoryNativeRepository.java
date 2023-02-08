package th.co.prior.training.spring.repository;

import th.co.prior.training.spring.model.InventoryModel;

import java.util.List;

public interface InventoryNativeRepository {

    public int insertBulkInventory(List<InventoryModel> inventoryModelList);
}
