package com.example.demo;

public class ReceiverEntity {
	private String receiverName;
	private String supplierName;

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

	public ReceiverEntity(String receiverName, String supplierName) {
		super();
		this.receiverName = receiverName;
		this.supplierName = supplierName;
	}

	public ReceiverEntity() {
		super();
	}

	@Override
	public String toString() {
		return "ReceiverEntity [receiverName=" + receiverName + ", supplierName=" + supplierName + "]";
	}

}
