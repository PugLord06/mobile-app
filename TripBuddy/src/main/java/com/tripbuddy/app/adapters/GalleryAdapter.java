package com.tripbuddy.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tripbuddy.app.R;
import com.tripbuddy.app.models.Memory;
import com.tripbuddy.app.utils.AnimationUtil;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private Context context;
    private List<Memory> memories;
    private OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(Memory memory, int position);
    }

    public GalleryAdapter(Context context, List<Memory> memories, OnImageClickListener listener) {
        this.context = context;
        this.memories = memories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        Memory memory = memories.get(position);
        
        // Set memory data
        holder.tvLocation.setText(memory.getLocation());
        holder.tvDate.setText(memory.getDate());
        
        // Set thumbnail image from local drawables based on key
        holder.ivThumbnail.setImageResource(getDrawableForKey(memory.getImagePath()));

        // Set click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleAnimation(v);
                if (listener != null) {
                    listener.onImageClick(memory, position);
                }
            }
        });

        // Animate item appearance
        holder.itemView.setAlpha(0f);
        holder.itemView.setTranslationY(50);
        holder.itemView.animate()
                .alpha(1f)
                .translationY(0)
                .setDuration(400)
                .setStartDelay(position * 100)
                .start();
    }

    @Override
    public int getItemCount() {
        return memories.size();
    }

    private int getDrawableForKey(String key) {
        if (key == null) return R.drawable.adventure;
        switch (key) {
            case "beach":
                return R.drawable.beach;
            case "mountain":
                return R.drawable.mountain;
            case "city":
                return R.drawable.city;
            case "forest":
                return R.drawable.forest;
            case "adventure":
                return R.drawable.adventure;
            default:
                return R.drawable.adventure;
        }
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivThumbnail;
        TextView tvLocation;
        TextView tvDate;

        GalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_gallery_item);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}