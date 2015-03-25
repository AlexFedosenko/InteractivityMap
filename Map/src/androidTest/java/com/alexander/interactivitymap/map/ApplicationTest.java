package com.alexander.interactivitymap.map;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import com.alexander.interactivitymap.map.core.CustomMarker;
import com.alexander.interactivitymap.map.core.DataManager;
import com.alexander.interactivitymap.map.core.DatabaseHelper;

public class ApplicationTest extends ApplicationTestCase<Application> {

    Context mContext;
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getContext();
        DataManager.getInstance(mContext);
        DatabaseHelper.getInstance(mContext);
    }

    @SmallTest
    public void dataManagerTest() {
        DataManager.getInstance(mContext).addMarker(new CustomMarker(0, 0, "a", "b"));
        DataManager.getInstance(mContext).addMarker(new CustomMarker(10, -0.5, "test", "test"));
        DataManager.getInstance(mContext).addMarker(new CustomMarker(100, -0.5, "marker", "test"));
        DataManager.getInstance(mContext).addMarker(new CustomMarker(18.2, -27.4, "title", "description"));
        DataManager.getInstance(mContext).addMarker(new CustomMarker(0.4, 14.87, "new", "text"));
        DataManager.getInstance(mContext).addMarker(new CustomMarker(0.02, 24.3, "point", "map"));
        DataManager.getInstance(mContext).addMarker(new CustomMarker(-15.369, -4.75, "home", "my home"));
        assertEquals(DataManager.getInstance(mContext).getMarkerList().size(), 7);
        DataManager.getInstance(mContext).addMarker(new CustomMarker(-15.369, -4.75, "new home", "my new home"));
        assertEquals(DataManager.getInstance(mContext).getMarkerList().size(), 7);
        DataManager.getInstance(mContext).addMarker(new CustomMarker(-15.36, -4.75, "tree", "tree"));
        assertEquals(DataManager.getInstance(mContext).getMarkerList().size(), 8);

        assertNull(DataManager.getInstance(mContext).getMarker(1, 1));
        assertNotNull(DataManager.getInstance(mContext).getMarker(0, 0));
    }

    @SmallTest
    public void databaseTest() {
        assertEquals(DatabaseHelper.getInstance(mContext).getDatabaseName(), "InterMap.db");

        DatabaseHelper.getInstance(mContext).addMarker(0, 0, "a", "b");
        DatabaseHelper.getInstance(mContext).addMarker(10, -0.5, "test", "test");
        assertEquals(DatabaseHelper.getInstance(mContext).getMarkers().size(), 2);
    }

    @SmallTest
    public void customMarkerTest() {
        CustomMarker marker1 = new CustomMarker(0, 0, "title", "description");
        assertEquals(marker1.getTitle(), "title");
        assertEquals(marker1.getDescription(), "description");

        CustomMarker marker2 = new CustomMarker(0, 0);
        assertEquals(marker1.getLatitude(), marker2.getLatitude());
        assertEquals(marker1.getLongitude(), marker2.getLongitude());
    }

    // TODO: test UI
}