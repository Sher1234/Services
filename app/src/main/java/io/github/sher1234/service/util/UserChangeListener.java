package io.github.sher1234.service.util;

import org.jetbrains.annotations.NotNull;

public interface UserChangeListener {
    void onAccountDelete(@NotNull String email);
    void onAdminChange(@NotNull String email, int value);
    void onAccountStateChange(@NotNull String email, int value);
}
