package th.co.prior.training.spring.repository;

import th.co.prior.training.spring.model.InventoryModel;

import java.util.List;

public class InventoryRepositoryTestCase200 implements InventoryNativeRepository{
    @Override
    public int insertBulkInventory(List<InventoryModel> inventoryModelList) {
        return inventoryModelList.size();
    }
}
