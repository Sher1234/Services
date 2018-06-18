package io.github.sher1234.service.util.formBuilder;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.github.sher1234.service.util.formBuilder.adapter.FormAdapter;
import io.github.sher1234.service.util.formBuilder.listener.OnFormElementValueChangedListener;
import io.github.sher1234.service.util.formBuilder.model.BaseFormElement;

public class FormBuilder {

    private FormAdapter mFormAdapter;

    public FormBuilder(Context context, RecyclerView recyclerView) {
        initializeFormBuildHelper(context, recyclerView, null);
    }

    public FormBuilder(Context context, RecyclerView recyclerView, OnFormElementValueChangedListener listener) {
        initializeFormBuildHelper(context, recyclerView, listener);
    }

    private void initializeFormBuildHelper(Context context, @NotNull RecyclerView recyclerView,
                                           OnFormElementValueChangedListener listener) {
        this.mFormAdapter = new FormAdapter(context, listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mFormAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void addFormElements(List<BaseFormElement> baseFormElements) {
        this.mFormAdapter.addElements(baseFormElements);
    }

    public BaseFormElement getFormElement(int tag) {
        return this.mFormAdapter.getValueAtTag(tag);
    }

    public FormAdapter getmFormAdapter() {
        return mFormAdapter;
    }

    public boolean isValidForm() {
        for (int i = 0; i < this.mFormAdapter.getItemCount(); i++) {
            BaseFormElement baseFormElement = this.mFormAdapter.getValueAtIndex(i);
            if (baseFormElement.isRequired() && TextUtils.isEmpty(baseFormElement.getValue())) {
                return false;
            }
        }
        return true;
    }
}