package com.example.demo;

public class SupplierEntity {

	private String supplierName;

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public SupplierEntity(String supplierName) {
		super();
		this.supplierName = supplierName;
	}

	public SupplierEntity() {
		super();
	}

	@Override
	public String toString() {
		return "SupplierEntity [supplierName=" + supplierName + "]";
	}

}
