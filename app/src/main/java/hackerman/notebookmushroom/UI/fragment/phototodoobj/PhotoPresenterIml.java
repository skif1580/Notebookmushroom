package hackerman.notebookmushroom.UI.fragment.phototodoobj;

import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;



public class PhotoPresenterIml extends PhotoPresenter {
    @Override
    public void deletePhoto(TodoObj todoObj) {
        todoObj.photo = null;
        TodoObjRepository.updateTodoObj(todoObj);
    }
}
