package io.github.sher1234.service.util;

import org.jetbrains.annotations.Nullable;

import io.github.sher1234.service.model.base.Query;

public interface ResultListener {
    void onResultChange(@Nullable Query query, boolean resetQuery);
}