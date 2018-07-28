package io.github.sher1234.service.util;

import org.jetbrains.annotations.NotNull;

public interface NavigateActivity {
    void navigateToActivity(@NotNull Class c, boolean finish);
}