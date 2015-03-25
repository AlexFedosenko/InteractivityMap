package com.alexander.InteractivityMap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    private View vActivityRootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        vActivityRootView = findViewById(R.id.layout_pagesContainer);

        final CustomMapFragment contactsFragment = new CustomMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_pagesContainer, contactsFragment,
                getString(R.string.fragmentTag_map)).commit();
    }

}
