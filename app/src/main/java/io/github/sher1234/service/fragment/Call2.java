package io.github.sher1234.service.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.RegVisitActivity;
import io.github.sher1234.service.adapter.VisitRecycler;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.util.Strings;

public class Call2 extends Fragment {
    private static final String TAG_ARGS = "SERIALIZABLE-PAGER-CALL";

    private RecyclerView recyclerView;
    private ServiceCall serviceCall;
    private TextView textView;

    public Call2() {
    }

    public static Call2 getInstance(ServiceCall serviceCall) {
        Call2 call2 = new Call2();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_ARGS, serviceCall);
        call2.setArguments(bundle);
        return call2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        serviceCall = (ServiceCall) getArguments().getSerializable(TAG_ARGS);
        if (serviceCall != null && !serviceCall.getRegistration().isCompleted())
            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_2, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (serviceCall == null || serviceCall.getVisits() == null || serviceCall.getVisits().size() == 0) {
            textView.setVisibility(View.VISIBLE);
            return;
        }
        VisitRecycler visitRecycler = new VisitRecycler(getActivity(), serviceCall);
        recyclerView.setAdapter(visitRecycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (serviceCall == null || serviceCall.getRegistration().isCompleted())
            menu.findItem(R.id.menuAdd).setEnabled(false).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getActivity(), RegVisitActivity.class);
        intent.putExtra(Strings.ExtraServiceCall, serviceCall);
        if (item.getItemId() == R.id.menuAdd && getActivity() != null) {
            getActivity().startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}