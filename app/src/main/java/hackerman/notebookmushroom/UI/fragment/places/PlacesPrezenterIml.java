package hackerman.notebookmushroom.UI.fragment.places;

import android.os.Handler;
import android.os.Looper;

import java.util.List;

import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;


public class PlacesPrezenterIml extends PlacesPrezenter {
    @Override
    void addNewTodoObjToList(TodoObj todoObj, CharSequence s) {
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                todoObj.name=s.toString();
                todoObj.date= System.currentTimeMillis();
                TodoObjRepository.updateTodoObj(todoObj);
               final List<TodoObj>list=TodoObjRepository.getTodoObjList();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getView().loadTodoOblList(list);
                    }
                });


            }
        }).start();
    }

    @Override
    void renameTodoObj(TodoObj todoObj, CharSequence input) {
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!todoObj.name.equals(input)){
                    todoObj.name=input.toString();
                    TodoObjRepository.updateTodoObj(todoObj);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            List<TodoObj>list=TodoObjRepository.getTodoObjList();
                            getView().loadRenameTodoObjList(list);
                        }
                    });
                }


            }
        }).start();

    }

    @Override
    void deleteTodoObj(TodoObj todoObj) {
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                TodoObjRepository.deleteTodoObj(todoObj);
               final List<TodoObj>list=TodoObjRepository.getTodoObjList();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                   getView().loadListDeleteTodoObj(list);
                    }
                });
            }
        }).start();
    }

    @Override
    void deleteNotesIsTodoObj(TodoObj todoObj) {

    }
}
