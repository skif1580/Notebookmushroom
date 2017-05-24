package hackerman.notebookmushroom.UI.fragment.noteviewer;

import hackerman.notebookmushroom.common.BasePresenter;
import hackerman.notebookmushroom.db.TodoObj;

/**
 * Created by hackerman on 12.03.17.
 */

public abstract class NoteViewerPresenter extends BasePresenter<NoteViewerView> {
    public  abstract void editTextNote(TodoObj todoObj, String text) ;
    public abstract void deleteText(TodoObj todoObj);


}
