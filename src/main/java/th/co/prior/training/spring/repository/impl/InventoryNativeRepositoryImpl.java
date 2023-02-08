package th.co.prior.training.spring.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.prior.training.spring.model.InventoryModel;
import th.co.prior.training.spring.repository.InventoryNativeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class InventoryNativeRepositoryImpl implements InventoryNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public InventoryNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int insertBulkInventory(List<InventoryModel> inventoryModelList) {

        List<Object> params = new ArrayList<>();
        String sql = "INSERT into inventory(item_name,item_qty, create_date, create_by, is_delete) \n" +
                " values\n" ;

        StringJoiner stringJoiner = new StringJoiner(",");
        for (InventoryModel inventoryModel: inventoryModelList) {
            stringJoiner.add("(?,?,?,?,?)");
            params.add(inventoryModel.getItemName());
            params.add(inventoryModel.getItemQty());
            params.add(inventoryModel.getCreateDate());
            params.add(inventoryModel.getCreateBy());
            params.add(inventoryModel.getIsDelete());
        }
        sql += stringJoiner.toString();

        return this.jdbcTemplate.update(sql, params.toArray());

    }
}
