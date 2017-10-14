package de.ubuntix.android.birthdayreminder.view.preference;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;

public class SummaryEditTextPreference extends EditTextPreference {

	private CharSequence unformatedSummary;

	public SummaryEditTextPreference(Context context) {
		super(context);
	}

	public SummaryEditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.unformatedSummary = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
	}

	public SummaryEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.unformatedSummary = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
	}

	@Override
	protected void onBindView(View view) {
		setAndFormatSummary(this.unformatedSummary);
		super.onBindView(view);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		setAndFormatSummary(this.unformatedSummary);
		super.onDialogClosed(positiveResult);
	}

	@Override
	public void setSummary(CharSequence summary) {
		this.unformatedSummary = summary;
	}

	private void setAndFormatSummary(CharSequence summary) {
		if (summary != null) {
			summary = summary.toString().replaceAll("@value\\b", getText());
		}
		super.setSummary(summary);
	}
}
