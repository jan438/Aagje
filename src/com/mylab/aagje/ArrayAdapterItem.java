package com.mylab.aagje;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ArrayAdapterItem extends ArrayAdapter<ObjectItem> {
	Context mContext;
	int layoutResourceId;
	List<ObjectItem> data = null;

	public ArrayAdapterItem(Context mContext, int layoutResourceId,
			List<ObjectItem> objectItemData) {
		super(mContext, layoutResourceId, objectItemData);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = objectItemData;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(layoutResourceId, parent, false);
		}
		final ObjectItem objectItem = data.get(position);
		ImageView imageViewItem = (ImageView) convertView
				.findViewById(R.id.imageViewItem);
		imageViewItem.setImageBitmap(objectItem.image);
		TextView textViewItem = (TextView) convertView
				.findViewById(R.id.textViewItem);
		textViewItem.setText(Html.fromHtml("<h5>" + objectItem.itemName + "  "
				+ objectItem.itemPrice + "</h5>\n<h6>" + objectItem.count + "</h6>"));
		textViewItem.setTag(objectItem.itemId);
		ImageButton buttonViewItem = (ImageButton) convertView
				.findViewById(R.id.buttonViewItem);
		buttonViewItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (objectItem.count > 0) {
					objectItem.count--;
				}
				if (objectItem.count == 0) data.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

}
