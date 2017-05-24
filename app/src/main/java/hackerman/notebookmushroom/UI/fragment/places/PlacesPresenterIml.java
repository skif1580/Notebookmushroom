package hackerman.notebookmushroom.UI.fragment.places;

import android.os.Looper;

import java.util.List;

import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;

/**
 * Created by hackerman on 17.03.17.
 */

public class PlacesPresenterIml extends PlacesPrezenter {
    @Override
    void addNewTodoObjToList(TodoObj todoObj, CharSequence s) {
        final android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        new Thread(() -> {
            todoObj.name = s.toString();
            todoObj.date = System.currentTimeMillis();
            TodoObjRepository.updateTodoObj(todoObj);
            List<TodoObj> list = TodoObjRepository.getTodoObjList();
            handler.post(() -> getView().loadTodoOblList(list));
        }).start();
    }

    @Override
    void renameTodoObj(TodoObj todoObj, CharSequence input) {
        final android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        new Thread(() -> {
            todoObj.name = input.toString();
            TodoObjRepository.updateTodoObj(todoObj);
            List<TodoObj> list = TodoObjRepository.getTodoObjList();
            handler.post(() -> getView().loadRenameTodoObjList(list));
        }).start();
    }

   

    @Override
    void deleteTodoObj(TodoObj todoObj) {
        final android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
        new Thread(() -> {
            TodoObjRepository.deleteTodoObj(todoObj);
            List<TodoObj> list = TodoObjRepository.getTodoObjList();
            handler.post(() -> getView().loadListDeleteTodoObj(list));
        }).start();
    }

    @Override
    void deleteNotesIsTodoObj(TodoObj todoObj) {
        todoObj.notes = null;
        TodoObjRepository.updateTodoObj(todoObj);
        List<TodoObj> list = TodoObjRepository.getTodoObjList();
        getView().loadListDeleteNotesIsTodoObj(list);
    }



}
