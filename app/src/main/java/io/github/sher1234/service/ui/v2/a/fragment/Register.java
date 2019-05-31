package io.github.sher1234.service.ui.v2.a.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import io.github.sher1234.service.R;
import io.github.sher1234.service.firebase.model.user.User;
import io.github.sher1234.service.functions.v4.OnEvent;
import io.github.sher1234.service.functions.v4.Validator;
import io.github.sher1234.service.functions.v4.user.FireUser;
import io.github.sher1234.service.functions.v4.user.OnSignUp;
import io.github.sher1234.service.ui.v2.b.Board;
import io.github.sher1234.service.util.MaterialDialog;

public class Register extends Fragment implements View.OnClickListener, TextWatcher, OnEvent<User> {

    private TextInputEditText editText1A, editText1B, editText2, editText3, editText4, editText5;
    private static final String ARG_EMAIL = "Email-Passed";
    private MaterialDialog dialog;
    private MaterialButton button;
    private Validator validator;
    private OnSignUp signUp;
    private String email;


    private Register() {
        signUp = new OnSignUp(this);
        validator = new Validator();
    }

    static Register newInstance(String... strings) {
        if (strings == null || strings.length != 1) return new Register();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, strings[0]);
        Register register = new Register();
        register.setArguments(args);
        return register;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) email = getArguments().getString(ARG_EMAIL);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_fragment_2, container, false);
        view.findViewById(R.id.button1).setOnClickListener(this);
        editText1A = view.findViewById(R.id.editText1A);
        editText1B = view.findViewById(R.id.editText1B);
        editText1A.addTextChangedListener(this);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        editText4.addTextChangedListener(this);
        editText5.addTextChangedListener(this);
        button = view.findViewById(R.id.button2);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText2.setText(email);
        editText1A.setText("");
        editText3.setText("");
        editText4.setText("");
        editText5.setText("");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2  && !(validator.isNameInvalid(editText1A) ||
                validator.isEmailInvalid(editText2) || validator.isIdInvalid(editText3) ||
                validator.isPhoneInvalid(editText4) || validator.isPasswordInvalid(editText5))) {
            User user = new User().signUp(validator.getString(editText1A),
                    validator.getString(editText1B), validator.getString(editText4),
                    validator.getString(editText2), validator.getString(editText3), "+91");
            signUp = signUp.onSignUp(user, validator.getString(editText5));
        }
        if (view.getId() == R.id.button1 && getActivity() instanceof Start.OnClick)
            ((Start.OnClick) getActivity()).onClick(Login.newInstance(validator.getString(editText2)));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        button.setEnabled(!(validator.isNameInvalid(editText1A) ||
                validator.isEmailInvalid(editText2) || validator.isIdInvalid(editText3) ||
                validator.isPhoneInvalid(editText4) || validator.isPasswordInvalid(editText5)));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onPostExecute(User user) {
        if (dialog != null) dialog.dismiss();
        if (user.enabled) {
            Toast.makeText(getContext(), "Signing up...", Toast.LENGTH_SHORT).show();
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
                        new FireUser().signOut();
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
    public void onPreExecute() {
        if (dialog == null) dialog = MaterialDialog.Progress(getContext());
        if (dialog != null && !dialog.isShowing()) dialog.show();
    }

    @Override
    public void onDestroy() {
        if (dialog != null) dialog.dismiss();
        super.onDestroy();
        dialog = null;
    }
}