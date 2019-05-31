package io.github.sher1234.service.ui.v1.a.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.AccountJson;
import io.github.sher1234.service.ui.v1.a.Splash;
import io.github.sher1234.service.ui.v1.b.Board;

public class Login extends Fragment implements View.OnClickListener,
        TextWatcher, TaskUser.TaskUpdate {

    private final TaskUser taskU = new TaskUser();
    private final Common common = new Common();
    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private MaterialButton button;

    public Login() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_fragment_1, container, false);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText2.addTextChangedListener(this);
        editText1.addTextChangedListener(this);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
        button.setEnabled(false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText1.setText("");
        editText2.setText("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        assert getActivity() != null;
        ((Splash) getActivity()).onFragmentAttached(R.string.action_sign_in_short);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        assert getActivity() != null;
        ((Splash) getActivity()).onFragmentDetached();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button)
            taskU.onLoginUser(this, common.getString(editText1), common.getString(editText2));
    }

    private boolean isUserIdInvalid() {
        return common.isEmailInvalid(common.getString(editText1)) &&
                common.isEmployeeIdInvalid(common.getString(editText1));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        button.setEnabled(true);
        if (isUserIdInvalid())
            button.setEnabled(false);
        if (common.isPasswordInvalid(common.getString(editText2)))
            button.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFetched(@Nullable AccountJson account, int i) {
        common.dismissProgressDialog();
        if (account != null) {
            Snackbar.make(editText2, account.Message, Snackbar.LENGTH_SHORT).show();
            if (account.Code == 1) {
                taskU.onLogin(account.User, account.Token);
                onLogin(account.User);
            }
        } else {
            if (i == 306)
                Snackbar.make(editText2, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(editText2, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(editText2, "Request cancelled", Snackbar.LENGTH_LONG).show();
            assert getContext() != null;
            taskU.onNetworkError2(getContext(), this);
        }
    }

    @Override
    public void onFetch() {
        if (getContext() != null)
            common.showProgressDialog(getContext());
    }

    @Override
    public void fetch() {
        taskU.onLoginUser(this, common.getString(editText1), common.getString(editText2));
    }

    private void onLogin(User user) {
        Log.v("Status", "Login complete");
        if (user != null && !user.isRegistered())
            taskU.onAccountError(getActivity(), this);
        else {
            assert getActivity() != null;
            startActivity(new Intent(getActivity(), Board.class));
            getActivity().finish();
        }
    }
}