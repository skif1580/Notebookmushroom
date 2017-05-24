package hackerman.notebookmushroom.UI.fragment.phototodoobj;

import hackerman.notebookmushroom.common.BasePresenter;
import hackerman.notebookmushroom.db.TodoObj;

/**
 * Created by hackerman on 13.03.17.
 */

public  abstract class PhotoPresenter extends BasePresenter<PhotoView>{
     public abstract void deletePhoto(TodoObj todoObj);
}
