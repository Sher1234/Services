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
import io.github.sher1234.service.activity.admin.UserDash;
import io.github.sher1234.service.model.base.Employee;
import io.github.sher1234.service.util.Strings;

public class EmployeeRecycler extends RecyclerView.Adapter<EmployeeRecycler.ViewHolder> {

    private List<Employee> users;
    private final Context context;

    public EmployeeRecycler(Context context, List<Employee> users) {
        this.context = context;
        this.users = users;
    }

    public void setUsers(List<Employee> users) {
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
    public void onBindViewHolder(@NonNull EmployeeRecycler.ViewHolder holder, int position) {
        try {
            final Employee employee = users.get(position);
            holder.textView1.setText(employee.Name);
            holder.textView2.setText(employee.Email);
            String s = "Registrations " + employee.TotalR;
            holder.textView3.setText(s);
            s = "Visits " + employee.TotalV;
            holder.textView4.setText(s);
            s = "Pending " + employee.TotalP;
            holder.textView5.setText(s);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserDash.class);
                    intent.putExtra(Strings.ExtraData, employee);
                    context.startActivity(intent);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (users != null)
            return users.size();
        return 0;
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