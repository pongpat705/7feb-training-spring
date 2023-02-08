package th.co.prior.training.spring.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import th.co.prior.training.spring.entity.InventoryEntity;
import th.co.prior.training.spring.model.ErrorModel;
import th.co.prior.training.spring.model.InventoryModel;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryUtilComponent {

    public InventoryEntity toEntity(InventoryModel inventoryModel){
        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setInventoryId(inventoryModel.getInventoryId());
        inventoryEntity.setItemQty(inventoryModel.getItemQty());
        inventoryEntity.setItemName(inventoryModel.getItemName());
        inventoryEntity.setCreateBy(inventoryModel.getCreateBy());
        inventoryEntity.setCreateDate(inventoryModel.getCreateDate());
        inventoryEntity.setUpdateBy(inventoryModel.getUpdateBy());
        inventoryEntity.setUpdateDate(inventoryModel.getUpdateDate());
        inventoryEntity.setIsDelete(inventoryModel.getIsDelete());
        return inventoryEntity;
    }

    public InventoryModel toModel(InventoryEntity inventoryEntity){
        InventoryModel inventoryModel = new InventoryModel();
        inventoryModel.setInventoryId(inventoryEntity.getInventoryId());
        inventoryModel.setItemQty(inventoryEntity.getItemQty());
        inventoryModel.setItemName(inventoryEntity.getItemName());
        inventoryModel.setCreateBy(inventoryEntity.getCreateBy());
        inventoryModel.setCreateDate(inventoryEntity.getCreateDate());
        inventoryModel.setUpdateBy(inventoryEntity.getUpdateBy());
        inventoryModel.setUpdateDate(inventoryEntity.getUpdateDate());
        inventoryModel.setIsDelete(inventoryEntity.getIsDelete());
        return inventoryModel;
    }

    public List<ErrorModel> validationInventoryModelList(List<InventoryModel> inventoryModelList){
        List<ErrorModel> errorModels = new ArrayList<>();
        for (InventoryModel x: inventoryModelList) {
            if(StringUtils.isEmpty(x.getItemName())){
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode("F001");
                errorModel.setDescription("item name is null");
                errorModels.add(errorModel);
            }
            if(null != x.getItemQty()){
                ErrorModel errorModel = new ErrorModel();
                errorModel.setCode("F001");
                errorModel.setDescription("item qty is null");
                errorModels.add(errorModel);
            }
        }

        return errorModels;
    }
}
