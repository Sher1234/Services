package io.github.sher1234.service.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.util.DialogX;
import io.github.sher1234.service.util.UserChangeClick;

/**
 * ListRecycler
 */

public class UsersRecyclerX extends RecyclerView.Adapter<UsersRecyclerX.ViewHolder> {

    private final Context context;
    private final List<User> users;

    public UsersRecyclerX(Context context, List<User> users) {
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
    public void onBindViewHolder(@NonNull UsersRecyclerX.ViewHolder holder, int position) {
        try {
            final User user = users.get(position);
            holder.textView1.setText(user.getName());
            holder.textView2.setText(user.getEmail());
            String s;
            if (user.isAdmin())
                s = "Administrator: Yes";
            else
                s = "Administrator: No";
            holder.textView3.setText(s);
            if (user.isRegistered())
                s = "Registered: Yes";
            else
                s = "Registered: No";
            holder.textView4.setText(s);
            holder.textView5.setVisibility(View.GONE);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DialogX dialogX = new DialogX(context).setTitle("Change Privileges")
                            .setDescription("Change Account Privileges of " + user.getName());
                    if (user.isAdmin())
                        dialogX.neutralButton("Dismiss Admin", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserChangeClick) context).onAdminChange(user.getEmail(), 0);
                                dialogX.dismiss();
                            }
                        });
                    else
                        dialogX.neutralButton("Add Admin", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserChangeClick) context).onAdminChange(user.getEmail(), 1);
                                dialogX.dismiss();
                            }
                        });
                    if (user.isRegistered())
                        dialogX.negativeButton("Disable Account", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserChangeClick) context).onAccountStateChange(user.getEmail(), 0);
                                dialogX.dismiss();
                            }
                        });
                    else
                        dialogX.negativeButton("Enable Account", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserChangeClick) context).onAccountStateChange(user.getEmail(), 1);
                                dialogX.dismiss();
                            }
                        });
                    dialogX.positiveButton("Delete Account", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((UserChangeClick) context).onAccountDelete(user.getEmail());
                            dialogX.dismiss();
                        }
                    });
                    dialogX.setButtonOrientation(LinearLayoutCompat.VERTICAL).show();
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