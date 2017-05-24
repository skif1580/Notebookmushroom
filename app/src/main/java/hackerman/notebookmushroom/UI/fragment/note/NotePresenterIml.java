package hackerman.notebookmushroom.UI.fragment.note;

import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;

/**
 * Created by hackerman on 12.03.17.
 */

public class NotePresenterIml  extends NotePresenter{
    @Override
    public void addText(TodoObj todoObj,String text) {
        todoObj.notes=text;
        TodoObjRepository.updateTodoObj(todoObj);
        if (isViewAttached()){
            getView().addNote();
        }
    }
}
