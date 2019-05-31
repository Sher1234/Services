package io.github.sher1234.service.ui.v2.a.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.github.sher1234.service.R;
import io.github.sher1234.service.firebase.model.user.User;
import io.github.sher1234.service.functions.v4.OnEvent;
import io.github.sher1234.service.functions.v4.user.FireUser;
import io.github.sher1234.service.functions.v4.user.OnGetData;
import io.github.sher1234.service.ui.v2.b.Board;
import io.github.sher1234.service.util.MaterialDialog;

public class Start extends Fragment implements View.OnClickListener, OnEvent<User> {

    private View buttonView, progressView;
    private FireUser fireUser;
    private OnClick onClick;

    public Start() {
        fireUser = new FireUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        View view = inflater.inflate(R.layout.a_fragment_0, group, false);
        view.findViewById(R.id.button2).setOnClickListener(this);
        view.findViewById(R.id.button1).setOnClickListener(this);
        progressView = view.findViewById(R.id.progressView);
        buttonView = view.findViewById(R.id.buttonView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressView.setVisibility(View.GONE);
        buttonView.setVisibility(View.GONE);
        if (!fireUser.isLoggedIn()) buttonView.setVisibility(View.VISIBLE);
        else new OnGetData(this).onGetData(fireUser.getUser().uid);
    }

    private void onAccountInvalid(final User user) {
        Toast.makeText(getContext(), "Complete account details.", Toast.LENGTH_SHORT).show();
        MaterialDialog dialog = MaterialDialog.Dialog(getContext());
        dialog.setTitle("Incomplete account")
                .setDescription(R.string.acc_disabled)
                .positiveButton("Next", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        Toast.makeText(getContext(), "Complete account...", Toast.LENGTH_SHORT).show();
                        onClick.onClick(Incomplete.newInstance(user));
                    }
                })
                .negativeButton(R.string.sign_out, new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        fireUser.signOut();
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                })
                .neutralButton("Cancel", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof OnClick)
            onClick = (OnClick) getActivity();
    }

    @Override
    public void onPostExecute(User user) {
        progressView.setVisibility(View.GONE);
        buttonView.setVisibility(View.VISIBLE);
        if (!user.isValid()) onAccountInvalid(user);
        else if (user.enabled) {
            Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), Board.class));
            assert getActivity() != null; getActivity().finish();
        } else onAccountDisabled();
    }

    private void onAccountDisabled() {
        Toast.makeText(getContext(), "Account disabled.", Toast.LENGTH_SHORT).show();
        MaterialDialog dialog = MaterialDialog.Dialog(getContext());
        dialog.setTitle("Account disabled")
                .setDescription(R.string.acc_disabled)
                .positiveButton(R.string.sign_out, new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        fireUser.signOut();
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                })
                .negativeButton("Cancel", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (onClick == null) return;
        if (view.getId() == R.id.button1) onClick.onClick(Login.newInstance());
        if (view.getId() == R.id.button2) onClick.onClick(Register.newInstance());
    }

    public void onNetworkError() {
        MaterialDialog dialog = MaterialDialog.Dialog(getContext());
        dialog.setTitle("Network issue").setDescription(R.string.offline_access)
                .negativeButton("Exit", new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                })
                .setCancelable(false);
        dialog.show();
    }

    @Override
    public void onPreExecute() {
        progressView.setVisibility(View.VISIBLE);
        buttonView.setVisibility(View.GONE);
    }

    public interface OnClick {
        void onClick(Fragment fragment);
    }
}