package com.wulouis.screenadjuster;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class Start extends AppCompatActivity {
    //ROOT CHECK FINISHED
    //SHELL COMMAND CHECK UNFINISHED CANCELLED
    //MOUNT COMMAND UNFINISHED CANCELLED
    //INPUT UI DESIGN FINISHED
    //MAIN FUNCTION FINISHED
    //CHECK RESOLUTIONS
    //CHECK DENSITY
    final CharSequence version="Test01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView root=(TextView)findViewById(R.id.textView2);
        Button resolutions=(Button)findViewById(R.id.reso);
        Button density=(Button)findViewById(R.id.dpi);
        Button change=(Button)findViewById(R.id.change);
        Button cal=(Button)findViewById(R.id.cal);

        boolean rooted=false;
        //boolean sued=false;
        //boolean mounted=false;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Created By Wu Louis", Snackbar.LENGTH_LONG)
                        .setAction("About", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogFragment dlg = new Dialogs.AboutDialog();
                                dlg.show(getFragmentManager(), "AboutDialogTag");
                            }
                        }).show();
            }
        });
        //Open Shared Preferences
        SharedPreferences sp=getSharedPreferences("root",0);
        SharedPreferences.Editor speditor=sp.edit();
        /*
        speditor.putString("rootText","Oops!");
        speditor.putBoolean("rootStatus", false);
        speditor.commit();
        */
        //Check Necessary Permissions
        //ScreenAdjuster global=((ScreenAdjuster)getApplicationContext());
        checkroot();
        if(sp.getBoolean("rootStatus", false)){
            root.setText(sp.getString("rootText","Looks Good!"));
            root.setTextColor(Color.GREEN);
            //ShellUtils.execCommand("su",true);
        }else{
            root.setText(sp.getString("rootText","Oops!"));
            root.setTextColor(Color.RED);
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("Warning");
            builder.setCancelable(false);
            builder.setMessage("Screen Adjuster needs root permission!\n\nPlease gain a root permission first!");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    closeApplication();
                }
            });
        }
        //Check SU CANCELLED
        //checksu();

        resolutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScreenResolutions(v);
            }
        });

        density.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScreenDensity(v);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SetResolutions.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(Start.this, SettingsFragment.class);
                startActivityForResult(intent, 0);
                return false;
            case R.id.action_exit:
                closeApplication();
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    public boolean checkroot(){

        //final ProgressDialog chkroot;
        //chkroot=ProgressDialog.show(this,"Checking Root","Please Wait",true,false);
        //final ScreenAdjuster global=(ScreenAdjuster)getApplicationContext();
        SharedPreferences sp=getSharedPreferences("root", 0);
        final SharedPreferences.Editor speditor=sp.edit();
        if(isRoot()) {
            //rootResult(true);
            //setRootVerify(true,true,"");
            //setRootVerify(false,true,"Looks Good!");
            speditor.putBoolean("rootStatus",true);
            speditor.putString("rootText","Looks Good!");
        }else{
            //rootResult(false);
            //global.setRootText("Oops!");
            //setRootVerify(true,false,"");
            //setRootVerify(false,true,"Oops");
            speditor.putBoolean("rootStatus",false);
            speditor.putString("rootText","Oops!");
            DialogFragment dlg=new Dialogs.ErrDialogUnrooted();
            dlg.show(getFragmentManager(), "ErrDialogUnrootedTag");
        }
        speditor.commit();

        //return global.getRootStatus();
        if(sp.getBoolean("rootStatus",false)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/",
                    "/data/local/xbin/", "/data/local/bin/",
                    "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;

                    break;
                }
            }
        }
        return found;
    }

    private static boolean isRoot() {
        return findBinary("su");
    }
    /*
    private void rootResult(boolean status){
        ScreenAdjuster global=(ScreenAdjuster)getApplicationContext();

        global.setRootStatus(status);
    }
    */
    public void closeApplication(){
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    public void getScreenResolutions(final View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShellUtils.CommandResult cmdresult;
                cmdresult=ShellUtils.execCommand("wm size",true,true);
                Snackbar.make(view,cmdresult.successMsg,Snackbar.LENGTH_LONG)
                        .show();
            }
        }).start();;
    }

    public void getScreenDensity(final View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShellUtils.CommandResult cmdresult;
                cmdresult=ShellUtils.execCommand("wm density",true,true);
                Snackbar.make(view, cmdresult.successMsg, Snackbar.LENGTH_LONG)
                        .show();
            }
        }).start();;
    }

    public void setScreen(String resolutions,String density,boolean reboot){

    }



}


