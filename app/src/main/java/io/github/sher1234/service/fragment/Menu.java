package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import io.github.sher1234.service.AppController;
import io.github.sher1234.service.R;
import io.github.sher1234.service.activity.admin.Employees;
import io.github.sher1234.service.activity.common.AccountSettings;
import io.github.sher1234.service.activity.common.Calls;
import io.github.sher1234.service.activity.common.DashBoard;
import io.github.sher1234.service.activity.common.RegisterCall;
import io.github.sher1234.service.activity.common.Splash;
import io.github.sher1234.service.model.base.User;
import io.github.sher1234.service.util.NavigateActivity;
import io.github.sher1234.service.util.Strings;

public class Menu extends BottomSheetDialogFragment implements RadioGroup.OnCheckedChangeListener {

    private TextView textView1, textView2;
    private RadioGroup radioGroup;
    private RadioButton button;
    private User user;
    private int i;

    public Menu() {
    }

    public static Menu newInstance(int i) {
        Menu menu = new Menu();
        Bundle bundle = new Bundle();
        bundle.putInt(Strings.ExtraString, i);
        menu.setArguments(bundle);
        return menu;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        assert getArguments() != null;
        user = AppController.getUserFromPrefs();
        i = getArguments().getInt(Strings.ExtraString, 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_menu, container, false);
        radioGroup = view.findViewById(R.id.chipGroup);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        radioGroup.setOnCheckedChangeListener(this);
        button = view.findViewById(R.id.chip2);
        onResume();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        button.setVisibility(View.GONE);
        if (user.isAdmin())
            button.setVisibility(View.VISIBLE);
        textView2.setText(user.EmployeeID);
        textView1.setText(user.Name);
        switch (i) {
            case 1:
                radioGroup.check(R.id.chip1);
                break;

            case 2:
                radioGroup.check(R.id.chip2);
                break;

            case 3:
                radioGroup.check(R.id.chip3);
                break;

            case 5:
                radioGroup.check(R.id.chip5);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        NavigateActivity navigateActivity = (NavigateActivity) getActivity();
        assert navigateActivity != null;
        switch (i) {
            case R.id.chip1:
                if (this.i == 1)
                    return;
                navigateActivity.navigateToActivity(DashBoard.class, true);
                break;

            case R.id.chip2:
                if (this.i == 2)
                    return;
                navigateActivity.navigateToActivity(Employees.class, true);
                break;

            case R.id.chip3:
                if (this.i == 3)
                    return;
                navigateActivity.navigateToActivity(Calls.class, true);
                break;

            case R.id.chip4:
                if (this.i == 4)
                    return;
                onResume();
                navigateActivity.navigateToActivity(RegisterCall.class, false);
                break;

            case R.id.chip5:
                if (this.i == 5)
                    return;
                navigateActivity.navigateToActivity(AccountSettings.class, true);
                break;

            case R.id.chip6:
                if (this.i == 6)
                    return;
                AppController.removeUserFromPrefs();
                navigateActivity.navigateToActivity(Splash.class, true);
                break;
        }
    }
}