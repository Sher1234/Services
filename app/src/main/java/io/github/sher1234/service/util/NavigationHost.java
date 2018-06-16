package io.github.sher1234.service.util;

import android.support.v4.app.Fragment;

import org.jetbrains.annotations.NotNull;

public interface NavigationHost {
    void navigateTo(@NotNull Fragment fragment, boolean isBackStacked);
}
