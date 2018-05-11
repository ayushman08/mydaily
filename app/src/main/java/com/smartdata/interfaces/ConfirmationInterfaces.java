package com.smartdata.interfaces;

import android.content.DialogInterface;

public interface ConfirmationInterfaces {
	void PositiveMethod(DialogInterface dialog, int id);
    void NegativeMethod(DialogInterface dialog, int id);
}
