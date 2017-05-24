package hackerman.notebookmushroom.UI.fragment.noteviewer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.common.BaseFragment;
import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;

import static hackerman.notebookmushroom.R.id.menu_delete;
import static hackerman.notebookmushroom.R.id.menu_save;

public class NoteViewerFragment extends BaseFragment<NoteViewerView,NoteViewerPresenter>
        implements NoteViewerView{
    private TodoObj todoObj;
    private Toolbar toolbar;
    private String todoObjKey;
    private EditText editText;
    private TextView textView;

    public static NoteViewerFragment newInstance(TodoObj todoObj) {
        NoteViewerFragment fragment = new NoteViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("note", todoObj.key);
        bundle.putString("date", todoObj.notes);
        bundle.putString("title", todoObj.name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoObjKey = getArguments().getString("note");
        todoObj = TodoObjRepository.getTodoObj(todoObjKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_viewer, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_note_viewer);
        editText = (EditText) view.findViewById(R.id.tv_viewer_note);
        textView = (TextView) view.findViewById(R.id.tv_note_viewer);
    }


    @Override
    public NoteViewerPresenter createPresenter() {
        return new NoteViewerPresenterIml();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Review notes");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_texttoolbar));
        toolbar.inflateMenu(R.menu.menu_toolbar_viewer);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id. menu_save: {
                        if (editText.length() > 0 && editText != null) {
                            final String textNote = editText.getText().toString();
                            getPresenter().editTextNote(todoObj,textNote);
                            getActivity().onBackPressed();
                        }
                        break;
                    }
                    case R.id.menu_delete: {
                        getPresenter().deleteText(todoObj);
                        getActivity().onBackPressed();
                        break;
                    }

                }
                return true;
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view1 -> getActivity().onBackPressed());
        showTextNotes();

    }

    private void showTextNotes() {
        textView.setText( todoObj.name);
        editText.setText(todoObj.notes);
    }

    @Override
    public void showText() {

    }
}
