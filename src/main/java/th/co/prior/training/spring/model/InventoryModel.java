package th.co.prior.training.spring.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
public class InventoryModel extends BaseModel{
    private Integer inventoryId;
    private String itemName;
    private Integer itemQty;

}
