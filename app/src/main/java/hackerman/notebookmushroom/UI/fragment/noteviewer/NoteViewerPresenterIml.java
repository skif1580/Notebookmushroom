package hackerman.notebookmushroom.UI.fragment.noteviewer;


import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;

public class NoteViewerPresenterIml extends NoteViewerPresenter {
    @Override
    public void editTextNote(TodoObj todoObj, String text) {
        new Thread(() -> {
            todoObj.notes = text;
            TodoObjRepository.updateTodoObj(todoObj);
            if (isViewAttached()) {
                getView().showText();
            }
        }).start();
    }

    @Override
    public void deleteText(TodoObj todoObj) {
        new Thread(() -> {
            todoObj.notes = null;
            TodoObjRepository.updateTodoObj(todoObj);
        }).start();
    }
}
