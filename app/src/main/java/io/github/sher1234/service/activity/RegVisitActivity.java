package io.github.sher1234.service.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.RegisteredCall;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.model.base.VisitedCall;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.model.response.ServiceCall;
import io.github.sher1234.service.service.LocationTrack;
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

@SuppressWarnings("All")
public class RegVisitActivity extends AppCompatActivity implements OnFormElementValueChangedListener,
        View.OnClickListener {

    private LocationTrack locationTrack;
    private FormBuilder mFormBuilder;
    private RegVisitTask task;

    private ServiceCall serviceCall;
    private String email;
    private Date date;

    private MaterialButton button1;
    private MaterialButton button2;
    private View mProgressView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regvisit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences(Strings.UserPreferences, MODE_PRIVATE);
        serviceCall = (ServiceCall) getIntent().getSerializableExtra(Strings.ExtraServiceCall);
        email = preferences.getString(Strings.Email, null);

        button2 = findViewById(R.id.button3);
        button1 = findViewById(R.id.button1);
        textView = findViewById(R.id.textView);
        mProgressView = findViewById(R.id.progressView);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, mRecyclerView, this);
        onValidateSaveButton();
        if (serviceCall.getVisits() != null && serviceCall.getVisits().size() > 0) {
            int i = serviceCall.getVisits().size() - 1;
            onCreateForm(serviceCall.getRegistration(), serviceCall.getVisits().get(i));
        } else
            onCreateForm(serviceCall.getRegistration(), null);
    }

    private void onValidateSaveButton() {
        if (date != null && email.contains("@") && getFormValue(486911).length() > 5 &&
                getFormValue(486903).length() > 2 && getFormValue(486904).length() == 10 &&
                getFormValue(486912).length() > 5 && getFormValue(486907).length() > 2 &&
                getFormValue(486906).length() > 2 && getFormValue(486905).contains("@") &&
                !getFormValue(486909).isEmpty() && mFormBuilder.isValidForm()) {
            button2.setEnabled(true);
            button2.setTextColor(getResources().getColor(R.color.colorAccent));
            button2.setStrokeColorResource(R.color.colorAccent);
        } else {
            button2.setEnabled(false);
            button2.setTextColor(getResources().getColor(R.color.colorHeaderBackground));
            button2.setStrokeColorResource(R.color.colorHeaderBackground);
        }
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

    @SuppressLint("SimpleDateFormat")
    private void onCreateForm(@NotNull RegisteredCall registration, @Nullable VisitedCall visit) {
        Calendar calendar = Calendar.getInstance();
        String s1, s2;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateFormatID);
            s2 = "MAV" + dateFormat.format(calendar.getTime());
            dateFormat = new SimpleDateFormat(Strings.DateFormatView);
            s1 = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            s1 = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            s2 = "MAV" + calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH) + 1) +
                    calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR_OF_DAY) +
                    calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) +
                    calendar.get(Calendar.MILLISECOND);
            e.printStackTrace();
        }
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
                .setValue(registration.getCallNumber()).setRequired(true)
                .setEnabled(false);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Visit Number*").setTag(486912)
                .setValue(s2).setRequired(true).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Start Time*")
                .setValue(s1).setRequired(true).setTag(486913).setEnabled(false);
        element4 = FormElementTextSingleLine.createInstance().setTitle("End Time*")
                .setValue(s1).setRequired(true).setTag(486914).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);
        formItems.add(element4);

        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name").setHint("Name")
                .setTag(486901).setValue(registration.getCustomerName())
                .setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details").setTag(486902)
                .setHint("Site Details").setValue(registration.getSiteDetails())
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
            element1.setValue(visit.getConcernName());
            element2.setValue(visit.getConcernPhone());
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
        element2 = FormElementTextSingleLine.createInstance().setTitle("Action Taken*")
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
        Toast.makeText(this, "RESUMED", Toast.LENGTH_SHORT).show();
        if (locationTrack != null)
            locationTrack.stopSelf();
        locationTrack = null;
        locationTrack = new LocationTrack(this);
        if (!locationTrack.isLocationAvailable()) {
            Toast.makeText(this, "Location Service Unavailable", Toast.LENGTH_SHORT).show();
            locationTrack.showSettingsAlert();
        }
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @NotNull
    private Visit getVisit() {
        boolean b = false;
        if (getFormValue(486908).equalsIgnoreCase("Completed"))
            b = true;
        int i = serviceCall.getRegistration().getNumberOfVisits() + 1;
        String location = getLocationString();
        if (location == null || getLocationString() == null)
            location = getLocationString();
        return new Visit(date, date, email, getFormValue(486911), getFormValue(486903),
                getFormValue(486904), b, i, "", getFormValue(486910), location,
                "", getFormValue(486912), getFormValue(486907),
                getFormValue(486906), getFormValue(486905), getFormValue(486909));
    }

    public String getFormValue(int i) {
        return mFormBuilder.getFormElement(i).getValue();
    }

    public void resetFormValue(int i) {
        mFormBuilder.getFormElement(i).setValue("");
    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        onValidateSaveButton();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Choose Application"), 4869);
                break;

            case R.id.button2:
                if (serviceCall.getVisits() == null || serviceCall.getVisits().size() == 0) {
                    onFormReset(serviceCall.getRegistration(), null);
                } else {
                    int i = serviceCall.getVisits().size() - 1;
                    onFormReset(serviceCall.getRegistration(), serviceCall.getVisits().get(i));
                }
                break;

            case R.id.button3:
                if (task != null)
                    task.cancel(true);
                task = new RegVisitTask(getVisit());
                task.execute();
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void onFormReset(RegisteredCall registration, VisitedCall visit) {
        Calendar calendar = Calendar.getInstance();
        String s1, s2;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateFormatID);
            s2 = "MAV" + dateFormat.format(calendar.getTime());
            dateFormat = new SimpleDateFormat(Strings.DateFormatView);
            s1 = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            s1 = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            s2 = "MAV" + calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH) + 1) +
                    calendar.get(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.HOUR_OF_DAY) +
                    calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) +
                    calendar.get(Calendar.MILLISECOND);
            e.printStackTrace();
        }
        date = calendar.getTime();
        for (int i = 486901; i <= 486914; i++)
            resetFormValue(i);
        mFormBuilder.getFormElement(486901).setValue(registration.getCustomerName());
        mFormBuilder.getFormElement(486902).setValue(registration.getSiteDetails());
        mFormBuilder.getFormElement(486911).setValue(registration.getCallNumber());
        mFormBuilder.getFormElement(486912).setValue(s2);
        mFormBuilder.getFormElement(486913).setValue(s1);
        mFormBuilder.getFormElement(486914).setValue(s1);
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
        if (visit == null)
            return;
        mFormBuilder.getFormElement(486903).setValue(visit.getConcernName());
        mFormBuilder.getFormElement(486904).setValue(visit.getConcernPhone());
        mFormBuilder.getFormElement(486906).setValue(visit.getObservation());
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class RegVisitTask extends AsyncTask<Void, Void, Boolean> {

        private Responded responded;
        private int i = 4869;
        private Visit visit;

        RegVisitTask(@NotNull Visit visit) {
            this.visit = visit;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api queryApi = retrofit.create(Api.class);
            Call<Responded> call = queryApi.RegisterVisit(visit.getVisitMap());
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null && response.body().getResponse() != null) {
                        Log.e("r/m/e", response.message() +
                                "\n" + response.toString() + "\n" +
                                response.code() + "\n" + response.body().getResponse().getMessage());
                        responded = response.body();
                        i = responded.getResponse().getCode();
                    } else {
                        responded = null;
                        i = -1;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Responded> call, @NonNull Throwable t) {
                    responded = null;
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = -1;
                    responded = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if (responded != null) {
                Toast.makeText(AppController.getInstance(), responded.getResponse().getMessage(), Toast.LENGTH_LONG).show();
                if (responded.getResponse().getCode() == 1)
                    RegVisitActivity.this.finish();
            } else {
                Toast.makeText(AppController.getInstance(), "Unknown Error, Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}