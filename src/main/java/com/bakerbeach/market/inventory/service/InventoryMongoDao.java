package com.bakerbeach.market.inventory.service;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.bakerbeach.market.inventory.api.model.InventoryStatus;
import com.bakerbeach.market.inventory.model.InventoryStatusImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class InventoryMongoDao {

	private MongoTemplate mongoTemplate;

	private String collectionName;

	public InventoryStatus setInventory(String gtin, Integer quantity) throws InventoryDaoException {
		try {
			DBObject dbo = getDBCollection().findAndModify(new BasicDBObject("gtin", gtin), null, null, false,
					new BasicDBObject("$set", new BasicDBObject("stock", quantity)), true, false);
			if (dbo != null) {
				return decode(dbo);				
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new InventoryDaoException(e);
		}
	}

	public InventoryStatus incrementInventory(String gtin, Integer quantity) throws InventoryDaoException {
		try {
			DBObject dbo = getDBCollection().findAndModify(new BasicDBObject("gtin", gtin), null, null, false,
					new BasicDBObject("$inc", new BasicDBObject("stock", quantity)), true, true);

			return decode(dbo);
		} catch (Exception e) {
			throw new InventoryDaoException(e);
		}
	}

	public InventoryStatus decrementInventory(String gtin, Integer quantity, Integer outOfStockLimit)
			throws InventoryDaoException {
		try {
			QueryBuilder qb = QueryBuilder.start();
			qb.and("gtin").is(gtin);
			qb.and("stock").greaterThanEquals(quantity - outOfStockLimit);
			qb.and("out_of_stock_limit").is(outOfStockLimit);

			DBObject dbo = getDBCollection().findAndModify(qb.get(), null, null, false,
					new BasicDBObject("$inc", new BasicDBObject("stock", -1 * quantity)), true, false);

			return decode(dbo);
		} catch (Exception e) {
			throw new InventoryDaoException(e);
		}
	}

	public InventoryStatus getInventoryStatus(String gtin) throws InventoryDaoException {
		QueryBuilder qb = QueryBuilder.start();
		qb.and("gtin").is(gtin);

		DBObject src = getDBCollection().findOne(qb.get());

		if (src != null)
			return decode(src);
		else
			throw new InventoryDaoException();
	}

	private InventoryStatus decode(DBObject source) {

		InventoryStatusImpl inventoryStatus = new InventoryStatusImpl((String) source.get("gtin"));

		inventoryStatus.setStock((Integer) source.get("stock"));
		inventoryStatus.setOutOfStockLimit((Integer) source.get("out_of_stock_limit"));
		inventoryStatus.setMaximumOrderQuantity(inventoryStatus.getStock() + inventoryStatus.getOutOfStockLimit());
		inventoryStatus.setAvailable(inventoryStatus.getMaximumOrderQuantity() > 0);
		inventoryStatus.setOutOfStock(inventoryStatus.getStock() <= 0);

		return inventoryStatus;
	}

	private DBCollection getDBCollection() {
		return mongoTemplate.getCollection(collectionName);
	}

	/**
	 * @return the mongoTemplate
	 */
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	/**
	 * @param mongoTemplate
	 *            the mongoTemplate to set
	 */
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * @return the collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * @param collectionName
	 *            the collectionName to set
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

}
