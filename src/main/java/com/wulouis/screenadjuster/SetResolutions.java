package com.wulouis.screenadjuster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SetResolutions extends AppCompatActivity {

    public String reso;
    public String dpi;
    public boolean isReboot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_resolutions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final CheckBox disclaimer=(CheckBox)findViewById(R.id.disclaimer);
        final Button start=(Button)findViewById(R.id.start);
        final Button getDensity=(Button)findViewById(R.id.density);

        disclaimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    start.setEnabled(true);
                }else{
                    start.setEnabled(false);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeResolution();
            }
        });

        getDensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void changeResolution(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SetResolutions.this);
        builder.setTitle("Input Resolutions: e.g.1080x1920");

        // Set up the input
        final EditText input = new EditText(SetResolutions.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reso = input.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(SetResolutions.this);
                builder.setTitle("Input Density: e.g.320");

                // Set up the input
                final EditText input = new EditText(getApplicationContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dpi = input.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SetResolutions.this);
                        builder.setTitle("Warning!");
                        builder.setMessage("It is strongly recommended that reboot after change the resolutions.\n\nWarning:Wrong resolutions can cause bricked after reboot!\n\nDo you want to reboot?");
                        builder.setPositiveButton("Yes, I want to reboot!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isReboot = true;
                                shellCommand(reso, dpi, isReboot);
                            }
                        });
                        builder.setNegativeButton("No, it's a bit risky!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isReboot = false;
                                shellCommand(reso, dpi, isReboot);
                            }
                        });

                        builder.show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        builder.show();
    }

    public void shellCommand(final String reso, final String dpi, final boolean reboot){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(reboot){
                    final String[] cmd=new String[]{"su","wm size "+reso,"wm dpi "+dpi,"reboot"};
                    ShellUtils.execCommand(cmd,true);
                }else{
                    final String[] cmd=new String[]{"su","wm size "+reso,"wm dpi "+dpi};
                    ShellUtils.execCommand(cmd,true);
                }

            }
        }).start();
    }

}
