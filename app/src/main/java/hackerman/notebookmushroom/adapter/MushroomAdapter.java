package hackerman.notebookmushroom.adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.db.MushroomObj;


public class MushroomAdapter extends RecyclerView.Adapter<MushroomAdapter.MushroomViewHolder> {
    List<MushroomObj> list;
    OnClickItemPhotoMyshroom onClickItemPhotoMyshroom;

    public void setOnClickItemPhotoMyshroom(OnClickItemPhotoMyshroom onClickItemPhotoMyshroom) {
        this.onClickItemPhotoMyshroom = onClickItemPhotoMyshroom;
    }

    public MushroomAdapter() {
        this.list = new ArrayList<>();
    }


    public void setItems(List<MushroomObj> items) {
   this.list = items;
    notifyDataSetChanged();}

    @Override
    public MushroomAdapter.MushroomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assets, parent, false);
        return new MushroomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MushroomViewHolder holder, int position) {
        final MushroomObj mushroomObj = list.get(position);
        holder.tvNameMushroom.setText(mushroomObj.title);
        if (mushroomObj.photo != null) {
            Glide.with(holder.ivPhotoMushroom.getContext())
                    .load(Uri.parse("file:///android_asset/images/" + mushroomObj.photo))
                    .into(holder.ivPhotoMushroom);
        }
         holder.ivPhotoMushroom.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (onClickItemPhotoMyshroom !=null) {
                     onClickItemPhotoMyshroom.onClickPhoto(mushroomObj);
                 }

             }
         });
        holder.tvNameMushroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickItemPhotoMyshroom !=null) {
                    onClickItemPhotoMyshroom.onClickNameMushroom(mushroomObj);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MushroomViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameMushroom;
        private ImageView ivPhotoMushroom;
        private CardView cardView;

        public MushroomViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_assets_container);
            tvNameMushroom = (TextView) itemView.findViewById(R.id.tv_name_mushroom);
            ivPhotoMushroom = (ImageView) itemView.findViewById(R.id.iv_photo_mushroom);

        }
    }
    public interface OnClickItemPhotoMyshroom{
         void onClickPhoto(MushroomObj mushroomObj);
        void onClickNameMushroom(MushroomObj mushroomObj);
    }
}
