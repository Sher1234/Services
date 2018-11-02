package io.github.sher1234.service.functions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.model.base.Visits;
import io.github.sher1234.service.model.response.Services;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.form.FormBuilder;
import io.github.sher1234.service.util.form.model.FormDivider;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.model.FormElementDatePicker;
import io.github.sher1234.service.util.form.model.FormElementMultiLine;
import io.github.sher1234.service.util.form.model.FormElementRating;
import io.github.sher1234.service.util.form.model.FormElementSingleLine;
import io.github.sher1234.service.util.form.model.FormHeader;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskShow {

    private final Common common;
    private Task task;

    private ShowCall showCall;
    private ShowVisit showVisit;

    public TaskShow() {
        common = new Common();
    }

    public void onCreateCallForm(Context context, RecyclerView recyclerView) {
        FormBuilder formBuilder = new FormBuilder(context, recyclerView);
        showCall = new ShowCall(formBuilder);
        showCall.onCreateForm();
    }

    public void onShowCall(Call c, User r, User a) {
        showCall.onSetForm(c, r, a);
    }

    public void onCreateVisitForm(Context context, RecyclerView recyclerView) {
        FormBuilder formBuilder = new FormBuilder(context, recyclerView);
        showVisit = new ShowVisit(formBuilder);
    }

    public void onShowVisit(Call c, Visits v) {
        if (v.visit.isIncomplete()) {
            showVisit.onCreateStart();
            showVisit.onSetStart(c, v.visit, v.user);
        } else {
            showVisit.onCreateFull();
            showVisit.onSetFull(c, v.visit, v.user);
        }
    }

    public void onRefreshCall(TaskUpdate taskUpdate, String s) {
        if (task != null)
            task.cancel(true);
        task = new Task(taskUpdate, s);
        task.execute();
    }

    public void onNetworkError(Context context, final TaskUpdate taskUpdate) {
        MaterialDialog dialog = MaterialDialog.Dialog(context);
        dialog.setTitle("Network issue")
                .setDescription(R.string.offline_access)
                .positiveButton("Retry", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        taskUpdate.fetch();
                        dialog.dismiss();
                    }
                })
                .negativeButton("Cancel", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    public interface TaskUpdate {
        void onFetched(Services services, int i);

        void onFetch();

        void fetch();
    }

    @SuppressLint("StaticFieldLeak")
    private class Task extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final String s;
        private Services services;
        private int i = 4869;

        Task(TaskUpdate taskUpdate, String s) {
            this.taskUpdate = taskUpdate;
            this.s = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskUpdate.onFetch();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Retrofit retrofit = AppController.getInstance().getRetrofitRequest(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            retrofit2.Call<Services> call = api.getFullCall(s);
            call.enqueue(new Callback<Services>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<Services> call, @NonNull Response<Services> response) {
                    if (response.body() != null) {
                        services = response.body();
                        i = services.code;
                    } else {
                        services = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<Services> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    services = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    services = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            taskUpdate.onFetched(services, i);
        }
    }

    private class ShowVisit {
        private final FormBuilder builder;

        private ShowVisit(FormBuilder builder) {
            this.builder = builder;
        }

        private void onCreateFull() {
            List<FormElement> items = new ArrayList<>();
            items.add(FormHeader.createInstance("Customer Details", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(1).setTitle("Name").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(2).setTitle("Site Details").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Concern Person", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(3).setTitle("Name").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(4).setTitle("Mobile Number").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(5).setTitle("e-Mail ID").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Observation & Action", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(6).setTitle("Observation").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(7).setTitle("Action Taken").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Other Details", false));
            items.add(FormElementRating.createInstance(false).setTag(8).setTitle("Customer Rating").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(9).setTitle("Customer Feedback").setHint("Feedback").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Visit Details", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(10).setTitle("Call ID").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(11).setTitle("Visit ID").setEnabled(false));
            items.add(FormElementDatePicker.createInstance(false).setTag(12).setDateFormat(Strings.DateTimeView).setTitle("Start Time").setEnabled(false));
            items.add(FormElementDatePicker.createInstance(false).setTag(13).setDateFormat(Strings.DateTimeView).setTitle("End Time").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Visit Engineer", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(14).setTitle("Name").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(15).setTitle("Mobile Number").setEnabled(false));
            items.add(FormElementSingleLine.createInstance(false).setTag(16).setTitle("Email").setEnabled(false));
            items.add(FormDivider.createInstance());
            builder.addFormElements(items);
        }

        private void onCreateStart() {
            List<FormElement> items = new ArrayList<>();
            items.add(FormHeader.createInstance("Customer Details", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(1).setTitle("Name").setRequired(true).setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(2).setTitle("Site Details").setRequired(true).setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Visit Details", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(3).setTitle("Call ID").setRequired(true).setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(4).setTitle("Visit ID").setRequired(true).setEnabled(false));
            items.add(FormElementDatePicker.createInstance(false).setTag(5).setTitle("Start Time").setDateFormat(Strings.DateTimeView).setRequired(true).setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Visit Person", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(6).setTitle("Name").setRequired(true).setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(7).setTitle("Mobile Number").setRequired(true).setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(8).setTitle("Email").setRequired(true).setEnabled(false));
            items.add(FormDivider.createInstance());
            builder.addFormElements(items);
        }

        private void onSetFull(Call call, Visit visit, User user) {
            if (visit == null) return;
            setFormValue(1, call.CustomerName);
            setFormValue(2, call.SiteDetails);
            setFormValue(3, visit.Name);
            setFormValue(4, visit.Phone);
            setFormValue(5, visit.Email);
            setFormValue(6, visit.Observation);
            setFormValue(7, visit.Action);
            common.setFormRating(builder, 8, visit.Satisfaction);
            setFormValue(9, visit.Feedback);
            setFormValue(10, call.CallID);
            setFormValue(11, visit.VisitID);
            common.setFormDate(builder, 12, visit.getStart());
            common.setFormDate(builder, 13, visit.getEnd());
            if (user == null) return;
            setFormValue(14, user.Name);
            setFormValue(15, user.Phone);
            setFormValue(16, user.Email);
        }

        private void onSetStart(Call call, Visit visit, User user) {
            if (visit == null) return;
            setFormValue(1, call.CustomerName);
            setFormValue(2, call.SiteDetails);
            setFormValue(3, visit.CallID);
            setFormValue(4, visit.VisitID);
            common.setFormDate(builder, 5, visit.getStart());
            if (user == null) return;
            setFormValue(6, user.Name);
            setFormValue(7, user.Phone);
            setFormValue(8, user.Email);
        }

        private void setFormValue(int tag, String s) {
            common.setFormValue(builder, tag, s);
        }
    }

    private class ShowCall {
        private final FormBuilder builder;

        private ShowCall(FormBuilder builder) {
            this.builder = builder;
        }

        private void onCreateForm() {
            List<FormElement> items = new ArrayList<>();
            items.add(FormHeader.createInstance("Customer Details", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(1).setTitle("Name").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(2).setTitle("Site Details").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Concern Person", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(3).setTitle("Name").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(4).setTitle("Mobile Number").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Product Details", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(5).setTitle("Detail/Info").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(6).setTitle("Serial Number").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Complaint Details", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(7).setTitle("Nature of Site").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(8).setTitle("Type of Complaint").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(9).setTitle("Warranty").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Call Details", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(10).setTitle("Call ID").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(11).setTitle("Date/Time").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Allotted To", false));
            items.add(FormElementMultiLine.createInstance(false).setTag(12).setTitle("Name").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(13).setTitle("Mobile Number").setEnabled(false));
            items.add(FormElementMultiLine.createInstance(false).setTag(14).setTitle("Email").setEnabled(false));
            items.add(FormDivider.createInstance());
            items.add(FormHeader.createInstance("Registered By", false));
            items.add(FormElementSingleLine.createInstance(false).setTag(15).setTitle("Name").setEnabled(false));
            items.add(FormElementSingleLine.createInstance(false).setTag(16).setTitle("Mobile Number").setEnabled(false));
            items.add(FormElementSingleLine.createInstance(false).setTag(17).setTitle("Email").setEnabled(false));
            items.add(FormDivider.createInstance());
            builder.addFormElements(items);
        }

        private void onSetForm(Call call, User userR, User userA) {
            if (call == null) return;
            setFormValue(1, call.CustomerName);
            setFormValue(2, call.SiteDetails);
            setFormValue(3, call.ConcernName);
            setFormValue(4, call.ConcernPhone);
            setFormValue(5, call.ProductDetail);
            setFormValue(6, call.ProductNumber);
            setFormValue(7, call.NatureOfSite);
            setFormValue(8, call.ComplaintType);
            setFormValue(9, call.Warranty);
            setFormValue(10, call.CallID);
            setFormValue(11, call.getDateTimeView());
            boolean b = userA != null;
            setFormValue(12, b ? userA.Name : null);
            setFormValue(13, b ? userA.Phone : null);
            setFormValue(14, b ? userA.Email : null);
            b = userR != null;
            setFormValue(15, b ? userR.Name : null);
            setFormValue(16, b ? userR.Phone : null);
            setFormValue(17, b ? userR.Email : null);
        }

        private void setFormValue(int tag, String s) {
            common.setFormValue(builder, tag, s);
        }
    }
}