package io.github.sher1234.service.ui.v1.i.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.VisitAdapter;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.model.base.Visits;
import io.github.sher1234.service.model.response.Services;
import io.github.sher1234.service.ui.v1.i.Show;
import io.github.sher1234.service.ui.v1.j.AddVisit;
import io.github.sher1234.service.ui.v1.j.EndVisit;
import io.github.sher1234.service.ui.v1.j.StartVisit;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;

public class ShowVisits extends Fragment implements View.OnClickListener {

    private LinearLayoutCompat layoutI;
    private RecyclerView recyclerView;
    private Services services;
    private TextView textView;
    private Visit visit;

    public ShowVisits() {
    }

    public static ShowVisits getInstance(Services services) {
        ShowVisits call2 = new ShowVisits();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, services);
        call2.setArguments(bundle);
        return call2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        services = (Services) getArguments().getSerializable(Strings.ExtraData);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.i_fragment_3, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutI = view.findViewById(R.id.linearLayout);
        textView = view.findViewById(R.id.textView);
        layoutI.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (services == null || services.visits == null || services.visits.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            return;
        }
        boolean b = false;
        for (Visits visit : services.visits)
            if (b = visit.visit.isIncomplete()) {
                this.visit = visit.visit;
                break;
            }
        if (b) layoutI.setVisibility(View.VISIBLE);
        VisitAdapter recycler = new VisitAdapter((VisitAdapter.ItemClick) getActivity(), services);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.visits, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean b = false;
        if (services.visits != null)
            for (Visits visits : services.visits) if (b = visits.visit.isIncomplete()) break;
        menu.findItem(R.id.add).setVisible(!b).setEnabled(!b);
        if (services.call.isCompleted())
            menu.findItem(R.id.add).setVisible(false).setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            onDialogAddVisit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert getActivity() != null;
        if (requestCode == 1412 && (resultCode == 100 || resultCode == 101))
            Snackbar.make(recyclerView, "Data Changed, Reload Call", Snackbar.LENGTH_SHORT)
                    .setAction("Reload", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            assert getActivity() != null;
                            ((Show) getActivity()).onReloadCall();
                        }
                    }).show();
    }

    private void onDialogAddVisit() {
        assert getContext() != null;
        MaterialDialog dialog = MaterialDialog.Dialog(getContext());
        dialog.setTitle("Add Visit").setCancelable(true);
        dialog.setDescription("Add a visit for Call Id \"" + services.call.CallID + "\".");
        dialog.positiveButton("Full", new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                Intent intent = new Intent(getActivity(), AddVisit.class);
                intent.putExtra(Strings.ExtraData, services.call);
                startActivityForResult(intent, 1412);
                dialog.dismiss();
            }
        }).negativeButton("Start", new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                Intent intent = new Intent(getActivity(), StartVisit.class);
                intent.putExtra(Strings.ExtraData, services.call);
                startActivityForResult(intent, 1412);
                dialog.dismiss();
            }
        }).neutralButton("Cancel", new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.linearLayout) {
            Intent intent = new Intent(getActivity(), EndVisit.class);
            intent.putExtra(Strings.ExtraData1, services.call);
            intent.putExtra(Strings.ExtraData2, visit);
            startActivity(intent);
        }
    }
}