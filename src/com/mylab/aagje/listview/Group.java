package com.mylab.aagje.listview;

import java.util.ArrayList;
import java.util.List;

import com.mylab.aagje.ObjectItem;

public class Group {

	public String string;
	public final List<ObjectItem> children = new ArrayList<ObjectItem>();

	public Group(String string) {
		this.string = string;
	}

}
