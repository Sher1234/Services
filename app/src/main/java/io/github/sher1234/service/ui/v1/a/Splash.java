package io.github.sher1234.service.ui.v1.a;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.AccountJson;
import io.github.sher1234.service.ui.v1.a.fragment.Login;
import io.github.sher1234.service.ui.v1.a.fragment.Register;
import io.github.sher1234.service.ui.v1.b.Board;

public class Splash extends AppCompatActivity implements View.OnClickListener, TaskUser.TaskUpdate {

    private final TaskUser functions = new TaskUser();
    private AppCompatTextView textViewTitle;
    private View viewA, viewB;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity);

        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        textViewTitle = findViewById(R.id.textViewTitle);
        viewB = findViewById(R.id.linearLayout2);
        viewA = findViewById(R.id.linearLayout1);
        toolbar = findViewById(R.id.toolbar);
        fetch();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] strings;
        strings = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, strings, 1412);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1)
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new Login()).commit();
        else if (v.getId() == R.id.button2)
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new Register()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == 4869) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Accept permissions in settings", Toast.LENGTH_SHORT).show();
                startActivity(
                        new Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getPackageName())
                        )
                );
            }
        }
    }

    public void onFragmentAttached(@StringRes int s) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back);
        textViewTitle.setText(s);
    }

    public void onFragmentDetached() {
        toolbar.setNavigationOnClickListener(null);
        toolbar.setNavigationIcon(null);
        textViewTitle.setText(null);
    }

    @Override
    public void fetch() {
        User user = functions.getUser();
        if (user != null && user.Email != null && user.Password != null)
            functions.onLoginUser(this, user.Email, user.Password);
        else
            viewB.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetch() {
        viewA.setVisibility(View.VISIBLE);
        viewB.setVisibility(View.GONE);
    }

    @Override
    public void onFetched(@Nullable AccountJson account, int i) {
        viewB.setVisibility(View.VISIBLE);
        viewA.setVisibility(View.GONE);
        if (account != null) {
            Snackbar.make(viewB, account.Message, Snackbar.LENGTH_SHORT).show();
            if (account.Code == 1) {
                functions.onRefresh(account.User, account.Token);
                onLogin(account.User);
            }
        } else {
            if (i == 306)
                Snackbar.make(viewB, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(viewB, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(viewB, "Request cancelled", Snackbar.LENGTH_LONG).show();
            functions.onNetworkError(this, this);
        }
    }

    private void onLogin(User user) {
        Log.v("Status", "Login complete");
        if (user != null && !user.isRegistered())
            functions.onAccountError(this, this);
        else {
            startActivity(new Intent(this, Board.class));
            finish();
        }
    }
}