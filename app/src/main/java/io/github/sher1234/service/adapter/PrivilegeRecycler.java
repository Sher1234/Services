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
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.UserChangeListener;

/**
 * ListRecycler
 */

public class PrivilegeRecycler extends RecyclerView.Adapter<PrivilegeRecycler.ViewHolder> {

    private final Context context;
    private final List<User> users;

    public PrivilegeRecycler(Context context, List<User> users) {
        this.context = context;
        this.users = users;
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
    public void onBindViewHolder(@NonNull PrivilegeRecycler.ViewHolder holder, int position) {
        try {
            final User user = users.get(position);
            holder.textView1.setText(user.Name);
            holder.textView2.setText(user.Email);
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
                    final MaterialDialog materialDialog = new MaterialDialog(context).setTitle("Change Privileges")
                            .setDescription("Change Account Privileges of " + user.Name);
                    if (user.isAdmin())
                        materialDialog.neutralButton("Dismiss Admin", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserChangeListener) context).onAdminChange(user.Email, 0);
                                materialDialog.dismiss();
                            }
                        });
                    else
                        materialDialog.neutralButton("Add Admin", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserChangeListener) context).onAdminChange(user.Email, 1);
                                materialDialog.dismiss();
                            }
                        });
                    if (user.isRegistered())
                        materialDialog.negativeButton("Disable Account", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserChangeListener) context).onAccountStateChange(user.Email, 0);
                                materialDialog.dismiss();
                            }
                        });
                    else
                        materialDialog.negativeButton("Enable Account", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserChangeListener) context).onAccountStateChange(user.Email, 1);
                                materialDialog.dismiss();
                            }
                        });
                    materialDialog.positiveButton("Delete Account", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((UserChangeListener) context).onAccountDelete(user.Email);
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.setButtonOrientation(LinearLayoutCompat.VERTICAL).show();
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