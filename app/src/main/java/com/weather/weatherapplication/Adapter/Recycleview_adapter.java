package com.weather.weatherapplication.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weather.weatherapplication.MainActivity;
import com.weather.weatherapplication.R;
import com.weather.weatherapplication.recycleviewlist.recyclelist;

import java.util.List;

public class Recycleview_adapter extends RecyclerView.Adapter<Recycleview_adapter.Viewholder> {
   List<recyclelist> list;
   Context context;

    public Recycleview_adapter(Context context) {
        this.context = context;
    }

    public Recycleview_adapter(List<recyclelist> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Recycleview_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_bottomsheet, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycleview_adapter.Viewholder holder, int position) {
        recyclelist list2=  list.get(position);
        holder.recycle_date_day.setText(list2.getDay());
        holder.recycle_high_temp.setText(list2.getHigh());
        holder.recycle_low_temp.setText(list2.getLow());
        Glide.with(holder.itemView.getContext()).load(list2.getImageurl()).into(holder.recycleview_image);

        setAnimation(holder.itemView.getContext(),holder.itemView, position);

    }
    private int lastPosition = 3;

    private void setAnimation(Context context1,View viewToAnimate, int position) {

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context1,R.anim.item_animation_fall_down);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else if ( position < lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context1,R.anim.item_animation_fall_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView recycleview_image;
        TextView recycle_date_day,recycle_high_temp,recycle_low_temp;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            recycleview_image=itemView.findViewById(R.id.recycleview_image);
            recycle_high_temp=itemView.findViewById(R.id.recycle_high_temp);
            recycle_date_day=itemView.findViewById(R.id.recycle_date_day);
            recycle_low_temp=itemView.findViewById(R.id.recycle_low_temp);


        }
    }
}
