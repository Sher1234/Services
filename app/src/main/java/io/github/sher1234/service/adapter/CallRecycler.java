package io.github.sher1234.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.common.ViewCall;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.util.Strings;

/**
 * ListRecycler
 */

public class CallRecycler extends RecyclerView.Adapter<CallRecycler.ViewHolder> {

    private final Context context;
    private List<Registration> registrations;

    public CallRecycler(Context context, List<Registration> registrations) {
        this.context = context;
        this.registrations = registrations;
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
    public void onBindViewHolder(@NonNull CallRecycler.ViewHolder holder, int position) {
        try {
            final Registration registration = registrations.get(position);
            String s = registration.CustomerName + ", " + registration.getDateTimeView();
            holder.textView1.setText(registration.CallNumber);
            holder.textView2.setText(s);
            if (registrations.get(position).isCompleted())
                holder.imageView.setImageResource(R.drawable.ic_completed);
            else
                holder.imageView.setImageResource(R.drawable.ic_pending);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewCall.class);
                    intent.putExtra(Strings.ExtraString, registration.CallNumber);
                    context.startActivity(intent);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (registrations == null)
            return 0;
        return registrations.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayoutCompat linearLayout;
        final ImageView imageView;
        final TextView textView1;
        final TextView textView2;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            linearLayout = layout.findViewById(R.id.linearLayout);
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            imageView = layout.findViewById(R.id.imageView);
        }
    }
}