package io.github.sher1234.service.ui.v1.c;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.adapter.CallsAdapter;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.FilterCalls;
import io.github.sher1234.service.functions.Navigate;
import io.github.sher1234.service.functions.TaskCallList;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.Query;
import io.github.sher1234.service.model.response.Calls;
import io.github.sher1234.service.ui.v1.i.Show;
import io.github.sher1234.service.util.Strings;

public class CallList extends AppCompatActivity
        implements TaskCallList.TaskUpdate, Toolbar.OnMenuItemClickListener, CallsAdapter.ItemClick {

    private final Common funH = new Common();
    private final TaskCallList funL = new TaskCallList();
    private FilterCalls funFC;
    private Navigate funN;

    private RecyclerView recyclerView;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity);
        funN = new Navigate(this, 2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        funFC = new FilterCalls(this, this);
        toolbar.inflateMenu(R.menu.filter);
        toolbar.setOnMenuItemClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        ((FloatingActionButton) findViewById(R.id.fab)).hide();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetch(funFC.getQuery());
    }

    @Override
    protected void onResume() {
        super.onResume();
        funN.onResumeActivity();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            return;
        }
        exit = true;
        Snackbar.make(recyclerView, "Press back again to exit", Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exit = false;
            }
        }, 2000);
    }

    private void onRefresh(List<Call> calls) {
        CallsAdapter callRecycler;
        if (recyclerView.getAdapter() != null) {
            callRecycler = (CallsAdapter) recyclerView.getAdapter();
            callRecycler.onUpdateCalls(calls);
        } else {
            callRecycler = new CallsAdapter(this, calls);
            recyclerView.setAdapter(callRecycler);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.filter) {
            funFC.onToggleFilter();
            return true;
        } else if (menuItem.getItemId() == R.id.refresh) {
            fetch(funFC.getQuery());
            return true;
        }
        return false;
    }

    @Override
    public void onFetched(@Nullable Calls calls, int i) {
        funH.dismissProgressDialog();
        if (calls != null) {
            Snackbar.make(recyclerView, calls.Message, Snackbar.LENGTH_SHORT).show();
            onRefresh(calls.Calls);
        } else {
            if (i == 306)
                Snackbar.make(recyclerView, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(recyclerView, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(recyclerView, "Request cancelled", Snackbar.LENGTH_LONG).show();
            funL.onNetworkError(this, this, funFC.getQuery());
        }
    }

    @Override
    public void fetch(Query query) {
        funL.onCallsRefresh(query, this);
    }

    @Override
    public void onFetch() {
        funH.showProgressDialog(this);
    }

    @Override
    public void onItemClick(Call call, View v) {
        Intent intent = new Intent(this, Show.class);
        intent.putExtra(Strings.ExtraString, call.CallID);
        startActivity(intent);
    }
}