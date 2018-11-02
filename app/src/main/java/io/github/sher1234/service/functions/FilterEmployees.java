package io.github.sher1234.service.functions;

import android.view.View;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.form.FormBuilder;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.model.FormElementDatePicker;
import io.github.sher1234.service.util.form.model.FormElementPicker;
import io.github.sher1234.service.util.form.model.FormElementSingleLine;
import io.github.sher1234.service.util.form.model.FormHeader;

public class FilterEmployees implements View.OnClickListener {

    private final TaskEmployeeList.TaskUpdate taskUpdate;
    private final Common common = new Common();
    private final AppCompatActivity activity;
    private final DrawerLayout drawerLayout;
    private final NavigationView navView;
    private FormBuilder formBuilder;

    public FilterEmployees(@NotNull AppCompatActivity activity, TaskEmployeeList.TaskUpdate taskUpdate) {
        this.drawerLayout = activity.findViewById(R.id.drawerLayout);
        this.navView = activity.findViewById(R.id.filterView);
        this.taskUpdate = taskUpdate;
        this.activity = activity;
        setNavigationView();
        setFormFilter();
        common.onResetForm(formBuilder, 5);
        common.setFormValue(formBuilder, 4, "Name");
    }

    private void setNavigationView() {
        RecyclerView recyclerView = navView.getHeaderView(0).findViewById(R.id.recyclerView);
        navView.getHeaderView(0).findViewById(R.id.button2).setOnClickListener(this);
        navView.getHeaderView(0).findViewById(R.id.button1).setOnClickListener(this);
        formBuilder = new FormBuilder(activity, recyclerView);
    }

    private void setFormFilter() {
        List<String> options = new ArrayList<>();
        options.add("Name");
        options.add("Calls");
        options.add("Visits");
        options.add("Pending");
        List<FormElement> elements = new ArrayList<>();
        elements.add(FormHeader.createInstance("Filter", true));
        elements.add(FormElementSingleLine.createInstance(true).setTitle("Search").setHint("None").setTag(1));
        elements.add(FormElementDatePicker.createInstance(true).setTitle("From").setHint("None").setTag(2).setDateFormat(Strings.DateView));
        elements.add(FormElementDatePicker.createInstance(true).setTitle("To").setHint("None").setTag(3).setDateFormat(Strings.DateView));
        elements.add(FormElementPicker.createInstance(true).setOptions(options).setTitle("Sort by").setHint("Sort").setTag(4));
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
        int i = 0;
        Query query = new Query();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateTimeServer, Locale.US);
        query.Query = "SELECT U.UserID, U.Name, U.Email, U.Phone, U.EmployeeID, (";
        String regsT = "SELECT COUNT(*) FROM Calls R WHERE (U.UserID = R.ID1 OR R.ID2 = U.UserID)";
        String visit = "SELECT COUNT(*) FROM Visits V WHERE (U.UserID = V.ID)";
        String pendT = "SELECT COUNT(*) FROM Calls R WHERE (U.UserID = R.ID1 OR R.ID2 = U.UserID) " +
                "AND R.IsCompleted = '0'";

        String search = common.getFormValue(formBuilder, 1);
        if (search != null && !search.isEmpty()) {
            i = 1;
            search = " AND " +
                    "(Name LIKE '%" + search + "%'" +
                    " OR Email LIKE '%" + search + "%'" +
                    " OR EmployeeID LIKE '%" + search + "%')";
        } else
            search = null;

        String from = common.getFormValue(formBuilder, 2);
        if (from != null && !from.isEmpty()) {
            i = 1;
            from = dateFormat.format(common.getFormDate(formBuilder, 2));
            from = " >= '" + from + "'";
        } else
            from = null;

        String to = common.getFormValue(formBuilder, 3);
        if (to != null && !to.isEmpty()) {
            i = 1;
            to = dateFormat.format(common.getFormDate(formBuilder, 3));
            to = " <= '" + to + "'";
        } else
            to = null;

        String sort = common.getFormValue(formBuilder, 4);
        if (sort != null && !sort.isEmpty()) {
            i = 1;
            if (sort.contains("Name"))
                sort = " ORDER BY Name";
            else if (sort.contains("Calls"))
                sort = " ORDER BY Calls DESC";
            else if (sort.contains("Visits"))
                sort = " ORDER BY Visits DESC";
            else if (sort.contains("Pending"))
                sort = " ORDER BY Pending DESC";
            else
                sort = null;
        } else
            sort = null;

        if (i == 1) {
            if (from != null) {
                visit = visit + " AND Start" + from;
                pendT = pendT + " AND DateTime" + from;
                regsT = regsT + " AND DateTime" + from;
            }
            if (to != null) {
                visit = visit + " AND Start" + to;
                pendT = pendT + " AND DateTime" + to;
                regsT = regsT + " AND DateTime" + to;
            }
            query.Query = query.Query + regsT + ") as Calls, (" + visit + ") as Visits, " +
                    "(" + pendT + ") as Pending FROM User U WHERE IsRegistered = 1";
            if (search != null && !search.isEmpty())
                query.Query = query.Query + search;
            if (sort != null)
                query.Query = query.Query + sort;
            else
                query.Query = query.Query + " ORDER BY Name";
        } else {
            visit = "SELECT COUNT(*) FROM Visits V WHERE U.UserID = V.ID";
            regsT = "SELECT COUNT(*) FROM Calls R WHERE U.UserID = R.ID1 OR R.ID2 = U.UserID";
            pendT = "SELECT COUNT(*) FROM Calls R WHERE (U.UserID = R.ID1 OR R.ID2 = U.UserID) " +
                    "AND R.IsCompleted = '0'";
            query = new Query();
            query.Query = "SELECT U.UserID, U.Name, U.Email, U.Phone, U.EmployeeID, (" + regsT + ") as Calls, " +
                    "(" + visit + ") as Visits, (" + pendT + ") as Pending FROM User U " +
                    "WHERE IsRegistered = 1 ORDER BY Name";
        }
        query.Table = "Employees";
        return query;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            common.onResetForm(formBuilder, 5);
            common.setFormValue(formBuilder, 4, "Name");
        } else if (view.getId() == R.id.button2)
            taskUpdate.fetch(getQuery());
    }
}