package io.github.sher1234.service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;

/**
 * ListRecycler
 */

public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.ViewHolder> {

    private List<Integer> integers;
    private final Context context;
    private List<String> strings;
    private int max;

    public GraphAdapter(Context context, List<Integer> integers, List<String> strings) {
        max = Collections.max(integers);
        this.integers = integers;
        this.strings = strings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_4, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull GraphAdapter.ViewHolder holder, int position) {
        holder.textView2.setText(strings.get(position).replace("-#-", "\n"));
        final String s = String.valueOf(integers.get(position));
        int i, t, a = max / 4, b = max / 2, c = a + b;
        holder.progressBar.setMax(max);
        holder.textView1.setText(s);
        if (integers.get(position) > c) {
            i = R.drawable.progress_bar_green;
            t = R.color.green;
        } else if (integers.get(position) > b) {
            i = R.drawable.progress_bar_blue;
            t = R.color.blue;
        } else if (integers.get(position) > a) {
            i = R.drawable.progress_bar_yellow;
            t = R.color.yellow;
        } else if (integers.get(position) == 0) {
            i = R.drawable.progress_bar_black;
            t = R.color.black;
        } else {
            i = R.drawable.progress_bar_red;
            t = R.color.red;
        }
        holder.progressBar.setProgress(integers.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(s);
            }
        });
        holder.textView1.setTextColor(context.getResources().getColor(t));
        holder.textView2.setTextColor(context.getResources().getColor(t));
        holder.progressBar.setProgressDrawable(context.getResources().getDrawable(i));
    }

    private void onItemClick(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return integers == null ? 0 : integers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onGraphUpdate(List<Integer> integers, List<String> strings) {
        max = Collections.max(integers);
        this.integers = integers;
        this.strings = strings;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView textView1;
        final TextView textView2;
        final ProgressBar progressBar;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}