package com.alexander.interactivitymap.map.core;

import android.content.Context;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager sInstance;

    private List<CustomMarker> mMarkerList;
    private Context mContext;

    private DataManager(Context context, List<CustomMarker> markers) {
        mMarkerList = new ArrayList<CustomMarker>(markers);
        mContext = context;
    }

    public static DataManager getInstance(Context context) {
        if (sInstance == null) {
            // in general case it would be better to load data from the database asynchronously in the separate thread (e.g. in loader)
            // but for now simple solution will be sufficient
            sInstance = new DataManager(context, DatabaseHelper.getInstance(context).getMarkers());
        }
        return sInstance;
    }


    public List<CustomMarker> getMarkerList() {
        return mMarkerList;
    }

    public CustomMarker getMarker(double lat, double lng) {
        for (CustomMarker marker : mMarkerList) {
            if (marker.getLatitude() == lat && marker.getLongitude() == lng) {
                return marker;
            }
        }
        return null;
    }

    public CustomMarker getMarker(Marker marker) {
        return getMarker(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    public void addMarker(CustomMarker marker) {
        if (getMarker(marker.getLatitude(), marker.getLongitude()) == null) {
            mMarkerList.add(marker);
            DatabaseHelper.getInstance(mContext).addMarker(marker.getLatitude(), marker.getLongitude(), marker.getTitle(), marker.getDescription());
        } else {
            mMarkerList.set(mMarkerList.indexOf(getMarker(marker.getLatitude(), marker.getLongitude())), marker);
            DatabaseHelper.getInstance(mContext).editMarker(marker.getLatitude(), marker.getLongitude(), marker.getTitle(), marker.getDescription());
        }
    }
}
