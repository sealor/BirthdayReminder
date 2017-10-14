package de.ubuntix.android.birthdayreminder.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class MultiListAdapter extends BaseAdapter {

	private final List<ListAdapter> listAdapters = new ArrayList<ListAdapter>();

	public List<ListAdapter> getListAdapters() {
		return listAdapters;
	}

	@Override
	public int getCount() {
		int count = 0;
		for (ListAdapter listAdapter : this.listAdapters) {
			count += listAdapter.getCount();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		int positionOffset = 0;
		for (ListAdapter listAdapter : this.listAdapters) {
			if (position - positionOffset < listAdapter.getCount()) {
				return listAdapter.getItem(position - positionOffset);
			}
			positionOffset += listAdapter.getCount();
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int positionOffset = 0;
		for (ListAdapter listAdapter : this.listAdapters) {
			if (position - positionOffset < listAdapter.getCount()) {
				return listAdapter.getView(position - positionOffset, convertView, parent);
			}
			positionOffset += listAdapter.getCount();
		}
		return null;
	}

	@Override
	public int getViewTypeCount() {
		int viewTypeCount = 0;
		for (ListAdapter listAdapter : this.listAdapters) {
			viewTypeCount += listAdapter.getViewTypeCount();
		}
		// viewTypeCount may not be 0
		if (viewTypeCount == 0) {
			return 1;
		}
		return viewTypeCount;
	}

	@Override
	public int getItemViewType(int position) {
		int positionOffset = 0;
		int typeOffset = 0;
		for (ListAdapter listAdapter : this.listAdapters) {
			if (position - positionOffset < listAdapter.getCount()) {
				return typeOffset + listAdapter.getItemViewType(position - positionOffset);
			}
			positionOffset += listAdapter.getCount();
			typeOffset += listAdapter.getViewTypeCount();
		}
		return -1;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		int positionOffset = 0;
		for (ListAdapter listAdapter : this.listAdapters) {
			if (position - positionOffset < listAdapter.getCount()) {
				return listAdapter.isEnabled(position - positionOffset);
			}
			positionOffset += listAdapter.getCount();
		}
		return false;
	}
}
