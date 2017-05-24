package hackerman.notebookmushroom.UI.fragment.note;

import hackerman.notebookmushroom.common.BasePresenter;
import hackerman.notebookmushroom.db.TodoObj;

/**
 * Created by hackerman on 12.03.17.
 */

public abstract class NotePresenter extends BasePresenter<NoteView> {
    public abstract void addText(TodoObj todoObj,String text);
}
