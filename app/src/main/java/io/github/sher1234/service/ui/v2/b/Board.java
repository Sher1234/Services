package io.github.sher1234.service.ui.v2.b;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.firebase.model.request.Request;
import io.github.sher1234.service.functions.v4.BottomNavDialog;
import io.github.sher1234.service.functions.v4.OnEvent;
import io.github.sher1234.service.util.MaterialDialog;

public class Board extends AppCompatActivity implements Toolbar.OnMenuItemClickListener,
        OnEvent<Void>, Observer<List<Request>> {

    private View.OnClickListener listener1, listener2;
    private BottomNavDialog navDialog;
    private MaterialDialog dialog;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_activity);
        model = ViewModelProviders.of(this).get(Model.class);
        onSetListeners();
        findViewById(R.id.fab).setOnClickListener(listener1);
        BottomAppBar toolbar = findViewById(R.id.toolbar);
        navDialog = BottomNavDialog.newInstance();
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setOnClickListener(listener2);
        toolbar.inflateMenu(R.menu.refresh);
        if (model.requests == null || model.requests.getValue() == null)
            model.refresh();
        model.requests(this).observe(this, this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            model.refresh();
            Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.fragment1);
            Fragment fragment2 = getSupportFragmentManager().findFragmentById(R.id.fragment2);
            Fragment fragment3 = getSupportFragmentManager().findFragmentById(R.id.fragment3);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        onPostExecute(null);
        super.onDestroy();
        model.stop();
    }

    public void onSetListeners() {
        listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab)
                    Toast.makeText(Board.this, R.string.register_call, Toast.LENGTH_SHORT).show();
            }
        };
        listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!navDialog.isVisible())
                    navDialog.show(getSupportFragmentManager(), getLocalClassName());
            }
        };
    }

    @Override
    public void onPostExecute(Void aVoid) {
        if (dialog != null) dialog.dismiss();
        dialog = null;
    }

    @Override
    public void onPreExecute() {
        if (dialog == null)
            dialog = new MaterialDialog(this);
        dialog.show();
    }

    @Override
    public void onChanged(List<Request> requests) {

    }
}