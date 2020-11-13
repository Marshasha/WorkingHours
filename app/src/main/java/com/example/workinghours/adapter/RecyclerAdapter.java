package com.example.workinghours.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workinghours.R;
import com.example.workinghours.database.entity.ActivityEntity;
import com.example.workinghours.database.entity.ProjectEntity;
import com.example.workinghours.util.RecyclerViewItemClickListener;

import java.util.List;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {

    private List<T> mData;
    private RecyclerViewItemClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textView;
        ViewHolder(@NonNull View itemView, RecyclerViewItemClickListener listener) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.ItemName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick((String) textView.getText());
                }
            });
        }
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = mData.get(position);
        if (item.getClass().equals(ProjectEntity.class))
            holder.textView.setText(((ProjectEntity) item).getProjectName());
        if (item.getClass().equals(ActivityEntity.class))
            holder.textView.setText(new StringBuilder().append(((ActivityEntity) item).getDateStart()).append(" - ")
                    .append(((ActivityEntity) item).getDateFinish()).append(" ").append(((ActivityEntity) item).getActivityName())
                    .append(" ").append(((ActivityEntity) item).getDuration()).toString());
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> data) {
        mData = data;
        notifyDataSetChanged();
        //notifyItemRangeInserted(0, data.size());
    }
}
