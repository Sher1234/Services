package io.github.sher1234.service.ui.j;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskVisit;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.SignatureView;
import io.github.sher1234.service.util.Strings;

public class SignVisit extends AppCompatActivity
        implements View.OnClickListener, TaskVisit.TaskUpdate {

    private final Common common = new Common();
    private AppCompatImageView imageView;
    private FrameLayout frameLayout;
    private TaskVisit taskVisit;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity);
        id = getIntent().getStringExtra(Strings.ExtraData);
        if (id == null || id.isEmpty() || id.length() < 4) {
            Toast.makeText(this, "Invalid visit", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        setResult(102);
        String s = "for " + id;
        taskVisit = new TaskVisit();
        imageView = findViewById(R.id.imageView);
        frameLayout = findViewById(R.id.frameLayout);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        ((AppCompatTextView) findViewById(R.id.textView)).setText(s);
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        onResetView();
    }

    private void onResetView() {
        frameLayout.removeAllViewsInLayout();
        frameLayout.addView(new SignatureView(this));
    }

    @Nullable
    private File getSavedSignFile() {
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(frameLayout.getDrawingCache());
        frameLayout.setDrawingCacheEnabled(false);
        try {
            File file = File.createTempFile("SIGN_" + id, ".jpg", getCacheDir());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2) {
            onConfirmDialog();
        } else if (view.getId() == R.id.button1) {
            imageView.setImageBitmap(null);
            onResetView();
        }
    }

    private void onConfirmDialog() {
        MaterialDialog dialog = MaterialDialog.Dialog(this)
                .setDescription("Tap on \"Ok\" to add signature to the visit.")
                .positiveButton("Ok", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        fetch();
                    }
                }).negativeButton("Cancel", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        imageView.setImageBitmap(null);
                        dialog.dismiss();
                    }
                });
        dialog.setTitle(R.string.add_signature);
        dialog.show();
    }

    @Override
    public void onFetched(Responded response, int i) {
        common.dismissProgressDialog();
        if (response != null) {
            Snackbar.make(imageView, response.Message, Snackbar.LENGTH_SHORT).show();
            if (response.Code == 1) taskVisit.onVisitSigned(this);
        } else {
            if (i == 306)
                Snackbar.make(imageView, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(imageView, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(imageView, "Request cancelled", Snackbar.LENGTH_LONG).show();
            taskVisit.onNetworkError(this, this);
        }
    }

    @Override
    public void onFetch() {
        common.showProgressDialog(this);
        setResult(100);
    }

    @Override
    public void fetch() {
        File file = getSavedSignFile();
        if (file != null && file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            taskVisit.onSignVisit(this, id, file);
        } else Snackbar.make(imageView, "IO Error", Snackbar.LENGTH_SHORT).show();

    }
}