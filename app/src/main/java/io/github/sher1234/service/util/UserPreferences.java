package io.github.sher1234.service.util;

import org.jetbrains.annotations.NotNull;

import io.github.sher1234.service.model.base.User;

public interface UserPreferences {
    void updateUserPreferences(@NotNull User user);
}
