package hackerman.notebookmushroom.UI.fragment.maps;

import android.content.Intent;

import com.google.android.gms.maps.model.Marker;

import hackerman.notebookmushroom.common.BasePresenter;


public abstract class MapsPresenter extends BasePresenter<MapsView> {

    public abstract void addPhotoFromGallery(Intent intent,String key);
    public abstract void markerListener(Marker marker);

    public abstract void deleteMarker(Marker marker);
}
