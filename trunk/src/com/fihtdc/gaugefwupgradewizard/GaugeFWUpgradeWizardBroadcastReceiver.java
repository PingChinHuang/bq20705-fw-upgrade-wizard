package com.fihtdc.gaugefwupgradewizard;

import android.provider.Telephony;
import static android.provider.Telephony.Intents.SECRET_CODE_ACTION;

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;

public class GaugeFWUpgradeWizardBroadcastReceiver extends BroadcastReceiver {
  
    public GaugeFWUpgradeWizardBroadcastReceiver() {
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, GaugeFWUpgradeWizard.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
