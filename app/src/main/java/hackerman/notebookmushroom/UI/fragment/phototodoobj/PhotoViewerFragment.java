package hackerman.notebookmushroom.UI.fragment.phototodoobj;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.app.App;
import hackerman.notebookmushroom.common.BaseFragment;
import hackerman.notebookmushroom.db.TodoObj;
import hackerman.notebookmushroom.db.repositories.TodoObjRepository;

public class PhotoViewerFragment extends BaseFragment<PhotoView,PhotoPresenter>
implements PhotoView{
    private Toolbar toolbar;
    private ImageView imageView;
    private String todoKey;
    private TodoObj todoObj;


    public static PhotoViewerFragment newInstance(TodoObj todoObj) {
        PhotoViewerFragment fragment = new PhotoViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("todo_key_arg", todoObj.key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoKey = getArguments().getString("todo_key_arg");
        todoObj = TodoObjRepository.getTodoObj(todoKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        imageView = (ImageView) view.findViewById(R.id.iv_fragment);
    }

    @Override
    public PhotoPresenter createPresenter() {
        return new PhotoPresenterIml();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(App.getAppContext())
                .load("file://" + todoObj.photo)
                .centerCrop()
                .crossFade()
                .into(imageView);
        toolbar.setTitle("Photo");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_texttoolbar));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view1 -> getActivity().onBackPressed());
        toolbar.inflateMenu(R.menu.menu_toolbar_image);
        toolbar.setOnMenuItemClickListener(item -> {
            deletePhotoIsTodoObj();
            return true;
        });
    }

    private void deletePhotoIsTodoObj() {
        new MaterialDialog.Builder(getContext())
                .title("Delete Photo ?")
                .positiveText("Ok")
                .negativeText("Cancel")
                .onPositive((dialog, which) -> {
                    getPresenter().deletePhoto(todoObj);
                    getActivity().onBackPressed();
                }).show();
    }

}
