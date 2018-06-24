package io.github.sher1234.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.admin.DashboardActivity;
import io.github.sher1234.service.model.base.UserX;

/**
 * ListRecycler
 */

public class UsersRecycler extends RecyclerView.Adapter<UsersRecycler.ViewHolder> {

    private final Context context;
    private List<UserX> users;

    public UsersRecycler(Context context, List<UserX> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_3, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRecycler.ViewHolder holder, int position) {
        try {
            final UserX user = users.get(position);
            holder.textView1.setText(user.getName());
            holder.textView2.setText(user.getEmail());
            String s = "Registrations " + user.getRegs();
            holder.textView3.setText(s);
            s = "Visits " + user.getRegs();
            holder.textView4.setText(s);
            s = "Pending " + user.getPending();
            holder.textView5.setText(s);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.putExtra("Intent:Name", user.getName());
                    intent.putExtra("Intent:Email", user.getEmail());
                    context.startActivity(intent);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView1;
        final TextView textView2;
        final TextView textView3;
        final TextView textView4;
        final TextView textView5;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            textView3 = layout.findViewById(R.id.textView3);
            textView4 = layout.findViewById(R.id.textView4);
            textView5 = layout.findViewById(R.id.textView5);
        }
    }
}