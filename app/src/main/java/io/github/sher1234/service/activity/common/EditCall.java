package io.github.sher1234.service.activity.common;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.listener.OnFormElementValueChangedListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerSingle;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextMultiLine;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextPhone;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import io.github.sher1234.service.util.formBuilder.model.FormHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditCall extends AppCompatActivity
        implements OnFormElementValueChangedListener, View.OnClickListener {

    private final Functions functions = new Functions();
    private Registration registration;
    private FormBuilder mFormBuilder;
    private MaterialButton button;
    private View progressView;
    private EditCallTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_call);

        registration = (Registration) getIntent().getSerializableExtra(Strings.ExtraData);
        if (registration == null || registration.CallNumber == null
                || registration.CallNumber.isEmpty()) {
            Toast.makeText(this, "Invalid Call..", Toast.LENGTH_SHORT).show();
            setResult(404);
            finish();
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, recyclerView, this);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        TextView textView = findViewById(R.id.textView);
        progressView = findViewById(R.id.progressView);
        textView.setText(R.string.edit_call);
        button = findViewById(R.id.button2);
        button.setOnClickListener(this);
        button.setText(R.string.save);
        onCreateForm(registration);
        onValueChanged(null);
        setResult(101);
    }

    private void onCreateForm(@NotNull Registration registration) {
        List<BaseFormElement> formItems = new ArrayList<>();

        List<String> op1 = new ArrayList<>();
        op1.add("Complaint");
        op1.add("New Commissioning");

        List<String> op2 = new ArrayList<>();
        op2.add("Yes");
        op2.add("No");
        op2.add("To be checked");

        List<String> op3 = new ArrayList<>();
        op3.add("Completed");
        op3.add("Pending");

        BaseFormElement element0, element1, element2, element3;

        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name")
                .setHint("Enter Name").setRequired(true).setTag(486901);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details")
                .setHint("Enter Site Details").setRequired(true).setTag(486902);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Concern Person
        element0 = FormHeader.createInstance("Concern Person");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name")
                .setHint("Enter Name").setRequired(true).setTag(486903);
        element2 = FormElementTextPhone.createInstance().setTitle("Mobile Number")
                .setHint("Enter Mobile").setRequired(true).setTag(486904);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Product Details
        element0 = FormHeader.createInstance("Product Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Detail/Info")
                .setHint("Enter Detail").setRequired(true).setTag(486905);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Serial Number")
                .setHint("Enter SNo").setRequired(false).setTag(486906);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Complaint Details
        element0 = FormHeader.createInstance("Complaint Details");
        element1 = FormElementPickerSingle.createInstance().setTitle("Nature of Site")
                .setOptions(op1).setRequired(true).setHint("Select One").setTag(486907);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Type of Complaint")
                .setHint("Enter Type").setRequired(true).setTag(486908);
        element3 = FormElementPickerSingle.createInstance().setTitle("Warranty")
                .setOptions(op2).setHint("Select One").setRequired(true).setTag(486909);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        element0 = FormElementPickerSingle.createInstance().setTitle("Complaint Status")
                .setOptions(op3).setRequired(true).setHint("Select One").setTag(486910);
        formItems.add(element0);

        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Call Number")
                .setRequired(true).setTag(486911).setEnabled(false);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Date/Time")
                .setRequired(true).setTag(486912).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        mFormBuilder.addFormElements(formItems);

        onFormReset(registration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @NotNull
    private Registration getRegistration() {
        boolean b = false;
        if (getFormValue(486910).equalsIgnoreCase("Completed"))
            b = true;
        return new Registration(getFormValue(486901), getFormValue(486902),
                getFormValue(486903), getFormValue(486904), getFormValue(486905),
                getFormValue(486906), getFormValue(486907), getFormValue(486908),
                getFormValue(486909), b, registration.CallNumber);
    }

    private String getFormValue(int i) {
        return mFormBuilder.getFormElement(i).getValue();
    }

    private void setFormValue(int i, String s) {
        mFormBuilder.getFormElement(i).setValue(s);
    }

    @Override
    public void onValueChanged(BaseFormElement ble) {
        if (getFormValue(486902).length() > 2 && getFormValue(486904).length() == 10 &&
                getFormValue(486907).length() > 2 && getFormValue(486901).length() > 2 &&
                getFormValue(486908).length() > 2 && getFormValue(486909).length() > 1 &&
                getFormValue(486910).length() > 2 && getFormValue(486911).length() > 5 &&
                getFormValue(486903).length() > 2 && getFormValue(486905).length() > 2 &&
                mFormBuilder.isValidForm())
            button.setEnabled(true);
        else
            button.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView1:
                onBackPressed();
                break;

            case R.id.button1:
                onFormReset(registration);
                break;

            case R.id.button2:
                if (task != null)
                    task.cancel(true);
                task = new EditCallTask(getRegistration());
                task.execute();
                break;

        }
    }

    private void onFormReset(@NotNull Registration registration) {
        setFormValue(486901, registration.CustomerName);
        setFormValue(486902, registration.SiteDetails);
        setFormValue(486903, registration.ConcernName);
        setFormValue(486904, registration.ConcernPhone);
        setFormValue(486905, registration.ProductDetail);
        setFormValue(486906, registration.ProductNumber);
        setFormValue(486907, registration.NatureOfSite);
        setFormValue(486908, registration.ComplaintType);
        setFormValue(486909, registration.WarrantyStatus);
        setFormValue(486910, registration.getIsCompletedString());
        setFormValue(486911, registration.CallNumber);
        setFormValue(486912, registration.getDateTimeView());
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class EditCallTask extends AsyncTask<Void, Void, Boolean> {

        private final Registration registration;
        private Responded responded;
        private int i = 4869;

        EditCallTask(@NotNull Registration registration) {
            this.registration = registration;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setResult(200);
            functions.showProgress(true, progressView);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Responded> call = api.EditCall(registration.getEditMap());
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null) {
                        responded = response.body();
                        i = responded.Code;
                    } else {
                        responded = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Responded> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    responded = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    responded = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            functions.showProgress(false, progressView);
            if (responded != null) {
                Toast.makeText(AppController.getInstance(), responded.Message, Toast.LENGTH_SHORT).show();
                if (responded.Code == 1) {
                    setResult(200);
                    finish();
                }
            } else if (i == 306)
                Toast.makeText(EditCall.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(EditCall.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(EditCall.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}