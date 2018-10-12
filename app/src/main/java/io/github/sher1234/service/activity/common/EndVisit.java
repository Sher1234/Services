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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.User;
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

public class EndVisit extends AppCompatActivity
        implements OnFormElementValueChangedListener, View.OnClickListener {

    private final Functions functions = new Functions();
    private Registration registration;
    private FormBuilder mFormBuilder;
    private MaterialButton button;
    private View progressView;
    private EndTask task;
    private Visit visit;
    private Date date;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_visit);

        setResult(101);
        user = AppController.getUserFromPrefs();
        visit = (Visit) getIntent().getSerializableExtra(Strings.ExtraData2);
        registration = (Registration) getIntent().getSerializableExtra(Strings.ExtraData1);

        button = findViewById(R.id.button2);
        progressView = findViewById(R.id.progressView);
        TextView textView = findViewById(R.id.textView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, recyclerView, this);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        button.setOnClickListener(this);
        button.setText(R.string.end_visit);
        textView.setText(R.string.end_visit);
        onCreateForm(registration, visit);
        onValueChanged(null);
    }

    private void onCreateForm(@NotNull Registration registration, @NotNull Visit visit) {
        List<BaseFormElement> formItems = new ArrayList<>();
        List<String> op1 = new ArrayList<>();
        op1.add("Completed");
        op1.add("Pending");
        List<String> op2 = new ArrayList<>();
        op2.add("Yes");
        op2.add("No");
        BaseFormElement element0, element1, element2, element3, element4;
        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Call Number*")
                .setTag(486911).setRequired(true).setEnabled(false);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Visit Number*")
                .setTag(486912).setRequired(true).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Start Time*")
                .setRequired(true).setTag(486913).setEnabled(false);
        element4 = FormElementTextSingleLine.createInstance().setTitle("End Time*")
                .setRequired(true).setTag(486914).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);
        formItems.add(element4);
        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name").setHint("Name")
                .setTag(486901).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details").setTag(486902)
                .setHint("Site Details").setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        // Concern Person
        element0 = FormHeader.createInstance("Concern Person");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name*").setHint("Enter Name")
                .setRequired(true).setTag(486903);
        element2 = FormElementTextPhone.createInstance().setTitle("Mobile Number*").setHint("Enter Mobile")
                .setRequired(true).setTag(486904);
        element3 = FormElementTextEmail.createInstance().setTitle("e-Mail ID*").setHint("Enter Email")
                .setRequired(true).setTag(486905);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        // Product Details
        element0 = FormHeader.createInstance("Visit Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Observation*")
                .setHint("Enter Observation").setRequired(true).setTag(486906);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Action Taken*")
                .setHint("Enter Action Taken").setRequired(true).setTag(486907);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        // Complaint Details
        element0 = FormHeader.createInstance("Other Details");
        element1 = FormElementPickerSingle.createInstance().setTitle("Complaint Status*")
                .setOptions(op1).setRequired(true).setHint("Select One").setTag(486908);
        element2 = FormElementPickerSingle.createInstance().setTitle("Customer Satisfaction*")
                .setOptions(op2).setHint("Select One").setRequired(true).setTag(486909);
        element3 = FormElementTextMultiLine.createInstance().setTitle("Customer Feedback")
                .setHint("Enter Feedback").setRequired(false).setTag(486910);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        mFormBuilder.addFormElements(formItems);
        onFormReset(registration, visit);
    }

    @NotNull
    private Map<String, String> getVisitEnd() {
        boolean b = false;
        if (getFormValue(486908).equalsIgnoreCase("Completed"))
            b = true;
        return new Visit(getFormValue(486912), getFormValue(486911), b, getFormValue(486907),
                getFormValue(486906), getFormValue(486903), getFormValue(486904),
                getFormValue(486905), getFormValue(486909), date, getFormValue(486910),
                "", "").getEndMap();
    }

    private String getFormValue(int i) {
        return mFormBuilder.getFormElement(i).getValue();
    }

    private void resetFormValue(int i) {
        mFormBuilder.getFormElement(i).setValue("");
    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        button.setEnabled(date != null && !user.EmployeeID.isEmpty() && getFormValue(486911).length() > 5 &&
                getFormValue(486903).length() > 2 && getFormValue(486904).length() == 10 &&
                getFormValue(486912).length() > 5 && getFormValue(486907).length() > 2 &&
                getFormValue(486906).length() > 2 && getFormValue(486905).contains("@") &&
                !getFormValue(486909).isEmpty() && mFormBuilder.isValidForm());
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
                onFormReset(registration, visit);
                break;

            case R.id.button2:
                if (task != null)
                    task.cancel(true);
                task = null;
                task = new EndTask(getVisitEnd());
                task.execute();
                break;
        }
    }

    private void onFormReset(@NotNull Registration registration, @NotNull Visit visit) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateTimeView, Locale.US);
        String ed = dateFormat.format(calendar.getTime());
        date = calendar.getTime();
        for (int i = 486901; i <= 486914; i++)
            resetFormValue(i);
        mFormBuilder.getFormElement(486901).setValue(registration.CustomerName);
        mFormBuilder.getFormElement(486902).setValue(registration.SiteDetails);
        mFormBuilder.getFormElement(486903).setValue(registration.ConcernName);
        mFormBuilder.getFormElement(486904).setValue(registration.ConcernPhone);
        mFormBuilder.getFormElement(486911).setValue(registration.CallNumber);
        mFormBuilder.getFormElement(486912).setValue(visit.getVisitNumber());
        mFormBuilder.getFormElement(486913).setValue(visit.getStartTimeView());
        mFormBuilder.getFormElement(486914).setValue(ed);
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class EndTask extends AsyncTask<Void, Void, Boolean> {

        private final Map<String, String> map;
        private int i = 4869;
        private Responded responded;

        EndTask(@NotNull Map<String, String> map) {
            this.map = map;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, progressView);
            setResult(200);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Responded> call = api.EndVisit(map);
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
                setResult(200);
                Toast.makeText(AppController.getInstance(), responded.Message, Toast.LENGTH_LONG).show();
                if (responded.Code == 1)
                    finish();
            } else if (i == 306)
                Toast.makeText(EndVisit.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(EndVisit.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(EndVisit.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}