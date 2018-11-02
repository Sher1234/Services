package io.github.sher1234.service.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.github.sher1234.service.util.MaterialDialog;

@SuppressWarnings("all")
public class LocationTrack extends Service implements LocationListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    private final Context mContext;
    public LocationManager locationManager;
    private boolean gpsAvailable = false;
    private Location networkLocation;
    private boolean networkAvailable = false;
    private Location gpsLocation;

    public LocationTrack() {
        mContext = this;
        onLocationTrack();
        gpsLocation = getLocationGps();
        networkLocation = getLocationNetwork();
    }

    public LocationTrack(Context context) {
        mContext = context;
        onLocationTrack();
        gpsLocation = getLocationGps();
        networkLocation = getLocationNetwork();
    }

    public boolean isLocationAvailable() {
        return gpsAvailable && networkAvailable;
    }

    private void onLocationTrack() {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            gpsAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkAvailable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } else {
            gpsAvailable = false;
            networkAvailable = false;
        }
    }

    @Nullable
    private Location getLocationNetwork() {
        if (locationManager == null)
            return null;
        networkAvailable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!networkAvailable)
            return null;
        else {
            if (mContext.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 0, 0)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((AppCompatActivity) mContext, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                return null;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }

    public void showSettingsAlert() {
        MaterialDialog dialog = MaterialDialog.Dialog(mContext)
                .setTitle("Enable Location")
                .setDescription("Set location mode to \"High Accuracy\" to continue using application.")
                .positiveButton("Yes", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .negativeButton("Exit", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        ((AppCompatActivity) mContext).finish();
                        dialog.dismiss();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void stopListener() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((AppCompatActivity) mContext, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return;
            }
            locationManager.removeUpdates(LocationTrack.this);
        }
    }

    @Nullable
    private Location getLocationGps() {
        if (locationManager == null)
            return null;
        gpsAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsAvailable)
            return null;
        else {
            if (mContext.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 0, 0)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((AppCompatActivity) mContext, new String[]
                        {Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return null;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public Location getGpsLocation() {
        gpsLocation = getLocationGps();
        return gpsLocation;
    }

    public Location getNetworkLocation() {
        networkLocation = getLocationNetwork();
        return networkLocation;
    }
}