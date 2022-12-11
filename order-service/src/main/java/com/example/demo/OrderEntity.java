package com.example.demo;

public class OrderEntity {

	private String orderItem;
	private String receiverName;
	private String supplierName;

	public String getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(String orderItem) {
		this.orderItem = orderItem;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public OrderEntity(String orderItem, String receiverName, String supplierName) {
		super();
		this.orderItem = orderItem;
		this.receiverName = receiverName;
		this.supplierName = supplierName;
	}

	public OrderEntity() {
		super();
	}

	@Override
	public String toString() {
		return "OrderEntity [orderItem=" + orderItem + ", receiverName=" + receiverName + ", supplierName="
				+ supplierName + "]";
	}

}
