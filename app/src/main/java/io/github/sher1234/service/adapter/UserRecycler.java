package io.github.sher1234.service.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.fragment.AllotCall;
import io.github.sher1234.service.model.base.User;

/**
 * ListRecycler
 */

public class UserRecycler extends RecyclerView.Adapter<UserRecycler.ViewHolder> {

    private final AllotCall context;
    private List<User> users;

    public UserRecycler(AllotCall context, List<User> users) {
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

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecycler.ViewHolder holder, int position) {
        try {
            final User user = users.get(position);
            holder.textView1.setText(user.Name);
            String s = user.EmployeeID + " / " + user.Email;
            holder.textView2.setText(s);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.onClickItem(user);
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
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
        }
    }
}