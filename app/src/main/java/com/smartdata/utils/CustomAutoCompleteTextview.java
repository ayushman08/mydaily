package com.smartdata.utils;

/**
 * Created by tejaswinibhandarkar on 26/7/17.
 */

        import java.util.HashMap;

        import android.content.Context;
        import android.util.AttributeSet;
        import android.widget.AutoCompleteTextView;

/** Customizing AutoCompleteTextView to return Place Description
 *  corresponding to the selected item
 */
public class CustomAutoCompleteTextview extends AutoCompleteTextView {

    public CustomAutoCompleteTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Returns the Place Description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }
}
