package io.github.sher1234.service.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.ResultListener;
import io.github.sher1234.service.util.Strings;

public class FilterEmployees extends BottomSheetDialogFragment implements View.OnClickListener {

    private final Functions functions = new Functions();
    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private TextInputEditText editText4;

    private DatePickerDialog fromPicker;
    private DatePickerDialog toPicker;
    private AlertDialog dialogSort;
    private Calendar calendar;
    private Date dateFrom;
    private Date dateTo;

    public FilterEmployees() {
        calendar = Calendar.getInstance();
    }

    @NotNull
    public static FilterEmployees newInstance() {
        return new FilterEmployees();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_filter_employees, container, false);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);

        editText2.setFocusableInTouchMode(false);
        editText3.setFocusableInTouchMode(false);
        editText4.setFocusableInTouchMode(false);

        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);
        editText4.setOnClickListener(this);

        onSetDatePickers();
        onSetTextPickers();
        return view;
    }

    @NotNull
    private Query getQuery() {
        int i = 0;
        Query query = new Query();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateServer, Locale.US);
        query.Query = "SELECT Email, Name, Phone, EmployeeID, (";
        String regsT = "SELECT COUNT(*) FROM RegisteredCallsX R WHERE (U.Email = R.Email OR " +
                "R.Email = U.EmployeeID OR U.Email = R.AllottedTo OR R.AllottedTo = U.EmployeeID)";
        String visit = "SELECT COUNT(*) FROM VisitedCallsX V WHERE (U.Email = V.Email OR " +
                "U.EmployeeID = V.Email)";
        String pendT = "SELECT COUNT(*) FROM RegisteredCallsX R WHERE (U.Email = R.Email OR " +
                "R.Email = U.EmployeeID OR U.Email = R.AllottedTo OR R.AllottedTo = " +
                "U.EmployeeID) AND R.IsCompleted = '0'";

        String search = functions.getString(editText1);
        if (!search.isEmpty()) {
            i = 1;
            search = " AND " +
                    "(Name LIKE '%" + search + "%'" +
                    " OR Email LIKE '%" + search + "%'" +
                    " OR EmployeeID LIKE '%" + search + "%')";
        } else
            search = null;

        String fromDate;
        if (dateFrom != null) {
            i = 1;
            fromDate = dateFormat.format(dateFrom);
            fromDate = " >= '" + fromDate + "'";
        } else
            fromDate = null;

        String toDate;
        if (dateTo != null) {
            i = 1;
            toDate = dateFormat.format(dateTo);
            toDate = " <= '" + toDate + "'";
        } else
            toDate = null;

        String sort = functions.getString(editText4);
        if (!sort.isEmpty()) {
            if (sort.equalsIgnoreCase("Name"))
                sort = " ORDER BY Name ASC";
            else if (sort.equalsIgnoreCase("Registrations"))
                sort = " ORDER BY TotalR DESC";
            else if (sort.equalsIgnoreCase("Visits"))
                sort = " ORDER BY TotalV DESC";
            else if (sort.equalsIgnoreCase("Pending"))
                sort = " ORDER BY TotalP DESC";
            else
                sort = null;
        } else
            sort = null;

        if (i == 1) {
            if (fromDate != null) {
                regsT = regsT + " AND DateTime" + fromDate;
                pendT = pendT + " AND DateTime" + fromDate;
                visit = visit + " AND StartTime" + fromDate;
            }
            if (toDate != null) {
                regsT = regsT + " AND DateTime" + toDate;
                pendT = pendT + " AND DateTime" + toDate;
                visit = visit + " AND StartTime" + toDate;
            }
            query.Query = query.Query +
                    regsT + ") as TotalR, (" +
                    visit + ") as TotalV, (" +
                    pendT + ") as TotalP  FROM UsersX U WHERE IsRegistered = 1 AND IsAdmin = 0";
            if (search != null && !search.isEmpty())
                query.Query = query.Query + search;
            if (sort != null && !sort.isEmpty())
                query.Query = query.Query + sort;
            else
                query.Query = query.Query + " ORDER BY Name";
        } else {
            regsT = "SELECT COUNT(*) FROM RegisteredCallsX R WHERE U.Email = R.Email OR " +
                    "R.Email = U.EmployeeID OR U.Email = R.AllottedTo OR R.AllottedTo = U.EmployeeID";
            visit = "SELECT COUNT(*) FROM VisitedCallsX V WHERE U.Email = V.Email OR " +
                    "U.EmployeeID = V.Email";
            pendT = "SELECT COUNT(*) FROM RegisteredCallsX R WHERE (U.Email = R.Email OR " +
                    "R.Email = U.EmployeeID OR U.Email = R.AllottedTo OR R.AllottedTo = " +
                    "U.EmployeeID) AND R.IsCompleted = '0'";
            query = new Query();
            query.Query = "SELECT Email, Name, Phone, EmployeeID, (" +
                    regsT + ") as TotalR, (" +
                    visit + ") as TotalV, (" +
                    pendT + ") as TotalP  FROM UsersX U WHERE IsRegistered = 1 AND IsAdmin = 0 " +
                    "ORDER BY Name";
        }
        query.Table = "Employees";
        return query;
    }

    private void onSetDatePickers() {
        assert getContext() != null;
        DatePickerDialog.OnDateSetListener fromListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 1);

                SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateView, Locale.US);
                String s = dateFormat.format(calendar.getTime());
                dateFrom = calendar.getTime();
                editText2.setText(s);
            }
        };
        fromPicker = new DatePickerDialog(getContext(), fromListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog.OnDateSetListener toListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);

                SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateView, Locale.US);
                String s = dateFormat.format(calendar.getTime());
                dateTo = calendar.getTime();
                editText3.setText(s);
            }
        };
        toPicker = new DatePickerDialog(getContext(), toListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void onSetTextPickers() {
        assert getContext() != null;
        final CharSequence[] options = new CharSequence[]{"Name", "Registrations", "Visits", "Pending"};

        dialogSort = new AlertDialog.Builder(getContext())
                .setTitle(R.string.nature_of_site)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editText4.setText(options[which]);
                        dialog.dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onClick(View view) {
        assert getActivity() != null;
        if (view.getId() == R.id.editText2)
            fromPicker.show();
        if (view.getId() == R.id.editText3)
            toPicker.show();
        if (view.getId() == R.id.editText4)
            dialogSort.show();
        if (view.getId() == R.id.button1) {
            dateTo = null;
            dateFrom = null;
            editText1.setText(null);
            editText2.setText(null);
            editText3.setText(null);
            editText4.setText(null);
            calendar = Calendar.getInstance();
        }
        if (view.getId() == R.id.button2) {
            ((ResultListener) getActivity()).onResultChange(getQuery(), false);
            dismiss();
        }
    }
}