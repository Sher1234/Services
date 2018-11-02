package io.github.sher1234.service.util.form.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.util.form.listener.FormItemEditTextListener;
import io.github.sher1234.service.util.form.listener.OnFormElementValueChange;
import io.github.sher1234.service.util.form.listener.ReloadListener;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.viewholder.FormElementDatePickerViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementDividerViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementEmailViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementHeaderViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementPickerViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementRatingViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementTextMultiLineViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementTextNumberViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementTextPhoneViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormElementTextSingleLineViewHolder;
import io.github.sher1234.service.util.form.viewholder.FormViewHolder;

public class FormAdapter extends RecyclerView.Adapter<FormViewHolder> implements ReloadListener {

    private final Context mContext;
    private final OnFormElementValueChange mListener;
    private List<FormElement> elements;

    public FormAdapter(Context context, OnFormElementValueChange listener) {
        mContext = context;
        mListener = listener;
        elements = new ArrayList<>();
    }

    public void addElements(List<FormElement> formObjects) {
        this.elements = formObjects;
        notifyDataSetChanged();
    }

    public void addElement(FormElement formObject) {
        this.elements.add(formObject);
        notifyDataSetChanged();
    }

    public void setValueAtIndex(int position, String value) {
        FormElement formElement = elements.get(position);
        formElement.setValue(value);
        notifyDataSetChanged();
    }

    public void setValueAtTag(int tag, String value) {
        for (FormElement f : this.elements) {
            if (f.getTag() == tag) {
                f.setValue(value);
                return;
            }
        }
        notifyDataSetChanged();
    }

    public FormElement getValueAtIndex(int index) {
        return (elements.get(index));
    }

    public FormElement getValueAtTag(int tag) {
        for (FormElement f : this.elements)
            if (f.getTag() == tag) return f;
        return null;
    }

    public List<FormElement> getElements() {
        return elements;
    }

    public OnFormElementValueChange getValueChangeListener() {
        return mListener;
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    @Override
    public int getItemViewType(int position) {
        return elements.get(position).getType();
    }

    @NonNull
    @Override
    public FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case FormElement.HEADER_EDIT:
                v = inflater.inflate(R.layout.form_header, parent, false);
                return new FormElementHeaderViewHolder(v);
            case FormElement.HEADER_VIEW:
                v = inflater.inflate(R.layout.form_header_view, parent, false);
                return new FormElementHeaderViewHolder(v);

            case FormElement.TEXT_SINGLELINE_EDIT:
                v = inflater.inflate(R.layout.form_element, parent, false);
                return new FormElementTextSingleLineViewHolder(v, new FormItemEditTextListener(this));
            case FormElement.TEXT_SINGLELINE_VIEW:
                v = inflater.inflate(R.layout.form_element_view, parent, false);
                return new FormElementTextSingleLineViewHolder(v, new FormItemEditTextListener(this));

            case FormElement.TEXT_MULTILINE_EDIT:
                v = inflater.inflate(R.layout.form_element, parent, false);
                return new FormElementTextMultiLineViewHolder(v, new FormItemEditTextListener(this));
            case FormElement.TEXT_MULTILINE_VIEW:
                v = inflater.inflate(R.layout.form_element_view, parent, false);
                return new FormElementTextMultiLineViewHolder(v, new FormItemEditTextListener(this));

            case FormElement.TEXT_NUMBER_EDIT:
                v = inflater.inflate(R.layout.form_element, parent, false);
                return new FormElementTextNumberViewHolder(v, new FormItemEditTextListener(this));
            case FormElement.TEXT_NUMBER_VIEW:
                v = inflater.inflate(R.layout.form_element_view, parent, false);
                return new FormElementTextNumberViewHolder(v, new FormItemEditTextListener(this));

            case FormElement.TEXT_EMAIL_EDIT:
                v = inflater.inflate(R.layout.form_element, parent, false);
                return new FormElementEmailViewHolder(v, new FormItemEditTextListener(this));
            case FormElement.TEXT_EMAIL_VIEW:
                v = inflater.inflate(R.layout.form_element_view, parent, false);
                return new FormElementEmailViewHolder(v, new FormItemEditTextListener(this));

            case FormElement.TEXT_PHONE_EDIT:
                v = inflater.inflate(R.layout.form_element, parent, false);
                return new FormElementTextPhoneViewHolder(v, new FormItemEditTextListener(this));
            case FormElement.TEXT_PHONE_VIEW:
                v = inflater.inflate(R.layout.form_element_view, parent, false);
                return new FormElementTextPhoneViewHolder(v, new FormItemEditTextListener(this));

            case FormElement.PICKER_EDIT:
                v = inflater.inflate(R.layout.form_element, parent, false);
                return new FormElementPickerViewHolder(v, mContext, this);
            case FormElement.PICKER_VIEW:
                v = inflater.inflate(R.layout.form_element_view, parent, false);
                return new FormElementPickerViewHolder(v, mContext, this);

            case FormElement.PICKER_DATE_EDIT:
                v = inflater.inflate(R.layout.form_element, parent, false);
                return new FormElementDatePickerViewHolder(v, mContext, this);
            case FormElement.PICKER_DATE_VIEW:
                v = inflater.inflate(R.layout.form_element_view, parent, false);
                return new FormElementDatePickerViewHolder(v, mContext, this);

            case FormElement.DIVIDER:
                v = inflater.inflate(R.layout.form_divider, parent, false);
                return new FormElementDividerViewHolder(v);

            case FormElement.RATING_EDIT:
                v = inflater.inflate(R.layout.form_rating, parent, false);
                return new FormElementRatingViewHolder(v, this);
            case FormElement.RATING_VIEW:
                v = inflater.inflate(R.layout.form_rating_view, parent, false);
                return new FormElementRatingViewHolder(v, this);

            default:
                v = inflater.inflate(R.layout.form_element, parent, false);
                return new FormElementTextSingleLineViewHolder(v, new FormItemEditTextListener(this));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FormViewHolder holder, final int position) {
        if (holder.getListener() != null)
            holder.getListener().updatePosition(holder.getAdapterPosition());
        holder.bind(position, elements.get(position), mContext);
    }

    @Override
    public void updateValue(int position, String updatedValue) {
        elements.get(position).setValue(updatedValue);
        notifyDataSetChanged();
        if (mListener != null) mListener.onValueChanged(elements.get(position));
    }
}