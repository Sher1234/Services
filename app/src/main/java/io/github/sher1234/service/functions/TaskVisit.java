package io.github.sher1234.service.functions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.base.Visit;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.form.FormBuilder;
import io.github.sher1234.service.util.form.listener.OnFormElementValueChange;
import io.github.sher1234.service.util.form.model.FormDivider;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.model.FormElementDatePicker;
import io.github.sher1234.service.util.form.model.FormElementEmail;
import io.github.sher1234.service.util.form.model.FormElementMultiLine;
import io.github.sher1234.service.util.form.model.FormElementPhone;
import io.github.sher1234.service.util.form.model.FormElementPicker;
import io.github.sher1234.service.util.form.model.FormElementRating;
import io.github.sher1234.service.util.form.model.FormElementSingleLine;
import io.github.sher1234.service.util.form.model.FormHeader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskVisit {

    private final FormBuilder builder;
    private final Common common;
    private Delete delete;
    private Start start;
    private Sign sign;
    private Edit edit;
    private End end;
    private Add add;

    private TaskVisit(@NotNull AppCompatActivity activity, OnFormElementValueChange listener) {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        builder = new FormBuilder(activity, recyclerView, listener);
        common = new Common();
    }

    public TaskVisit() {
        common = new Common();
        builder = null;
    }

    public static TaskVisit VisitAdd(@NotNull AppCompatActivity activity, @NotNull Call call,
                                     @NotNull OnFormElementValueChange listener) {
        TaskVisit taskCall = new TaskVisit(activity, listener);
        taskCall.onCreateForm(true);
        taskCall.onResetFull(call);
        return taskCall;
    }

    public static TaskVisit VisitEnd(@NotNull AppCompatActivity activity, @NotNull Visit v,
                                     @NotNull Call c, @NotNull OnFormElementValueChange change) {
        TaskVisit taskCall = new TaskVisit(activity, change);
        taskCall.onCreateForm(true);
        taskCall.onResetEnd(c, v);
        return taskCall;
    }

    public static TaskVisit VisitStart(@NotNull AppCompatActivity activity, @NotNull Call call,
                                       @NotNull OnFormElementValueChange listener) {
        TaskVisit taskCall = new TaskVisit(activity, listener);
        taskCall.onCreateFormStart();
        taskCall.onResetStart(call);
        return taskCall;
    }

    public static TaskVisit VisitEdit(@NotNull AppCompatActivity activity, @NotNull Visit v,
                                      @NotNull Call c, @NotNull OnFormElementValueChange change) {
        TaskVisit taskCall = new TaskVisit(activity, change);
        taskCall.onCreateForm(false);
        taskCall.onResetEdit(c, v);
        return taskCall;
    }

    private void onCreateFormStart() {
        List<FormElement> elements = new ArrayList<>();
        elements.add(FormDivider.createInstance());
        elements.add(FormHeader.createInstance("Customer Details", false));
        elements.add(FormElementMultiLine.createInstance(false).setTag(1).setTitle("Name").setEnabled(false));
        elements.add(FormElementMultiLine.createInstance(false).setTag(2).setTitle("Site Details").setEnabled(false));
        elements.add(FormDivider.createInstance());
        elements.add(FormHeader.createInstance("Visit Details", false));
        elements.add(FormElementSingleLine.createInstance(false).setTag(3).setTitle("Call ID").setEnabled(false));
        elements.add(FormElementSingleLine.createInstance(false).setTag(4).setTitle("Visit ID").setEnabled(false));
        elements.add(FormElementDatePicker.createInstance(false).setTag(5).setTitle("Start Time").setDateFormat(Strings.DateTimeView).setEnabled(false));
        elements.add(FormDivider.createInstance());
        elements.add(FormHeader.createInstance("Visit Engineer", false));
        elements.add(FormElementSingleLine.createInstance(false).setTag(6).setTitle("Name").setEnabled(false));
        elements.add(FormElementSingleLine.createInstance(false).setTag(7).setTitle("Email").setEnabled(false));
        elements.add(FormElementSingleLine.createInstance(false).setTag(8).setTitle("Phone").setEnabled(false));
        elements.add(FormDivider.createInstance());
        builder.addFormElements(elements);
    }

    private void onCreateForm(boolean b) {
        List<FormElement> items = new ArrayList<>();
        List<String> options1 = new ArrayList<>();
        options1.add("Closed/Completed");
        options1.add("Opened/Pending");
        items.add(FormHeader.createInstance("Visit Details", true));
        items.add(FormElementSingleLine.createInstance(true).setTag(1).setTitle("Call ID").setEnabled(false));
        items.add(FormElementSingleLine.createInstance(true).setTag(2).setTitle("Visit ID").setEnabled(false));
        items.add(FormElementDatePicker.createInstance(true).setTag(3).setTitle("Start Time").setDateFormat(Strings.DateTimeView).setEnabled(false));
        items.add(FormElementDatePicker.createInstance(true).setTag(4).setTitle("End Time").setDateFormat(Strings.DateTimeView).setEnabled(false));
        // Customer Details
        items.add(FormHeader.createInstance("Customer Details", true));
        items.add(FormElementMultiLine.createInstance(true).setTag(5).setTitle("Name").setEnabled(false));
        items.add(FormElementMultiLine.createInstance(true).setTag(6).setTitle("Site Details").setEnabled(false));
        // Concern Person
        items.add(FormHeader.createInstance("Concern Person", true));
        items.add(FormElementMultiLine.createInstance(true).setTag(7).setTitle("Name").setHint("Enter Name").setRequired(true));
        items.add(FormElementPhone.createInstance(true).setTag(8).setTitle("Mobile Number").setHint("Enter Mobile").setRequired(true));
        items.add(FormElementEmail.createInstance(true).setTag(9).setTitle("e-Mail ID").setHint("Enter Email").setRequired(true));
        // Visit Details
        items.add(FormHeader.createInstance("Observation & Action", true));
        items.add(FormElementMultiLine.createInstance(true).setTag(10).setTitle("Observation").setHint("Enter Observation").setRequired(true));
        items.add(FormElementMultiLine.createInstance(true).setTag(11).setTitle("Action Taken").setHint("Enter Action Taken").setRequired(true));
        // Complaint Details
        items.add(FormHeader.createInstance("Other Details", true));
        if (b)
            items.add(FormElementPicker.createInstance(true).setTag(12).setTitle("Complaint Status").setOptions(options1).setRequired(true).setHint("Select One"));
        items.add(FormElementMultiLine.createInstance(true).setTag(13).setTitle("Customer Feedback").setHint("Enter Feedback").setRequired(false));
        items.add(FormElementRating.createInstance(true).setTag(14).setTitle("Customer Rating").setRequired(true));
        items.add(FormDivider.createInstance());
        builder.addFormElements(items);
    }

    public boolean isFullFormValid() {
        return isEditFormValid() && common.getFormValue(builder, 12).length() > 2;
    }

    public boolean isEditFormValid() {
        return builder.isValidForm()
                && common.getFormValue(builder, 8).length() == 10
                && common.getFormValue(builder, 7).length() > 2
                && common.getFormValue(builder, 9).length() > 2
                && common.getFormValue(builder, 9).contains("@")
                && common.getFormValue(builder, 10).length() > 3
                && common.getFormValue(builder, 11).length() > 3
                && common.getFormRating(builder, 14) > -1
                && common.getFormRating(builder, 14) < 6;
    }

    private void onResetStart(@NotNull Call call) {
        User user = new TaskUser().getUser();
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(Strings.DateID, Locale.US);
        String visitId = "MAV" + dateFormat.format(calendar.getTime());
        common.setFormValue(builder, 1, call.CustomerName);
        common.setFormValue(builder, 2, call.SiteDetails);
        common.setFormValue(builder, 3, call.CallID);
        common.setFormValue(builder, 4, visitId);
        common.setFormDate(builder, 5, calendar.getTime());
        common.setFormValue(builder, 6, user.Name);
        common.setFormValue(builder, 7, user.Email);
        common.setFormValue(builder, 8, user.Phone);
    }

    private void onResetEnd(Call call, Visit visit) {
        Calendar calendar = Calendar.getInstance();
        common.setFormValue(builder, 1, call.CallID);       //CallID
        common.setFormValue(builder, 2, visit.VisitID);     //VisitID
        common.setFormDate(builder, 3, visit.getStart());   //StartTime
        common.setFormDate(builder, 4, calendar.getTime()); //EndTime
        common.setFormValue(builder, 5, call.CustomerName); //CustomerName
        common.setFormValue(builder, 6, call.SiteDetails);  //SiteDetails
        common.setFormRating(builder, 14, 0);            //Satisfaction
    }

    private void onResetFull(Call call) {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(Strings.DateID, Locale.US);
        String visitID = "MAV" + dateFormat.format(calendar.getTime());
        common.setFormValue(builder, 1, call.CallID);       //CallID
        common.setFormValue(builder, 2, visitID);           //VisitID
        common.setFormDate(builder, 3, calendar.getTime()); //StartTime
        common.setFormDate(builder, 4, calendar.getTime()); //EndTime
        common.setFormValue(builder, 5, call.CustomerName); //CustomerName
        common.setFormValue(builder, 6, call.SiteDetails);  //SiteDetails
        common.setFormRating(builder, 14, 0);            //Satisfaction
    }

    private void onResetEdit(Call call, Visit visit) {
        common.setFormValue(builder, 1, call.CallID);       //CallID
        common.setFormValue(builder, 2, visit.VisitID);     //VisitID
        common.setFormDate(builder, 3, visit.getStart());   //StartTime
        common.setFormDate(builder, 4, visit.getEnd());     //EndTime
        common.setFormValue(builder, 5, call.CustomerName); //CustomerName
        common.setFormValue(builder, 6, call.SiteDetails);  //SiteDetails
        common.setFormValue(builder, 7, visit.Name);        //ConcernName
        common.setFormValue(builder, 8, visit.Phone);       //ConcernPhone
        common.setFormValue(builder, 9, visit.Email);       //ConcernEmail
        common.setFormValue(builder, 10, visit.Observation);//Observation
        common.setFormValue(builder, 11, visit.Action);     //Action
        common.setFormValue(builder, 13, visit.Feedback);  //Feedback
        common.setFormRating(builder, 14, visit.Satisfaction);//Satisfaction
    }

    public Visit getEditVisit() {
        return new Visit(common.getFormValue(builder, 2), common.getFormValue(builder, 7),
                common.getFormValue(builder, 8), common.getFormValue(builder, 9),
                common.getFormValue(builder, 10), common.getFormValue(builder, 11),
                common.getFormRating(builder, 14), common.getFormValue(builder, 13));
    }

    public Visit getVisitEnd() {
        int status = common.getFormValue(builder, 12).contains("Pending") ? 0 : 1;
        return new Visit(common.getFormValue(builder, 2), common.getFormValue(builder, 1),
                common.getFormValue(builder, 11), common.getFormValue(builder, 10),
                common.getFormValue(builder, 7), common.getFormValue(builder, 8),
                common.getFormValue(builder, 9), common.getFormRating(builder, 14),
                common.getFormDate(builder, 4), common.getFormValue(builder, 13), status);
    }

    public Visit getVisitStart(String location) {
        User user = new TaskUser().getUser();
        return new Visit(common.getFormValue(builder, 4), common.getFormValue(builder, 3),
                common.getFormDate(builder, 5), user.UserID, location);
    }

    public Visit getVisit(String location) {
        User user = new TaskUser().getUser();
        int status = common.getFormValue(builder, 12).contains("Pending") ? 0 : 1;
        return new Visit(user.UserID, common.getFormValue(builder, 7),
                common.getFormDate(builder, 4), common.getFormValue(builder, 9),
                common.getFormValue(builder, 8), common.getFormValue(builder, 1),
                common.getFormDate(builder, 3), common.getFormValue(builder, 11),
                common.getFormValue(builder, 2), common.getFormValue(builder, 13), location,
                common.getFormValue(builder, 10), common.getFormRating(builder, 14), status);
    }

    public void onAddVisit(TaskUpdate taskUpdate, Visit visit) {
        if (delete != null) delete.cancel(true);
        if (start != null) start.cancel(true);
        if (edit != null) edit.cancel(true);
        if (end != null) end.cancel(true);
        if (add != null) add.cancel(true);
        Log.e("Visit Map", visit.toString());
        add = new Add(taskUpdate, visit);
        add.execute();
    }

    public void onEndVisit(TaskUpdate taskUpdate, Visit visit) {
        if (delete != null) delete.cancel(true);
        if (start != null) start.cancel(true);
        if (edit != null) edit.cancel(true);
        if (end != null) end.cancel(true);
        if (add != null) add.cancel(true);
        Log.e("Visit Map", visit.toString());
        end = new End(taskUpdate, visit);
        end.execute();
    }

    public void onEditVisit(TaskUpdate taskUpdate, Visit visit) {
        if (delete != null) delete.cancel(true);
        if (start != null) start.cancel(true);
        if (edit != null) edit.cancel(true);
        if (end != null) end.cancel(true);
        if (add != null) add.cancel(true);
        Log.e("Visit Map", visit.toString());
        edit = new Edit(taskUpdate, visit);
        edit.execute();
    }

    public void onSignVisit(TaskUpdate taskUpdate, String id, File file) {
        if (delete != null) delete.cancel(true);
        if (start != null) start.cancel(true);
        if (edit != null) edit.cancel(true);
        if (sign != null) sign.cancel(true);
        if (end != null) end.cancel(true);
        if (add != null) add.cancel(true);
        sign = new Sign(taskUpdate, id, file);
        sign.execute();
    }

    public void onDeleteVisit(TaskUpdate taskUpdate, String id) {
        if (delete != null) delete.cancel(true);
        if (start != null) start.cancel(true);
        if (edit != null) edit.cancel(true);
        if (end != null) end.cancel(true);
        if (add != null) add.cancel(true);
        delete = new Delete(taskUpdate, id);
        delete.execute();
    }

    public void onStartVisit(TaskUpdate taskUpdate, Visit visit) {
        if (delete != null) delete.cancel(true);
        if (start != null) start.cancel(true);
        if (edit != null) edit.cancel(true);
        if (end != null) end.cancel(true);
        if (add != null) add.cancel(true);
        Log.e("Visit Map", visit.toString());
        start = new Start(taskUpdate, visit);
        start.execute();
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

    public void onVisitEnded(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Visit Ended")
                .setDescription("Visit ended successfully, tap on \"Done\" to continue.")
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

    public void onVisitStarted(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Visit Started")
                .setDescription("Visit started successfully, tap on \"Done\" to continue.")
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

    public void onVisitEdited(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Visit Updated")
                .setDescription("Visit edited successfully, tap on \"Done\" to continue.")
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

    public void onVisitDeleted(final FragmentActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Visit Deleted")
                .setDescription("Visit deleted successfully, tap on \"Done\" to continue.")
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

    public void onVisitAdded(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Visit Added")
                .setDescription("Visit Added successfully, tap on \"Done\" to continue.")
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

    public void onVisitSigned(final AppCompatActivity activity) {
        MaterialDialog dialog = MaterialDialog.Dialog(activity);
        dialog.setTitle("Visit Signed")
                .setDescription("Visit Signed successfully, tap on \"Done\" to continue.")
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
        private final Visit visit;
        private Responded responded;
        private int i = 4869;

        Add(TaskUpdate taskUpdate, Visit visit) {
            this.taskUpdate = taskUpdate;
            this.visit = visit;
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
            retrofit2.Call<Responded> call = api.onAddVisit(this.visit.getMap());
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
    private class End extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final Visit visit;
        private Responded responded;
        private int i = 4869;

        End(TaskUpdate taskUpdate, Visit visit) {
            this.taskUpdate = taskUpdate;
            this.visit = visit;
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
            retrofit2.Call<Responded> call = api.onEndVisit(this.visit.getEndMap());
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
    private class Start extends AsyncTask<Void, Void, Boolean> {

        private final TaskUpdate taskUpdate;
        private final Visit visit;
        private Responded responded;
        private int i = 4869;

        Start(TaskUpdate taskUpdate, Visit visit) {
            this.taskUpdate = taskUpdate;
            this.visit = visit;
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
            retrofit2.Call<Responded> call = api.onStartVisit(this.visit.getStartMap());
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
        private final Visit visit;
        private Responded responded;
        private int i = 4869;

        Edit(TaskUpdate taskUpdate, Visit visit) {
            this.taskUpdate = taskUpdate;
            this.visit = visit;
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
            retrofit2.Call<Responded> call = api.onEditVisit(this.visit.getEditMap());
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
            retrofit2.Call<Responded> call = api.onDeleteVisit(id);
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
    private class Sign extends AsyncTask<Void, Void, Boolean> {
        private final TaskUpdate taskUpdate;
        private final String string;
        private final File file;
        private Responded responded;
        private int i = 4869;

        Sign(TaskUpdate taskUpdate, String string, File file) {
            this.taskUpdate = taskUpdate;
            this.string = string;
            this.file = file;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskUpdate.onFetch();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            Retrofit retrofit = AppController.getInstance().getRetrofitRequest(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            retrofit2.Call<Responded> call = api.onSignVisit(string, part);
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
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            taskUpdate.onFetched(responded, i);
        }
    }
}