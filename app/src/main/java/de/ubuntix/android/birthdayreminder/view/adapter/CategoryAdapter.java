package de.ubuntix.android.birthdayreminder.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {

	private final String name;
	private final LayoutInflater inflater;

	public CategoryAdapter(Context context, String name) {
		this.inflater = LayoutInflater.from(context);
		this.name = name;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) {
			return this.name;
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView categoryView;
		if (convertView == null) {
			categoryView = (TextView) this.inflater.inflate(android.R.layout.preference_category, null);
		} else {
			categoryView = (TextView) convertView;
		}

		categoryView.setText(this.name);
		return categoryView;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}
}
