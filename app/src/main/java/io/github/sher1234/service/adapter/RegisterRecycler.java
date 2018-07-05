package io.github.sher1234.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.ViewActivity;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.util.Strings;

/**
 * ListRecycler
 */

public class RegisterRecycler extends RecyclerView.Adapter<RegisterRecycler.ViewHolder> {

    private final Context context;
    private final List<Registration> registrations;

    public RegisterRecycler(Context context, List<Registration> registrations) {
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
    public void onBindViewHolder(@NonNull RegisterRecycler.ViewHolder holder, int position) {
        try {
            final Registration registration = registrations.get(position);
            String s = registration.getCustomerName() + ", " + registration.getDateTimeView();
            holder.textView1.setText(registration.getCallNumber());
            holder.textView2.setText(s);
            if (registrations.get(position).isCompleted())
                holder.imageView.setImageResource(R.drawable.ic_completed);
            else
                holder.imageView.setImageResource(R.drawable.ic_pending);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewActivity.class);
                    intent.putExtra(Strings.ExtraString, registration.getCallNumber());
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView1;
        final TextView textView2;
        final ImageView imageView;
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