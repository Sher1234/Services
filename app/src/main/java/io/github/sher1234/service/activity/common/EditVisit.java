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
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.listener.OnFormElementValueChangedListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementPickerSingle;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextEmail;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextMultiLine;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextPhone;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import io.github.sher1234.service.util.formBuilder.model.FormHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditVisit extends AppCompatActivity implements OnFormElementValueChangedListener,
        View.OnClickListener {

    private final Functions functions = new Functions();
    private FormBuilder mFormBuilder;
    private MaterialButton button;
    private EditVisitTask task;
    private View progressView;
    private Visit visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_call);

        visit = (Visit) getIntent().getSerializableExtra(Strings.ExtraData);
        if (visit == null || visit.VisitNumber == null
                || visit.VisitNumber.isEmpty()) {
            Toast.makeText(this, "Invalid Visit..", Toast.LENGTH_SHORT).show();
            setResult(404);
            finish();
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, recyclerView, this);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        TextView textView = findViewById(R.id.textView);
        progressView = findViewById(R.id.progressView);
        textView.setText(R.string.edit_visit);
        button = findViewById(R.id.button2);
        button.setOnClickListener(this);
        button.setText(R.string.save);
        onCreateForm(visit);
        onValueChanged(null);
        setResult(101);
    }

    @NotNull
    private Visit getVisit() {
        return new Visit(visit.VisitNumber, getFormValue(486905), getFormValue(486906),
                getFormValue(486907), getFormValue(486908), getFormValue(486909),
                getFormValue(486910), getFormValue(486911));
    }

    private String getFormValue(int i) {
        return mFormBuilder.getFormElement(i).getValue();
    }

    private void setFormValue(int i, String s) {
        mFormBuilder.getFormElement(i).setValue(s);
    }

    @Override
    public void onValueChanged(BaseFormElement ble) {
        button.setEnabled(getFormValue(486901).length() > 5 &&
                getFormValue(486902).length() > 5 && getFormValue(486909).length() > 2 &&
                !getFormValue(486910).isEmpty() && getFormValue(486903).length() > 2 &&
                getFormValue(486904).length() > 2 && getFormValue(486907).contains("@") &&
                getFormValue(486908).length() > 2 && getFormValue(486905).length() > 2 &&
                getFormValue(486906).length() == 10 && mFormBuilder.isValidForm());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView1:
                onBackPressed();
                break;

            case R.id.button1:
                onFormReset(visit);
                break;

            case R.id.button2:
                if (task != null)
                    task.cancel(true);
                task = new EditVisitTask(getVisit());
                task.execute();
                break;
        }
    }

    private void onCreateForm(@NotNull Visit visit) {
        List<BaseFormElement> formItems = new ArrayList<>();

        List<String> op2 = new ArrayList<>();
        op2.add("Yes");
        op2.add("No");

        BaseFormElement element0, element1, element2, element3, element4;

        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Call Number*")
                .setTag(486901).setRequired(true).setEnabled(false);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Visit Number*")
                .setTag(486902).setRequired(true).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Start Time*")
                .setTag(486903).setRequired(true).setEnabled(false);
        element4 = FormElementTextSingleLine.createInstance().setTitle("End Time*")
                .setTag(486904).setRequired(true).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);
        formItems.add(element4);

        // Concern Person
        element0 = FormHeader.createInstance("Concern Person");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name*").setHint("Enter Name")
                .setRequired(true).setTag(486905);
        element2 = FormElementTextPhone.createInstance().setTitle("Mobile Number*").setHint("Enter Mobile")
                .setRequired(true).setTag(486906);
        element3 = FormElementTextEmail.createInstance().setTitle("e-Mail ID*").setHint("Enter Email")
                .setRequired(true).setTag(486907);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        // Product Details
        element0 = FormHeader.createInstance("Visit Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Observation*")
                .setHint("Enter Observation").setRequired(true).setTag(486908);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Action Taken*")
                .setHint("Enter Action Taken").setRequired(true).setTag(486909);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Complaint Details
        element0 = FormHeader.createInstance("Other Details");
        element2 = FormElementPickerSingle.createInstance().setTitle("Customer Satisfaction*")
                .setOptions(op2).setHint("Select One").setRequired(true).setTag(486910);
        element3 = FormElementTextMultiLine.createInstance().setTitle("Customer Feedback")
                .setHint("Enter Feedback").setRequired(false).setTag(486911);
        formItems.add(element0);
        formItems.add(element2);
        formItems.add(element3);

        mFormBuilder.addFormElements(formItems);

        onFormReset(visit);
    }

    private void onFormReset(@NotNull Visit visit) {
        setFormValue(486901, visit.CallNumber);
        setFormValue(486902, visit.VisitNumber);
        setFormValue(486903, visit.getStartTimeView());
        setFormValue(486904, visit.getEndTimeView());
        setFormValue(486905, visit.ConcernName);
        setFormValue(486906, visit.ConcernPhone);
        setFormValue(486907, visit.ConcernEmail);
        setFormValue(486908, visit.Observation);
        setFormValue(486909, visit.ActionTaken);
        setFormValue(486910, visit.CustomerSatisfaction);
        setFormValue(486911, visit.Feedback);
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class EditVisitTask extends AsyncTask<Void, Void, Boolean> {

        private final Visit visit;
        private Responded responded;
        private int i = 4869;

        EditVisitTask(@NotNull Visit visit) {
            this.visit = visit;
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
            Call<Responded> call = api.EditVisit(visit.getEditMap());
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
                Toast.makeText(EditVisit.this, responded.Message, Toast.LENGTH_LONG).show();
                if (responded.Code == 1) {
                    setResult(200);
                    finish();
                }
            } else if (i == 306)
                Toast.makeText(EditVisit.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(EditVisit.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(EditVisit.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}