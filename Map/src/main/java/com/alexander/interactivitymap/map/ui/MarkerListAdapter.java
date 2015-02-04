package com.alexander.interactivitymap.map.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alexander.interactivitymap.map.core.CustomMarker;
import com.alexander.interactivitymap.map.R;

import java.util.ArrayList;
import java.util.List;

public class MarkerListAdapter extends BaseAdapter implements View.OnClickListener{

    private MainActivity mActivity;
    private List<CustomMarker> mMarkerList;
    private ListView vList;

    public MarkerListAdapter(final Activity activity, final List<CustomMarker> markers, final ListView listView) {
        mActivity = (MainActivity) activity;
        mMarkerList = new ArrayList<CustomMarker>(markers);
        vList = listView;
    }
    @Override
    public int getCount() {
        return mMarkerList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMarkerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_marker, viewGroup, false);

            viewHolder.vLayoutMarker = (LinearLayout) view.findViewById(R.id.layout_marker);
            viewHolder.vLayoutMarker.setTag(position);
            viewHolder.vTextMarkerTitle = (TextView) view.findViewById(R.id.text_markerTitle);
            viewHolder.vTextMarkerDescription = (TextView) view.findViewById(R.id.text_markerDescription);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.vLayoutMarker.setOnClickListener(this);

        viewHolder.vTextMarkerTitle.setText(mMarkerList.get(position).getTitle());
        viewHolder.vTextMarkerDescription.setText(mMarkerList.get(position).getDescription());

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() != null) {
            mActivity.showMarkerOnMap(mMarkerList.get(vList.getPositionForView(view)));
        }
    }

    private static class ViewHolder {
        private ViewGroup   vLayoutMarker;
        private TextView    vTextMarkerTitle;
        private TextView	vTextMarkerDescription;
    }
}
