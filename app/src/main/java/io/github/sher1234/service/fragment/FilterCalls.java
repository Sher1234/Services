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

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.ResultListener;
import io.github.sher1234.service.util.Strings;

public class FilterCalls extends BottomSheetDialogFragment implements View.OnClickListener {

    private final Functions functions = new Functions();
    private final User user;
    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private TextInputEditText editText4;
    private TextInputEditText editText5;
    private TextInputEditText editText6;
    private DatePickerDialog fromPicker;
    private AlertDialog dialogWarranty;
    private DatePickerDialog toPicker;
    private AlertDialog dialogStatus;
    private AlertDialog dialogNos;
    private Calendar calendar;
    private Date dateFrom;
    private Date dateTo;

    public FilterCalls() {
        calendar = Calendar.getInstance();
        user = AppController.getUserFromPrefs();
    }

    @NotNull
    public static FilterCalls newInstance() {
        return new FilterCalls();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_filter_calls, container, false);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);
        editText6 = view.findViewById(R.id.editText6);

        editText2.setFocusableInTouchMode(false);
        editText3.setFocusableInTouchMode(false);
        editText4.setFocusableInTouchMode(false);
        editText5.setFocusableInTouchMode(false);
        editText6.setFocusableInTouchMode(false);

        editText2.setOnClickListener(this);
        editText3.setOnClickListener(this);
        editText4.setOnClickListener(this);
        editText5.setOnClickListener(this);
        editText6.setOnClickListener(this);

        onSetDatePickers();
        onSetTextPickers();
        return view;
    }

    @NotNull
    private Query getQuery() {
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat(Strings.DateServer, Locale.US);
        Query query = new Query();
        query.Query = "SELECT * FROM RegisteredCallsX WHERE ";

        String search = functions.getString(editText1);
        if (!search.isEmpty()) {
            i = 1;
            search = "(CustomerName LIKE '%" + search + "%'" +
                    " OR ProductDetail LIKE '%" + search + "%'" +
                    " OR ComplaintType LIKE '%" + search + "%'" +
                    " OR SiteDetails LIKE '%" + search + "%'" +
                    " OR ConcernName LIKE '%" + search + "%')";
        } else
            search = null;

        String fromDate;
        if (dateFrom != null) {
            i = 1;
            fromDate = dateFormat.format(dateFrom);
            fromDate = "DateTime >= '" + fromDate + "'";
        } else
            fromDate = null;

        String toDate;
        if (dateTo != null) {
            i = 1;
            toDate = dateFormat.format(dateTo);
            toDate = "DateTime <= '" + toDate + "'";
        } else
            toDate = null;

        String status = functions.getString(editText5);
        if (!status.isEmpty()) {
            i = 1;
            if (status.equalsIgnoreCase("Opened"))
                status = "IsCompleted = '0'";
            else if (status.equalsIgnoreCase("Closed"))
                status = "IsCompleted = '1'";
        } else
            status = null;

        String siteNature = functions.getString(editText4);
        if (!siteNature.isEmpty()) {
            i = 1;
            siteNature = "NatureOfSite = '" + siteNature + "'";
        } else
            siteNature = null;

        String warrantyStatus = functions.getString(editText6);
        if (!warrantyStatus.isEmpty()) {
            i = 1;
            warrantyStatus = "WarrantyStatus = '" + warrantyStatus + "'";
        } else
            warrantyStatus = null;

        if (i == 1) {
            if (search != null)
                query.Query = query.Query + search + " AND ";
            if (fromDate != null)
                query.Query = query.Query + fromDate + " AND ";
            if (toDate != null)
                query.Query = query.Query + toDate + " AND ";
            if (status != null)
                query.Query = query.Query + status + " AND ";
            if (siteNature != null)
                query.Query = query.Query + siteNature + " AND ";
            if (warrantyStatus != null)
                query.Query = query.Query + warrantyStatus;
            if (!user.isAdmin())
                query.Query = query.Query + "(Email = '" + user.Email +
                        "' OR Email = '" + user.EmployeeID + "' OR AllottedTo = '" + user.Email +
                        "' OR AllottedTo = '" + user.EmployeeID + "')";
            else if (query.Query.endsWith(" AND "))
                query.Query = query.Query.substring(0, query.Query.length() - 5);
            query.Query = query.Query + " ORDER BY IsCompleted ASC, DateTime ASC";
        } else {
            if (user.isAdmin())
                query.Query = "SELECT * FROM RegisteredCallsX ORDER BY IsCompleted ASC, DateTime ASC";
            else
                query.Query = "SELECT * FROM RegisteredCallsX WHERE (Email = '" + user.Email +
                        "' OR Email = '" + user.EmployeeID + "' OR AllottedTo = '" + user.Email +
                        "' OR AllottedTo = '" + user.EmployeeID + "') ORDER BY IsCompleted ASC," +
                        " DateTime ASC";
        }
        query.Table = "Registrations";
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
        final CharSequence[] options1 = new CharSequence[]{"Complaint", "New Commissioning"};
        final CharSequence[] options2 = new CharSequence[]{"Yes", "No", "To be checked"};
        final CharSequence[] options3 = new CharSequence[]{"All", "Opened", "Closed"};

        dialogNos = new AlertDialog.Builder(getContext())
                .setTitle(R.string.nature_of_site)
                .setItems(options1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editText4.setText(options1[which]);
                        dialog.dismiss();
                    }
                })
                .create();

        dialogStatus = new AlertDialog.Builder(getContext())
                .setTitle(R.string.status)
                .setItems(options3, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editText5.setText(options3[which]);
                        dialog.dismiss();
                    }
                })
                .create();

        dialogWarranty = new AlertDialog.Builder(getContext())
                .setTitle(R.string.warranty)
                .setItems(options2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editText6.setText(options2[which]);
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
            dialogNos.show();
        if (view.getId() == R.id.editText5)
            dialogStatus.show();
        if (view.getId() == R.id.editText6)
            dialogWarranty.show();
        if (view.getId() == R.id.button1) {
            dateTo = null;
            dateFrom = null;
            editText1.setText(null);
            editText2.setText(null);
            editText3.setText(null);
            editText4.setText(null);
            editText5.setText(null);
            editText6.setText(null);
            calendar = Calendar.getInstance();
        }
        if (view.getId() == R.id.button2) {
            ((ResultListener) getActivity()).onResultChange(getQuery(), false);
            dismiss();
        }
    }
}