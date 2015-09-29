package com.mylab.aagje;

import android.graphics.Bitmap;

public class ObjectItem {

	public String itemId;
	public String itemName;
	public Bitmap image;
	public String itemPrice;
	public String itemAssortment;
	public boolean onoff;
	
	public boolean getOnoff() {
		return onoff;
	}

	public void setOnoff(boolean onoff) {
		this.onoff = onoff;
	}

	public String getItemAssortment() {
		return itemAssortment;
	}

	public void setItemAssortment(String itemAssortment) {
		this.itemAssortment = itemAssortment;
	}

	public int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String barcode) {
		this.itemId = barcode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public ObjectItem(String itemId, String itemName, String itemPrice,
			Bitmap image, String itemAssortment, int count, boolean onoff) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.image = image;
		this.itemPrice = itemPrice;
		this.itemAssortment = itemAssortment;
		this.count = count;
		this.onoff = onoff;
	}

}