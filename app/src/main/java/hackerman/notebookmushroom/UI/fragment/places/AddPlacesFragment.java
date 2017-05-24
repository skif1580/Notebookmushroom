package hackerman.notebookmushroom.UI.fragment.places;


import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.effects.FlyEffect;
import com.twotoasters.jazzylistview.effects.SlideInEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.UI.fragment.maps.MapsActivity;
import hackerman.notebookmushroom.adapter.TodoPlacesAdapter;
import hackerman.notebookmushroom.common.BaseFragment;
import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;
import hackerman.notebookmushroom.interactions.AddNoteIteraction;
import hackerman.notebookmushroom.interactions.AddPlaceInteraction;
import hackerman.notebookmushroom.interactions.DrawerChangeStateInteraction;
import hackerman.notebookmushroom.interactions.NoteAddTodoObj;

import static android.app.Activity.RESULT_OK;

public class AddPlacesFragment extends BaseFragment<PlacesView, PlacesPrezenter>
        implements PlacesView {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TodoPlacesAdapter adapter;
    private static final int CAMERA_REQUEST = 1000;
    private static final int GALLERY_REQUEST = 2000;
    private static final int REQEST_MAPS_ACTIVITY = 3000;
    private String photoPath;
    private String keyTodoObj;

    public static AddPlacesFragment newInstance() {
        AddPlacesFragment fragment = new AddPlacesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        initToolbar(view);
        adapter = new TodoPlacesAdapter(TodoObjRepository.getTodoObjList());
        final TodoPlacesAdapter.OnItemClickListner setOnItemClickListner = new TodoPlacesAdapter.OnItemClickListner() {

            @Override
            public void onLongTodoClick(TodoObj todoObj) {
                showContextMenu(todoObj);
            }

            @Override
            public void onPhotoClick(TodoObj todoObj) {
                ((AddPlaceInteraction) getContext()).openTodoPhotoInViewer(todoObj);
                // EventBus.getDefault().post(new OpenPhotoEvent(todoObj));
            }

            @Override
            public void onNotebookClick(TodoObj todoObj) {
                ((AddNoteIteraction) getContext()).openNoteTodoObj(todoObj);


            }
        };

        adapter.setClickListener(setOnItemClickListner);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rl_container_add);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final JazzyRecyclerViewScrollListener listener = new JazzyRecyclerViewScrollListener();
        listener.setTransitionEffect(new FlyEffect());
        recyclerView.setOnScrollListener(listener);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Замітки грибника");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_texttoolbar));
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu: {
                        addNewTodoObjToList();
                        break;

                    }
                }
                return true;
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_dehaze_black_24dp);
        toolbar.setNavigationOnClickListener(view1 -> ((DrawerChangeStateInteraction) getContext()).invertDrawerState());
    }

    private void addNewTodoObjToList() {
        new MaterialDialog.Builder(getContext())
                .title("Add todoObj")
                .input("Add", null, (dialog, input) -> {
                    if (input.length() > 0) {
                        TodoObj todoObj = new TodoObj();
                        getPresenter().addNewTodoObjToList(todoObj, input);
                    }
                }).show();
    }

    @Override
    public void loadTodoOblList(List<TodoObj> list) {
        adapter.setList(list);
    }


    private void showContextMenu(final TodoObj todoObj) {
        Map<String, Integer> integerMap = new LinkedHashMap<>();
        integerMap.put("Rename", 0);
        integerMap.put("Delete", 1);
        integerMap.put("Add places", 2);
        if (todoObj.photo == null) {
            integerMap.put("Add photo", 3);
        } else {
            integerMap.put("Replace photo", 4);
            integerMap.put("Delete photo", 5);
        }
        if (todoObj.notes == null) {
            integerMap.put("Add note", 6);
        } else {
            integerMap.put("Delete note", 7);
        }
        Object object[] = integerMap.keySet().toArray();
        String items[] = new String[object.length];
        for (int i = 0; i < object.length; i++) {
            items[i] = (String) object[i];
        }
        new MaterialDialog.Builder(getContext())
                .items(items)
                .itemsColorRes(R.color.cast_intro_overlay_background_color)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            case 0: {
                                renameTodoObj(todoObj);
                                break;
                            }
                            case 1: {
                                delteTodoObj(todoObj);
                                break;
                            }
                            case 2: {
                                addPleseMap(todoObj);
                                break;
                            }
                            case 3: {
                                addMenuPhotoToTodoObj(todoObj);
                                break;
                            }
                            case 4: {
                                if (todoObj.photo == null) {
                                    addMenuNoteToTodoObj(todoObj);
                                } else {
                                    contextMenuDeletePhoto(todoObj);
                                }
                                break;
                            }
                            case 5: {
                                if (todoObj.notes != null && todoObj.notes.length() > 0) {
                                    deleteNoteIsTodoObj(todoObj);
                                } else {
                                    addMenuNoteToTodoObj(todoObj);
                                }
                                break;
                            }
                            case 6: {

                                break;
                            }
                            case 7: {

                                break;
                            }
                        }
                    }
                }).show();
    }

    private void deleteNoteIsTodoObj(final TodoObj todoObj) {
        new MaterialDialog.Builder(getContext())
                .title("You delete notes")
                .positiveText("Ok")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {
                    getPresenter().deleteNotesIsTodoObj(todoObj);
                }).show();
    }

    private void addMenuNoteToTodoObj(TodoObj todoObj) {
        ((NoteAddTodoObj) getContext()).newNoteTodoObj(todoObj);
    }

    private void addPleseMap(TodoObj todoObj) {
        String key = todoObj.key;
        Intent intent = new Intent(getContext(), MapsActivity.class);
        intent.putExtra("add plase", key);
        startActivityForResult(intent, REQEST_MAPS_ACTIVITY);
    }

    private void contextMenuDeletePhoto(final TodoObj todoObj) {
        new MaterialDialog.Builder(getContext())
                .title("Delete photo")
                .positiveText("Ok")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {
                    todoObj.photo = null;
                    TodoObjRepository.updateTodoObj(todoObj);
                    adapter.setList(TodoObjRepository.getTodoObjList());
                }).show();
    }

    private void addMenuPhotoToTodoObj(final TodoObj todoObj) {
        new MaterialDialog.Builder(getContext())
                .title("Add Photo")
                .items("From camera", "Gallery")
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0: {
                            addPhotoFromCamera(todoObj);
                            break;
                        }
                        case 1: {
                            addPhotoFromGallery(todoObj);
                            break;
                        }
                    }
                }).show();
    }

    private void addPhotoFromGallery(TodoObj todoObj) {
        keyTodoObj = todoObj.key;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    private void addPhotoFromCamera(TodoObj todoObj) {
        keyTodoObj = todoObj.key;
        Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intentPhoto.resolveActivity(getActivity().getPackageManager()) != null) {
            final String folderPath = Environment.getExternalStorageDirectory() + "/TodoPhotos";
            File photosFolder = new File(folderPath);
            final boolean isCreatedFolder = photosFolder.mkdirs();
            Log.d("takePhotoFromCamera()", "isCreateFolder::" + isCreatedFolder);
            photoPath = folderPath + "/Todo-photo-" + System.currentTimeMillis() + ".jpg";
            File photoFile = new File(photoPath);
            try {
                photoFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri outPutFileUri = Uri.fromFile(photoFile);
            intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, outPutFileUri);
            startActivityForResult(intentPhoto, CAMERA_REQUEST);
        }
    }

    private void delteTodoObj(final TodoObj todoObj) {
        new MaterialDialog.Builder(getContext())
                .title("Delete TodoObj" + todoObj.name)
                .positiveText("Ok")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {
                    getPresenter().deleteTodoObj(todoObj);
                }).show();
    }

    private void renameTodoObj(final TodoObj todoObj) {
        new MaterialDialog.Builder(getContext())
                .title("Rename TodoObj")
                .input("Enter Name", null, (dialog, input) -> getPresenter().renameTodoObj(todoObj, input)).show();
    }

    @Override
    public void loadRenameTodoObjList(List<TodoObj> list) {
        adapter.setList(list);
    }

    @Override
    public void loadListDeleteTodoObj(List<TodoObj> list) {
        adapter.setList(list);
    }

    @Override
    public void loadListDeleteNotesIsTodoObj(List<TodoObj> list) {
        adapter.setList(list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST: {
                    TodoObj todoObj = TodoObjRepository.getTodoObj(keyTodoObj);
                    todoObj.photo = photoPath;
                    TodoObjRepository.updateTodoObj(todoObj);
                    adapter.setList(TodoObjRepository.getTodoObjList());
                    break;
                }
                case GALLERY_REQUEST: {
                    if (data != null) {
                        final Uri uri = data.getData();
                        final String pathFromUri = getPathFromUri(uri);
                        TodoObj todoObj = TodoObjRepository.getTodoObj(keyTodoObj);
                        todoObj.photo = pathFromUri;
                        TodoObjRepository.updateTodoObj(todoObj);
                        adapter.setList(TodoObjRepository.getTodoObjList());
                    }
                    break;
                }
                case REQEST_MAPS_ACTIVITY: {
                    adapter.setList(TodoObjRepository.getTodoObjList());
                    break;
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPathFromUri(Uri uri) {
        String filePath = null;
        try {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split("=")[1];

            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = getContext().getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            filePath = getPathFromGalleryV14(uri);
        }
        return filePath;
    }

    private String getPathFromGalleryV14(Uri uri) {
        if (uri == null) {
            return null;
        }
        String filePath = "";
        Cursor cursor = null;
        java.io.File file = null;

        switch (0) {
            case 0: {
                cursor = getContext().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            case 1: {
                cursor = getContext().getContentResolver().query(uri, new String[]{MediaStore.Video.VideoColumns.DATA}, null, null, null);
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            case 2: {
                filePath = uri.getPath();
                if (filePath != null && !TextUtils.isEmpty(filePath)) {
                    file = new java.io.File(filePath);
                    if (file.exists()) {
                        break;
                    }
                }
            }
            default: {
                return null;
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (filePath != null && !TextUtils.isEmpty(filePath)) {
            filePath = filePath.trim();
        }
        return filePath;
    }

    @Override
    public PlacesPrezenter createPresenter() {
        return new PlacesPresenterIml();
    }
}
