package hackerman.notebookmushroom.UI.fragment.places;

import java.util.List;

import hackerman.notebookmushroom.common.BaseView;
import hackerman.notebookmushroom.db.TodoObj;

public interface PlacesView extends BaseView {
    void loadTodoOblList(List<TodoObj> list);

    void loadRenameTodoObjList(List<TodoObj> list);

    void loadListDeleteTodoObj(List<TodoObj> list);

    void loadListDeleteNotesIsTodoObj(List<TodoObj> list);

}
