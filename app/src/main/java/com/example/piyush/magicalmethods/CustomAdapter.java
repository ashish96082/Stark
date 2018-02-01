package com.example.piyush.magicalmethods;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by USER
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private List<MyData> my_data;

    public CustomAdapter(Context context, List<MyData> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    public void addData(List<MyData> myData) {
        this.my_data = myData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println(my_data.get(position));
        holder.description.setText(my_data.get(position).getDescription());
        Glide
                .with(context)
                .load(my_data.get(position).getImage_link())
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView description;
        public ImageView imageView;
        public TextView viewCourse;

        public ViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            viewCourse = itemView.findViewById(R.id.viewCourse);
            viewCourse.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            MyData selectedCard = my_data.get(position);
            Intent intent = new Intent(view.getContext(), CourseDetail.class);
            intent.putExtra("Card", selectedCard);
            view.getContext().startActivity(intent);

        }
    }

    public void setFilter(ArrayList<MyData> newList) {
        my_data = new ArrayList<>();
        my_data.addAll(newList);
        notifyDataSetChanged();
    }
}
