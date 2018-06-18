package io.github.sher1234.service.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.Registration;

/**
 * ListRecycler
 */

public class RegisterRecycler extends RecyclerView.Adapter<RegisterRecycler.ViewHolder> {

    private final Context context;
    private List<Registration> registrations;

    public RegisterRecycler(Context context, List<Registration> registrations) {
        this.context = context;
        this.registrations = registrations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterRecycler.ViewHolder holder, int position) {
        try {
            final Registration registration = registrations.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(registration.getDateTime());
            String s;
            try {
                @SuppressLint("SimpleDateFormat")
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
                s = dateFormat.format(calendar.getTime());
            } catch (Exception e) {
                s = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1)
                        + "/" + calendar.get(Calendar.YEAR) + ", " + calendar.get(Calendar.HOUR_OF_DAY)
                        + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
                e.printStackTrace();
            }
            s = registration.getCallNumber() + ", " + s;
            holder.textView2.setText(s);
            holder.textView1.setText(registration.getCustomerName());
            if (registrations.get(position).isCompleted()) {
                holder.imageView.setImageResource(R.drawable.ic_completed);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_pending);
            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, registration.getCallNumber(), Toast.LENGTH_SHORT).show();
                    // new GetTask(registration.getCallNumber()).execute();
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return registrations.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView1;
        final TextView textView2;
        final ImageView imageView;
        final View layout;

        ViewHolder(View view) {
            super(view);
            layout = view;
            textView1 = layout.findViewById(R.id.textView1);
            textView2 = layout.findViewById(R.id.textView2);
            imageView = layout.findViewById(R.id.imageView);
        }
    }
/*
    @SuppressLint("StaticFieldLeak")
    public class GetTask extends AsyncTask<Void, Void, ServiceCall> {

        private int i = 0;
        private String callNumber;

        GetTask(String callNumber) {
            this.callNumber = callNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((AllCalls) context).showProgress(true);
        }

        @Override
        protected ServiceCall doInBackground(Void... params) {
            String s = Login.API + "call-d&cn=" + this.callNumber;
            try {
                URL url = new URL(s);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                try {
                    httpURLConnection.connect();
                    BufferedReader bufferedReader = new BufferedReader(new
                            InputStreamReader(httpURLConnection.getInputStream()));
                    if (i == 0)
                        i = 1;
                    return new Gson().fromJson(bufferedReader, ServiceCall.class);
                } finally {
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                }
            } catch (IOException e) {
                i = -1;
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ServiceCall serviceCall) {
            ((AllCalls) context).showProgress(false);
            if (serviceCall != null && i == 1) {
                Intent intent = new Intent(context, ViewCall.class);
                intent.putExtra("extra-sc", serviceCall);
                context.startActivity(intent);
            } else
                Alerter.create((AppCompatActivity) context).setTitle("Error Fetching Details")
                        .enableInfiniteDuration(true).enableIconPulse(true).disableOutsideTouch()
                        .setIcon(R.drawable.ic_error).enableProgress(true)
                        .setBackgroundColorRes(android.R.color.holo_red_dark).show();
        }

        @Override
        protected void onCancelled() {
            ((AllCalls) context).showProgress(false);
        }
    }
*/
}