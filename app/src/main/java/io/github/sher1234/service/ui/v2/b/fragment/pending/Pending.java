package io.github.sher1234.service.ui.v2.b.fragment.pending;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.v4.OnItemClick;
import io.github.sher1234.service.adapter.v4.RequestAdapter;
import io.github.sher1234.service.firebase.model.request.Request;

public class Pending extends Fragment implements OnItemClick<Request> {

    private AppCompatTextView textView1, textView2;
    private RecyclerView recyclerView;

    public Pending() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        View view = inflater.inflate(R.layout.b_fragment_2, group, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView2 = view.findViewById(R.id.textView2);
        textView1 = view.findViewById(R.id.textView1);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean b = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), b?1:2));
        textView2.setText(R.string.no_pending_requests);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        textView1.setText(R.string.pending_requests);
        textView2.setVisibility(View.VISIBLE);
        //test-code
        test();
    }

    private void onReset(List<Request> requests) {
        RequestAdapter requestAdapter = (RequestAdapter) recyclerView.getAdapter();
        if (requestAdapter != null) requestAdapter.onUpdate(requests);
        else {
            requestAdapter = RequestAdapter.newInstance(this, requests);
            recyclerView.setAdapter(requestAdapter);
            requestAdapter.onUpdate(requests);
        }
    }

    @Override
    public void onClick(Request request) {
        String s = request.customerName + "\n" + request.rid + "\n" + request.status.type;
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    //Test Code
    private Request data() {
        return new Request().request("cust-name", "9899", "detail-cust",
                "con-name", "123", "111", "prod-detail",
                0, -1, "comp-type", "comp-detail");
    }

    private void test() {
        List<Request> requests = new ArrayList<>();
        requests.add(data());
        requests.add(data());
        requests.add(data());
        requests.add(data());
        onReset(requests);
    }
}