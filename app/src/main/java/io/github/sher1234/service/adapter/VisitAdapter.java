package io.github.sher1234.service.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.Visits;
import io.github.sher1234.service.model.response.Services;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.ViewHolder> {

    private final ItemClick itemClick;
    private final Services services;

    public VisitAdapter(ItemClick itemClick, Services services) {
        this.itemClick = itemClick;
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitAdapter.ViewHolder holder, int position) {
        final Visits v = services.visits.get(position);
        holder.imageView.setImageResource(v.visit.isIncomplete() ? R.drawable.pending : R.drawable.completed);
        holder.textView1.setText(v.visit.getStartTimeView());
        holder.textView2.setText(v.visit.VisitID);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onItemClick(services.call, v, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.visits.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView textView1;
        final TextView textView2;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            imageView = layout.findViewById(R.id.imageView);
        }
    }

    public interface ItemClick {
        void onItemClick(Call call, Visits visit, View v);
    }
}