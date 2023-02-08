package th.co.prior.training.spring.model;

import lombok.Data;


@Data
public class InventoryModel extends BaseModel{
    private Integer inventoryId;
    private String itemName;
    private Integer itemQty;
}
