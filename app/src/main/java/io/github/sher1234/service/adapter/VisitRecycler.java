package io.github.sher1234.service.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.common.ViewCall;
import io.github.sher1234.service.fragment.ViewVisit;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.model.base.VisitX;
import io.github.sher1234.service.model.response.ServiceCall;

public class VisitRecycler extends RecyclerView.Adapter<VisitRecycler.ViewHolder> {

    private final FragmentActivity activity;
    private final ServiceCall serviceCall;

    public VisitRecycler(FragmentActivity activity, ServiceCall serviceCall) {
        this.activity = activity;
        this.serviceCall = serviceCall;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull VisitRecycler.ViewHolder holder, int position) {
        final Visit visit = serviceCall.visits.get(position).visit;
        final VisitX visitX = serviceCall.visits.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(visit.getStartTime());
        if (visit.isVisitStarted())
            holder.imageView.setImageResource(R.drawable.ic_pending);
        else
            holder.imageView.setImageResource(R.drawable.ic_completed);
        holder.textView1.setText(visit.getStartTimeView());
        holder.textView2.setText(visit.getVisitNumber());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewCall) activity).navigateToFragment(ViewVisit
                        .getInstance(serviceCall.registration, visitX));
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceCall.visits.size();
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
}