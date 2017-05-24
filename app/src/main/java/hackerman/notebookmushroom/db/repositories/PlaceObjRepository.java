package hackerman.notebookmushroom.db.repositories;

import com.github.gfx.android.orma.Selector;
import com.google.android.gms.location.places.Place;

import java.util.List;

import hackerman.notebookmushroom.app.App;
import hackerman.notebookmushroom.db.PlaceObj;
import hackerman.notebookmushroom.db.PlaceObj_Relation;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static hackerman.notebookmushroom.app.App.orma;


public class PlaceObjRepository {
    public static List<PlaceObj> getPlaseObjList() {
        return orma()
                .relationOfPlaceObj()
                .orderByParentKeyAsc()
                .selector()
                .toList();
    }

    public static void addPlaceObj(PlaceObj placeObj) {
        App.orma()
                .relationOfPlaceObj()
                .upserter()
                .execute(placeObj);
    }

    public static void deletePlaceObj(PlaceObj placeObj) {
        App.orma()
                .deleteFromPlaceObj()
                .dateAddedEq(placeObj.dateAdded)
                .execute();

    }

    public static void updatePlaceObj(PlaceObj placeObj) {
        App.orma()
                .relationOfPlaceObj()
                .upserter()
                .execute(placeObj);

    }

    public static PlaceObj getPlaceObj(String dateAdded) {
        return App.orma()
                .relationOfPlaceObj()
                .dateAddedEq(dateAdded)
                .selector()
                .getOrNull(0);
    }

    public static List<PlaceObj> getPlaces(String parentkey) {
        return App.orma()
                .relationOfPlaceObj()
                .parentKeyEq(parentkey)
                .selector()
                .toList();
    }

    public static Observable<List<PlaceObj>> getPlacesAsObservasble(String parentKey) {
        return Observable.create(new ObservableOnSubscribe<List<PlaceObj>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PlaceObj>> emmiter) throws Exception {
                final PlaceObj_Relation placeObjs = App.orma()
                        .relationOfPlaceObj()
                        .parentKeyEq(parentKey);
                final Observable<Selector<PlaceObj, ?>> queryObservable = placeObjs.createQueryObservable();
                queryObservable.subscribe(places -> {
                    emmiter.onNext(places.toList());
                });
                emmiter.onNext(placeObjs.selector().toList());
            }
        });
    }
}
