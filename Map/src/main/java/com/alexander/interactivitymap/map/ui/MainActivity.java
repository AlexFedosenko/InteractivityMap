package com.alexander.interactivitymap.map.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.ActionModeWrapper;
import android.support.v7.view.ActionMode;
import android.view.View;
import com.alexander.interactivitymap.map.core.CustomMarker;
import com.alexander.interactivitymap.map.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity {

    private GoogleMap   mMap; // Might be null if Google Play services APK is not available.
    private ActionBar   mActionBar;
    private Fragment    mCurrentMainActivityFragment;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        setUpMapIfNeeded();
//    }

    private View vActivityRootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                //if (tab.getText().equals(MainActivity.this.getResources().getStringArray(R.array.navigationTabs)[0]))
                switch (tab.getPosition()) {
                    case 1:
                        ((CustomMapFragment)mCurrentMainActivityFragment).stopActionMode();
                        final MarkersFragment markersFragment = new MarkersFragment();
                        mCurrentMainActivityFragment = markersFragment;
                        fragmentTransaction.replace(R.id.layout_pagesContainer, markersFragment,
                                getString(R.string.fragmentTag_markers));
                        break;
                    case 0:
                    default:
                        final CustomMapFragment customMapFragment = new CustomMapFragment();
                        mCurrentMainActivityFragment = customMapFragment;
                        fragmentTransaction.replace(R.id.layout_pagesContainer, customMapFragment,
                                getString(R.string.fragmentTag_map));

                }
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        for (String tabName : getResources().getStringArray(R.array.navigationTabs)) {
            mActionBar.addTab(mActionBar.newTab().setText(tabName).setTabListener(tabListener));
        }

        vActivityRootView = findViewById(R.id.layout_pagesContainer);

//        showMapFragment();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mCurrentMainActivityFragment.getTag().equals(getString(R.string.fragmentTag_map))) {
            ((CustomMapFragment)mCurrentMainActivityFragment).moveToCurrentLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void showMarkerOnMap(CustomMarker marker) {
        if (mCurrentMainActivityFragment == null ||
                !mCurrentMainActivityFragment.getTag().equals(getString(R.string.fragmentTag_map))) {
            showMapFragment();
        }
        ((CustomMapFragment)mCurrentMainActivityFragment).centerOnMarker(marker.getLatitude(), marker.getLongitude());
    }



    public void showMapFragment() {
        mActionBar.selectTab(mActionBar.getTabAt(0));
    }

    /**
     * Fixes current CAB Issue 58321: The ActionMode of the ActionBarCompat is being constructed twice in ICS
     * Method must be obsolete after next CAB update
     * @param callback - callback to handle contextual actionbar actions
     * @return started support action mode
     */
    @Override
    public ActionMode startSupportActionMode(ActionMode.Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("ActionMode callback can not be null.");
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return super.startSupportActionMode(callback);
        } else {
            Context context = getSupportActionBar().getThemedContext();

            ActionModeWrapper.CallbackWrapper wrappedCallback = new ActionModeWrapper.CallbackWrapper(
                    context, callback);
            ActionModeWrapper wrappedMode = new ActionModeWrapper(context,
                    startActionMode(wrappedCallback));
            wrappedCallback.setLastStartedActionMode(wrappedMode);

            return wrappedMode;
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                    .getMap();
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                setUpMap();
//            }
//        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
