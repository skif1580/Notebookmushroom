package hackerman.notebookmushroom.events;

import hackerman.notebookmushroom.db.TodoObj;



public class OpenPhotoEvent {
    private TodoObj todoObj;

    public OpenPhotoEvent(TodoObj todoObj) {
        this.todoObj = todoObj;
    }

    public TodoObj getTodoObj() {
        return todoObj;
    }
}
