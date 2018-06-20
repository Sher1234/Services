package io.github.sher1234.service.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import io.github.sher1234.service.R;
import io.github.sher1234.service.fragment.Call3;
import io.github.sher1234.service.model.base.VisitedCall;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.util.NavigationHost;

public class VisitRecycler extends RecyclerView.Adapter<VisitRecycler.ViewHolder> {

    private final FragmentActivity activity;
    private ServiceCall serviceCall;

    public VisitRecycler(FragmentActivity activity, ServiceCall serviceCall) {
        this.activity = activity;
        this.serviceCall = serviceCall;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitRecycler.ViewHolder holder, int position) {
        final int i = position;
        final VisitedCall visitedCall = serviceCall.getVisits().get(i);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(visitedCall.getStartTime());
        holder.textView0.setText(visitedCall.getVisitString());
        holder.textView1.setText(visitedCall.getStartTimeView());
        holder.textView2.setText(visitedCall.getVisitNumber());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) activity).navigateTo(Call3.getInstance(serviceCall, i), true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceCall.getVisits().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView0;
        final TextView textView1;
        final TextView textView2;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView0 = layout.findViewById(R.id.textView0);
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
        }
    }
}