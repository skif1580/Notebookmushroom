package hackerman.notebookmushroom.UI.fragment.places;

import hackerman.notebookmushroom.common.BasePresenter;
import hackerman.notebookmushroom.db.TodoObj;

/**
 * Created by hackerman on 15.03.17.
 */

public abstract class PlacesPrezenter extends BasePresenter<PlacesView> {
    abstract void addNewTodoObjToList(TodoObj todoObj, CharSequence s);

    abstract void renameTodoObj(TodoObj todoObj, CharSequence input);

    abstract void deleteTodoObj(TodoObj todoObj);

    abstract void deleteNotesIsTodoObj(TodoObj todoObj);


}
