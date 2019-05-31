package io.github.sher1234.service.ui.v1.e.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.ui.v1.e.EmployeeList;
import io.github.sher1234.service.util.Strings;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Excel extends Fragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final Common common = new Common();
    private DateFormat format1, format2;
    private String start, end, name;
    private List<Integer> years;
    private Calendar calendar;

    private AppCompatTextView textView;
    private Spinner spinner1, spinner2;
    private MaterialButton button;

    private int month = 1412, year = 1412;

    public Excel() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null)
            ((EmployeeList) getActivity()).onFragmentAttached();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null)
            ((EmployeeList) getActivity()).onFragmentDetached();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        format2 = new SimpleDateFormat(Strings.DateServer, Locale.US);
        format1 = new SimpleDateFormat(Strings.MonthView, Locale.US);
        calendar = Calendar.getInstance();
        years = new ArrayList<>();
        for (int i = 2018; i <= calendar.get(Calendar.YEAR); i++)
            years.add(i);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.e_fragment, container, false);
        textView = view.findViewById(R.id.textView);
        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        button = view.findViewById(R.id.button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getContext() != null;
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getContext(), R.array.months, android.R.layout.simple_spinner_item);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button)
            new DownloadTask().execute();
    }

    private void onDateChange() {
        if (year == 1412 || month == 1412)
            return;
        calendar.set(year, month, 1);
        start = format2.format(calendar.getTime());
        name = format1.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        end = format2.format(calendar.getTime());
        textView.setText(name);
        name = "Report - " + name + ".xlsx";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner1)
            month = position;
        else if (parent.getId() == R.id.spinner2)
            year = years.get(position);
        onDateChange();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, Void, Boolean> {

        private File file;
        private int i = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            assert getContext() != null;
            common.showProgressDialog(getContext());
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Retrofit retrofit = AppController.getInstance().getRetrofitRequest(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<ResponseBody> call = api.onDownloadFile(start, end);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call,
                                       @NonNull Response<ResponseBody> response) {
                    if (response.body() != null && downloadFile(response.body()))
                        i = 1;
                    else
                        i = -1;
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            common.dismissProgressDialog();
            if (i == 1) {
                Snackbar.make(spinner1, "File Downloaded", Snackbar.LENGTH_SHORT).show();
            } else
                Snackbar.make(spinner1, "Error downloading file", Snackbar.LENGTH_SHORT).show();
        }

        private boolean downloadFile(@NotNull ResponseBody body) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String ESD = Environment.getExternalStorageDirectory().getPath();
            File folder = new File(ESD, "Reports");
            if (folder.mkdir())
                file = new File(folder, name);
            else
                file = new File(folder, name);
            try {
                long downloaded = 0;
                byte[] bytes = new byte[4096];
                long size = body.contentLength();
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(bytes);
                    if (read == -1)
                        break;
                    outputStream.write(bytes, 0, read);
                    downloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}