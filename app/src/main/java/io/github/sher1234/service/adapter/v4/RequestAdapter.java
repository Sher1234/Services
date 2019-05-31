package io.github.sher1234.service.adapter.v4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.firebase.model.request.Request;
import io.github.sher1234.service.functions.v4.DateFormat;

/**
 * ListRecycler
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private final OnItemClick<Request> itemClick;
    private List<Request> requests;

    private RequestAdapter(OnItemClick<Request> itemClick) {
        this.itemClick = itemClick;
    }

    public static RequestAdapter newInstance(OnItemClick<Request> itemClick, List<Request> requests) {
        RequestAdapter requestAdapter = new RequestAdapter(itemClick);
        requestAdapter.onUpdate(requests);
        return requestAdapter;
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
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, int position) {
        holder.onBind(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests == null? 0 : requests.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onUpdate(List<Request> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView textView1, textView2, textView3;
        private final MaterialCardView cardView;
        private final View view;

        ViewHolder(View view) {
            super(view);
            textView3 = view.findViewById(R.id.textView3);
            textView2 = view.findViewById(R.id.textView2);
            textView1 = view.findViewById(R.id.textView1);
            cardView = view.findViewById(R.id.cardView);
            this.view = view;
        }

        void onBind(final Request request) {
            String s = request.customerName + ", " + request.customerDetail;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onClick(request);
                }
            });
            textView3.setBackgroundColor(getColor(request.status.code));
            textView3.setText(DateFormat.Date.format(request.time));
            cardView.setStrokeColor(getColor(request.status.code));
            textView1.setText(request.rid);
            textView2.setText(s);
        }

        @ColorInt
        private int getColor(int code) {
            if (code == 0) code = R.color.red;
            if (code == 1) code = R.color.green;
            if (code == -1) code = R.color.yellow;
            return view.getResources().getColor(code);
        }
    }
}