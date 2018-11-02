package io.github.sher1234.service.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.User;

/**
 * ListRecycler
 */

public class PrivilegeAdapter extends RecyclerView.Adapter<PrivilegeAdapter.ViewHolder> {

    private final ItemClick itemClick;
    private List<User> users;

    public PrivilegeAdapter(ItemClick itemClick, List<User> users) {
        this.itemClick = itemClick;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_5, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void onUpdateUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PrivilegeAdapter.ViewHolder holder, int position) {
        try {
            final User user = users.get(position);
            holder.textView1.setText(user.Name);
            holder.textView2.setText(user.Email);
            if (user.isRegistered()) {
                holder.textView3.setText(R.string.registered);
                holder.textView3.setBackgroundResource(R.drawable.tag_green);
            } else {
                holder.textView3.setText(R.string.unregistered);
                holder.textView3.setBackgroundResource(R.drawable.tag_red);
            }
            if (user.isAdmin()) {
                holder.textView4.setText(R.string.admin);
                holder.textView4.setBackgroundResource(R.drawable.tag_green);
            } else {
                holder.textView4.setText(R.string.not_admin);
                holder.textView4.setBackgroundResource(R.drawable.tag_red);
            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(user, v);
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

    public interface ItemClick {
        void onItemClick(User user, View v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final AppCompatTextView textView1, textView2, textView3, textView4;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            textView3 = layout.findViewById(R.id.textView3);
            textView4 = layout.findViewById(R.id.textView4);
        }
    }
}