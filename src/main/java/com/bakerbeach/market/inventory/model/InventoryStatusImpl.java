package com.bakerbeach.market.inventory.model;

import com.bakerbeach.market.inventory.api.model.InventoryStatus;

public class InventoryStatusImpl implements InventoryStatus {
	
	private boolean outOfStock = false;
	private boolean available = false;
	private String gtin;
	private Integer maximumOrderQuantity = 0;
	private Integer outOfStockLimit = 0;
	private Integer stock = 0;
	
	public InventoryStatusImpl(String gtin){
		this.gtin = gtin;
	}
	
	@Override
	public boolean isOutOfStock() {
		return outOfStock;
	}
	
	public void setOutOfStock(boolean outOfStock) {
		this.outOfStock = outOfStock;
	}
	
	@Override
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	@Override
	public String getGtin() {
		return gtin;
	}
	
	public void setGtin(String gtin) {
		this.gtin = gtin;
	}
	
	@Override
	public Integer getMaximumOrderQuantity() {
		return maximumOrderQuantity;
	}
	
	public void setMaximumOrderQuantity(Integer maximumOrderQuantity) {
		this.maximumOrderQuantity = maximumOrderQuantity;
	}
	
	@Override
	public Integer getOutOfStockLimit() {
		return outOfStockLimit;
	}
	
	public void setOutOfStockLimit(Integer outOfStockLimit) {
		this.outOfStockLimit = outOfStockLimit;
	}
	
	@Override
	public Integer getStock() {
		return stock;
	}
	
	public void setStock(Integer stock) {
		this.stock = stock;
	}

}
