package de.ubuntix.android.birthdayreminder;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import de.ubuntix.android.birthdayreminder.database.Preferences;
import de.ubuntix.android.birthdayreminder.service.BirthdayBroadcastReceiver;

public class PreferenceWindow extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	public static final String ACTIVATE_SERVICE = "activateService";
	public static final String DAYS_BEFORE_REMINDER = "daysBeforeReminder";
	public static final String HIDE_NOTIFICATION = "hideNotificationAfterConfirmation";
	public static final String UPDATE_TIME = "updateTime";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Set up a listener whenever a key changes
		this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onStop() {
		// Unregister the listener whenever a key changes
		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

		super.onStop();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
		if (key.equals(ACTIVATE_SERVICE)) {
			boolean activateService = sharedPrefs.getBoolean(ACTIVATE_SERVICE, true);
			if (activateService) {
				BirthdayBroadcastReceiver.start(getApplicationContext());
			} else {
				BirthdayBroadcastReceiver.stop(getApplicationContext());
			}
		} else if (key.equals(UPDATE_TIME)) {
			Preferences prefs = Preferences.getInstance(getApplicationContext());
			if (prefs.getActivateService()) {
				BirthdayBroadcastReceiver.restart(getApplicationContext());
			}
		}
	}
}
