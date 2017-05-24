package hackerman.notebookmushroom.UI.fragment.maps;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MyLocationByGS implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, android.location.LocationListener {
    private LocationChangeStateListener locationChangeStateListener;
    private String TAG = "MyLocationByGS";
    private GoogleApiClient mGoogleApiClient;
    private Context context;

    interface LocationChangeStateListener {
        void onCurrentLocationChanged(Location location);
    }

    public MyLocationByGS(Context context) {
        this.context = context;
        buildGoogleApiClient(context);
    }

    public static boolean isGooglePlayServicesAvailable(@NonNull Context context) {
        boolean isAvailable = false;
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (status == ConnectionResult.SUCCESS) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public static boolean isGPSTurned(@NonNull Context context) {
        boolean isGPSTurned = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGPSTurned = true;
        }
        return isGPSTurned;
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        makeGetLocationRequest();
    }

    public void makeGetLocationRequest() {
        if (isGooglePlayServicesAvailable(context)) {
            mGoogleApiClient.connect();
        } else {
            if (locationChangeStateListener != null) {
                locationChangeStateListener.onCurrentLocationChanged(null);
            }
        }
    }

    public void setLocationChangeStateListener(LocationChangeStateListener locationChangeStateListener) {
        this.locationChangeStateListener = locationChangeStateListener;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (isGPSTurned(context)) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, getRequestRequest(), new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (locationChangeStateListener != null) {
                        locationChangeStateListener.onCurrentLocationChanged(location);
                    }
                    Log.d(TAG, "requestLocationUpdates()::" + location.getLatitude() + " " + location.getLongitude());
                    if (mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                    }

                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    private LocationRequest getRequestRequest() {
        LocationRequest request = new LocationRequest();
        request.setFastestInterval(5000);
        request.setInterval(10000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
