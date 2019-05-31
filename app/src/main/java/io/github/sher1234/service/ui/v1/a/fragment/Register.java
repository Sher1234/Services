package io.github.sher1234.service.ui.v1.a.fragment;

import android.content.Context;
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

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskUser;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.model.response.AccountJson;
import io.github.sher1234.service.ui.v1.a.Splash;

public class Register extends Fragment
        implements View.OnClickListener, TextWatcher, TaskUser.TaskUpdate {

    private final Common functionsH = new Common();
    private final TaskUser functionsU = new TaskUser();
    private TextInputEditText editText1, editText2, editText3, editText4, editText5;
    private MaterialButton button;

    public Register() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_fragment_2, container, false);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        editText4.addTextChangedListener(this);
        editText5.addTextChangedListener(this);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText1.setText("");
        editText2.setText("");
        editText3.setText("");
        editText4.setText("");
        editText5.setText("");
    }

    @NonNull
    private String getString(@NotNull TextInputEditText editText) {
        if (editText.getText() == null)
            return "";
        return editText.getText().toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        assert getActivity() != null;
        ((Splash) getActivity()).onFragmentAttached(R.string.register);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        assert getActivity() != null;
        ((Splash) getActivity()).onFragmentDetached();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                User user = new User();
                user.Name = getString(editText1);
                user.Email = getString(editText2);
                user.EmployeeID = getString(editText3);
                user.Phone = getString(editText4);
                user.Password = getString(editText5);
                functionsU.onRegisterUser(this, user);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        button.setEnabled(true);
        if (functionsH.isNameInvalid(functionsH.getString(editText1)))
            button.setEnabled(false);
        if (functionsH.isEmailInvalid(functionsH.getString(editText2)))
            button.setEnabled(false);
        if (functionsH.isEmployeeIdInvalid(functionsH.getString(editText3)))
            button.setEnabled(false);
        if (functionsH.isPhoneInvalid(functionsH.getString(editText4)))
            button.setEnabled(false);
        if (functionsH.isPasswordInvalid(functionsH.getString(editText5)))
            button.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFetched(@Nullable AccountJson account, int i) {
        functionsH.dismissProgressDialog();
        if (account != null) {
            Snackbar.make(editText1, account.Message, Snackbar.LENGTH_SHORT).show();
            if (account.Code == 1) {
                functionsU.onLogin(account.User, account.Token);
                onRegister(account.User);
            }
        } else {
            if (i == 306)
                Snackbar.make(editText1, "Content parse error", Snackbar.LENGTH_LONG).show();
            else if (i == 307)
                Snackbar.make(editText1, "Network failure", Snackbar.LENGTH_LONG).show();
            else if (i == 308)
                Snackbar.make(editText1, "Request cancelled", Snackbar.LENGTH_LONG).show();
            assert getContext() != null;
            functionsU.onNetworkError2(getContext(), this);
        }
    }

    @Override
    public void onFetch() {
        if (getContext() != null)
            functionsH.showProgressDialog(getContext());
    }

    @Override
    public void fetch() {
        User user = new User();
        user.Name = getString(editText1);
        user.Email = getString(editText2);
        user.Phone = getString(editText4);
        user.Password = getString(editText5);
        user.EmployeeID = getString(editText3);
        functionsU.onRegisterUser(this, user);
    }

    private void onRegister(User user) {
        Log.v("Status", "Login complete");
        if (user != null && !user.isRegistered())
            functionsU.onAccountError(getActivity(), this);
    }
}