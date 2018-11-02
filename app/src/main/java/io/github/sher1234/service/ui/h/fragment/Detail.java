package io.github.sher1234.service.ui.h.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import io.github.sher1234.service.R;
import io.github.sher1234.service.functions.Common;
import io.github.sher1234.service.functions.TaskEmployee;
import io.github.sher1234.service.functions.TaskPrivilege;
import io.github.sher1234.service.model.response.Dashboard;
import io.github.sher1234.service.model.response.Users;
import io.github.sher1234.service.ui.h.EmployeeBoard;
import io.github.sher1234.service.util.Strings;

public class Detail extends Fragment implements View.OnClickListener, TaskPrivilege.TaskUpdate {

    private final AppCompatTextView[] textViews = new AppCompatTextView[11];
    private final TaskPrivilege taskP = new TaskPrivilege();
    private final Common common = new Common();
    private MaterialButton button1, button2;
    private Dashboard dashboard;

    public Detail() {
    }

    public static Detail getInstance(Dashboard dashboard) {
        Bundle bundle = new Bundle();
        Detail detail = new Detail();
        bundle.putSerializable(Strings.ExtraData, dashboard);
        detail.setArguments(bundle);
        return detail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null)
            ((EmployeeBoard) getActivity()).onFragmentAttached(true, R.string.overview);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null)
            ((EmployeeBoard) getActivity()).onFragmentDetached();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        dashboard = (Dashboard) getArguments().getSerializable(Strings.ExtraData);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h_fragment_1, container, false);
        view.findViewById(R.id.button3).setOnClickListener(this);
        textViews[10] = view.findViewById(R.id.text11);
        textViews[9] = view.findViewById(R.id.text10);
        textViews[0] = view.findViewById(R.id.text1);
        textViews[1] = view.findViewById(R.id.text2);
        textViews[2] = view.findViewById(R.id.text3);
        textViews[3] = view.findViewById(R.id.text4);
        textViews[4] = view.findViewById(R.id.text5);
        textViews[5] = view.findViewById(R.id.text6);
        textViews[6] = view.findViewById(R.id.text7);
        textViews[7] = view.findViewById(R.id.text8);
        textViews[8] = view.findViewById(R.id.text9);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textViews[8].setText(getString("Pending", dashboard.pCalls));
        textViews[7].setText(getString("Visits", dashboard.tVisits));
        textViews[6].setText(getString("Calls", dashboard.tCalls));
        textViews[5].setText(dashboard.user.getRecent());
        textViews[4].setText(dashboard.user.EmployeeID);
        textViews[3].setText(dashboard.user.Phone);
        textViews[2].setText(dashboard.user.Email);
        textViews[0].setText(dashboard.user.Name);
        if (dashboard.user.isAdmin()) {
            textViews[9].setText(R.string.admin_dismiss);
            textViews[1].setVisibility(View.VISIBLE);
            button1.setText(R.string.dismiss_admin);
        } else {
            textViews[9].setText(R.string.admin_add);
            textViews[1].setVisibility(View.GONE);
            button1.setText(R.string.add_admin);
        }
        if (dashboard.user.isRegistered()) {
            textViews[10].setText(R.string.acc_disable);
            button2.setText(R.string.disable_acc);
        } else {
            textViews[10].setText(R.string.acc_enable);
            button2.setText(R.string.enable_acc);
        }
    }

    private String getString(String s1, String s2) {
        return s1 + ": " + s2;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            if (dashboard.user.isAdmin())
                taskP.onChangeAdministrator(this, dashboard.user.UserID, 0);
            else
                taskP.onChangeAdministrator(this, dashboard.user.UserID, 1);
        } else if (v.getId() == R.id.button2)
            if (dashboard.user.isRegistered())
                taskP.onChangeAccountState(this, dashboard.user.UserID, 0);
            else
                taskP.onChangeAccountState(this, dashboard.user.UserID, 1);
        else if (v.getId() == R.id.button3)
            taskP.onDeleteAccount(this, dashboard.user.UserID, dashboard.user.Email);
    }

    @Override
    public void onFetched(@Nullable Users users, int i, boolean b) {
        common.dismissProgressDialog();
    }

    @Override
    public void onFetch() {
        assert getActivity() != null;
        common.showProgressDialog(getActivity());
    }

    @Override
    public void fetch() {
        assert getActivity() != null;
        ((TaskEmployee.TaskUpdate) getActivity()).fetch();
    }
}