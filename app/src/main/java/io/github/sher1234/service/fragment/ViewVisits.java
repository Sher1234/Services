package io.github.sher1234.service.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.common.EndVisit;
import io.github.sher1234.service.activity.common.RegisterVisit;
import io.github.sher1234.service.activity.common.StartVisit;
import io.github.sher1234.service.activity.common.ViewCall;
import io.github.sher1234.service.adapter.VisitRecycler;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.model.base.VisitX;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;

public class ViewVisits extends Fragment implements View.OnClickListener {

    private LinearLayoutCompat layoutIncomplete;
    private RecyclerView recyclerView;
    private TextView textView;
    private ServiceCall call;
    private Visit visit;

    public ViewVisits() {
    }

    public static ViewVisits getInstance(ServiceCall call) {
        ViewVisits call2 = new ViewVisits();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, call);
        call2.setArguments(bundle);
        return call2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        call = (ServiceCall) getArguments().getSerializable(Strings.ExtraData);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_visit, container, false);
        layoutIncomplete = view.findViewById(R.id.layoutIncomplete);
        view.findViewById(R.id.button1).setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.textView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (call == null || call.visits == null || call.visits.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            return;
        }
        boolean b = false;
        for (VisitX visitX : call.visits) {
            if (visitX.visit.isVisitStarted()) {
                visit = visitX.visit;
                b = true;
                break;
            }
        }
        if (b)
            layoutIncomplete.setVisibility(View.VISIBLE);
        VisitRecycler recycler = new VisitRecycler(getActivity(), call);
        recyclerView.setAdapter(recycler);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.view_visit, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean b = false;
        User user = AppController.getUserFromPrefs();
        menu.findItem(R.id.map).setEnabled(false).setVisible(false);
        menu.findItem(R.id.pdf).setEnabled(false).setVisible(false);
        menu.findItem(R.id.edit).setVisible(false).setEnabled(false);
        menu.findItem(R.id.delete).setVisible(false).setEnabled(false);
        if (user.isAdmin()) {
            if (call.visits != null)
                for (VisitX visitX : call.visits) {
                    if (visitX.visit.isVisitStarted()) {
                        b = true;
                        break;
                    }
                }
            if (b)
                menu.findItem(R.id.add).setVisible(false).setEnabled(false);
        } else {
            if (call.visits != null)
                for (VisitX visitX : call.visits) {
                    if (visitX.visit.isVisitStarted()) {
                        b = true;
                        break;
                    }
                }
            if (b)
                menu.findItem(R.id.add).setVisible(false).setEnabled(false);
        }
        if (call.registration.isCompleted())
            menu.findItem(R.id.add).setVisible(false).setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                assert getActivity() != null;
                ((ViewCall) getActivity()).onTapRefresh();
                return true;

            case R.id.add:
                onDialogAddVisit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onDialogAddVisit() {
        assert getContext() != null;
        final MaterialDialog materialDialog = new MaterialDialog(getContext());
        materialDialog.setTitle("Add Visit").setCancelable(true);
        materialDialog.setDescription("Register a service for call number " + call.registration.CallNumber);
        materialDialog.positiveButton("Full", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Visit visit = null;
                if (call.visits != null && !call.visits.isEmpty())
                    visit = call.visits.get(call.visits.size() - 1).visit;
                Intent intent = new Intent(getActivity(), RegisterVisit.class);
                intent.putExtra(Strings.ExtraData1, call.registration);
                intent.putExtra(Strings.ExtraData2, visit);
                if (getActivity() != null)
                    getActivity().startActivity(intent);
                materialDialog.dismiss();
            }
        }).negativeButton("Start", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartVisit.class);
                intent.putExtra(Strings.ExtraData, call.registration);
                if (getActivity() != null)
                    getActivity().startActivity(intent);
                materialDialog.dismiss();
            }
        }).neutralButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            Intent intent = new Intent(getActivity(), EndVisit.class);
            intent.putExtra(Strings.ExtraData1, call.registration);
            intent.putExtra(Strings.ExtraData2, visit);
            startActivity(intent);
        }
    }
}