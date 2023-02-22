package th.co.prior.training.spring;


import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.transaction.NoTransactionException;
import th.co.prior.training.spring.entity.InventoryEntity;
import th.co.prior.training.spring.model.InventoryModel;
import th.co.prior.training.spring.model.ResponseModel;
import th.co.prior.training.spring.repository.InventoryNativeRepository;
import th.co.prior.training.spring.repository.InventoryRepository;
import th.co.prior.training.spring.repository.InventoryRepositoryTestCase200;
import th.co.prior.training.spring.repository.InventoryRepositoryTestCaseException;
import th.co.prior.training.spring.service.InventoryService;
import th.co.prior.training.spring.utils.InventoryUtilComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ApplicationTests {


	@Test
	public void test_insertBulkInventory_expect_ResponseModel_eq_200(){
		InventoryUtilComponent inventoryUtilComponent = new InventoryUtilComponent();
		InventoryNativeRepository inventoryNativeRepository = new InventoryRepositoryTestCase200();
		InventoryService inventoryService = new InventoryService(inventoryUtilComponent,null,inventoryNativeRepository,null);

		List<InventoryModel> inventoryModels = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			InventoryModel x = new InventoryModel();
			x.setItemName("item "+i);
			x.setItemQty(i+20);
			x.setCreateBy("test");
			inventoryModels.add(x);
		}
		ResponseModel<Integer> result =  inventoryService.insertBulkInventory(inventoryModels);
		assertTrue(result.getCode().equals("200"));
		Assertions.assertEquals(10, result.getData());
	}

	@Test
	public void test_insertBulkInventory_expect_ResponseModel_eq_400(){
		InventoryUtilComponent inventoryUtilComponent = new InventoryUtilComponent();
		InventoryNativeRepository inventoryNativeRepository = new InventoryNativeRepository() {
			@Override
			public int insertBulkInventory(List<InventoryModel> inventoryModelList) {
				return 0;
			}
		};
		InventoryService inventoryService = new InventoryService(inventoryUtilComponent,null,inventoryNativeRepository,null);

		List<InventoryModel> inventoryModels = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			InventoryModel x = new InventoryModel();
			x.setItemName("item "+i);
			inventoryModels.add(x);
		}
		ResponseModel<Integer> result =  inventoryService.insertBulkInventory(inventoryModels);
		assertTrue(result.getCode().equals("400"));
	}

	@Test
	public void test_insertBulkInventory_expect_ResponseModel_eq_500() {
		InventoryUtilComponent inventoryUtilComponent = new InventoryUtilComponent();
		InventoryNativeRepository inventoryNativeRepository = new InventoryRepositoryTestCaseException();
		InventoryService inventoryService = new InventoryService(inventoryUtilComponent,null,inventoryNativeRepository,null);

		List<InventoryModel> inventoryModels = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			InventoryModel x = new InventoryModel();
			x.setItemName("item "+i);
			x.setItemQty(i+20);
			x.setCreateBy("test");
			inventoryModels.add(x);
		}
		try {
			ResponseModel<Integer> result =  inventoryService.insertBulkInventory(inventoryModels);
		} catch (NoTransactionException e){
			Assertions.assertTrue(null != e.getMessage());
		}
	}


	@Test
	public void test_getTotalQty_expect_ResponseModel_data_eq_20() {

		InventoryRepository inventoryRepository = new InventoryRepository() {
			@Override
			public List<InventoryEntity> findAll() {
				List<InventoryEntity> result = new ArrayList<>();
				InventoryEntity a = new InventoryEntity();
				a.setItemQty(10);
				InventoryEntity b = new InventoryEntity();
				b.setItemQty(10);

				result.add(a);
				result.add(b);
				return result;
			}

			@Override
			public List<InventoryEntity> findAll(Sort sort) {
				return null;
			}

			@Override
			public List<InventoryEntity> findAllById(Iterable<Integer> integers) {
				return null;
			}

			@Override
			public <S extends InventoryEntity> List<S> saveAll(Iterable<S> entities) {
				return null;
			}

			@Override
			public void flush() {

			}

			@Override
			public <S extends InventoryEntity> S saveAndFlush(S entity) {
				return null;
			}

			@Override
			public <S extends InventoryEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
				return null;
			}

			@Override
			public void deleteAllInBatch(Iterable<InventoryEntity> entities) {

			}

			@Override
			public void deleteAllByIdInBatch(Iterable<Integer> integers) {

			}

			@Override
			public void deleteAllInBatch() {

			}

			@Override
			public InventoryEntity getOne(Integer integer) {
				return null;
			}

			@Override
			public InventoryEntity getById(Integer integer) {
				return null;
			}

			@Override
			public InventoryEntity getReferenceById(Integer integer) {
				return null;
			}

			@Override
			public <S extends InventoryEntity> List<S> findAll(Example<S> example) {
				return null;
			}

			@Override
			public <S extends InventoryEntity> List<S> findAll(Example<S> example, Sort sort) {
				return null;
			}

			@Override
			public Page<InventoryEntity> findAll(Pageable pageable) {
				return null;
			}

			@Override
			public <S extends InventoryEntity> S save(S entity) {
				return null;
			}

			@Override
			public Optional<InventoryEntity> findById(Integer integer) {
				return Optional.empty();
			}

			@Override
			public boolean existsById(Integer integer) {
				return false;
			}

			@Override
			public long count() {
				return 0;
			}

			@Override
			public void deleteById(Integer integer) {

			}

			@Override
			public void delete(InventoryEntity entity) {

			}

			@Override
			public void deleteAllById(Iterable<? extends Integer> integers) {

			}

			@Override
			public void deleteAll(Iterable<? extends InventoryEntity> entities) {

			}

			@Override
			public void deleteAll() {

			}

			@Override
			public <S extends InventoryEntity> Optional<S> findOne(Example<S> example) {
				return Optional.empty();
			}

			@Override
			public <S extends InventoryEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
				return null;
			}

			@Override
			public <S extends InventoryEntity> long count(Example<S> example) {
				return 0;
			}

			@Override
			public <S extends InventoryEntity> boolean exists(Example<S> example) {
				return false;
			}

			@Override
			public <S extends InventoryEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
				return null;
			}
		};
		InventoryService inventoryService = new InventoryService(null,inventoryRepository,null,null);

		ResponseModel<Integer> result =  inventoryService.getTotalQty();
		Assertions.assertEquals(20, result.getData());
	}
}
