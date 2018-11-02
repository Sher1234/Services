package io.github.sher1234.service.util.form.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import io.github.sher1234.service.R;
import io.github.sher1234.service.util.form.listener.ReloadListener;
import io.github.sher1234.service.util.form.model.FormElement;
import io.github.sher1234.service.util.form.model.FormElementRating;

public class FormElementRatingViewHolder extends FormViewHolder {

    private final AppCompatTextView textView;
    private final AppCompatRatingBar ratingBar;
    private final ReloadListener reloadListener;
    private int mPosition;
    private FormElementRating formElementRating;

    public FormElementRatingViewHolder(View v, ReloadListener reloadListener) {
        super(v);
        this.reloadListener = reloadListener;
        textView = v.findViewById(R.id.textView);
        ratingBar = v.findViewById(R.id.ratingBar);
    }

    @Override
    public void bind(final int position, FormElement formElement, final Context context) {
        mPosition = position;
        formElementRating = (FormElementRating) formElement;
        ratingBar.setRating(formElementRating.getRating());
        ratingBar.setEnabled(formElement.isEnabled());
        textView.setEnabled(formElement.isEnabled());
        textView.setText(formElement.getTitle());
        ratingBar.setStepSize((float) 1);
        ratingBar.setIsIndicator(false);
        ratingBar.setNumStars(5);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) reloadListener.updateValue(mPosition, String.valueOf(rating));
                formElementRating.setRating(rating);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}