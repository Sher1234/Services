package io.github.sher1234.service.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.Employee;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private final ItemClick itemClick;
    private List<Employee> employees;

    public EmployeeAdapter(ItemClick itemClick, List<Employee> employees) {
        this.itemClick = itemClick;
        this.employees = employees;
    }

    public void onUpdateEmployees(List<Employee> employees) {
        this.employees = employees;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.ViewHolder holder, int position) {
        try {
            final Employee employee = employees.get(position);
            holder.textView1.setText(employee.Name);
            holder.textView2.setText(employee.Email);
            String s = "Calls: " + employee.Calls;
            holder.textView3.setText(s);
            s = "Visits: " + employee.Visits;
            holder.textView4.setText(s);
            s = "Pending: " + employee.Pending;
            holder.textView5.setText(s);
            if (employee.Name == null || employee.Name.isEmpty())
                holder.textView1.setVisibility(View.GONE);
            if (employee.Email == null || employee.Email.isEmpty())
                holder.textView2.setVisibility(View.GONE);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(employee, v);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (employees != null)
            return employees.size();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface ItemClick {
        void onItemClick(Employee employee, View v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final AppCompatTextView textView1, textView2, textView3, textView4, textView5;
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