package hackerman.notebookmushroom.UI.fragment.maps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.common.BaseActivity;
import hackerman.notebookmushroom.db.PlaceObj;
import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.PlaceObjRepository;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;

public class MapsActivity extends BaseActivity<MapsView, MapsPresenter> implements OnMapReadyCallback, MapsView {
    private TodoObj todoObj;
    private PlaceObj placeObj;
    private MyLocationByGS myLocationByGS;
    private List<PlaceObj> placeItems;
    private String keyTodoObj;
    private static final int GALLERY_REQUEST = 4000;
    private String keyPlaceObj;
    private Marker myMarker;
    private GoogleMap googleMap;
    private String photo;
    private Polyline polyline;
    private List<LatLng> latLngs;
    private ClusterManager<MyItem> clusterManager;
    private ClusterRenderer<MyItem> clusterRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        placeItems = new ArrayList<>();
        Intent intent = getIntent();
        keyTodoObj = intent.getStringExtra("add plase");
        todoObj = TodoObjRepository.getTodoObj(keyTodoObj);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latLngs = new ArrayList<>();

    }

    @NonNull
    @Override
    public MapsPresenter createPresenter() {
        return new MapsPresenterIml(getApplicationContext());
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setMyLocationEnabled(true);
        if (todoObj.longitude == 0 && todoObj.latitude == 0) {
            myLocationByGS = new MyLocationByGS(this);
            myLocationByGS.setLocationChangeStateListener(location -> getAddressesByTodoCoordinates(MapsActivity.this, location.getLatitude(), location.getLongitude(), new Callback() {
                @Override
                public void called(final String adress) {
                    runOnUiThread(() -> {
                        todoObj.latitude = location.getLatitude();
                        todoObj.longitude = location.getLongitude();
                        TodoObjRepository.updateTodoObj(todoObj);
                        todoObj.address = adress;
                        TodoObjRepository.updateTodoObj(todoObj);
                        setResult(RESULT_OK);
                    });
                }
            }));
            myLocationByGS.makeGetLocationRequest();

        } else {
            getAddressesByTodoCoordinates(this, todoObj.latitude, todoObj.longitude, adress -> {
                // addMarkerToTodoPosition(googleMap, adress, todoObj.latitude, todoObj.longitude);
//               runOnUiThread(() -> {
//                   clusterManager=new ClusterManager<MyItem>(MapsActivity.this,googleMap);
//                   googleMap.setOnCameraIdleListener(clusterManager);
//
//               });

                addMarkerToPlaseObj(googleMap, adress);

            });
        }
        googleMap.setOnMapLongClickListener(latLng -> {
            placeObj = new PlaceObj();
            placeObj.dateAdded = String.valueOf(System.currentTimeMillis());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .snippet(placeObj.dateAdded)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            placeObj.parentKey = todoObj.key;
            placeObj.lat = latLng.latitude;
            placeObj.lng = latLng.longitude;
            PlaceObjRepository.addPlaceObj(placeObj);
            placeItems.add(placeObj);
            PlaceObjRepository.updatePlaceObj(placeObj);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        });
        googleMap.setOnMarkerClickListener(marker -> {
            new MaterialDialog.Builder(MapsActivity.this)
                    .items("Delete Marker", "Add photo")
                    .itemsCallback((dialog, itemView, position, text) -> {
                        switch (position) {
                            case 0: {
                                getPresenter().deleteMarker(marker);
                                restartActivity();
                                break;
                            }
                            case 1: {
                                addPhotoFromGallery(marker);
                                break;
                            }
                        }
                    }).show();
            return true;
        });
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                getPresenter().markerListener(marker);

            }
        });
    }


    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    getPresenter().addPhotoFromGallery(data, keyPlaceObj);
                    restartActivity();
            }
        }
    }

    private void addPhotoFromGallery(Marker marker) {
        LatLng position = marker.getPosition();
        final String snippet = marker.getSnippet();
        placeObj = PlaceObjRepository.getPlaceObj(snippet);
        keyPlaceObj = placeObj.dateAdded;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent, GALLERY_REQUEST);
        myMarker = marker;
    }

    private void addMarkerToPlaseObj(final GoogleMap googleMap, final String address) {
        runOnUiThread(() -> {
            final String key = todoObj.key;
            PlaceObjRepository.getPlacesAsObservasble(key)
                    .subscribe(places -> {
                        for (PlaceObj obj : places) {
                            if (obj.photo != null) {
                                final LatLng position1 = new LatLng(obj.lat, obj.lng);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 20;
                                final Bitmap bitmap = BitmapFactory.decodeFile(obj.photo, options);
                                googleMap.addMarker(new MarkerOptions()
                                        .position(position1)
                                        .alpha(1)
                                        .snippet(obj.dateAdded)
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                        .anchor(0.1f, 1.5f));
                            }
                            LatLng latLngLocation = new LatLng(obj.lat, obj.lng);
                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .width(6)
                                    .color(R.color.md_blue_300)
                                    .geodesic(true);
                            latLngs.add(latLngLocation);

                            polylineOptions.addAll(latLngs);
                            polyline = googleMap.addPolyline(polylineOptions);
                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLngLocation)
                                    .draggable(false)
                                    .snippet(obj.dateAdded)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
//                            MyItem offsetItem = new MyItem(latLngLocation.latitude, latLngLocation.longitude);
//                            clusterManager.addItem(offsetItem);
                        }
                    });
        });

    }

    private void addMarkerToTodoPosition(final GoogleMap googleMap, final String address, final double latitude, final double longitude) {
        runOnUiThread(() -> {
                    LatLng spas = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions()
                            .visible(true)
                            .position(spas)
                            .snippet(address)
                            .draggable(true)
                            .title(address));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(spas));
                }
        );
    }

    public static void getAddressesByTodoCoordinates(final Context context, final double latitude, final double longitude, final Callback callback) {
        new Thread(() -> {
            Location location = new Location("");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            Geocoder geocoder =
                    new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
            // If the reverse geocode returned an address
            String addressText = null;
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);

                String addressStr = address.getAddressLine(0);
                String addressCity = address.getLocality();
                if (addressCity == null) {
                    addressCity = addressStr;
                }

                String addressCountry = address.getCountryName();
                addressText = String.format(
                        "%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ?
                                addressStr : "",
                        addressCity,
                        addressCountry);
                callback.called(addressText);
            }
        }).start();
    }

    public interface Callback {
        void called(String adress);
    }
}
