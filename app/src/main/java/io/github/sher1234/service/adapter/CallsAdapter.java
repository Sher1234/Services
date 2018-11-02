package io.github.sher1234.service.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.Call;

/**
 * ListRecycler
 */

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.ViewHolder> {

    private final ItemClick itemClick;
    private List<Call> calls;

    public CallsAdapter(ItemClick itemClick, List<Call> calls) {
        this.itemClick = itemClick;
        this.calls = calls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull CallsAdapter.ViewHolder holder, int position) {
        try {
            final Call call = calls.get(position);
            String s = call.CustomerName + ", " + call.getDateTimeView();
            holder.textView1.setText(call.CallID);
            holder.textView2.setText(s);
            if (calls.get(position).isCompleted())
                holder.imageView.setImageResource(R.drawable.completed);
            else
                holder.imageView.setImageResource(R.drawable.pending);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(call, v);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (calls == null)
            return 0;
        return calls.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onUpdateCalls(List<Call> calls) {
        this.calls = calls;
        this.notifyDataSetChanged();
    }

    public interface ItemClick {
        void onItemClick(Call call, View v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final AppCompatTextView textView1, textView2;
        final LinearLayoutCompat linearLayout;
        final AppCompatImageView imageView;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            imageView = layout.findViewById(R.id.imageView);
            linearLayout = layout.findViewById(R.id.linearLayout);
        }
    }
}