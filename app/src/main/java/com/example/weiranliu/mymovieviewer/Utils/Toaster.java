package com.example.weiranliu.mymovieviewer.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by weiran.liu on 12/17/2015.
 */
public class Toaster {

    public Context c;
    String message;
    Toast mToast;

    public Toaster(Context c){
        this.c = c;
    }

    public void toastLong(String message){
        mToast = Toast.makeText(c, message, Toast.LENGTH_LONG);
        mToast.show();
    }

    public void toastShort(String message){
        mToast = Toast.makeText(c, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
