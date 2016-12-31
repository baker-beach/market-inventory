package com.bakerbeach.market.inventory.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bakerbeach.market.inventory.api.model.InventoryStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class InventoryMongoDaoTest {
	
	@Autowired
	private InventoryMongoDao inventoryMongoDao;
	
	@Test
	public void find(){
		try {
			InventoryStatus inventoryStatus = inventoryMongoDao.getInventoryStatus("TEST_GTIN");
			inventoryStatus = inventoryMongoDao.decrementInventory("TEST_GTIN", 45, inventoryStatus.getOutOfStockLimit());
		//	inventoryStatus = inventoryMongoDao.incrementInventory("TEST_GTIN", 40);
		} catch (InventoryDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
