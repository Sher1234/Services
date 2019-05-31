package io.github.sher1234.service.ui.v1.g.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.AccountJson;
import io.github.sher1234.service.ui.v1.g.Profile;

public class ChangePassword extends Fragment implements View.OnClickListener, TaskUser.TaskUpdate {

    private final Common common = new Common();
    private TextInputEditText text1, text2, text3, text4;
    private TaskUser taskU;
    private User user;

    public ChangePassword() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null)
            ((Profile) getActivity()).onFragmentAttached(R.string.change_password);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null)
            ((Profile) getActivity()).onFragmentDetached();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskU = new TaskUser();
        user = taskU.getUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.g_fragment_2, container, false);
        view.findViewById(R.id.button2).setOnClickListener(this);
        view.findViewById(R.id.button1).setOnClickListener(this);
        text4 = view.findViewById(R.id.text4);
        text3 = view.findViewById(R.id.text3);
        text2 = view.findViewById(R.id.text2);
        text1 = view.findViewById(R.id.text1);
        return view;
    }

    private void onReset() {
        if (user != null) {
            text1.setText(user.Email);
        } else {
            String s = "null";
            text1.setText(s);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onReset();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            assert getActivity() != null;
            getActivity().onBackPressed();
        } else if (v.getId() == R.id.button2) {
            if (common.isPasswordInvalid(common.getString(text3))) {
                Snackbar.make(v, "Invalid data", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (!common.getString(text2).equals(user.Password)) {
                Snackbar.make(v, "Invalid old password", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (!common.getString(text3).equals(common.getString(text4))) {
                Snackbar.make(v, "New password mismatch", Snackbar.LENGTH_SHORT).show();
                return;
            }
            fetch();
        }
    }

    @Override
    public void onFetched(@Nullable AccountJson account, int i) {
        common.dismissProgressDialog();
        if (account != null) {
            if (account.Message.contains("Signing"))
                Snackbar.make(text1, "Password changed", Snackbar.LENGTH_SHORT).show();
            if (account.Code == 1)
                taskU.onRefresh(account.User, account.Token);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    assert getActivity() != null;
                    getActivity().onBackPressed();
                }
            }, 600);
        } else {
            if (i == 306)
                Snackbar.make(text1, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(text1, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(text1, "Request cancelled", Snackbar.LENGTH_LONG).show();
            assert getContext() != null;
            taskU.onNetworkError2(getContext(), this);
        }
    }

    @Override
    public void onFetch() {
        assert getContext() != null;
        common.showProgressDialog(getContext());
    }

    @Override
    public void fetch() {
        taskU.onChangePassword(this, getPasswordMap());
    }

    private Map<String, String> getPasswordMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Password", common.getString(text2));
        map.put("Value", common.getString(text4));
        map.put("UserID", user.UserID);
        map.put("Email", user.Email);
        return map;
    }
}