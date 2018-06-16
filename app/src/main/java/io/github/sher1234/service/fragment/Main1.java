package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.sher1234.service.R;

import static io.github.sher1234.service.activity.MainActivity.F_TAG;

public class Main1 extends Fragment implements View.OnClickListener {

    public Main1() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_1, container, false);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        Fragment outFragment = getFragmentManager().findFragmentByTag(F_TAG);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left,
                R.anim.enter_left, R.anim.exit_right);
        switch (view.getId()) {
            case R.id.button1:
                if (outFragment != null)
                    fragmentTransaction.remove(outFragment);
                fragmentTransaction.replace(R.id.frameLayout, new Main2(), F_TAG);
                fragmentTransaction.addToBackStack(F_TAG).commit();
                break;

            case R.id.button2:
                if (outFragment != null)
                    fragmentTransaction.remove(outFragment);
                fragmentTransaction.replace(R.id.frameLayout, new Main3(), F_TAG);
                fragmentTransaction.addToBackStack(F_TAG).commit();
                break;
        }
    }
}
