package hackerman.notebookmushroom.UI.fragment.descriptionmushroom;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.common.BaseFragment;
import hackerman.notebookmushroom.db.MushroomObj;

public class DescriptionMushroomFragment extends BaseFragment<DescriptionView, DescriptionPresenter>
        implements DescriptionView {
    private Toolbar toolbar;
    private TextView tvDescriptionMushroom;
    private TextView tvNameMushroom;
    private String mushroomKey;
    private String mushroomDescriptoin;
    private String photo;
    private String name;
    private ImageView ivPhoto;

    public static DescriptionMushroomFragment newInstance(MushroomObj mushroomObj) {
        DescriptionMushroomFragment fragment = new DescriptionMushroomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", mushroomObj.title);
        bundle.putLong("key", mushroomObj.key);
        bundle.putString("description", mushroomObj.description);
        bundle.putString("photo", mushroomObj.photo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mushroomKey = getArguments().getString("key");
        mushroomDescriptoin = getArguments().getString("description");
        photo = getArguments().getString("photo");
        name = getArguments().getString("name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mushroom_description, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_description_mushroom);
        tvDescriptionMushroom = (TextView) view.findViewById(R.id.tv_description_mushroom);
        ivPhoto = (ImageView) view.findViewById(R.id.iv_photo_mushroom2);
        tvNameMushroom=(TextView) view.findViewById(R.id.tv_mushroom_name);
    }

    @Override
    public DescriptionPresenter createPresenter() {
        return new DescriptionPresenterIml();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Description");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_texttoolbar));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view1 -> getActivity().onBackPressed());

        getPresenter().descriptionText(mushroomDescriptoin);

        tvNameMushroom.setText(name);

        Glide.with(getContext())
                .load(Uri.parse("file:///android_asset/images/" + photo))
                .into(ivPhoto);
    }

    @Override
    public void readText(String text) {
        tvDescriptionMushroom.setText(text);
    }
}
