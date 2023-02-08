package com.snyam.designwale.utils;

import android.media.ExifInterface;
import android.text.TextUtils;


public class ExifUtils {
    private ExifUtils() {
    }

    public static int getExifRotation(String str) {
        try {
            String attribute = new ExifInterface(str).getAttribute("Orientation");
            if (TextUtils.isEmpty(attribute)) {
                return 0;
            }
            int parseInt = Integer.parseInt(attribute);
            if (parseInt == 3) {
                return 180;
            }
            if (parseInt == 6) {
                return 90;
            }
            if (parseInt != 8) {
                return 0;
            }
            return 270;
        } catch (Exception unused) {
            return 0;
        }
    }
}
