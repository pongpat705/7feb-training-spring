package th.co.prior.training.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.prior.training.spring.entity.InventoryEntity;
import th.co.prior.training.spring.model.ErrorModel;
import th.co.prior.training.spring.model.InventoryModel;
import th.co.prior.training.spring.model.ResponseModel;
import th.co.prior.training.spring.repository.InventoryNativeRepository;
import th.co.prior.training.spring.repository.InventoryRepository;
import th.co.prior.training.spring.utils.InventoryUtilComponent;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private InventoryUtilComponent inventoryUtilComponent;
    private InventoryRepository inventoryRepository;
    private InventoryNativeRepository inventoryNativeRepository;

    public InventoryService(InventoryUtilComponent inventoryUtilComponent
            , InventoryRepository inventoryRepository
            , InventoryNativeRepository inventoryNativeRepository) {
        this.inventoryUtilComponent = inventoryUtilComponent;
        this.inventoryRepository = inventoryRepository;
        this.inventoryNativeRepository = inventoryNativeRepository;
    }

    public ResponseModel<Void> insertInventory(InventoryModel inventoryModel) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
//            transform
            InventoryEntity inventoryEntity =  this.inventoryUtilComponent.toEntity(inventoryModel);
            this.inventoryRepository.save(inventoryEntity);

        } catch (Exception e){
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }



    public ResponseModel<InventoryModel> getInventory(Integer id) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
//            transform
            Optional<InventoryEntity> optionalInventoryEntity =  this.inventoryRepository.findById(id);
            if(optionalInventoryEntity.isPresent()){

                InventoryEntity inventoryEntity = optionalInventoryEntity.get();
                inventoryEntity.setIsDelete("Y");

                InventoryModel data = this.inventoryUtilComponent.toModel(inventoryEntity);
                result.setData(data);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseModel<Void> insertBulkInventory(List<InventoryModel> inventoryModels) {

        ResponseModel<Void> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
//            validation
            List<ErrorModel> errorModels = this.inventoryUtilComponent.validationInventoryModelList(inventoryModels);

            if(errorModels.size() == 0){
                this.inventoryNativeRepository.insertBulkInventory(inventoryModels);
            } else if(errorModels.size() > 0){
                result.setCode("400");
                result.setDescription("invalid input");
                result.setErrors(errorModels);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
