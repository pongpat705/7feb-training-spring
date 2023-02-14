package th.co.prior.training.spring;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import th.co.prior.training.spring.model.InventoryModel;

class ApplicationTests {


	@Test
	public void testJsonIgnoreProperties()  {

		String json = "{\n" +
				"  \"itemName\":\"xxxx10\",\n" +
				"  \"itemQty\": 50,\n" +
				"  \"createDate\": \"08-02-2023 11:26:51\",\n" +
				"  \"createBy\": \"test\",\n" +
				"  \"isDelete\": \"N\",\n" +
				"  \"employeeName\": \"test\"\n" +
				"}";

		ObjectMapper mapper = new ObjectMapper();

		InventoryModel inventoryModel = null;
		try {
			inventoryModel = mapper.readValue(json, InventoryModel.class);
		} catch (JsonProcessingException e) {
			Assertions.assertTrue(null != e.getMessage());
		}
	}

}
