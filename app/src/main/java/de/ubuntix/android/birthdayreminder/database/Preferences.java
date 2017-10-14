package de.ubuntix.android.birthdayreminder.database;

import java.sql.Time;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Preferences {

	// preference key constants
	private static final String UPDATE_TIME = "updateTime";
	private static final String HIDE_NOTIFICATION_AFTER_CONFIRMATION = "hideNotificationAfterConfirmation";
	private static final String DAYS_BEFORE_REMINDER = "daysBeforeReminder";
	private static final String ACTIVATE_SERVICE = "activateService";

	// singleton
	private static Preferences instance = null;

	// class members
	private SharedPreferences sharedPreferences;

	private Preferences(Context baseContext) {
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext);
	}

	public static Preferences getInstance(Context baseContext) {
		if (instance == null) {
			instance = new Preferences(baseContext);
		}
		return instance;
	}

	public void clearAll() {
		Editor editor = this.sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	public boolean getActivateService() {
		return this.sharedPreferences.getBoolean(ACTIVATE_SERVICE, true);
	}

	public void setActivateService(boolean activateService) {
		Editor editor = this.sharedPreferences.edit();
		editor.putBoolean(ACTIVATE_SERVICE, activateService);
		editor.commit();
	}

	public int getDaysBeforeReminder() {
		return Integer.valueOf(this.sharedPreferences.getString(DAYS_BEFORE_REMINDER, "3"));
	}

	public void setDaysBeforeReminder(int daysBeforeReminder) {
		Editor editor = this.sharedPreferences.edit();
		editor.putString(DAYS_BEFORE_REMINDER, Integer.toString(daysBeforeReminder));
		editor.commit();
	}

	public boolean getHideNotificationAfterConfirmation() {
		return this.sharedPreferences.getBoolean(HIDE_NOTIFICATION_AFTER_CONFIRMATION, false);
	}

	public void setHideNotificationAfterConfirmation(boolean hideNotificationAfterConfirmation) {
		Editor editor = this.sharedPreferences.edit();
		editor.putBoolean(HIDE_NOTIFICATION_AFTER_CONFIRMATION, hideNotificationAfterConfirmation);
		editor.commit();
	}

	public Time getUpdateTime() {
		return Time.valueOf(this.sharedPreferences.getString(UPDATE_TIME, "09:00") + ":00");
	}

	public void setUpdateTime(Time updateTime) {
		Editor editor = this.sharedPreferences.edit();
		editor.putString(UPDATE_TIME, updateTime.toString());
		editor.commit();
	}
}
