package io.github.sher1234.service.activity.common;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Registration;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.service.LocationTrack;
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

public class RegisterVisit extends AppCompatActivity implements OnFormElementValueChangedListener,
        View.OnClickListener {

    private final Functions functions = new Functions();
    private LocationTrack locationTrack;
    private Registration registration;
    private FormBuilder mFormBuilder;
    private MaterialButton button;
    private View progressView;
    private RegVisitTask task;
    private Visit visit;
    private Date date;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_visit);

        user = AppController.getUserFromPrefs();

        registration = (Registration) getIntent().getSerializableExtra(Strings.ExtraData1);
        visit = (Visit) getIntent().getSerializableExtra(Strings.ExtraData2);
        setResult(101);

        button = findViewById(R.id.button2);
        progressView = findViewById(R.id.progressView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, recyclerView, this);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        button.setOnClickListener(this);
        onCreateForm(registration, visit);
        onValueChanged(null);
    }

    @Nullable
    private String getLocationString() {
        if (locationTrack.isLocationAvailable()) {
            Location location = locationTrack.getGpsLocation();
            if (location == null) {
                if ((location = locationTrack.getNetworkLocation()) != null)
                    return location.getLatitude() + "-#-" + location.getLongitude();
                else
                    return null;
            } else
                return location.getLatitude() + "-#-" + location.getLongitude();
        } else {
            locationTrack.showSettingsAlert();
            return null;
        }
    }

    private void onCreateForm(@NotNull Registration registration, @Nullable Visit visit) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateID, Locale.US);
        String id = "MAV" + dateFormat.format(calendar.getTime());
        dateFormat = new SimpleDateFormat(Strings.DateTimeView, Locale.US);
        String sd = dateFormat.format(calendar.getTime());
        date = calendar.getTime();

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
        element1 = FormElementTextSingleLine.createInstance().setTitle("Call Number*").setTag(486911)
                .setValue(registration.CallNumber).setRequired(true)
                .setEnabled(false);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Visit Number*").setTag(486912)
                .setValue(id).setRequired(true).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Start Time*")
                .setValue(sd).setRequired(true).setTag(486913).setEnabled(false);
        element4 = FormElementTextSingleLine.createInstance().setTitle("End Time*")
                .setValue(sd).setRequired(true).setTag(486914).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);
        formItems.add(element4);

        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name").setHint("Name")
                .setTag(486901).setValue(registration.CustomerName)
                .setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details").setTag(486902)
                .setHint("Site Details").setValue(registration.SiteDetails)
                .setEnabled(false);
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
        if (visit != null) {
            element1.setValue(visit.ConcernName);
            element2.setValue(visit.ConcernPhone);
        } else {
            element1.setValue(registration.ConcernName);
            element2.setValue(registration.ConcernPhone);
        }
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);

        // Product Details
        element0 = FormHeader.createInstance("Visit Details");
        element1 = FormElementTextMultiLine.createInstance().setTitle("Observation*")
                .setHint("Enter Observation").setRequired(true).setTag(486906);
        if (visit != null)
            element1.setValue(visit.getObservation());
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationTrack != null)
            locationTrack.stopSelf();
        locationTrack = null;
        locationTrack = new LocationTrack(this);
        if (!locationTrack.isLocationAvailable()) {
            Toast.makeText(this, "Location Service Unavailable", Toast.LENGTH_SHORT).show();
            locationTrack.showSettingsAlert();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @NotNull
    private Visit getVisit() {
        boolean b = false;
        if (getFormValue(486908).equalsIgnoreCase("Completed"))
            b = true;
        int i = registration.NumberOfVisits + 1;
        String location = getLocationString();
        if (location == null || getLocationString() == null)
            location = getLocationString();
        return new Visit(date, date, user.EmployeeID, getFormValue(486911), getFormValue(486903),
                getFormValue(486904), b, i, "", getFormValue(486910), location,
                "", getFormValue(486912), getFormValue(486907),
                getFormValue(486906), getFormValue(486905), getFormValue(486909));
    }

    private String getFormValue(int i) {
        return mFormBuilder.getFormElement(i).getValue();
    }

    private void resetFormValue(int i) {
        mFormBuilder.getFormElement(i).setValue("");
    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        if (date != null && !user.EmployeeID.isEmpty() && getFormValue(486911).length() > 5 &&
                getFormValue(486903).length() > 2 && getFormValue(486904).length() == 10 &&
                getFormValue(486912).length() > 5 && getFormValue(486907).length() > 2 &&
                getFormValue(486906).length() > 2 && getFormValue(486905).contains("@") &&
                !getFormValue(486909).isEmpty() && mFormBuilder.isValidForm())
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
                onFormReset(registration, visit);
                break;

            case R.id.button2:
                if (task != null)
                    task.cancel(true);
                task = new RegVisitTask(getVisit());
                task.execute();
                break;
        }
    }

    private void onFormReset(@NotNull Registration registration, @Nullable Visit visit) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateID, Locale.US);
        String id = "MAV" + dateFormat.format(calendar.getTime());
        dateFormat = new SimpleDateFormat(Strings.DateTimeView, Locale.US);
        String sd = dateFormat.format(calendar.getTime());
        date = calendar.getTime();
        for (int i = 486901; i <= 486914; i++)
            resetFormValue(i);
        mFormBuilder.getFormElement(486901).setValue(registration.CustomerName);
        mFormBuilder.getFormElement(486902).setValue(registration.SiteDetails);
        mFormBuilder.getFormElement(486911).setValue(registration.CallNumber);
        mFormBuilder.getFormElement(486903).setValue(registration.ConcernName);
        mFormBuilder.getFormElement(486904).setValue(registration.ConcernPhone);
        mFormBuilder.getFormElement(486912).setValue(id);
        mFormBuilder.getFormElement(486913).setValue(sd);
        mFormBuilder.getFormElement(486914).setValue(sd);
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
        if (visit == null)
            return;
        mFormBuilder.getFormElement(486903).setValue(visit.ConcernName);
        mFormBuilder.getFormElement(486904).setValue(visit.ConcernPhone);
        mFormBuilder.getFormElement(486906).setValue(visit.getObservation());
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class RegVisitTask extends AsyncTask<Void, Void, Boolean> {

        private final Visit visit;
        private Responded responded;
        private int i = 4869;

        RegVisitTask(@NotNull Visit visit) {
            this.visit = visit;
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
            Call<Responded> call = api.RegisterVisit(visit.getMap());
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
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            functions.showProgress(false, progressView);
            if (responded != null) {
                setResult(200);
                Toast.makeText(RegisterVisit.this, responded.Message, Toast.LENGTH_LONG).show();
                if (responded.Code == 1)
                    finish();
            } else if (i == 306)
                Toast.makeText(RegisterVisit.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(RegisterVisit.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(RegisterVisit.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}