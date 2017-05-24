package hackerman.notebookmushroom.UI.fragment.mushroom;

import java.util.List;

import hackerman.notebookmushroom.common.BaseView;
import hackerman.notebookmushroom.db.MushroomObj;

/**
 * Created by hackerman on 12.03.17.
 */

public interface MusroomView extends BaseView {
    void onListDataLoaded(List<MushroomObj> items);
}
