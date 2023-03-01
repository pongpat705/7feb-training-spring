package th.co.prior.training.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import th.co.prior.training.spring.component.kafka.ProducerComponent;
import th.co.prior.training.spring.entity.InventoryEntity;
import th.co.prior.training.spring.model.ErrorModel;
import th.co.prior.training.spring.model.FileAndAttributeModel;
import th.co.prior.training.spring.model.InventoryModel;
import th.co.prior.training.spring.model.ResponseModel;
import th.co.prior.training.spring.repository.InventoryNativeRepository;
import th.co.prior.training.spring.repository.InventoryRepository;
import th.co.prior.training.spring.utils.InventoryUtilComponent;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryService {

    private InventoryUtilComponent inventoryUtilComponent;
    private InventoryRepository inventoryRepository;
    private InventoryNativeRepository inventoryNativeRepository;
    private ProducerComponent producerComponent;

    public InventoryService(InventoryUtilComponent inventoryUtilComponent
            , InventoryRepository inventoryRepository
            , InventoryNativeRepository inventoryNativeRepository, ProducerComponent producerComponent) {
        this.inventoryUtilComponent = inventoryUtilComponent;
        this.inventoryRepository = inventoryRepository;
        this.inventoryNativeRepository = inventoryNativeRepository;
        this.producerComponent = producerComponent;
    }

    @Transactional
    public ResponseModel<Void> insertInventory(InventoryModel inventoryModel) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
//            transform
            InventoryEntity inventoryEntity =  this.inventoryUtilComponent.toEntity(inventoryModel);
            this.inventoryRepository.save(inventoryEntity);

        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
    @Transactional
    public ResponseModel<Integer> insertBulkInventory(List<InventoryModel> inventoryModels) {

        ResponseModel<Integer> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
            List<ErrorModel> errorModels = this.inventoryUtilComponent.validationInventoryModelList(inventoryModels);

            if(errorModels.size() == 0){

                int affectedRow = this.inventoryNativeRepository.insertBulkInventory(inventoryModels);
                result.setData(affectedRow);

            } else if(errorModels.size() > 0){
                result.setCode("400");
                result.setDescription("invalid input");
                result.setErrors(errorModels);
            }

        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Transactional
    public ResponseModel<Integer> getTotalQty() {

        ResponseModel<Integer> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {

            List<InventoryEntity> inventoryEntities = this.inventoryRepository.findAll();
            result.setData(this.sumAllInventory(inventoryEntities));

        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }
    private int sumAllInventory(List<InventoryEntity> inventoryEntities){
        int result = 0;
        for (InventoryEntity x: inventoryEntities) {
            result = result+x.getItemQty();
        }
        return result;
    }



    public ResponseModel<Void> uploadFile(FileAndAttributeModel inventoryModels) {

        ResponseModel<Void> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("ok");

        try {
            log.info(inventoryModels.toString());

            MultipartFile multipartFile = inventoryModels.getFile();

            File tmpFile = new File(
                    "/home/pongpat/IdeaProjects/training/6feb/spring/tmp/"
                            +inventoryModels.getName()+"_"+multipartFile.getOriginalFilename()
            );

            FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();

//

        } catch (Exception e){
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }


    public void downloadFile(HttpServletResponse httpServletResponse) {
        try {
            File tmpFile = new File("/home/app/boarding-passes-DZH2M2.pdf");
            FileInputStream fileInputStream = new FileInputStream(tmpFile);
            httpServletResponse.setContentType("application/octet-stream");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setHeader("Content-Disposition"
                    , "attachment; filename=" + "pdf"+ new Date().getTime()+".pdf");
            OutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(fileInputStream.readAllBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
            String result = "{\"result\":\""+e.getMessage()+"\"}";
            try {
                httpServletResponse.setContentType("application/json");
                OutputStream outputStream = httpServletResponse.getOutputStream();
                outputStream.write(result.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public ResponseModel<Void> pushInventoryToKafka(Integer id, String userLogin) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("push success");

        log.info("user {} pushing a message", userLogin);
        try {
//            transform
            Optional<InventoryEntity> optionalInventoryEntity =  this.inventoryRepository.findById(id);
            if(optionalInventoryEntity.isPresent()){

                InventoryEntity inventoryEntity = optionalInventoryEntity.get();
                inventoryEntity.setIsDelete("Y");

                InventoryModel data = this.inventoryUtilComponent.toModel(inventoryEntity);

                this.producerComponent.pushMessage(data,"test.event");

            }

        } catch (Exception e){
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResponseModel<Void> updateInventory(InventoryModel inventoryModel, String userLogin) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("update success");

        log.info("user {} update success", userLogin);
        try {
//            transform
//            validate
            boolean isValid = true;
            if (null == inventoryModel.getItemQty()) {
                result.setCode("400");
                result.setDescription("item qty is null");
                isValid = false;
            }
            if (isValid) {
                Optional<InventoryEntity> optionalInventoryEntity = this.inventoryRepository.findByInventoryIdAndIsDelete(inventoryModel.getInventoryId(), "N");
                if (optionalInventoryEntity.isPresent()) {

                    InventoryEntity inventoryEntity = optionalInventoryEntity.get();
                    inventoryEntity.setItemName(inventoryModel.getItemName());
                    inventoryEntity.setItemQty(inventoryModel.getItemQty());

                    inventoryEntity.setUpdateDate(LocalDateTime.now());
                    inventoryEntity.setUpdateBy(userLogin);

                    this.inventoryRepository.save(inventoryEntity);

                } else {
                    result.setCode("404");
                    result.setDescription("กูหาไม่เจอ");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("500");
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
