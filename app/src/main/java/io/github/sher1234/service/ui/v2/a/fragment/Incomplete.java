package io.github.sher1234.service.ui.v2.a.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.R;
import io.github.sher1234.service.firebase.model.user.User;
import io.github.sher1234.service.functions.v4.OnEvent;
import io.github.sher1234.service.functions.v4.Validator;
import io.github.sher1234.service.functions.v4.user.OnSetData;
import io.github.sher1234.service.util.MaterialDialog;

public class Incomplete extends Fragment implements View.OnClickListener, TextWatcher, OnEvent<User> {

    private TextInputEditText editText1A, editText1B, editText2, editText3, editText4;
    private static final String ARG_USER = "User-Passed";
    private AppCompatTextView textView;
    private MaterialDialog dialog;
    private MaterialButton button;
    private Validator validator;
    private OnSetData setData;
    private User user;


    private Incomplete() {
        setData = new OnSetData(this);
        validator = new Validator();
    }

    static Incomplete newInstance(@NotNull User user) {
        Bundle args = new Bundle();
        Incomplete incomplete = new Incomplete();
        args.putSerializable(ARG_USER, user);
        incomplete.setArguments(args);
        return incomplete;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) user = (User) getArguments().getSerializable(ARG_USER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_fragment_2, container, false);
        view.findViewById(R.id.editText5).setVisibility(View.GONE);
        view.findViewById(R.id.button1).setVisibility(View.GONE);
        editText1A = view.findViewById(R.id.editText1A);
        editText1B = view.findViewById(R.id.editText1B);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        textView = view.findViewById(R.id.textView);
        button = view.findViewById(R.id.button2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView.setText(getString(R.string.complete_account));
        editText1A.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        editText4.addTextChangedListener(this);
        editText4.setText(user.phone.number);
        button.setOnClickListener(this);
        editText1A.setText(user.firstName);
        editText1B.setText(user.lastName);
        button.setText(R.string.save);
        editText2.setText(user.email);
        editText3.setText(user.eid);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2  && !(validator.isNameInvalid(editText1A) ||
                validator.isEmailInvalid(editText2) || validator.isIdInvalid(editText3) ||
                validator.isPhoneInvalid(editText4))) {
            User u = new User().edit(validator.getString(editText1A), validator.getString(editText1B),
                    validator.getString(editText4), validator.getString(editText2),
                    validator.getString(editText3), user.uid, "+91", user.enabled, user.admin);
            setData = setData.onSetData(u);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        button.setEnabled(!(validator.isNameInvalid(editText1A) || validator.isEmailInvalid(editText2)
                || validator.isIdInvalid(editText3) || validator.isPhoneInvalid(editText4)));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onPostExecute(User user) {
        if (dialog != null) dialog.dismiss();
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