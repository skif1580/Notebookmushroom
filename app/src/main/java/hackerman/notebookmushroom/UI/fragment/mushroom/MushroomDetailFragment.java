package hackerman.notebookmushroom.UI.fragment.mushroom;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twotoasters.jazzylistview.effects.FlyEffect;
import com.twotoasters.jazzylistview.effects.SlideInEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.List;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.adapter.MushroomAdapter;
import hackerman.notebookmushroom.common.BaseFragment;
import hackerman.notebookmushroom.db.MushroomObj;
import hackerman.notebookmushroom.interactions.AddDescriptionMushroom;
import hackerman.notebookmushroom.interactions.MushroomPhotoViwerIteraction;

public class MushroomDetailFragment extends BaseFragment<MusroomView, MusroomPresenter>
        implements MusroomView {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MushroomAdapter mushroomAdapter;

    public static MushroomDetailFragment newInstance() {
        MushroomDetailFragment fragment = new MushroomDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mushroom_detail, container, false);
        initToolbar(view);
        mushroomAdapter = new MushroomAdapter();
        final MushroomAdapter.OnClickItemPhotoMyshroom clickItemPhotoMyshroom = new MushroomAdapter.OnClickItemPhotoMyshroom() {
            @Override
            public void onClickPhoto(MushroomObj mushroomObj) {
                ((MushroomPhotoViwerIteraction) getContext()).mushroomViwerPhoto(mushroomObj);
            }

            @Override
            public void onClickNameMushroom(MushroomObj mushroomObj) {
                ((AddDescriptionMushroom) getContext()).openDescriptionMushroom(mushroomObj);
            }
        };
        mushroomAdapter.setOnClickItemPhotoMyshroom(clickItemPhotoMyshroom);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rl_container_cardView);
        JazzyRecyclerViewScrollListener listener=new JazzyRecyclerViewScrollListener();
        listener.setTransitionEffect(new FlyEffect());
        recyclerView.setOnScrollListener(listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mushroomAdapter);
    }

    private void initToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_assets);
        toolbar.setTitle("Description Mushroom");
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_texttoolbar));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view1 -> getActivity().onBackPressed());
        toolbar.setOnMenuItemClickListener(item -> false);
    }

    @Override
    public MusroomPresenter createPresenter() {
        return new MusroomPresenterIml();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().loadList();
    }

    @Override
    public void onListDataLoaded(List<MushroomObj> items) {
        mushroomAdapter.setItems(items);
       // mushroomAdapter.notifyDataSetChanged();
    }
}
