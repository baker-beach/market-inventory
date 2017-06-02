package com.bakerbeach.market.inventory.service;

import java.util.ArrayList;
import java.util.List;

import com.bakerbeach.market.commons.MessageImpl;
import com.bakerbeach.market.commons.Messages;
import com.bakerbeach.market.commons.MessagesImpl;
import com.bakerbeach.market.core.api.model.CartItemQualifier;
import com.bakerbeach.market.inventory.api.model.InventoryStatus;
import com.bakerbeach.market.inventory.api.model.TransactionData;
import com.bakerbeach.market.inventory.api.service.InventoryService;
import com.bakerbeach.market.inventory.api.service.InventoryServiceException;
import com.bakerbeach.market.inventory.model.InventoryStatusImpl;
import com.bakerbeach.market.order.api.model.Order;
import com.bakerbeach.market.order.api.model.OrderItem;

public class InventoryServiceImpl implements InventoryService {
	private InventoryMongoDao inventoryMongoDao;

	@Override
	public TransactionData decrement(Order order) throws InventoryServiceException {
		SimpleTransactionData transactionData = new SimpleTransactionData();
		Messages messages = new MessagesImpl();

		for (OrderItem item : order.getItems()) {
			try {
				if (item.getQualifier().equals(CartItemQualifier.PRODUCT)
						|| item.getQualifier().equals(CartItemQualifier.VPRODUCT)) {
					decrementInventory(item.getGtin(), item.getQuantity().intValue());
					transactionData.getBookedItems().add(item);
				}
			} catch (Exception e) {
				messages.add(new MessageImpl(MessageImpl.TYPE_ERROR, "inventory.outOfStock", item.getGtin()));
				throw new InventoryServiceException(messages);
			}
		}

		return transactionData;
	}

	@Override
	public void confirm(TransactionData transactionData, Order order) throws InventoryServiceException {
	}
	
	@Override
	public void rollBack(TransactionData transactionData) throws InventoryServiceException {
		if (transactionData != null) {
			for (OrderItem item : transactionData.getBookedItems()) {
				if (item.getQualifier().equals(CartItemQualifier.PRODUCT)
						|| item.getQualifier().equals(CartItemQualifier.VPRODUCT)) {
					incrementInventory(item.getGtin(), item.getQuantity().intValue());
				}
			}
		}
	}

	@Override
	public InventoryStatus getInventoryStatus(String gtin) throws InventoryServiceException {
		try {
			return inventoryMongoDao.getInventoryStatus(gtin);
		} catch (InventoryDaoException e) {
			return new InventoryStatusImpl(gtin);
		}
	}

	@Override
	public void incrementInventory(String gtin, Integer quantity) throws InventoryServiceException {
		try {
			inventoryMongoDao.incrementInventory(gtin, quantity);
		} catch (InventoryDaoException e) {
		}
	}

	@Override
	public void decrementInventory(String gtin, Integer quantity) throws InventoryServiceException {
		try {
			InventoryStatus inventoryStatus = inventoryMongoDao.getInventoryStatus(gtin);
			inventoryMongoDao.decrementInventory(gtin, quantity, inventoryStatus.getOutOfStockLimit());
		} catch (InventoryDaoException e) {
			throw new InventoryServiceException(new MessageImpl(MessageImpl.TYPE_ERROR, "inventory.outOfStock", gtin));
		}
	}

	@Override
	public void setInventory(String gtin, Integer quantity) throws InventoryServiceException {
		try {
			inventoryMongoDao.setInventory(gtin, quantity);
		} catch (Exception e) {
			throw new InventoryServiceException();
		}
	}

	/**
	 * @return the inventoryMongoDao
	 */
	public InventoryMongoDao getInventoryMongoDao() {
		return inventoryMongoDao;
	}

	/**
	 * @param inventoryMongoDao
	 *            the inventoryMongoDao to set
	 */
	public void setInventoryMongoDao(InventoryMongoDao inventoryMongoDao) {
		this.inventoryMongoDao = inventoryMongoDao;
	}

	public static class SimpleTransactionData implements TransactionData {
		private List<OrderItem> bookedItems = new ArrayList<OrderItem>();

		@Override
		public List<OrderItem> getBookedItems() {
			return bookedItems;
		}

	}

}
