package io.github.sher1234.service.activity.common;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.api.Api;
import io.github.sher1234.service.model.base.Responded;
import io.github.sher1234.service.util.Functions;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.SignatureView;
import io.github.sher1234.service.util.Strings;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Signature extends AppCompatActivity implements View.OnClickListener {

    private final Functions functions = new Functions();
    private AppCompatImageView imageView;
    private FrameLayout frameLayout;
    private View progressView;
    private SignTask task;
    private String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        setResult(101);

        imageView = findViewById(R.id.imageView);
        frameLayout = findViewById(R.id.frameLayout);
        progressView = findViewById(R.id.progressView);
        TextView textView = findViewById(R.id.textView);
        findViewById(R.id.imageButton1).setOnClickListener(this);
        findViewById(R.id.imageButton2).setOnClickListener(this);
        findViewById(R.id.imageButton3).setOnClickListener(this);

        string = getIntent().getStringExtra(Strings.ExtraData);
        if (string == null || string.isEmpty()) {
            Toast.makeText(this, "Invalid visit...", Toast.LENGTH_SHORT).show();
            setResult(404);
            finish();
            return;
        }
        String s = "for " + string;
        textView.setText(s);
        resetView();
    }

    private void resetView() {
        frameLayout.removeAllViewsInLayout();
        frameLayout.addView(new SignatureView(this));
    }

    @Nullable
    private File getSavedSign() {
        frameLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(frameLayout.getDrawingCache());
        frameLayout.setDrawingCacheEnabled(false);
        try {
            File file = File.createTempFile("Sign-" + string, ".jpg", getCacheDir());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
        task = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButton1) {
            File file = getSavedSign();
            if (file != null && file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
                confirmDialog(file);
            }
        } else if (view.getId() == R.id.imageButton2) {
            imageView.setImageBitmap(null);
            resetView();
        } else if (view.getId() == R.id.imageButton3)
            finish();
    }

    private void confirmDialog(final File file) {
        final MaterialDialog materialDialog = new MaterialDialog(this)
                .setDescription("Press ok to add signature to the visit.");
        materialDialog.positiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task != null)
                    task.cancel(true);
                task = null;
                task = new SignTask(string, file);
                task.execute();
                materialDialog.dismiss();
            }
        }).negativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);
                materialDialog.dismiss();
            }
        }).setTitle(R.string.add_signature);
        materialDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    private class SignTask extends AsyncTask<Void, Void, Boolean> {

        private final String string;
        private final File file;
        private Responded responded;
        private int i = 4869;

        SignTask(String string, File file) {
            this.string = string;
            this.file = file;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setResult(200);
            functions.showProgress(true, progressView);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            Retrofit retrofit = AppController.getRetrofit(Api.BASE_URL);
            Api api = retrofit.create(Api.class);
            Call<Responded> call = api.SignVisit(string, part);
            call.enqueue(new Callback<Responded>() {
                @Override
                public void onResponse(@NonNull Call<Responded> call, @NonNull Response<Responded> response) {
                    if (response.body() != null) {
                        responded = response.body();
                        i = responded.Code;
                    } else {
                        responded = null;
                        i = 306;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Responded> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    responded = null;
                    i = 307;
                }
            });
            while (true) {
                if (isCancelled()) {
                    i = 308;
                    responded = null;
                    return true;
                }
                if (i != 4869)
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            functions.showProgress(false, progressView);
            if (responded != null) {
                Toast.makeText(Signature.this, responded.Message, Toast.LENGTH_LONG).show();
                if (responded.Code == 1)
                    Signature.this.finish();
            } else if (i == 306)
                Toast.makeText(Signature.this, "Content parse error...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(Signature.this, "Network failure, try again...", Toast.LENGTH_SHORT).show();
            else if (i == 308)
                Toast.makeText(Signature.this, "Request cancelled...", Toast.LENGTH_SHORT).show();
        }
    }
}