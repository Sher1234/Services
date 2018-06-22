package io.github.sher1234.service.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.RegVisitActivity;
import io.github.sher1234.service.activity.RegVisitSeActivity;
import io.github.sher1234.service.model.base.RegisteredCall;
import io.github.sher1234.service.model.base.VisitedCall;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextMultiLine;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import io.github.sher1234.service.util.formBuilder.model.FormHeader;

public class Call3 extends Fragment implements View.OnClickListener {
    private static final String TAG_ARGS_1 = "SERIALIZABLE-PAGER-CALL";
    private static final String TAG_ARGS_2 = "INTEGER-PAGER-CALL";

    private int i;
    private View view;
    private String location;
    private ServiceCall serviceCall;
    private FormBuilder mFormBuilder;

    public Call3() {
    }

    public static Call3 getInstance(ServiceCall serviceCall, int i) {
        Call3 call1 = new Call3();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_ARGS_1, serviceCall);
        bundle.putInt(TAG_ARGS_2, i);
        call1.setArguments(bundle);
        return call1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        serviceCall = (ServiceCall) getArguments().getSerializable(TAG_ARGS_1);
        i = getArguments().getInt(TAG_ARGS_2, 0);
        location = serviceCall.getVisits().get(i).getLocation();
        if (location != null && !location.isEmpty())
            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_1, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(getContext(), recyclerView);
        view.findViewById(R.id.button).setOnClickListener(this);
        this.view = view.findViewById(R.id.editView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (serviceCall.getVisits().get(i).isVisitStarted()) {
            onCreateFormStarted();
            view.setVisibility(View.VISIBLE);
            setVisitModelStarted(serviceCall.getVisits().get(i), serviceCall.getRegistration());
        } else {
            onCreateForm();
            view.setVisibility(View.GONE);
            setVisitModel(serviceCall.getVisits().get(i), serviceCall.getRegistration());
        }
    }

    private void setVisitModelStarted(VisitedCall visitedCall, RegisteredCall registeredCall) {
        if (visitedCall == null)
            return;
        setFormValue(486901, registeredCall.getCustomerName());
        setFormValue(486902, registeredCall.getSiteDetails());

        setFormValue(486903, visitedCall.getCallNumber());
        setFormValue(486904, visitedCall.getVisitNumber());
        setFormValue(486905, visitedCall.getStartTimeView());

        setFormValue(486907, visitedCall.getName());
        setFormValue(486908, registeredCall.getPhone());
        setFormValue(486909, visitedCall.getEmail());
    }

    private void onCreateFormStarted() {

        List<BaseFormElement> formItems = new ArrayList<>();
        BaseFormElement element0, element1, element2, element3;

        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Name")
                .setTag(486901).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details").setTag(486902)
                .setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Call Number").setTag(486903)
                .setRequired(true).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);

        element1 = FormElementTextMultiLine.createInstance().setTitle("Visit Number").setTag(486904)
                .setRequired(true).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Start Time")
                .setRequired(true).setTag(486905).setEnabled(false);
        formItems.add(element1);
        formItems.add(element2);

        element0 = FormHeader.createInstance("Visit Person");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Name").setTag(486907)
                .setRequired(true).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Mobile Number")
                .setRequired(true).setTag(486908).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Email").setRequired(true)
                .setTag(486909).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        mFormBuilder.addFormElements(formItems);
    }

    private void setVisitModel(VisitedCall visitedCall, RegisteredCall registeredCall) {
        if (visitedCall == null)
            return;
        setFormValue(486901, registeredCall.getCustomerName());
        setFormValue(486902, registeredCall.getSiteDetails());

        setFormValue(486903, visitedCall.getConcernName());
        setFormValue(486904, visitedCall.getConcernPhone());
        setFormValue(486905, visitedCall.getConcernEmail());

        setFormValue(486906, visitedCall.getObservation());
        setFormValue(486907, visitedCall.getActionTaken());
        setFormValue(486908, registeredCall.getIsCompletedString());
        setFormValue(486909, visitedCall.getCustomerSatisfaction());
        setFormValue(486910, visitedCall.getFeedback());

        setFormValue(486911, visitedCall.getCallNumber());
        setFormValue(486912, visitedCall.getStartTimeView());
        setFormValue(486913, visitedCall.getEndTimeView());
        setFormValue(486914, visitedCall.getName());
        setFormValue(486915, visitedCall.getPhone());
        setFormValue(486916, visitedCall.getEmail());
        setFormValue(486917, visitedCall.getVisitNumber());
    }

    private void onCreateForm() {

        List<BaseFormElement> formItems = new ArrayList<>();
        BaseFormElement element0, element1, element2, element3;

        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Name")
                .setTag(486901).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details").setTag(486902)
                .setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Concern Person
        element0 = FormHeader.createInstance("Concern Person");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Name").setTag(486903)
                .setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Mobile Number").setTag(486904)
                .setEnabled(false);
        element3 = FormElementTextMultiLine.createInstance().setTitle("e-Mail ID").setTag(486905)
                .setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        // Product Details
        element0 = FormHeader.createInstance("Visit Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Observation")
                .setTag(486906).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Action Taken")
                .setTag(486907).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Complaint Details
        element0 = FormHeader.createInstance("Other Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Complaint Status")
                .setTag(486908).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Customer Satisfaction")
                .setTag(486909).setEnabled(false);
        element3 = FormElementTextMultiLine.createInstance().setTitle("Customer Feedback")
                .setHint("Feedback").setTag(486910).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Call Number").setTag(486911)
                .setRequired(true).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        element1 = FormElementTextMultiLine.createInstance().setTitle("Visit Number").setTag(486917)
                .setRequired(true).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Start Time")
                .setRequired(true).setTag(486912).setEnabled(false);
        element3 = FormElementTextMultiLine.createInstance().setTitle("End Time")
                .setRequired(true).setTag(486913).setEnabled(false);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        element0 = FormHeader.createInstance("Visit Person");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Name").setTag(486914)
                .setRequired(true).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Mobile Number")
                .setRequired(true).setTag(486915).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Email").setRequired(true)
                .setTag(486916).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        mFormBuilder.addFormElements(formItems);
    }

    private void setFormValue(int i, String s) {
        mFormBuilder.getFormElement(i).setValue(s);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.findItem(R.id.menuAdd) != null)
            menu.findItem(R.id.menuAdd).setEnabled(false).setVisible(false);
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (location != null && location.length() > 5)
            menu.findItem(R.id.menuLocation).setVisible(true).setEnabled(true);
        else
            menu.findItem(R.id.menuLocation).setVisible(false).setEnabled(false);
        if (serviceCall == null || serviceCall.getRegistration().isCompleted())
            menu.findItem(R.id.menuAdd).setEnabled(false).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuLocation && getActivity() != null) {
            String[] s = location.split("-#-");
            double lat = Double.parseDouble(s[0]);
            double lon = Double.parseDouble(s[1]);
            String q = "geo:" + lat + "," + lon + "?q=" + Uri.encode(lat + "," + lon + "(Service Call)") + "&z=16";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(q)));
            return true;
        } else if (item.getItemId() == R.id.menuAdd && getActivity() != null) {
            showDialog();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            Intent intent = new Intent(getActivity(), RegVisitSeActivity.class);
            intent.putExtra(Strings.ExtraServiceCall, serviceCall);
            intent.putExtra(Strings.ExtraString, 1);
            if (getFragmentManager() != null)
                getFragmentManager().beginTransaction().remove(this).commit();
            startActivity(intent);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Visit").setCancelable(true)
                .setPositiveButton("Full", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), RegVisitActivity.class);
                        intent.putExtra(Strings.ExtraServiceCall, serviceCall);
                        if (getActivity() != null)
                            getActivity().startActivity(intent);
                    }
                })
                .setNegativeButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), RegVisitSeActivity.class);
                        intent.putExtra(Strings.ExtraServiceCall, serviceCall);
                        intent.putExtra(Strings.ExtraString, 0);
                        if (getActivity() != null)
                            getActivity().startActivity(intent);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}