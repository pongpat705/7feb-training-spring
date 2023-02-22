package th.co.prior.training.spring.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.prior.training.spring.model.InventoryModel;
import th.co.prior.training.spring.repository.InventoryNativeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        Connection connection = null;

        int affectedRow = 0;
        try {
            connection = this.jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i+1, params.get(i));
            }
            affectedRow = preparedStatement.executeUpdate()
            connection.commit();
        } catch (Exception e){
            if(null != connection){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } finally {
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return affectedRow;

    }
}
