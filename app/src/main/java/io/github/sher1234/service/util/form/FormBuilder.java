package io.github.sher1234.service.util.form;

import android.content.Context;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.util.form.adapter.FormAdapter;
import io.github.sher1234.service.util.form.listener.OnFormElementValueChange;
import io.github.sher1234.service.util.form.model.FormElement;

public class FormBuilder {

    private FormAdapter mFormAdapter;

    public FormBuilder(Context context, RecyclerView recyclerView) {
        initializeFormBuildHelper(context, recyclerView, null);
    }

    public FormBuilder(Context context, RecyclerView recyclerView, OnFormElementValueChange listener) {
        initializeFormBuildHelper(context, recyclerView, listener);
    }

    private void initializeFormBuildHelper(Context context, @NotNull RecyclerView recyclerView,
                                           OnFormElementValueChange listener) {
        this.mFormAdapter = new FormAdapter(context, listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mFormAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void addFormElements(List<FormElement> formElements) {
        this.mFormAdapter.addElements(formElements);
        this.mFormAdapter.notifyDataSetChanged();
    }

    public FormElement getFormElement(int tag) {
        return this.mFormAdapter.getValueAtTag(tag);
    }

    public FormAdapter getFormAdapter() {
        return mFormAdapter;
    }

    public boolean isValidForm() {
        for (int i = 0; i < this.mFormAdapter.getItemCount(); i++) {
            FormElement formElement = this.mFormAdapter.getValueAtIndex(i);
            if (formElement.isRequired() && TextUtils.isEmpty(formElement.getValue())) {
                return false;
            }
        }
        return true;
    }
}