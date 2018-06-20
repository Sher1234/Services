package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.RegisteredCall;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextMultiLine;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import io.github.sher1234.service.util.formBuilder.model.FormHeader;

public class Call1 extends Fragment {
    private static final String TAG_ARGS = "SERIALIZABLE-PAGER-CALL";

    private ServiceCall serviceCall;
    private FormBuilder mFormBuilder;

    public Call1() {
    }

    public static Call1 getInstance(ServiceCall serviceCall) {
        Call1 call1 = new Call1();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_ARGS, serviceCall);
        call1.setArguments(bundle);
        return call1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        serviceCall = (ServiceCall) getArguments().getSerializable(TAG_ARGS);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_1, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(getContext(), recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreateForm();
        setRegistration(serviceCall.getRegistration());
    }

    private void onCreateForm() {
        List<BaseFormElement> formItems = new ArrayList<>();

        BaseFormElement element0, element1, element2, element3;

        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Name")
                .setHint("Name").setTag(486901).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details")
                .setHint("Detail").setTag(486902).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Concern Person
        element0 = FormHeader.createInstance("Concern Person");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Name")
                .setHint("Name").setTag(486903).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Mobile Number")
                .setHint("Mobile").setTag(486904).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Product Details
        element0 = FormHeader.createInstance("Product Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Detail/Info")
                .setHint("Detail").setTag(486905).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Serial Number")
                .setHint("SNo").setTag(486906).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Complaint Details
        element0 = FormHeader.createInstance("Complaint Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Nature of Site")
                .setHint("Site's Nature").setTag(486907).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Type of Complaint")
                .setHint("Type").setTag(486908).setEnabled(false);
        element3 = FormElementTextMultiLine.createInstance().setTitle("Warranty")
                .setHint("Warranty").setTag(486909).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Call Number")
                .setHint("Call Number").setTag(486910).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Date/Time")
                .setHint("Date/Time").setTag(486911).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        element0 = FormHeader.createInstance("Registering Person");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name").setTag(486912)
                .setRequired(true).setEnabled(false).setHint("Name");
        element2 = FormElementTextSingleLine.createInstance().setTitle("Mobile Number")
                .setHint("Mobile").setRequired(true).setTag(486913).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Email")
                .setHint("Email").setRequired(true).setTag(486914).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        mFormBuilder.addFormElements(formItems);
    }

    public void setFormValue(int i, String s) {
        mFormBuilder.getFormElement(i).setValue(s);
    }

    private void setRegistration(RegisteredCall registeredCall) {
        if (registeredCall == null)
            return;
        setFormValue(486901, registeredCall.getCustomerName());
        setFormValue(486902, registeredCall.getSiteDetails());

        setFormValue(486903, registeredCall.getConcernName());
        setFormValue(486904, registeredCall.getConcernPhone());

        setFormValue(486905, registeredCall.getProductDetail());
        setFormValue(486906, registeredCall.getProductNumber());
        setFormValue(486907, registeredCall.getNatureOfSite());
        setFormValue(486908, registeredCall.getComplaintType());
        setFormValue(486909, registeredCall.getWarrantyStatus());
        setFormValue(486910, registeredCall.getCallNumber());
        setFormValue(486911, registeredCall.getDateTimeView());
        setFormValue(486912, registeredCall.getName());
        setFormValue(486913, registeredCall.getPhone());
        setFormValue(486914, registeredCall.getEmail());
    }
}