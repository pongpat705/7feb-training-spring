package th.co.prior.training.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import th.co.prior.training.spring.component.kafka.ProducerComponent;
import th.co.prior.training.spring.entity.InventoryEntity;
import th.co.prior.training.spring.model.*;
import th.co.prior.training.spring.repository.InventoryNativeRepository;
import th.co.prior.training.spring.repository.InventoryRepository;
import th.co.prior.training.spring.utils.InventoryUtilComponent;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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

    public void downloadFileExcel(HttpServletResponse response) {
        try {
            List<InventoryEntity> inventoryEntities = this.inventoryRepository.findAll();

            List<String> header = Arrays.asList(
                    "No",
                    "Item Name",
                    "Qty"
            );

            List<ExcelDataBean> dataBeans = new ArrayList<>();
            int no = 1;
            for (InventoryEntity d : inventoryEntities) {
                ExcelDataBean row = new ExcelDataBean();
                List<Object> values = new ArrayList<Object>();
                List<Class> types = new ArrayList<Class>();

                values.add(no++);
                types.add(Integer.class);

                values.add(d.getItemName());
                types.add(String.class);

                values.add(d.getItemQty());
                types.add(Integer.class);

                row.setValues(values);
                row.setTypes(types);

                dataBeans.add(row);
            }

            XSSFWorkbook workbook = generatExcelData(header, dataBeans, "data", "Inventory data sheet");

            Calendar calendar = Calendar.getInstance();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=inventory-"+calendar.getTime().getTime()+".xlsx");
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            String errorResult =
                    "{\"total\":0, \"data\":null, \"success\": false, \"message\":\""+e.getMessage()+"\"}";
            try {
                OutputStream out = response.getOutputStream();
                out.write(errorResult.getBytes());
                out.flush();
                out.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public XSSFWorkbook generatExcelData(List<String> headers, List<ExcelDataBean> datas, String sheetName, String titleName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        int titleRowIndex       = 3-1;
        int titleColumnIndex    = 4-1;
        int startRowIndex       = 5-1;

        Row rowTitle = sheet.createRow(titleRowIndex);
        Cell cellTitle = rowTitle.createCell(titleColumnIndex);
        cellTitle.setCellValue(titleName);

        Row rowHeader = sheet.createRow(startRowIndex++);
        for (int i = 0; i < headers.size(); i++) {
            Cell cellHeader = rowHeader.createCell(i);
            cellHeader.setCellValue(headers.get(i));
        }
        for (int i = 0; i < datas.size(); i++) {
            Row rowData = sheet.createRow(startRowIndex++);
            for (int j = 0; j < datas.get(i).getValues().size(); j++) {
                Class type = datas.get(i).getTypes().get(j);
                Cell cellData = rowData.createCell(j);

                Object xValue = datas.get(i).getValues().get(j);

                if(type == Long.class) {
                    if(null != xValue) {
                        Long v = (Long) xValue;
                        CellStyle cellStyle = cellData.getCellStyle();
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        cellData.setCellValue(v);
                    }
                }
                if(type == BigDecimal.class) {
                    if(null != xValue) {
                        BigDecimal v = (BigDecimal) xValue;
                        CellStyle cellStyle = cellData.getCellStyle();
                        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                        cellData.setCellValue(v.toPlainString());
                    }
                }
                if(type == String.class) {
                    if(null != xValue) {
                        String v = String.valueOf(xValue);
                        cellData.setCellValue(v);
                    }
                }

                if(type == Integer.class) {
                    if(null != xValue) {
                        Integer v = (Integer) xValue;
                        cellData.setCellValue(v);
                    }
                }
            }
        }


        return workbook;
    }

    public ResponseModel<Void> pushInventoryToKafka(Integer id, String userLogin) {

        ResponseModel<Void> result = new ResponseModel<>();
        result.setCode("200");
        result.setDescription("push success");

        log.info("user {} pushing a message", userLogin);
        try {
            Thread.sleep(4000L);
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
