package hackerman.notebookmushroom.UI;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import android.widget.LinearLayout.LayoutParams;

import java.io.IOException;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.db.MushroomObj;


public class MushroomPhoto extends Fragment {
    private Toolbar toolbar;
    private String mushroomPhoto;
    private String photo[];
    private String nameMushroom;
    private String name;
    private ImageView imageView;
    private ImageView imageViewbyCode;
    private LinearLayout myLayout;
    private AssetManager assetManager;

    public MushroomPhoto() {
    }

    public static MushroomPhoto newInstance(MushroomObj mushroomObj) {
        MushroomPhoto fragment = new MushroomPhoto();
        Bundle bundle = new Bundle();
        bundle.putString("photoMushroom", mushroomObj.photo);
        bundle.putString("wayPhoto", mushroomObj.photomushroom);
        bundle.putStringArray("photo", mushroomObj.file);
        bundle.putString("name", mushroomObj.title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mushroomPhoto = getArguments().getString("photoMushroom");
        photo = getArguments().getStringArray("photo");
        name = getArguments().getString("name");
        nameMushroom = getArguments().getString("wayPhoto");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_mushroom_photo, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_mushroom_photo);
        imageView = (ImageView) view.findViewById(R.id.iv_mushroom_photo);
        myLayout = (LinearLayout) view.findViewById(R.id.ll_container);
        assetManager = getContext().getAssets();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle("Photo Mushroom");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_texttoolbar));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view1 -> getActivity().onBackPressed());
        if (nameMushroom != null) {
            getImageView();

        } else {
            Glide.with(getContext())
                    .load(Uri.parse("file:///android_asset/images/" + mushroomPhoto))
                    .into(imageView);
        }
        toolbar.inflateMenu(R.menu.menu_toolbar_mushroom_photo);
        clickMenu();
    }

    private void clickMenu() {
        toolbar.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.ua/search?q=" + name)));
            return true;
        });
    }

    public void getImageView() {
        try {
            String[] imgPath = assetManager.list(nameMushroom);
            for (int i = 0; i < imgPath.length; i++) {
                Log.d("stream::", "photo " + imgPath[i]);
                imageViewbyCode = new ImageView(getContext());
                final LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                myLayout.addView(imageViewbyCode, layoutParams);

                Glide.with(getContext())
                        .load(Uri.parse("file:///android_asset/" + nameMushroom + "/" + imgPath[i]))
                        .into(imageViewbyCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


