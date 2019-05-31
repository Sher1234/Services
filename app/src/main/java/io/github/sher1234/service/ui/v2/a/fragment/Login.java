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
import io.github.sher1234.service.functions.v4.user.OnSignIn;
import io.github.sher1234.service.ui.v2.b.Board;
import io.github.sher1234.service.util.MaterialDialog;

public class Login extends Fragment implements View.OnClickListener, TextWatcher, OnEvent<User> {

    private static final String ARG_EMAIL = "Email-Passed";
    private TextInputEditText editText1, editText2;
    private MaterialDialog dialog;
    private MaterialButton button;
    private Validator validator;
    private OnSignIn signIn;
    private String email;


    private Login() {
        signIn = new OnSignIn(this);
        validator = new Validator();
    }

    static Login newInstance(String... strings) {
        if (strings == null || strings.length != 1)
            return new Login();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, strings[0]);
        Login login = new Login();
        login.setArguments(args);
        return login;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) email = getArguments().getString(ARG_EMAIL);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_fragment_1, container, false);
        view.findViewById(R.id.button2).setOnClickListener(this);
        view.findViewById(R.id.button1).setOnClickListener(this);
        editText2 = view.findViewById(R.id.editText2);
        editText1 = view.findViewById(R.id.editText1);
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        button = view.findViewById(R.id.button3);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button.setEnabled(false);
        editText1.setText(email);
        editText2.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        button.setEnabled(!(validator.isEmailInvalid(editText1) || validator.isPasswordInvalid(editText2)));
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    @Override
    public void onPostExecute(User user) {
        if (dialog != null) dialog.dismiss();
        if (!user.isValid()) onAccountInvalid(user);
        else if (user.enabled) {
            Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), Board.class));
            assert getActivity() != null; getActivity().finish();
        } else onAccountDisabled();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1 && !validator.isEmailInvalid(editText1))
            signIn.onResetPassword(validator.getString(editText1), getContext());

        if (view.getId() == R.id.button2 && getActivity() instanceof Start.OnClick)
            ((Start.OnClick) getActivity()).onClick(Register.newInstance(validator.getString(editText1)));

        if (view.getId() == R.id.button3 &&
                !(validator.isEmailInvalid(editText1) || validator.isPasswordInvalid(editText2)))
            signIn = signIn.onSignIn(validator.getString(editText1), validator.getString(editText2));
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
                        if (getActivity() instanceof Start.OnClick)
                            ((Start.OnClick) getActivity()).onClick(Incomplete.newInstance(user));
                    }
                })
                .negativeButton(R.string.sign_out, new MaterialDialog.ButtonClick() {
                    @Override
                    public void onClick(MaterialDialog dialog, View v) {
                        new FireUser().signOut();
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
        dialog.show();
    }

    @Override
    public void onDestroy() {
        if (dialog != null) dialog.dismiss();
        super.onDestroy();
        dialog = null;
    }
}