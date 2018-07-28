package io.github.sher1234.service.fragment;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.common.EditCall;
import io.github.sher1234.service.activity.common.ViewCall;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextMultiLine;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import io.github.sher1234.service.util.formBuilder.model.FormHeader;

public class ViewReg extends Fragment {

    private ServiceCall call;
    private AllotCall allotCall;
    private FormBuilder mFormBuilder;

    public ViewReg() {
    }

    public static ViewReg getInstance(ServiceCall serviceCall) {
        ViewReg viewReg = new ViewReg();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, serviceCall);
        viewReg.setArguments(bundle);
        return viewReg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        call = (ServiceCall) getArguments().getSerializable(Strings.ExtraData);
        allotCall = AllotCall.newInstance(call != null ? call.registration : null);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(getContext(), recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreateViewLayout();
        if (call != null)
            setRegistration(call.registration, call.registeredBy, call.allottedTo);
        else
            Toast.makeText(getContext(), "Invalid call...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.view_regs, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        User user = AppController.getUserFromPrefs();
        if (user.isAdmin()) {
            if (call.registration.isCompleted())
                menu.findItem(R.id.allot).setVisible(false).setEnabled(false);
        } else {
            if (call.registration.isCompleted())
                menu.findItem(R.id.edit).setVisible(false).setEnabled(false);
            menu.findItem(R.id.allot).setVisible(false).setEnabled(false);
            menu.findItem(R.id.delete).setVisible(false).setEnabled(false);
        }
    }

    private void onCreateViewLayout() {
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

        element0 = FormHeader.createInstance("Allotted To");
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

        element0 = FormHeader.createInstance("Registered By");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name").setTag(486915)
                .setRequired(true).setEnabled(false).setHint("Name");
        element2 = FormElementTextSingleLine.createInstance().setTitle("Mobile Number")
                .setHint("Mobile").setRequired(true).setTag(486916).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Email")
                .setHint("Email").setRequired(true).setTag(486917).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        mFormBuilder.addFormElements(formItems);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                assert getActivity() != null;
                ((ViewCall) getActivity()).onTapRefresh();
                return true;

            case R.id.allot:
                assert getFragmentManager() != null;
                allotCall.showNow(getFragmentManager(), Strings.TAG + "A");
                return true;

            case R.id.edit:
                assert getActivity() != null;
                ((ViewCall) getActivity()).navigateWithData(EditCall.class, call.registration, 4869);
                return true;

            case R.id.delete:
                onDeleteDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setFormValue(int i, String s) {
        mFormBuilder.getFormElement(i).setValue(s);
    }

    private void setRegistration(Registration registration, User registeredBy, User allottedTo) {
        if (registration == null)
            return;
        setFormValue(486901, registration.getCustomerName());
        setFormValue(486902, registration.getSiteDetails());

        setFormValue(486903, registration.getConcernName());
        setFormValue(486904, registration.getConcernPhone());

        setFormValue(486905, registration.getProductDetail());
        setFormValue(486906, registration.getProductNumber());
        setFormValue(486907, registration.getNatureOfSite());
        setFormValue(486908, registration.getComplaintType());
        setFormValue(486909, registration.getWarrantyStatus());
        setFormValue(486910, registration.getCallNumber());
        setFormValue(486911, registration.getDateTimeView());
        if (allottedTo != null) {
            setFormValue(486912, allottedTo.Name);
            setFormValue(486913, allottedTo.Phone);
            setFormValue(486914, allottedTo.Email);
        } else {
            setFormValue(486912, "-");
            setFormValue(486913, "-");
            setFormValue(486914, "-");
        }
        if (registeredBy != null) {
            setFormValue(486915, registeredBy.Name);
            setFormValue(486916, registeredBy.Phone);
            setFormValue(486917, registeredBy.Email);
        } else {
            setFormValue(486915, "-");
            setFormValue(486916, "-");
            setFormValue(486917, "-");
        }
    }

    private void onDeleteDialog() {
        assert getActivity() != null;
        final MaterialDialog materialDialog = new MaterialDialog(getActivity());
        materialDialog.setDescription(R.string.delete_call_text).setTitle(R.string.delete_call);
        materialDialog.positiveButton(R.string.delete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getActivity() != null;
                ((ViewCall) getActivity()).onDelete(call.registration.CallNumber, true);
                materialDialog.dismiss();
            }
        }).negativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }
}