package com.alexander.interactivitymap.map.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.alexander.interactivitymap.map.core.DataManager;
import com.alexander.interactivitymap.map.R;

public class MarkersFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View rootView = inflater.inflate(R.layout.markers_fragment, container, false);
//        ((AvoxMainActivity) getActivity()).getNavigationDrawerToggle().setDrawerIndicatorEnabled(false);

        final ListView vMarkerList = (ListView) rootView.findViewById(R.id.list_markers);
        final MarkerListAdapter adapter = new MarkerListAdapter(getActivity(), DataManager.getInstance(getActivity()).getMarkerList(), vMarkerList);
        vMarkerList.setAdapter(adapter);

        return rootView;
    }
}
