package com.example.easyglide;

import android.content.Context;

public class MyGlide {

    public static BitmapRequest with(Context context) {
        return new BitmapRequest(context);
    }
}
