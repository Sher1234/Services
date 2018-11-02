package io.github.sher1234.service.ui.i.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskCall;
import io.github.sher1234.service.functions.TaskShow;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.model.base.Call;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.Responded;
import io.github.sher1234.service.model.response.Services;
import io.github.sher1234.service.ui.d.AllotCall;
import io.github.sher1234.service.ui.d.EditCall;
import io.github.sher1234.service.ui.i.Show;
import io.github.sher1234.service.util.MaterialDialog;
import io.github.sher1234.service.util.Strings;

public class ShowCall extends Fragment implements TaskCall.TaskUpdate {

    private final Common common;
    private final TaskShow show;
    private final TaskCall call;
    private AppCompatTextView textViewA, textViewB;
    private Services services;

    public ShowCall() {
        common = new Common();
        show = new TaskShow();
        call = new TaskCall();
    }

    public static ShowCall getInstance(Services services) {
        ShowCall showCall = new ShowCall();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, services);
        showCall.setArguments(bundle);
        return showCall;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        services = (Services) getArguments().getSerializable(Strings.ExtraData);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.i_fragment_1, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        show.onCreateCallForm(getContext(), recyclerView);
        textViewA = view.findViewById(R.id.text1);
        textViewB = view.findViewById(R.id.text2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (services != null) {
            show.onShowCall(services.call, services.user1, services.user2);
            textViewA.setText(services.call.CallID);
            if (services.call.isCompleted()) {
                textViewA.setTextColor(getResources().getColor(R.color.green));
                textViewB.setTextColor(getResources().getColor(R.color.green));
                textViewB.setBackgroundResource(R.drawable.tag_green);
                textViewB.setText(R.string.completed);
            } else {
                textViewA.setTextColor(getResources().getColor(R.color.red));
                textViewB.setTextColor(getResources().getColor(R.color.red));
                textViewB.setBackgroundResource(R.drawable.tag_red);
                textViewB.setText(R.string.pending);
            }
        } else
            Toast.makeText(getContext(), "Invalid call", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.call, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        User user = new TaskUser().getUser();
        if (user.isAdmin()) {
            if (services.call.isCompleted())
                menu.findItem(R.id.allot).setVisible(false).setEnabled(false);
        } else {
            menu.findItem(R.id.allot).setVisible(false).setEnabled(false);
            menu.findItem(R.id.delete).setVisible(false).setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.allot) {
            startActivity(AllotCall.class, services.call);
            return true;
        } else if (item.getItemId() == R.id.edit) {
            startActivity(EditCall.class, services.call);
            return true;
        } else if (item.getItemId() == R.id.delete) {
            onDeleteCall();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    private void startActivity(Class c, Call call) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra(Strings.ExtraData, call);
        startActivityForResult(intent, 1412);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1412 && (resultCode == 101 || resultCode == 100))
            Snackbar.make(textViewA, "Data Changed, Reload Call", Snackbar.LENGTH_SHORT)
                    .setAction("Reload", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            assert getActivity() != null;
                            ((Show) getActivity()).onReloadCall();
                        }
                    }).show();
    }

    private void onDeleteCall() {
        assert getContext() != null;
        MaterialDialog dialog = MaterialDialog.Dialog(getContext());
        dialog.setDescription(R.string.delete_call_text).setTitle(R.string.delete_call);
        dialog.positiveButton(R.string.delete, new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                fetch();
                dialog.dismiss();
            }
        }).negativeButton(R.string.cancel, new MaterialDialog.ButtonClick() {
            @Override
            public void onClick(MaterialDialog dialog, View v) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onFetched(Responded response, int i) {
        common.dismissProgressDialog();
        if (response != null) {
            Snackbar.make(textViewA, response.Message, Snackbar.LENGTH_SHORT).show();
            if (response.Code == 1) call.onCallDeleted(getActivity());
        } else {
            if (i == 306)
                Snackbar.make(textViewA, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(textViewA, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(textViewA, "Request cancelled", Snackbar.LENGTH_LONG).show();
            call.onNetworkError(getContext(), this);
        }
    }

    @Override
    public void onFetch() {
        assert getActivity() != null && getContext() != null;
        common.showProgressDialog(getContext());
        getActivity().setResult(100);
    }

    @Override
    public void fetch() {
        assert getActivity() != null;
        call.onDeleteCall(this, services.call.CallID);
    }
}