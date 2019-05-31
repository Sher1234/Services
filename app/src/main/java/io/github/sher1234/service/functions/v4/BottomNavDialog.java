package io.github.sher1234.service.functions.v4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import io.github.sher1234.service.R;
import io.github.sher1234.service.firebase.model.user.User;
import io.github.sher1234.service.functions.v4.user.FireUser;
import io.github.sher1234.service.ui.v2.b.Board;

public class BottomNavDialog extends BottomSheetDialogFragment
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppCompatTextView textView1, textView2;
    private NavigationView navigationView;
    private FragmentActivity activity;
    private User user;

    private BottomNavDialog() {}

    public static BottomNavDialog newInstance() {
        return new BottomNavDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_FRAME, R.style.Application_Dialog);
        super.onCreate(savedInstanceState);
        user = new FireUser().getUser();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        View view = inflater.inflate(R.layout.menu_fragment, group, false);
        navigationView = view.findViewById(R.id.navigationView);
        navigationView.inflateMenu(user.admin?R.menu.admin:R.menu.user);
        textView1 = navigationView.getHeaderView(0).findViewById(R.id.textView1);
        textView2 = navigationView.getHeaderView(0).findViewById(R.id.textView2);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        textView2.setText(user.email);
        textView1.setText(user.name.toString());
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() == null) return;
        if (activity instanceof Board) navigationView.setCheckedItem(R.id.home);
        else if (activity instanceof AppCompatActivity)               //TODO:
            navigationView.setCheckedItem(R.id.calls);
        else if (activity instanceof AppCompatActivity)               //TODO:
            navigationView.setCheckedItem(R.id.records);
        else if (activity instanceof AppCompatActivity)               //TODO:
            navigationView.setCheckedItem(R.id.privileges);
        else if (activity instanceof AppCompatActivity)               //TODO:
            navigationView.setCheckedItem(R.id.account);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.privileges: return true;
            case R.id.account: return true;
            case R.id.calls: return true;
            case R.id.home: return true;
        }
        return false;
    }
}