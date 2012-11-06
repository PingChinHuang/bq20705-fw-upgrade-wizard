package com.fihtdc.gaugefwupgradewizard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
//import android.os.ServiceManager;
import android.widget.Button;
import android.widget.Toast;

//import com.android.internal.app.IBatteryStats;

public class GaugeFWUpgradeWizard extends Activity {
    //private IBatteryStats mBatteryStats = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button upgrade_btn = (Button) findViewById(R.id.Button01);
        Button dismiss_btn = (Button) findViewById(R.id.Button02);
        
        upgrade_btn.setOnClickListener(upgradeButton);
        dismiss_btn.setOnClickListener(dismissButton);
        

        /*if (mBatteryStats == null) {
            IBinder service = ServiceManager.getService("batteryinfo");
            if (service != null) {
                mBatteryStats = IBatteryStats.Stub.asInterface(service);
            } else {
                Toast.makeText(GaugeFWUpgradeWizard.this,
                                this.getString (R.string.toast_get_battery_state_service_warning),
                                Toast.LENGTH_LONG).show();
            }
        }*/
    }
    
    private OnClickListener upgradeButton = new OnClickListener () {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            AlertDialog dialog = new AlertDialog.Builder(GaugeFWUpgradeWizard.this).create();
            dialog.setTitle(GaugeFWUpgradeWizard.this.getString (R.string.upgrade_confirm_dialog_title));
            dialog.setMessage(GaugeFWUpgradeWizard.this.getString (R.string.upgrade_confirm_dialog_text));
            dialog.setButton(GaugeFWUpgradeWizard.this.getString (R.string.upgrade_confirm_dialog_ok_btn), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    AlertDialog warning_dialog = new AlertDialog.Builder(GaugeFWUpgradeWizard.this).create();
                    warning_dialog.setTitle(GaugeFWUpgradeWizard.this.getString (R.string.upgrade_warning_dialog_title));
                    warning_dialog.setMessage(GaugeFWUpgradeWizard.this.getString (R.string.upgrade_warning_dialog_text));
                    warning_dialog.setButton(GaugeFWUpgradeWizard.this.getString (R.string.upgrade_warning_dialog_ok_btn), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            /*try {
                                if (mBatteryStats != null) {
                                    mBatteryStats.setForbidOTP(true);
                                    mBatteryStats.setForbidHalting(true);
                                }
                            } catch (RemoteException e) {
                                Toast.makeText(GaugeFWUpgradeWizard.this,
                                                GaugeFWUpgradeWizard.this.getString (R.string.toast_remote_exception_warning),
                                                Toast.LENGTH_LONG).show();
                            }*/
                        	
                        	new asyncTaskUpdateProgress().execute();
                        	
                            // TODO Start to flash dfi.
                            /*try {
                                FileWriter fw = new FileWriter ("/proc/dfi_upgrade", false);
                                
                                fw.write("flash22685511@FIHLX\n");
                                fw.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Toast.makeText(GaugeFWUpgradeWizard.this,
                                        GaugeFWUpgradeWizard.this.getString (R.string.toast_open_file_failed_warning),
                                        Toast.LENGTH_LONG).show();
                            }*/
                            
                            /*try {
                                if (mBatteryStats != null) {
                                    mBatteryStats.setForbidOTP(false);
                                    mBatteryStats.setForbidHalting(false);
                                }
                            } catch (RemoteException e) {
                                Toast.makeText(GaugeFWUpgradeWizard.this,
                                                GaugeFWUpgradeWizard.this.getString (R.string.toast_remote_exception_warning),
                                                Toast.LENGTH_LONG).show();
                            }*/
                        }
                    });
                    warning_dialog.show();
                }
            });
            dialog.setButton2(GaugeFWUpgradeWizard.this.getString(R.string.upgrade_confirm_dialog_cancel_btn), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        
    };
    
    private OnClickListener dismissButton = new OnClickListener () {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            GaugeFWUpgradeWizard.this.finish();
        }
        
    };
    
    public class asyncTaskUpdateProgress extends AsyncTask<Void, Integer, Void> {
        int progress;
        ProgressDialog progressDialog;
        PowerManager.WakeLock wakeLock;
         
        protected void onPostExecute(Void result) {
        	// TODO Auto-generated method stub
        	progressDialog.dismiss();
            try {
            	BufferedReader buf = new BufferedReader(new FileReader("/data/test"));
            	progress = Integer.parseInt(buf.readLine());
            	buf.close();
            } catch (FileNotFoundException e) {
            	// TODO Auto-generated catch block
            	e.printStackTrace();
            } catch (IOException e) {
            	// TODO Auto-generated catch block
            	e.printStackTrace();
            }
        	
            AlertDialog result_dialog = new AlertDialog.Builder(GaugeFWUpgradeWizard.this).create();
            result_dialog.setTitle("Report");
            result_dialog.setButton("OK", new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int which) {
            		// TODO Auto-generated method stub
            		dialog.dismiss();
            		if (progress >= 0)
            			GaugeFWUpgradeWizard.this.finish();
            	}
            });
            if (progress >= 0)
            	result_dialog.setMessage("Upgrading Finished");
            else
            	result_dialog.setMessage("Upgrading Failed");
            result_dialog.show();
        	wakeLock.release();
        }
    
        protected void onPreExecute() {
        	// TODO Auto-generated method stub
        	progress = 0;
        	progressDialog =  new ProgressDialog(GaugeFWUpgradeWizard.this);
        	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        	progressDialog.setMessage("Upgrading...");
        	progressDialog.setCancelable(false);
        	progressDialog.show();
        	wakeLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "GaugeFWUpgradeWiz");
        	wakeLock.acquire();
        }
    
        protected Void doInBackground(Void... arg0) {
        	// TODO Auto-generated method stub
            while (progress < 100 && progress >= 0) {
                try {
                	BufferedReader buf = new BufferedReader(new FileReader("/data/test"));
                	progress = Integer.parseInt(buf.readLine());
                	if (progress >= 0)
                        progressDialog.setProgress(progress);
                	buf.close();
                } catch (FileNotFoundException e) {
                	// TODO Auto-generated catch block
                	e.printStackTrace();
                } catch (IOException e) {
                	// TODO Auto-generated catch block
                	e.printStackTrace();
                }
                
                SystemClock.sleep(500);
            } // While

        	return null;
        }
    }
}
