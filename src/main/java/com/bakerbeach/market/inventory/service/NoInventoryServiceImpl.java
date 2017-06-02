package com.bakerbeach.market.inventory.service;

import com.bakerbeach.market.inventory.api.model.InventoryStatus;
import com.bakerbeach.market.inventory.api.model.TransactionData;
import com.bakerbeach.market.inventory.api.service.InventoryService;
import com.bakerbeach.market.inventory.api.service.InventoryServiceException;
import com.bakerbeach.market.inventory.model.InventoryStatusImpl;
import com.bakerbeach.market.order.api.model.Order;

public class NoInventoryServiceImpl implements InventoryService {

	@Override
	public InventoryStatus getInventoryStatus(String gtin) throws InventoryServiceException {
		InventoryStatusImpl inventoryStatus = new InventoryStatusImpl(gtin);
		inventoryStatus.setStock(1000);
		inventoryStatus.setMaximumOrderQuantity(inventoryStatus.getStock() + inventoryStatus.getOutOfStockLimit());
		inventoryStatus.setAvailable(inventoryStatus.getMaximumOrderQuantity() > 0);
		inventoryStatus.setOutOfStock(inventoryStatus.getStock() <= 0);

		return inventoryStatus;
	}

	@Override
	public void incrementInventory(String gtin, Integer quantity) throws InventoryServiceException {
	}

	@Override
	public void decrementInventory(String gtin, Integer quantity) throws InventoryServiceException {
	}

	@Override
	public void setInventory(String gtin, Integer quantity) throws InventoryServiceException {
	}

	@Override
	public void confirm(TransactionData transactionData, Order order) throws InventoryServiceException {
	}

	@Override
	public TransactionData decrement(Order arg0) throws InventoryServiceException {
		return null;
	}

	@Override
	public void rollBack(TransactionData arg0) throws InventoryServiceException {
	}

}
