package hackerman.notebookmushroom.UI.fragment.maps;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import hackerman.notebookmushroom.db.PlaceObj;
import hackerman.notebookmushroom.db.repositories.PlaceObjRepository;

import static hackerman.notebookmushroom.app.App.getAppContext;

/**
 * Created by hackerman on 13.03.17.
 */

public class MapsPresenterIml extends MapsPresenter {

    private Context context;

    public MapsPresenterIml(Context context) {
        this.context = getAppContext();
    }

    public MapsPresenterIml() {
    }

    @Override
    public void addPhotoFromGallery(Intent data, String key) {
        if (data != null) {
            final Uri uri = data.getData();
            String pathFromUri = getPathFromUri(uri);
            PlaceObj placeObj = PlaceObjRepository.getPlaceObj(key);
            if (placeObj.photo == null) {
                placeObj.photo = pathFromUri;
                PlaceObjRepository.updatePlaceObj(placeObj);
            } else {
                placeObj.photo = null;
                placeObj.photo = pathFromUri;
                PlaceObjRepository.updatePlaceObj(placeObj);
            }

        }
    }

    @Override
    public void markerListener(Marker marker) {
        final String snippet = marker.getSnippet();
        final LatLng position = marker.getPosition();
        final PlaceObj plaseObj = PlaceObjRepository.getPlaceObj(snippet);
        plaseObj.lat = position.latitude;
        plaseObj.lng = position.longitude;
        PlaceObjRepository.updatePlaceObj(plaseObj);
    }

    @Override
    public void deleteMarker(Marker marker) {
        if (marker != null) {
            final String snippet = marker.getSnippet();
            PlaceObj placeObj = PlaceObjRepository.getPlaceObj(snippet);
            if (placeObj.photo == null) {
                PlaceObjRepository.deletePlaceObj(placeObj);
                marker.remove();
            } else {
                placeObj.photo = null;
                PlaceObjRepository.deletePlaceObj(placeObj);
                marker.remove();

            }

        }

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPathFromUri(Uri uri) {
        String filePath = null;
        try {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split("=")[1];

            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            filePath = getPathFromGalleryV14(uri);
        }
        return filePath;
    }

    private String getPathFromGalleryV14(Uri uri) {
        if (uri == null) {
            return null;
        }
        String filePath = "";
        Cursor cursor = null;
        java.io.File file = null;

        switch (0) {
            case 0: {
                cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            case 1: {
                cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Video.VideoColumns.DATA}, null, null, null);
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            case 2: {
                filePath = uri.getPath();
                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            default: {
                return null;
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (filePath != null && !TextUtils.isEmpty(filePath)) {
            filePath = filePath.trim();
        }
        return filePath;
    }

}
