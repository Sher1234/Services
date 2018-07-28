package io.github.sher1234.service.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.common.EditVisit;
import io.github.sher1234.service.activity.common.EndVisit;
import io.github.sher1234.service.activity.common.RegisterVisit;
import io.github.sher1234.service.activity.common.Signature;
import io.github.sher1234.service.activity.common.StartVisit;
import io.github.sher1234.service.activity.common.ViewCall;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.model.base.VisitX;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextMultiLine;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import io.github.sher1234.service.util.formBuilder.model.FormHeader;

public class ViewVisit extends Fragment implements View.OnClickListener {

    private VisitX visitX;
    private String location;
    private FormBuilder formBuilder;
    private Registration registration;
    private LinearLayoutCompat layoutI;
    private LinearLayoutCompat layoutS;

    public ViewVisit() {

    }

    public static ViewVisit getInstance(Registration registration, VisitX visitX) {
        ViewVisit call1 = new ViewVisit();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, visitX);
        bundle.putSerializable(Strings.ExtraString, registration);
        call1.setArguments(bundle);
        return call1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        visitX = (VisitX) getArguments().getSerializable(Strings.ExtraData);
        registration = (Registration) getArguments().getSerializable(Strings.ExtraString);
        assert visitX != null && visitX.visit != null;
        location = visitX.visit.getLocation();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_visit, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        formBuilder = new FormBuilder(getContext(), recyclerView);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        layoutI = view.findViewById(R.id.layoutIncomplete);
        layoutS = view.findViewById(R.id.layoutSignature);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.view_visit, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        User user = AppController.getUserFromPrefs();
        if (user.isAdmin())
            menu.findItem(R.id.delete).setVisible(true).setEnabled(true);
        else
            menu.findItem(R.id.delete).setVisible(false).setEnabled(false);
        if (visitX.visit.isVisitStarted()) {
            menu.findItem(R.id.add).setVisible(false).setEnabled(false);
            menu.findItem(R.id.pdf).setVisible(false).setEnabled(false);
            menu.findItem(R.id.edit).setVisible(false).setEnabled(false);
        }
        if (registration.isCompleted())
            menu.findItem(R.id.add).setEnabled(false).setVisible(false);
        if (location == null || location.isEmpty())
            menu.findItem(R.id.map).setEnabled(false).setVisible(false);
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

            case R.id.map:
                String[] s = location.split("-#-");
                double lat = Double.parseDouble(s[0]);
                double lon = Double.parseDouble(s[1]);
                String q = "geo:" + lat + "," + lon + "?q=" + Uri.encode(lat + "," + lon +
                        "(Service Call)") + "&z=16";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(q)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Maps Unavailable", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;

            case R.id.pdf:
                String url = Api.BASE_URL + "pdf/visit/" + visitX.visit.VisitNumber;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;

            case R.id.edit:
                assert getActivity() != null;
                ((ViewCall) getActivity()).navigateWithData(EditVisit.class, visitX.visit, 4869);
                return true;

            case R.id.delete:
                onDeleteDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (visitX.visit.isVisitStarted()) {
            onCreateFormStarted();
            layoutI.setVisibility(View.VISIBLE);
            setVisitModelStarted(visitX.visit, visitX.visitBy);
        } else {
            if (visitX.visit.getSignature() == null || visitX.visit.getSignature().isEmpty())
                layoutS.setVisibility(View.VISIBLE);
            onCreateForm();
            setVisitModel(visitX.visit, visitX.visitBy);
        }
    }

    private void setVisitModelStarted(Visit visit, User user) {
        if (visit == null)
            return;
        setFormValue(486901, registration.CustomerName);
        setFormValue(486902, registration.SiteDetails);

        setFormValue(486903, visit.getCallNumber());
        setFormValue(486904, visit.getVisitNumber());
        setFormValue(486905, visit.getStartTimeView());

        setFormValue(486907, user.Name);
        setFormValue(486908, user.Phone);
        setFormValue(486909, user.Email);
    }

    private void onCreateFormStarted() {

        List<BaseFormElement> formItems = new ArrayList<>();
        BaseFormElement element0, element1, element2, element3;

        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Name").setTag(486901)
                .setRequired(true).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details")
                .setRequired(true).setTag(486902).setEnabled(false);
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

        formBuilder.addFormElements(formItems);
    }

    private void setVisitModel(Visit visit, User user) {
        if (visit == null)
            return;
        setFormValue(486901, registration.CustomerName);
        setFormValue(486902, registration.SiteDetails);

        setFormValue(486903, visit.getConcernName());
        setFormValue(486904, visit.getConcernPhone());
        setFormValue(486905, visit.getConcernEmail());

        setFormValue(486906, visit.getObservation());
        setFormValue(486907, visit.getActionTaken());
        setFormValue(486908, registration.getIsCompletedString());
        setFormValue(486909, visit.getCustomerSatisfaction());
        setFormValue(486910, visit.getFeedback());

        setFormValue(486911, visit.getCallNumber());
        setFormValue(486912, visit.getStartTimeView());
        setFormValue(486913, visit.getEndTimeView());
        setFormValue(486914, user.Name);
        setFormValue(486915, user.Phone);
        setFormValue(486916, user.Email);
        setFormValue(486917, visit.getVisitNumber());
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

        formBuilder.addFormElements(formItems);
    }

    private void setFormValue(int i, String s) {
        formBuilder.getFormElement(i).setValue(s);
    }

    private void onDialogAddVisit() {
        assert getContext() != null;
        final MaterialDialog materialDialog = new MaterialDialog(getContext());
        materialDialog.setTitle("Add Visit").setCancelable(true);
        materialDialog.setDescription("Register a service for call number " + visitX.visit.getCallNumber());
        materialDialog.positiveButton("Full", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterVisit.class);
                intent.putExtra(Strings.ExtraData1, registration);
                intent.putExtra(Strings.ExtraData2, visitX.visit);
                startActivityForResult(intent, 1001);
                materialDialog.dismiss();
            }
        }).negativeButton("Start", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartVisit.class);
                intent.putExtra(Strings.ExtraData, registration);
                startActivityForResult(intent, 1002);
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
            intent.putExtra(Strings.ExtraData1, registration);
            intent.putExtra(Strings.ExtraData2, visitX.visit);
            startActivityForResult(intent, 1003);
        } else if (view.getId() == R.id.button2) {
            assert getActivity() != null;
            ((ViewCall) getActivity()).navigateWithData(Signature.class, visitX.visit.VisitNumber, 9684);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 || requestCode == 1002 || requestCode == 1003) {
            if (resultCode == 101)
                Toast.makeText(getContext(), "No changes...", Toast.LENGTH_SHORT).show();
            else if (resultCode == 200)
                Toast.makeText(getContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
        }
    }

    private void onDeleteDialog() {
        assert getActivity() != null;
        final MaterialDialog materialDialog = new MaterialDialog(getActivity());
        materialDialog.setDescription(R.string.delete_visit_text).setTitle(R.string.delete_visit);
        materialDialog.positiveButton(R.string.delete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewCall) getActivity()).onDelete(visitX.visit.getVisitNumber(), false);
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