package io.github.sher1234.service.activity.common;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class RegisterCall extends AppCompatActivity
        implements OnFormElementValueChangedListener, View.OnClickListener {

    private final Functions functions = new Functions();
    private FormBuilder mFormBuilder;
    private MaterialButton button;
    private View progressView;
    private RegisterTask task;
    private User user;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_call);

        user = AppController.getUserFromPrefs();

        button = findViewById(R.id.button2);
        progressView = findViewById(R.id.progressView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, recyclerView, this);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        button.setOnClickListener(this);
        onCreateForm();
        onValueChanged(null);
    }

    private void onCreateForm() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateID, Locale.US);
        String id = "MAC" + dateFormat.format(calendar.getTime());
        dateFormat = new SimpleDateFormat(Strings.DateTimeView, Locale.US);
        String sd = dateFormat.format(calendar.getTime());
        date = calendar.getTime();

        List<BaseFormElement> formItems = new ArrayList<>();

        List<String> op1 = new ArrayList<>();
        op1.add("Complaint");
        op1.add("New Commissioning");

        List<String> op2 = new ArrayList<>();
        op2.add("Yes");
        op2.add("No");
        op2.add("To be checked");

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

        // Call Details
        element0 = FormHeader.createInstance("Call Details");
        element1 = FormElementTextSingleLine.createInstance().setTitle("Call Number")
                .setValue(id).setRequired(true).setTag(486910).setEnabled(false);
        element2 = FormElementTextSingleLine.createInstance().setTitle("Date/Time")
                .setValue(sd).setRequired(true).setTag(486911).setEnabled(false);
        formItems.add(element0);
        formItems.add(element1);
        formItems.add(element2);

        mFormBuilder.addFormElements(formItems);
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
        return new Registration(0, getFormValue(486902), getFormValue(486901),
                getFormValue(486907), getFormValue(486905), getFormValue(486906),
                getFormValue(486908), getFormValue(486909), date, user.EmployeeID,
                getFormValue(486910), getFormValue(486903), getFormValue(486904),
                false, null);
    }

    private String getFormValue(int i) {
        return mFormBuilder.getFormElement(i).getValue();
    }

    private void resetFormValue(int i) {
        mFormBuilder.getFormElement(i).setValue("");
    }

    @Override
    public void onValueChanged(BaseFormElement baseFormElement) {
        if (getFormValue(486902).length() > 2 && getFormValue(486901).length() > 2 &&
                getFormValue(486907).length() > 2 && getFormValue(486905).length() > 2 &&
                getFormValue(486908).length() > 2 && getFormValue(486909).length() > 1 &&
                date != null && !user.EmployeeID.isEmpty() && getFormValue(486910).length() > 5 &&
                getFormValue(486903).length() > 2 && getFormValue(486904).length() == 10 &&
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
                onFormReset();
                break;

            case R.id.button2:
                if (task != null)
                    task.cancel(true);
                task = new RegisterTask(getRegistration());
                task.execute();
                break;

        }
    }

    private void onFormReset() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateID, Locale.US);
        String id = "MAC" + dateFormat.format(calendar.getTime());
        dateFormat = new SimpleDateFormat(Strings.DateTimeView, Locale.US);
        String sd = dateFormat.format(calendar.getTime());
        date = calendar.getTime();
        for (int i = 486901; i <= 486911; i++)
            resetFormValue(i);
        mFormBuilder.getFormElement(486910).setValue(id);
        mFormBuilder.getFormElement(486911).setValue(sd);
        mFormBuilder.getFormAdapter().notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final Registration registration;
        private Responded responded;
        private int i = 4869;

        RegisterTask(@NotNull Registration registration) {
            this.registration = registration;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            functions.showProgress(true, progressView);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Responded> call = api.RegisterCall(registration.getMap());
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
                if (responded.Code == 1)
                    finish();
            } else if (i == 306)
                Toast.makeText(RegisterCall.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(RegisterCall.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(RegisterCall.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}