package io.github.sher1234.service.ui.v2.a;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import io.github.sher1234.service.R;
import io.github.sher1234.service.ui.v2.a.fragment.Incomplete;
import io.github.sher1234.service.ui.v2.a.fragment.Start;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Splash extends AppCompatActivity implements Start.OnClick {

    private final String[] permissions = new String[] {INTERNET, ACCESS_FINE_LOCATION,
            READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, WRITE_EXTERNAL_STORAGE};
    private final int requestCode = 2141;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragment != null && !(fragment instanceof Incomplete))
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        else super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.requestCode && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName())));
            Toast.makeText(this, "Accept permissions in settings", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }
}