package com.wulouis.screenadjuster;

import android.app.Application;

public class ScreenAdjuster extends Application {
    public String rootText;
    public boolean rootStatus;

    public String getRootText(){
        return rootText;
    }
    public void setRootText(String s){
        rootText=s;
    }

    public boolean getRootStatus(){
        return rootStatus;
    }
    public void setRootStatus(boolean b){
        rootStatus=b;
    }
}
