package io.github.sher1234.service.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.sher1234.service.R;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.util.Strings;

public class UserFrag extends Fragment implements View.OnClickListener {

    private User user;
    private TextView textView1;
    private TextView textView2;
    private TextInputEditText editText1;
    private TextInputEditText editText2;
    private TextInputEditText editText3;
    private TextInputEditText editText4;

    public UserFrag() {
    }

    public static UserFrag newInstance(User user) {
        UserFrag frag = new UserFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, user);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        user = (User) getArguments().getSerializable(Strings.ExtraData);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        editText1.setOnClickListener(this);
        editText2.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView1.setText(user.Name);
        textView2.setText(user.EmployeeID);
        editText1.setText(user.Phone);
        editText2.setText(user.Email);
        editText3.setText(user.EmployeeID);
        editText4.setText(user.JoinDate);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.editText1) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + user.Phone));
            startActivity(intent);
        } else if (view.getId() == R.id.editText2) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{user.Email});
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, null));
        }
    }
}