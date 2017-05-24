package hackerman.notebookmushroom.UI.fragment.mushroom;

import android.os.Handler;
import android.os.Looper;

import java.util.List;

import hackerman.notebookmushroom.db.MushroomObj;
import hackerman.notebookmushroom.db.repositories.MushroomObjRepository;

/**
 * Created by hackerman on 12.03.17.
 */

public class MusroomPresenterIml extends MusroomPresenter {
    @Override
    public void loadList() {
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            final List<MushroomObj> list = MushroomObjRepository.getMushroomObjList();
            handler.post(() -> {
                if (isViewAttached()){
                    getView().onListDataLoaded(list);
                }
            });
        }).start();
    }
}
