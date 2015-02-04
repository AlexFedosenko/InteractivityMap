package com.alexander.interactivitymap.map.core;

public class CustomMarker {
    private final double mLatitude;
    private final double mLongitude;
    private String mTitle;
    private String mDescription;

    public CustomMarker(double latitude, double longtitude) {
        mLatitude = latitude;
        mLongitude = longtitude;
    }

    public CustomMarker(double latitude, double longtitude, String title, String description) {
        mLatitude = latitude;
        mLongitude = longtitude;
        mTitle = title;
        mDescription = description;
    }

    public void setTitle(String newTitle) {
        mTitle = newTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDescription(String newDescription) {
        mDescription = newDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public boolean isNew() {
        return mTitle == null || mTitle.isEmpty();
    }
}
