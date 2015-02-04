package com.alexander.interactivitymap.map.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.*;
import com.alexander.interactivitymap.map.*;
import com.alexander.interactivitymap.map.core.Consts;
import com.alexander.interactivitymap.map.core.CustomMarker;
import com.alexander.interactivitymap.map.core.DataManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomMapFragment extends Fragment implements ActionMode.Callback{
    private MapView vMap;
    private GoogleMap mMap;
    private Marker marker;
    private Marker mCurrentMarker;
    private MainActivity mActivity;
    private ActionMode mActionMode;

    private double latitudeToCenter;
    private double longitudeToCenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        mActivity = (MainActivity)getActivity();

        vMap = (MapView) v.findViewById(R.id.mapview);
        vMap.onCreate(savedInstanceState);
        // gets to GoogleMap from the MapView and does initialization stuff
        mMap = vMap.getMap();
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);

        // needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        // updates the location and mZoom of the MapView

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mActionMode != null) {
                    mActionMode.finish();
                }
                if (marker != null) {
                    marker.remove();
                }
                mCurrentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.toString()));
                if (mActionMode == null) {
                    mActionMode = mActivity.startSupportActionMode(CustomMapFragment.this);
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mActionMode != null && !mCurrentMarker.equals(marker)) {
                    mActionMode.finish();
                }
                mCurrentMarker = marker;
                if (mActionMode == null) {
                    mActionMode = mActivity.startSupportActionMode(CustomMapFragment.this);
                }
                return false;
            }
        });

        showMarkersOnMap();

        if (latitudeToCenter != 0 && longitudeToCenter != 0) {
            moveCameraToPosition(latitudeToCenter, longitudeToCenter);
        } else {
            // move to the last known location
            LocationManager service = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = service.getBestProvider(criteria, false);
            Location location = service.getLastKnownLocation(provider);
            if (location != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), Consts.DEFAULT_ZOOM);
                mMap.animateCamera(cameraUpdate);
            }
        }

        return v;
    }

    @Override
    public void onResume() {
        vMap.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        vMap.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        vMap.onLowMemory();
    }

    public void moveToCurrentLocation() {
        if (mMap.getMyLocation() != null) {
            moveCameraToPosition(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        }
    }

    public void moveCameraToPosition(double lat, double lng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), Consts.DEFAULT_ZOOM);
        mMap.animateCamera(cameraUpdate);
    }

    public void centerOnMarker(double lat, double lng) {
        latitudeToCenter = lat;
        longitudeToCenter = lng;
    }

    public void showMarkersOnMap() {
        for (CustomMarker marker : DataManager.getInstance(mActivity).getMarkerList()) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(marker.getLatitude(), marker.getLongitude())).title(marker.getTitle()));
        }
    }

    public void stopActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.marker_contextual_actions, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        CustomMarker customMarker = DataManager.getInstance(mActivity).getMarker(mCurrentMarker);
        if (customMarker == null || customMarker.isNew()) {
            menu.findItem(R.id.menu_edit).setVisible(false);
            menu.findItem(R.id.menu_done).setVisible(true);
        } else {
            menu.findItem(R.id.menu_edit).setVisible(true);
            menu.findItem(R.id.menu_done).setVisible(false);
        }
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        Intent intent = new Intent(mActivity, EditMarkerActivity.class);
        switch (menuItem.getItemId()) {
            case R.id.menu_edit:
                intent.putExtra(Consts.MARKER_TITLE_FIELD, DataManager.getInstance(mActivity).getMarker(mCurrentMarker).getTitle());
                intent.putExtra(Consts.MARKER_DESCRIPTION_FIELD, DataManager.getInstance(mActivity).getMarker(mCurrentMarker).getDescription());
            case R.id.menu_done:
                startActivityForResult(intent, Consts.EDIT_MARKER_ACTIVITY);
                mActionMode.finish();
                break;
            default:
                // do nothing or logging
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        CustomMarker customMarker = DataManager.getInstance(mActivity).getMarker(mCurrentMarker);
        if (customMarker == null || customMarker.isNew()) {
            mCurrentMarker.remove();
        }
        mActionMode = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Consts.EDIT_MARKER_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    String markerTitle = data.getStringExtra(Consts.MARKER_TITLE_FIELD);
                    String markerDescription = data.getStringExtra(Consts.MARKER_DESCRIPTION_FIELD);
                    double lat = mCurrentMarker.getPosition().latitude;
                    double lng  = mCurrentMarker.getPosition().longitude;
                    CustomMarker marker = new CustomMarker(lat, lng, markerTitle, markerDescription);
                    if (DataManager.getInstance(mActivity).getMarker(lat, lng) != null) {
                        mCurrentMarker.remove();
                        mCurrentMarker = null;
                    }
                    // draw or redraw marker
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(markerTitle));
                    DataManager.getInstance(mActivity).addMarker(marker);
                }
                break;
            default:
                // do nothing or logging
        }
    }
}