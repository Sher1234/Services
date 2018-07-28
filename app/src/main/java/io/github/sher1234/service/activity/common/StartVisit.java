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
import io.github.sher1234.service.service.LocationTrack;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.formBuilder.FormBuilder;
import io.github.sher1234.service.util.formBuilder.listener.OnFormElementValueChangedListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextMultiLine;
import io.github.sher1234.service.util.formBuilder.model.FormElementTextSingleLine;
import io.github.sher1234.service.util.formBuilder.model.FormHeader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StartVisit extends AppCompatActivity
        implements OnFormElementValueChangedListener, View.OnClickListener {

    private final Functions functions = new Functions();
    private LocationTrack locationTrack;
    private Registration registration;
    private FormBuilder mFormBuilder;
    private MaterialButton button;
    private View progressView;
    private StartTask task;
    private Date date;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_visit);
        user = AppController.getUserFromPrefs();

        registration = (Registration) getIntent().getSerializableExtra(Strings.ExtraData);
        setResult(101);

        button = findViewById(R.id.button2);
        progressView = findViewById(R.id.progressView);
        TextView textView = findViewById(R.id.textView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, recyclerView, this);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        button.setOnClickListener(this);
        button.setText(R.string.start_visit);
        textView.setText(R.string.start_visit);
        onCreateForm(registration);
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

    private void onCreateForm(@NotNull Registration registration) {
        List<BaseFormElement> formItems = new ArrayList<>();
        BaseFormElement element0, element1, element2, element3;
        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Call Number*")
                .setTag(486901).setRequired(true).setEnabled(false);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Visit Number*")
                .setTag(486902).setRequired(true).setEnabled(false);
        element3 = FormElementTextSingleLine.createInstance().setTitle("Start Time*")
                .setRequired(true).setTag(486903).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);
        formItems.add(element3);
        // Customer Details
        element0 = FormHeader.createInstance("Customer Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Name").setHint("Name")
                .setTag(486904).setEnabled(false);
        element2 = FormElementTextMultiLine.createInstance().setTitle("Site Details").setTag(486905)
                .setHint("Site Details").setEnabled(false);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (locationTrack != null)
            locationTrack.stopSelf();
        locationTrack = null;
        locationTrack = new LocationTrack(this);
        if (!locationTrack.isLocationAvailable()) {
            Toast.makeText(this, "Location service unavailable...", Toast.LENGTH_SHORT).show();
            locationTrack.showSettingsAlert();
        }
    }

    @NotNull
    private Map<String, String> getVisitStart() {
        int i = registration.NumberOfVisits + 1;
        String location = getLocationString();
        if (location == null || getLocationString() == null)
            location = getLocationString();
        return new Visit(getFormValue(486902), getFormValue(486901), i, date, user.EmployeeID,
                location).getStartMap();
    }

    private String getFormValue(int i) {
        return mFormBuilder.getFormElement(i).getValue();
    }

    private void resetFormValue(int i) {
        mFormBuilder.getFormElement(i).setValue("");
    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        button.setEnabled(mFormBuilder.isValidForm());
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
                task = null;
                task = new StartTask(getVisitStart());
                task.execute();
                break;
        }
    }

    private void onFormReset(Registration registration) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateID, Locale.US);
        String id = "MAV" + dateFormat.format(calendar.getTime());
        dateFormat = new SimpleDateFormat(Strings.DateTimeView, Locale.US);
        String sd = dateFormat.format(calendar.getTime());
        date = calendar.getTime();
        for (int i = 486901; i <= 486905; i++)
            resetFormValue(i);
        mFormBuilder.getFormElement(486904).setValue(registration.CustomerName);
        mFormBuilder.getFormElement(486905).setValue(registration.SiteDetails);
        mFormBuilder.getFormElement(486901).setValue(registration.CallNumber);
        mFormBuilder.getFormElement(486902).setValue(id);
        mFormBuilder.getFormElement(486903).setValue(sd);
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class StartTask extends AsyncTask<Void, Void, Boolean> {

        private final Map<String, String> map;
        private int i = 4869;
        private Responded responded;

        StartTask(@NotNull Map<String, String> map) {
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
            Call<Responded> call = api.StartVisit(map);
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
                    responded = null;
                    t.printStackTrace();
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
                Toast.makeText(StartVisit.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(StartVisit.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(StartVisit.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}