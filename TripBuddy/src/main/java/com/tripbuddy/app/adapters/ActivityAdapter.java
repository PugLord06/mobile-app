package com.tripbuddy.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tripbuddy.app.R;
import com.tripbuddy.app.models.Activity;
import com.tripbuddy.app.utils.AnimationUtil;

import java.util.List;
import java.util.Locale;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private Context context;
    private List<Activity> activities;
    private OnActivitySelectedListener listener;

    public interface OnActivitySelectedListener {
        void onActivitySelected();
    }

    public ActivityAdapter(Context context, List<Activity> activities, OnActivitySelectedListener listener) {
        this.context = context;
        this.activities = activities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity activity = activities.get(position);
        
        holder.tvActivityName.setText(activity.getName());
        holder.tvActivityCost.setText(String.format(Locale.US, "$%.2f", activity.getCost()));
        holder.tvActivityCategory.setText(activity.getCategory());
        holder.cbSelectActivity.setChecked(activity.isSelected());

        // Set checkbox listener
        holder.cbSelectActivity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.setSelected(isChecked);
                if (listener != null) {
                    listener.onActivitySelected();
                }
                
                // Apply animation
                if (isChecked) {
                    AnimationUtil.scaleAnimation(holder.itemView);
                }
            }
        });

        // Animate item appearance
        holder.itemView.setAlpha(0f);
        holder.itemView.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(position * 50)
                .start();
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelectActivity;
        TextView tvActivityName;
        TextView tvActivityCost;
        TextView tvActivityCategory;

        ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSelectActivity = itemView.findViewById(R.id.cb_select_activity);
            tvActivityName = itemView.findViewById(R.id.tv_activity_name);
            tvActivityCost = itemView.findViewById(R.id.tv_activity_cost);
            tvActivityCategory = itemView.findViewById(R.id.tv_activity_category);
        }
    }
}