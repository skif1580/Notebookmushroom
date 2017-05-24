package hackerman.notebookmushroom.db.repositories;


import java.util.List;

import hackerman.notebookmushroom.app.App;
import hackerman.notebookmushroom.db.TodoObj;

import static hackerman.notebookmushroom.app.App.orma;

public class TodoObjRepository {
    public static List<TodoObj> getTodoObjList() {
        return orma()
                .relationOfTodoObj()
                .orderByKeyAsc()
                .selector()
                .toList();
    }

    public static void addTodoObj(TodoObj todoObj) {
        App.orma().relationOfTodoObj()
                .upserter()
                .execute(todoObj);
    }

    public static void deleteTodoObj(TodoObj todoObj) {
        App.orma().deleteFromTodoObj()
                .keyEq(todoObj.key)
                .execute();

    }

    public static void updateTodoObj(TodoObj todoObj) {
        App.orma().relationOfTodoObj()
                .upserter()
                .execute(todoObj);
    }

    public static TodoObj getTodoObj(String key) {
        return App.orma()
                .relationOfTodoObj()
                .keyEq(key)
                .selector()
                .getOrNull(0);
    }

}
