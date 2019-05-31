package io.github.sher1234.service.functions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.ui.v1.d.AddCall;
import io.github.sher1234.service.ui.v1.d.EditCall;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.form.FormBuilder;
import io.github.sher1234.service.util.form.listener.OnFormElementValueChange;
import io.github.sher1234.service.util.form.model.FormDivider;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.model.FormElementDatePicker;
import io.github.sher1234.service.util.form.model.FormElementMultiLine;
import io.github.sher1234.service.util.form.model.FormElementPhone;
import io.github.sher1234.service.util.form.model.FormElementPicker;
import io.github.sher1234.service.util.form.model.FormElementSingleLine;
import io.github.sher1234.service.util.form.model.FormHeader;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskCall {

    private final FormBuilder formBuilder;
    private final Common common;
    private Delete delete;
    private Allot allot;
    private Edit edit;
    private Add add;

    private TaskCall(@NotNull AppCompatActivity activity, OnFormElementValueChange listener) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        formBuilder = new FormBuilder(activity, recyclerView, listener);
        common = new Common();
    }

    public TaskCall() {
        common = new Common();
        formBuilder = null;
    }

    public static TaskCall CallAdd(@NotNull AppCompatActivity activity,
                                   @NotNull OnFormElementValueChange listener) {
        TaskCall taskCall = new TaskCall(activity, listener);
        taskCall.onCreateForm(false);
        return taskCall;
    }

    public static TaskCall CallEdit(@NotNull AppCompatActivity activity,
                                    @NotNull OnFormElementValueChange listener) {
        TaskCall taskCall = new TaskCall(activity, listener);
        taskCall.onCreateForm(true);
        return taskCall;
    }

    private void onCreateForm(boolean editEnabled) {
        List<FormElement> elements = new ArrayList<>();
        List<String> options1 = new ArrayList<>();
        options1.add("Complaint");
        options1.add("New Commissioning");

        List<String> options2 = new ArrayList<>();
        options2.add("Yes");
        options2.add("No");
        options2.add("To be checked");

        List<String> options3 = new ArrayList<>();
        options3.add("Opened/Pending");
        options3.add("Closed/Completed");

        elements.add(FormHeader.createInstance("Customer Details", true));
        elements.add(FormElementSingleLine.createInstance(true).setTag(1).setTitle("Name").setHint("Enter Name").setRequired(true).setEnabled(true));
        elements.add(FormElementMultiLine.createInstance(true).setTag(2).setTitle("Site Details").setHint("Enter Site Details").setRequired(true).setEnabled(true));

        if (editEnabled) elements.add(FormHeader.createInstance("Complaint Status", true));
        if (editEnabled)
            elements.add(FormElementPicker.createInstance(true).setTag(12).setTitle("Status").setHint("Status").setOptions(options3).setRequired(true).setEnabled(true));

        elements.add(FormHeader.createInstance("Concern Person", true));
        elements.add(FormElementSingleLine.createInstance(true).setTag(3).setTitle("Name").setHint("Enter Name").setRequired(true).setEnabled(true));
        elements.add(FormElementPhone.createInstance(true).setTag(4).setTitle("Mobile Number").setHint("Enter Mobile").setRequired(true).setEnabled(true));

        elements.add(FormHeader.createInstance("Product Details", true));
        elements.add(FormElementMultiLine.createInstance(true).setTag(5).setTitle("Detail/Info").setHint("Enter Detail").setRequired(true).setEnabled(true));
        elements.add(FormElementSingleLine.createInstance(true).setTag(6).setTitle("Serial Number").setHint("Enter SNo").setRequired(false).setEnabled(true));

        elements.add(FormHeader.createInstance("Complaint Details", true));
        elements.add(FormElementPicker.createInstance(true).setTag(7).setTitle("Nature of Site").setOptions(options1).setRequired(true).setHint("Select One").setEnabled(true));
        elements.add(FormElementMultiLine.createInstance(true).setTag(8).setTitle("Type of Complaint").setHint("Enter Type").setRequired(true).setEnabled(true));
        elements.add(FormElementPicker.createInstance(true).setTag(9).setTitle("Warranty").setOptions(options2).setHint("Select One").setRequired(true).setEnabled(true));

        elements.add(FormHeader.createInstance("Call Details", true));
        elements.add(FormElementSingleLine.createInstance(true).setTag(10).setTitle("ID").setRequired(true).setEnabled(false));
        elements.add(FormElementDatePicker.createInstance(true).setTag(11).setTitle("Date/Time").setDateFormat(Strings.DateTimeView).setRequired(true).setEnabled(false));

        elements.add(FormDivider.createInstance());
        formBuilder.addFormElements(elements);
    }

    public boolean isFormValid() {
        return formBuilder.isValidForm()
                && common.getFormValue(formBuilder, 1).length() > 2
                && common.getFormValue(formBuilder, 2).length() > 2
                && common.getFormValue(formBuilder, 3).length() > 2
                && common.getFormValue(formBuilder, 4).length() == 10
                && common.getFormValue(formBuilder, 5).length() > 2
                && common.getFormValue(formBuilder, 7).length() > 2
                && common.getFormValue(formBuilder, 8).length() > 2
                && common.getFormValue(formBuilder, 9).length() > 2
                && common.getFormValue(formBuilder, 10).length() > 2
                && common.getFormValue(formBuilder, 11).length() > 2;
    }

    public void onResetForm(AppCompatActivity activity, Call call) {
        if (activity instanceof EditCall) onResetForm(call);
        else if (activity instanceof AddCall) onResetForm();
    }

    private void onResetForm(Call call) {
        if (call == null) return;
        common.setFormValue(formBuilder, 1, call.CustomerName);
        common.setFormValue(formBuilder, 2, call.SiteDetails);
        common.setFormValue(formBuilder, 3, call.ConcernName);
        common.setFormValue(formBuilder, 4, call.ConcernPhone);
        common.setFormValue(formBuilder, 5, call.ProductDetail);
        common.setFormValue(formBuilder, 6, call.ProductNumber);
        common.setFormValue(formBuilder, 7, call.NatureOfSite);
        common.setFormValue(formBuilder, 8, call.ComplaintType);
        common.setFormValue(formBuilder, 9, call.Warranty);
        common.setFormValue(formBuilder, 10, call.CallID);
        common.setFormDate(formBuilder, 11, call.getDateTime());
        common.setFormValue(formBuilder, 12, call.getIsCompletedString());
    }

    private void onResetForm() {
        Calendar calendar = Calendar.getInstance();
        common.setFormDate(formBuilder, 11, calendar.getTime());
        DateFormat dateFormat = new SimpleDateFormat(Strings.DateID, Locale.US);
        String callId = "MAC" + dateFormat.format(calendar.getTime());
        common.setFormValue(formBuilder, 10, callId);
    }

    private Call getEditCall() {
        return new Call(common.getFormValue(formBuilder, 12).contains("Pending"),
                common.getFormValue(formBuilder, 10), common.getFormValue(formBuilder, 9),
                common.getFormValue(formBuilder, 3), common.getFormValue(formBuilder, 2),
                common.getFormValue(formBuilder, 4), common.getFormValue(formBuilder, 1),
                common.getFormValue(formBuilder, 7), common.getFormValue(formBuilder, 6),
                common.getFormValue(formBuilder, 5), common.getFormValue(formBuilder, 8)
        );
    }

    private Call getAddCall() {
        User user = new TaskUser().getUser();
        return new Call(user.UserID, null, common.getFormValue(formBuilder, 10),
                common.getFormDate(formBuilder, 11), common.getFormValue(formBuilder, 9),
                false, common.getFormValue(formBuilder, 3),
                common.getFormValue(formBuilder, 2), common.getFormValue(formBuilder, 4),
                common.getFormValue(formBuilder, 1), common.getFormValue(formBuilder, 7),
                common.getFormValue(formBuilder, 6), common.getFormValue(formBuilder, 5),
                common.getFormValue(formBuilder, 8));
    }

    public void onAddCall(TaskUpdate taskUpdate) {
        if (delete != null) delete.cancel(true);
        if (allot != null) allot.cancel(true);
        if (edit != null) edit.cancel(true);
        if (add != null) add.cancel(true);
        add = new Add(taskUpdate, getAddCall());
        add.execute();
    }

    public void onEditCall(TaskUpdate taskUpdate) {
        if (delete != null) delete.cancel(true);
        if (allot != null) allot.cancel(true);
        if (edit != null) edit.cancel(true);
        if (add != null) add.cancel(true);
        edit = new Edit(taskUpdate, getEditCall());
        edit.execute();
    }

    public void onDeleteCall(TaskUpdate taskUpdate, String id) {
        if (delete != null) delete.cancel(true);
        if (allot != null) allot.cancel(true);
        if (edit != null) edit.cancel(true);
        if (add != null) add.cancel(true);
        delete = new Delete(taskUpdate, id);
        delete.execute();
    }

    public void onAllotCall(TaskUpdate taskUpdate, String cid, String uid) {
        if (delete != null) delete.cancel(true);
        if (allot != null) allot.cancel(true);
        if (edit != null) edit.cancel(true);
        if (add != null) add.cancel(true);
        allot = new Allot(taskUpdate, cid, uid);
        allot.execute();
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

    public void onCallAllotted(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Call Allotted")
                .setDescription("Successfully allotted call, tap on \"Done\" to continue.")
                .positiveButton("Done", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        activity.setResult(101);
                        activity.finish();
                        dialog.dismiss();
                    }
                }).setCancelable(false);
        dialog.show();
    }

    public void onCallEdited(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Call Updated")
                .setDescription("Call edited successfully, tap on \"Done\" to continue.")
                .positiveButton("Done", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        activity.setResult(101);
                        activity.finish();
                        dialog.dismiss();
                    }
                }).setCancelable(false);
        dialog.show();
    }

    public void onCallDeleted(final FragmentActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Call Deleted")
                .setDescription("Call deleted successfully, tap on \"Done\" to continue.")
                .positiveButton("Done", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        activity.setResult(101);
                        activity.finish();
                        dialog.dismiss();
                    }
                }).setCancelable(false);
        dialog.show();
    }

    public void onCallAdded(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Call Registered")
                .setDescription("Call registered successfully, tap on \"Done\" to continue.")
                .positiveButton("Done", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        activity.setResult(101);
                        activity.finish();
                        dialog.dismiss();
                    }
                }).setCancelable(false);
        dialog.show();
    }

    public interface TaskUpdate {
        void onFetched(Responded response, int i);

        void onFetch();

        void fetch();
    }

    @SuppressLint("StaticFieldLeak")
    private class Add extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final Call call;
        private Responded responded;
        private int i = 4869;

        Add(TaskUpdate taskUpdate, Call call) {
            this.taskUpdate = taskUpdate;
            this.call = call;
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
            retrofit2.Call<Responded> call = api.onAddCall(this.call.getAddMap());
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null) {
                        responded = response.body();
                        i = responded.Code;
                    } else {
                        responded = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<Responded> call, @NonNull Throwable t) {
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
            taskUpdate.onFetched(responded, i);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Edit extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final Call call;
        private Responded responded;
        private int i = 4869;

        Edit(TaskUpdate taskUpdate, Call call) {
            this.taskUpdate = taskUpdate;
            this.call = call;
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
            retrofit2.Call<Responded> call = api.onEditCall(this.call.getEditMap());
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null) {
                        responded = response.body();
                        i = responded.Code;
                    } else {
                        responded = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<Responded> call, @NonNull Throwable t) {
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
            taskUpdate.onFetched(responded, i);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Allot extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final String cid;
        private final String uid;
        private Responded responded;
        private int i = 4869;

        Allot(TaskUpdate taskUpdate, String cid, String uid) {
            this.taskUpdate = taskUpdate;
            this.uid = uid;
            this.cid = cid;
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
            retrofit2.Call<Responded> call = api.onAllotCall(cid, uid);
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null) {
                        responded = response.body();
                        i = responded.Code;
                    } else {
                        responded = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<Responded> call, @NonNull Throwable t) {
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
            taskUpdate.onFetched(responded, i);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Delete extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final String id;
        private Responded responded;
        private int i = 4869;

        Delete(TaskUpdate taskUpdate, String id) {
            this.taskUpdate = taskUpdate;
            this.id = id;
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
            retrofit2.Call<Responded> call = api.onDeleteCall(id);
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null) {
                        responded = response.body();
                        i = responded.Code;
                    } else {
                        responded = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<Responded> call, @NonNull Throwable t) {
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
            taskUpdate.onFetched(responded, i);
        }
    }
}