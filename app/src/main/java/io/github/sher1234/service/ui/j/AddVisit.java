package io.github.sher1234.service.ui.j;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskVisit;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.service.LocationTrack;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.form.listener.OnFormElementValueChange;
import io.github.sher1234.service.util.form.model.FormElement;

public class AddVisit extends AppCompatActivity
        implements OnFormElementValueChange, View.OnClickListener, TaskVisit.TaskUpdate {

    private final Common common = new Common();
    private FloatingActionButton fab;
    private LocationTrack mTrack;
    private TaskVisit taskV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_1);
        Call call = (Call) getIntent().getSerializableExtra(Strings.ExtraData);
        if (call == null) {
            Toast.makeText(this, "Unspecified error", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((AppCompatTextView) findViewById(R.id.textViewTitle)).setText(R.string.register_visit);
        taskV = TaskVisit.VisitAdd(this, call, this);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        setResult(102);
        fab.hide();
    }

    @NotNull
    private String getLocationString() {
        Location l;
        if (mTrack.isLocationAvailable()) {
            if ((l = mTrack.getGpsLocation()) == null)
                return (l = mTrack.getNetworkLocation()) != null ? l.getLatitude() + "-#-" + l.getLongitude() : "";
            return l.getLatitude() + "-#-" + l.getLongitude();
        }
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTrack != null) mTrack.stopSelf();
        mTrack = new LocationTrack(this);
        if (!mTrack.isLocationAvailable()) {
            Toast.makeText(this, "Location service unavailable", Toast.LENGTH_SHORT).show();
            mTrack.showSettingsAlert();
        }
    }

    @Override
    public void onValueChanged(FormElement formElement) {
        if (taskV.isFullFormValid()) fab.show();
        else fab.hide();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) fetch();
    }

    @Override
    public void onFetched(Responded response, int i) {
        common.dismissProgressDialog();
        if (response != null) {
            Snackbar.make(fab, response.Message, Snackbar.LENGTH_SHORT).show();
            if (response.Code == 1) taskV.onVisitAdded(this);
        } else {
            if (i == 306) Snackbar.make(fab, "Content parse error", Snackbar.LENGTH_LONG).show();
            if (i == 308) Snackbar.make(fab, "Request cancelled", Snackbar.LENGTH_LONG).show();
            if (i == 307) Snackbar.make(fab, "Network failure", Snackbar.LENGTH_LONG).show();
            taskV.onNetworkError(this, this);
        }
    }

    @Override
    public void onFetch() {
        common.showProgressDialog(this);
        setResult(100);
    }

    @Override
    public void fetch() {
        if (getLocationString().isEmpty()) getLocationString();
        taskV.onAddVisit(this, taskV.getVisit(getLocationString()));
    }
}