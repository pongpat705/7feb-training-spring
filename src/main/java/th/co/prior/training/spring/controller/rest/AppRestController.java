package th.co.prior.training.spring.controller.rest;

import org.springframework.web.bind.annotation.*;
import th.co.prior.training.spring.model.*;
import th.co.prior.training.spring.service.InventoryService;
import th.co.prior.training.spring.service.EmployeeService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppRestController {

    private EmployeeService employeeeService;
    private InventoryService inventoryService;

    public AppRestController(EmployeeService employeeeService, InventoryService inventoryService) {
        this.employeeeService = employeeeService;
        this.inventoryService = inventoryService;
    }

    @GetMapping("/employee/{firstName}")
    public ResponseModel<List<EmployeeModel>> getEmployee(@PathVariable String firstName){
        return this.employeeeService.getEmployee(firstName);
    }

    @PostMapping("/inquiry/employee")
    public ResponseModel<List<EmployeeDepartmentModel>> getEmployee2(@RequestBody EmployeeDepartmentModel employeeDepartmentModel){
        return this.employeeeService.getEmployeeByCriteria(employeeDepartmentModel);
    }

    @PostMapping("/inventory")
    public ResponseModel<Void> insert(
            @RequestBody InventoryModel inventoryModel
    ){
        return this.inventoryService.insertInventory(inventoryModel);
    }


    @GetMapping("/inventory/{id}")
    public ResponseModel<InventoryModel> getInventory(
            @PathVariable Integer id
    ){
        return this.inventoryService.getInventory(id);
    }


    @PostMapping("/inventory/update")
    public ResponseModel<Void> updateInventory( @RequestHeader(name = "userLogin") String userLogin,
            @RequestBody InventoryModel inventoryModel
    ){
        return this.inventoryService.updateInventory(inventoryModel, userLogin);
    }



    @PostMapping("/inventory/bulk")
    public ResponseModel<Integer> insertBulkInventory(
            @RequestBody List<InventoryModel> inventoryModels
    ){
        return this.inventoryService.insertBulkInventory(inventoryModels);
    }

    @PostMapping("/upload/file")
    public ResponseModel<Void> uploadFile(
            @ModelAttribute FileAndAttributeModel fileAndAttributeModel
    ){
        return this.inventoryService.uploadFile(fileAndAttributeModel);
    }

    @GetMapping("/download/file")
    public void downloadFile(HttpServletResponse response){
        this.inventoryService.downloadFile(response);
    }

    @GetMapping("/download/file/excel")
    public void downloadFileExcel(HttpServletResponse response){
        this.inventoryService.downloadFileExcel(response);
    }


    @GetMapping("/push/inventory/{id}")
    public ResponseModel<Void> pushInventory(@RequestHeader(name = "userLogin") String userLogin,
            @PathVariable Integer id
    ){
        return this.inventoryService.pushInventoryToKafka(id, userLogin);
    }

    @GetMapping("/health/check")
    public ResponseModel<Void> healthCheck(){
        ResponseModel<Void> responseModel = new ResponseModel<>();
        responseModel.setCode("200");
        return responseModel;
    }
}
