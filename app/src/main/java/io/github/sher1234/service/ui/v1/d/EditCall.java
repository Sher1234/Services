package io.github.sher1234.service.ui.v1.d;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskCall;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.util.Strings;
import io.github.sher1234.service.util.form.listener.OnFormElementValueChange;
import io.github.sher1234.service.util.form.model.FormElement;

public class EditCall extends AppCompatActivity implements OnFormElementValueChange,
        View.OnClickListener, TaskCall.TaskUpdate {

    private final Common common = new Common();
    private FloatingActionButton fab;
    private TaskCall taskCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity);
        Call call = (Call) getIntent().getSerializableExtra(Strings.ExtraData);
        if (call == null) {
            Toast.makeText(this, "Invalid Call ID", Toast.LENGTH_SHORT).show();
            return;
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((AppCompatTextView) findViewById(R.id.textViewTitle)).setText(R.string.edit_call);
        taskCall = TaskCall.CallEdit(this, this);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        taskCall.onResetForm(this, call);
        setResult(102);
    }

    @Override
    public void onValueChanged(FormElement formElement) {
        if (taskCall.isFormValid()) fab.show();
        else fab.hide();
    }

    @Override
    public void onClick(View v) {
        if ((v.getId() == R.id.fab)) fetch();
    }

    @Override
    public void onFetched(Responded response, int i) {
        common.dismissProgressDialog();
        if (response != null) {
            Snackbar.make(fab, response.Message, Snackbar.LENGTH_SHORT).show();
            if (response.Code == 1) taskCall.onCallEdited(this);
        } else {
            if (i == 306)
                Snackbar.make(fab, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(fab, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(fab, "Request cancelled", Snackbar.LENGTH_LONG).show();
            taskCall.onNetworkError(this, this);
        }
    }

    @Override
    public void onFetch() {
        common.showProgressDialog(this);
        setResult(100);
    }

    @Override
    public void fetch() {
        taskCall.onEditCall(this);
    }
}