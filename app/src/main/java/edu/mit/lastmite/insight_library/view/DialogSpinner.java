package edu.mit.lastmite.insight_library.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;

public class DialogSpinner extends Spinner {
    public DialogSpinner(Context context) {
        super(context);
    }

    public DialogSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, Spinner.MODE_DIALOG);
    }

    public DialogSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}