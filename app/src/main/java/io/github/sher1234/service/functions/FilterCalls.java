package io.github.sher1234.service.functions;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.form.FormBuilder;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.model.FormElementDatePicker;
import io.github.sher1234.service.util.form.model.FormElementPicker;
import io.github.sher1234.service.util.form.model.FormElementSingleLine;
import io.github.sher1234.service.util.form.model.FormHeader;

public class FilterCalls implements View.OnClickListener {

    private final AppCompatActivity activity;
    private final TaskCallList.TaskUpdate taskUpdate;
    private final DrawerLayout drawerLayout;
    private final NavigationView navView;
    private final Common funH = new Common();
    private final User user;
    private FormBuilder formBuilder;

    public FilterCalls(@NotNull AppCompatActivity activity, TaskCallList.TaskUpdate taskUpdate) {
        this.drawerLayout = activity.findViewById(R.id.drawerLayout);
        this.navView = activity.findViewById(R.id.navigationView);
        this.taskUpdate = taskUpdate;
        user = new TaskUser().getUser();
        this.activity = activity;
        setNavigationView();
        setFormFilter();
        funH.onResetForm(formBuilder, 7);
        funH.setFormValue(formBuilder, 6, "All");
    }

    private void setNavigationView() {
        RecyclerView recyclerView = navView.getHeaderView(0).findViewById(R.id.recyclerView);
        navView.getHeaderView(0).findViewById(R.id.button2).setOnClickListener(this);
        navView.getHeaderView(0).findViewById(R.id.button1).setOnClickListener(this);
        formBuilder = new FormBuilder(activity, recyclerView);
    }

    private void setFormFilter() {
        List<String> options1 = new ArrayList<>();
        options1.add("Complaint");
        options1.add("New Commissioning");
        List<String> options2 = new ArrayList<>();
        options2.add("Yes");
        options2.add("No");
        options2.add("To be checked");
        List<String> options3 = new ArrayList<>();
        options3.add("All");
        options3.add("Opened/Pending");
        options3.add("Closed/Completed");
        List<FormElement> elements = new ArrayList<>();
        elements.add(FormHeader.createInstance("Filter", true));
        elements.add(FormElementSingleLine.createInstance(true).setTitle("Search").setHint("None").setTag(1));
        elements.add(FormElementDatePicker.createInstance(true).setTitle("From").setHint("None").setTag(2).setDateFormat(Strings.DateView));
        elements.add(FormElementDatePicker.createInstance(true).setTitle("To").setHint("None").setTag(3).setDateFormat(Strings.DateView));
        elements.add(FormElementPicker.createInstance(true).setOptions(options1).setTitle("Site's Nature").setHint("None").setTag(4));
        elements.add(FormElementPicker.createInstance(true).setOptions(options2).setTitle("Warranty").setHint("None").setTag(5));
        elements.add(FormElementPicker.createInstance(true).setOptions(options3).setTitle("Status").setHint("None").setTag(6));
        formBuilder.addFormElements(elements);
    }

    public void onToggleFilter() {
        if (drawerLayout.isDrawerOpen(navView))
            drawerLayout.closeDrawer(navView);
        else
            drawerLayout.openDrawer(navView);
    }

    @NotNull
    public Query getQuery() {
        Query query = new Query();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateTimeServer, Locale.US);
        query.Query = "SELECT * FROM Calls WHERE ";
        int i = 0;
        String search = funH.getFormValue(formBuilder, 1);
        if (search != null && !search.isEmpty()) {
            i = 1;
            search = "(CustomerName LIKE '%" + search + "%'" +
                    " OR ProductDetail LIKE '%" + search + "%'" +
                    " OR ComplaintType LIKE '%" + search + "%'" +
                    " OR SiteDetails LIKE '%" + search + "%'" +
                    " OR ConcernName LIKE '%" + search + "%')";
        } else
            search = null;

        String from = funH.getFormValue(formBuilder, 2);
        if (from != null && !from.isEmpty()) {
            i = 1;
            from = dateFormat.format(funH.getFormDate(formBuilder, 2));
            from = "DateTime >= '" + from + "'";
        } else
            from = null;

        String to = funH.getFormValue(formBuilder, 3);
        if (to != null && !to.isEmpty()) {
            i = 1;
            to = dateFormat.format(funH.getFormDate(formBuilder, 3));
            to = "DateTime <= '" + to + "'";
        } else
            to = null;

        String status = funH.getFormValue(formBuilder, 6);
        if (status != null && !status.isEmpty() && !status.contains("All")) {
            i = 1;
            if (status.contains("Opened"))
                status = "IsCompleted = '0'";
            else if (status.contains("Closed"))
                status = "IsCompleted = '1'";
        } else
            status = null;

        String siteNature = funH.getFormValue(formBuilder, 4);
        if (siteNature != null && !siteNature.isEmpty()) {
            i = 1;
            siteNature = "NatureOfSite = '" + siteNature + "'";
        } else
            siteNature = null;

        String warranty = funH.getFormValue(formBuilder, 5);
        if (warranty != null && !warranty.isEmpty()) {
            i = 1;
            warranty = "Warranty = '" + warranty + "'";
        } else
            warranty = null;

        if (i == 1) {
            if (search != null)
                query.Query = query.Query + search + " AND ";
            if (from != null)
                query.Query = query.Query + from + " AND ";
            if (to != null)
                query.Query = query.Query + to + " AND ";
            if (status != null)
                query.Query = query.Query + status + " AND ";
            if (siteNature != null)
                query.Query = query.Query + siteNature + " AND ";
            if (warranty != null)
                query.Query = query.Query + warranty;
            if (!user.isAdmin())
                query.Query = query.Query + "(ID1 = '" + user.UserID + "' OR ID2 = '" + user.UserID + "')";
            else if (query.Query.endsWith(" AND "))
                query.Query = query.Query.substring(0, query.Query.length() - 5);
            query.Query = query.Query + " ORDER BY IsCompleted, DateTime";
        } else {
            if (user.isAdmin())
                query.Query = "SELECT * FROM Calls ORDER BY IsCompleted, DateTime";
            else
                query.Query = "SELECT * FROM Calls WHERE (ID1 = '" + user.UserID +
                        "' OR ID2 = '" + user.UserID + "') ORDER BY IsCompleted, DateTime ";
        }
        query.Table = "Calls";
        return query;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            funH.onResetForm(formBuilder, 7);
            funH.setFormValue(formBuilder, 6, "All");
        } else if (view.getId() == R.id.button2)
            taskUpdate.fetch(getQuery());
    }
}