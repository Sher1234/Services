package io.github.sher1234.service.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.sher1234.service.R;
import io.github.sher1234.service.util.NavigationHost;

public class Start1 extends Fragment implements View.OnClickListener {

    public Start1() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_1, container, false);
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                assert getActivity() != null;
                ((NavigationHost) getActivity()).navigateTo(new Start2(), true);
                break;

            case R.id.button2:
                assert getActivity() != null;
                ((NavigationHost) getActivity()).navigateTo(new Start3(), true);
                break;
        }
    }
}
