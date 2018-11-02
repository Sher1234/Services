package io.github.sher1234.service.util.form.model;

@SuppressWarnings("all")
public class FormElementRating extends FormElement {

    private float mRating;

    private FormElementRating() {
    }

    public static FormElementRating createInstance(boolean edit) {
        return new FormElementRating().setType(edit ? RATING_EDIT : RATING_VIEW).setRating(0);
    }

    public FormElementRating setTag(int mTag) {
        return (FormElementRating) super.setTag(mTag);
    }

    public FormElementRating setType(int mType) {
        return (FormElementRating) super.setType(mType);
    }

    public FormElementRating setTitle(String mTitle) {
        return (FormElementRating) super.setTitle(mTitle);
    }

    public float getRating() {
        return this.mRating;
    }

    public FormElementRating setRating(float mRating) {
        super.setValue(String.valueOf(mRating));
        this.mRating = mRating;
        return this;
    }

    public FormElementRating setValue(String mValue) {
        mRating = Float.parseFloat(mValue);
        return (FormElementRating) super.setValue(mValue);
    }

    public FormElementRating setHint(String mHint) {
        return (FormElementRating) super.setHint(mHint);
    }

    public FormElementRating setRequired(boolean required) {
        return (FormElementRating) super.setRequired(required);
    }
}