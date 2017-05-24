package hackerman.notebookmushroom.UI.fragment.note;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class AddNoteFragment extends BaseFragment<NoteView,NotePresenter> implements NoteView{
    private Toolbar toolbar;
    private TodoObj todoObj;
    private String todoKey;
    private TextView noteTitle;
    private EditText edNote;
    private String textNote;

    public static AddNoteFragment newInstance(TodoObj todoObj) {
        AddNoteFragment fragment = new AddNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("add note", todoObj.key);
        bundle.putString("add date", todoObj.name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoKey = getArguments().getString("add note");
        todoObj = TodoObjRepository.getTodoObj(todoKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);
        initUI(view);
        return view;

    }

    private void initUI(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_note);
        noteTitle = (TextView) view.findViewById(R.id.tv_note_title);
        edNote = (EditText) view.findViewById(R.id.et_note);
    }


    @Override
    public NotePresenter createPresenter() {
        return new NotePresenterIml();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.inflateMenu(R.menu.menu_toolber_add_note);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (edNote.length() > 0 && edNote != null) {
                    textNote = edNote.getText().toString();
                    getPresenter().addText(todoObj,textNote);
                    getActivity().onBackPressed();
                }
                return true;
            }
        });

        String name = getArguments().getString("add date");
        noteTitle.setText("" + name);
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle("Note");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_texttoolbar));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void addNote() {

    }
}
