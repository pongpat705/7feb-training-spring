package th.co.prior.training.spring.repository;

import th.co.prior.training.spring.model.InventoryModel;

import java.util.List;

public class InventoryRepositoryTestCaseException implements InventoryNativeRepository{
    @Override
    public int insertBulkInventory(List<InventoryModel> inventoryModelList) {
        throw new RuntimeException("insert error");
    }
}
