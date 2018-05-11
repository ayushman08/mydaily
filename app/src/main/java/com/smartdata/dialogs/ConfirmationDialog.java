package com.smartdata.dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.smartdata.interfaces.ConfirmationInterfaces;
import com.smartdata.mydaily.R;


public class ConfirmationDialog {

    public static void getConfirmDialog(Context mContext, String title, String msg, String positiveBtnCaption,
                                        String negativeBtnCaption, boolean isCancelable, final ConfirmationInterfaces target) {
        AlertDialog.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(mContext, R.style.alertDialogCustom);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }

        int imageResource = android.R.drawable.ic_dialog_alert;
        Drawable image = ContextCompat.getDrawable(mContext, imageResource);

        builder.setTitle(title).setMessage(msg).setIcon(image).setCancelable(false).setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                target.PositiveMethod(dialog, id);
            }
        }).setNegativeButton(negativeBtnCaption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                target.NegativeMethod(dialog, id);
            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.show();
        if (isCancelable) {
            alert.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    target.NegativeMethod(null, 0);
                }
            });
        }
    }
}
