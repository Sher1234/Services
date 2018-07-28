package io.github.sher1234.service.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import io.github.sher1234.service.R;

/**
 * ListRecycler
 */

public class ProgressRecycler extends RecyclerView.Adapter<ProgressRecycler.ViewHolder> {

    private final Context context;
    public int max;
    private List<Integer> integers;
    private List<String> strings;

    public ProgressRecycler(Context context, List<Integer> integers, List<String> strings) {
        max = Collections.max(integers);
        this.integers = integers;
        this.strings = strings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recycler_item_4, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressRecycler.ViewHolder holder, int position) {
        int i, c;
        String s = integers.get(position) + "";
        if (position % 4 == 1) {
            c = R.color.gRed;
            i = R.drawable.progress_bar_red;
        } else if (position % 4 == 2) {
            c = R.color.gYellow;
            i = R.drawable.progress_bar_yellow;
        } else if (position % 4 == 3) {
            c = R.color.gGreen;
            i = R.drawable.progress_bar_green;
        } else {
            c = R.color.gBlue;
            i = R.drawable.progress_bar_blue;
        }
        holder.textView1.setText(s);
        holder.progressBar.setMax(max);
        holder.textView2.setText(strings.get(position));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            holder.progressBar.setProgress(integers.get(position));
        else
            holder.progressBar.setProgress(integers.get(position), true);
        holder.textView1.setTextColor(context.getResources().getColor(c));
        holder.textView2.setTextColor(context.getResources().getColor(c));
        holder.progressBar.setProgressDrawable(context.getResources().getDrawable(i));
    }

    @Override
    public int getItemCount() {
        if (integers == null)
            return 0;
        return integers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setIntegers(List<Integer> integers) {
        this.integers = integers;
        max = Collections.max(integers);
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView1;
        final TextView textView2;
        final ProgressBar progressBar;

        ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }
}