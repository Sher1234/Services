package io.github.sher1234.service.adapter.v4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.sher1234.service.R;

@SuppressWarnings("ConstantConditions")
public class GraphAdapter extends RecyclerView.Adapter<GraphAdapter.ViewHolder> {

    private OnItemClick<Map<String, Integer>> itemClick;
    private List<Integer> requests;
    private List<Integer> visits;
    private int max;

    private GraphAdapter(OnItemClick<Map<String, Integer>> itemClick) {
        this.itemClick = itemClick;
    }

    public static GraphAdapter newInstance(OnItemClick<Map<String, Integer>> itemClick) {
        return new GraphAdapter(itemClick);
    }

    public static GraphAdapter newInstance(OnItemClick<Map<String, Integer>> itemClick,
                                           List<Integer> requests, List<Integer> visits) {
        GraphAdapter adapter = newInstance(itemClick);
        adapter.onGraphUpdate(requests, visits);
        return adapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_4, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphAdapter.ViewHolder holder, int position) {
        Map<String, Integer> map = new HashMap<>();
        map.put("request", requests.get(position));
        map.put("visit", visits.get(position));
        holder.onBindData(map, max);
    }

    @Override
    public int getItemCount() {
        return requests == null ? 0 : requests.size();
    }

    public void onGraphUpdate(List<Integer> requests, List<Integer> visits) {
        int max1 = Collections.max(requests);
        int max2 = Collections.max(visits);
        max = max1 >= max2? max1:max2;
        this.requests = requests;
        this.visits = visits;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar requests;
        private final ProgressBar visits;
        private final View view;

        ViewHolder(View view) {
            super(view);
            requests = view.findViewById(R.id.progressBarA);
            visits = view.findViewById(R.id.progressBarB);
            this.view = view;
        }

        void onBindData(final Map<String, Integer> map, int max) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onClick(map);
                }
            });
            requests.setProgress(map.get("request"));
            visits.setProgress(map.get("visit"));
            requests.setMax(max);
            visits.setMax(max);
        }
    }
}