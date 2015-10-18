package com.wulouis.screenadjuster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class PrefsFragement extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        switch((String)preference.getTitle()){
            case "About Us":
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("About This App");
                builder.setMessage("Screen Adjuster is an app to adjust the screen resolution.\nCreated by Wu Louis\n\nVersion: TEST01");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // You don't have to do anything here if you just want it dismissed when clicked
                    }
                });
                builder.show();
            }
            case "Disclaimer":
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Disclaimer");
                builder.setMessage("You are changing your device's screen resolution, this may casue the unstablity, high temp, slow react even bricked.\n\nI have no responsiblity if you continue to use this app to change your device's screen resolutions.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // You don't have to do anything here if you just want it dismissed when clicked
                    }
                });
                builder.show();
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
