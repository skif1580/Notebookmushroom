package hackerman.notebookmushroom.adapter;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hackerman.notebookmushroom.R;
import hackerman.notebookmushroom.app.App;
import hackerman.notebookmushroom.db.TodoObj;

public class TodoPlacesAdapter extends RecyclerView.Adapter<TodoPlacesAdapter.TodosViewHolder> {
    private List<TodoObj> list;
    private SimpleDateFormat dateFormat;
    private OnItemClickListner clickListener;

    public TodoPlacesAdapter(List<TodoObj> list) {
        this.list = list;
        this.dateFormat = new SimpleDateFormat(" dd.MM.yyyy");
    }

    public void setList(List<TodoObj> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public TodoPlacesAdapter.TodosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TodosViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TodoPlacesAdapter.TodosViewHolder holder, int position) {
        final TodoObj todoObj = list.get(position);
        holder.textName.setText(todoObj.name);
        holder.textDate.setText(dateFormat.format(new Date(todoObj.date)));
        if (todoObj.notes !=null) {
        holder.llAddNonebook.setVisibility(View.VISIBLE);
        }else {
            holder.llAddNonebook.setVisibility(View.GONE);
        }
        if (todoObj.photo != null) {
            holder.llAddPhoto.setVisibility(View.VISIBLE);
            Glide.with(App.getAppContext())
                    .load("file://" + todoObj.photo)
                    .centerCrop()
                    .placeholder(R.drawable.md_item_selected)
                    .crossFade()
                    .into(holder.ivPhoto);
        } else {
            holder.llAddPhoto.setVisibility(View.GONE);
        }
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (clickListener != null) {
                    clickListener.onLongTodoClick(todoObj);
                }
                return true;
            }
        });
        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onPhotoClick(todoObj);
                }
            }

        });
        holder.ivNotebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener!=null){
                    clickListener.onNotebookClick(todoObj);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TodosViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDate;
        ImageView ivPhoto;
        LinearLayout llAddPhoto;
        CardView cardView;
        ImageView ivNotebook;
        LinearLayout llAddNonebook;

        public TodosViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.tv_name);
            textDate = (TextView) itemView.findViewById(R.id.tv_date);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            llAddPhoto = (LinearLayout) itemView.findViewById(R.id.ll_container_photo);
            cardView = (CardView) itemView.findViewById(R.id.cv_container_list);
            llAddNonebook=(LinearLayout)itemView.findViewById(R.id.ll_container_notebook);
            ivNotebook=(ImageView)itemView.findViewById(R.id.iv_notebook);

        }
    }

    public void setClickListener(OnItemClickListner clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListner {
        void onLongTodoClick(TodoObj todoObj);

        void onPhotoClick(TodoObj todoObj);
        void onNotebookClick(TodoObj todoObj);
    }
}


