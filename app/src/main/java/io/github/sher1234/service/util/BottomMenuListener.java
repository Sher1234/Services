package io.github.sher1234.service.util;

import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import io.github.sher1234.service.fragment.Menu;

public class BottomMenuListener implements View.OnClickListener {
    private final BottomSheetDialogFragment fragment;
    private final FragmentManager fragmentManager;

    public BottomMenuListener(FragmentManager fragmentManager, int i) {
        this.fragmentManager = fragmentManager;
        fragment = Menu.newInstance(i);
    }

    @Override
    public void onClick(View view) {
        fragment.showNow(fragmentManager, Strings.TAG);
    }
}