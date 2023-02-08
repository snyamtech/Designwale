package com.snyam.designwale.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.snyam.designwale.Config;
import com.snyam.designwale.R;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private static Typeface fromAsset;
    private static Fonts currentTypeface;

    public static void showLog(String message) {

        if (Config.IS_DEVELOPING) {
            Log.d("Team_iQueen", message);
        }
    }

    public static void showErrorLog(String s, JSONException e) {
        if (Config.IS_DEVELOPING) {
            try {
                StackTraceElement l = e.getStackTrace()[0];
                Log.d("Team_iQueen", s);
                Log.d("Team_iQueen", "Line : " + l.getLineNumber());
                Log.d("Team_iQueen", "Method : " + l.getMethodName());
                Log.d("Team_iQueen", "Class : " + l.getClassName());
            } catch (Exception ee) {
                Log.d("Team_iQueen", "Error in psErrorLogE");
            }
        }
    }

    public static void showErrorLog(String log, Object obj) {
        if (Config.IS_DEVELOPING) {
            try {
                Log.d("Team_iQueen", log);
                Log.d("Team_iQueen", "Line : " + getLineNumber());
                Log.d("Team_iQueen", "Class : " + getClassName(obj));
            } catch (Exception ee) {
                Log.d("Team_iQueen", "Error in psErrorLog");
            }
        }
    }


    public static String getSHA(String str) {

        MessageDigest md;
        String out = "";
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes());
            byte[] mb = md.digest();

            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(new Byte(temp));
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Util.showLog("SHA: " + out);
        return out.toLowerCase();

    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }

    public static int getTotalMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return (int) (memoryInfo.totalMem / 1000000);
    }

    public static ArrayList<Integer> convertDecimalToFraction(float f, float f2) {
        float f3 = f / f2;
        if (f3 < 0.0f) {
            return null;
        }
        double d = (double) f3;
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 1.0d;
        double d5 = 1.0d;
        double d6 = d;
        while (true) {
            double floor = Math.floor(d6);
            double d7 = (floor * d4) + d2;
            double d8 = (floor * d3) + d5;
            d6 = 1.0d / (d6 - floor);
            double d9 = d7 / d8;
            Double.isNaN(d);
            double abs = Math.abs(d - d9);
            Double.isNaN(d);
            if (abs <= d * 1.0E-6d) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(Integer.valueOf((int) d7));
                arrayList.add(Integer.valueOf((int) d8));
                StringBuilder sb = new StringBuilder();
                sb.append("ratio = ");
                sb.append(f3);
                sb.append(" ");
                sb.append(d7);
                sb.append(":");
                sb.append(d8);
                return arrayList;
            }
            d5 = d3;
            d3 = d8;
            double d10 = d4;
            d4 = d7;
            d2 = d10;
        }
    }

    public static String getClassName(Object obj) {
        return "" + ((Object) obj).getClass();
    }

    public static String[] strTOStrArray(String str, String str2) {
        if (str == null) {
            return null;
        }
        return str.split(str2);
    }

    public static void StatusBarColor(Window window, int color) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static PaintDrawable generateViewGradient(String[] strArr, final String str, final String str2, int i, int i2) {
        final int[] iArr = new int[strArr.length];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            iArr[i3] = Color.parseColor(strArr[i3]);
        }
        final Shader.TileMode tileMode = Shader.TileMode.MIRROR;
        ShapeDrawable.ShaderFactory r5 = new ShapeDrawable.ShaderFactory() {
            public Shader resize(int i, int i2) {
                LinearGradient linearGradient;
                if (str.equals("Linear")) {
                    if (str2.equals("Horizontal")) {
                        LinearGradient linearGradient2 = new LinearGradient(0.0f, 0.0f, (float) i, 0.0f, iArr, null, tileMode);
                        linearGradient = linearGradient2;
                    } else {
                        linearGradient = str2.equals("Vertical") ? new LinearGradient(0.0f, 0.0f, 0.0f, (float) i2, iArr, null, tileMode) : null;
                    }
                    return linearGradient;
                } else if (str.equals("Radial")) {
                    RadialGradient radialGradient = new RadialGradient((float) (i / 2), (float) (i2 / 2), (float) i, iArr, null, tileMode);
                    return radialGradient;
                } else if (str.equals("Sweep")) {
                    return new SweepGradient((float) (i / 2), (float) (i2 / 2), iArr, null);
                } else {
                    return null;
                }
            }
        };
        PaintDrawable paintDrawable = new PaintDrawable();
        paintDrawable.setShape(new RectShape());
        paintDrawable.setShaderFactory(r5);
        return paintDrawable;
    }

    public static List<String> listAssetFiles(Context context, String path) {

        List<String> list = new ArrayList<String>();
        try {
            String[] plist = context.getAssets().list(path);
            if (plist.length > 0) {
                for (String file : plist) {
                    if (context.getAssets().open(path + "/" + file) != null){
                        list.add(file);
                    }
                }
            }
        } catch (IOException e) {
            return list;
        }

        return list;
    }

    public static Bitmap getBitmapFromAssets(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        istr.close();

        return bitmap;
    }

    public static void fadeIn(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    public static Animation getAnimUp(Activity activity) {
        return AnimationUtils.loadAnimation(activity, R.anim.anim_slide_up);
    }

    public static Animation getAnimDown(Activity activity) {
        return AnimationUtils.loadAnimation(activity, R.anim.anim_slide_down);
    }

    public static Typeface getTypeFace(Context context, Fonts fonts) {

        if (currentTypeface == fonts) {
            if (fromAsset == null) {
                if (fonts == Fonts.NOTO_SANS) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/NotoSans-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
                } else if (fonts == Fonts.ROBOTO_LIGHT) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
                } else if (fonts == Fonts.ROBOTO_BOLD) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
                }
            }
        } else {
            if (fonts == Fonts.NOTO_SANS) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/NotoSans-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
            } else if (fonts == Fonts.ROBOTO_LIGHT) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
            } else if (fonts == Fonts.ROBOTO_BOLD) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
            } else {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
            }

            //fromAsset = Typeface.createFromAsset(activity.getAssets(), "font/Roboto-Italic.ttf");
            currentTypeface = fonts;
        }
        return fromAsset;
    }

    public enum Fonts {
        ROBOTO,
        NOTO_SANS,
        ROBOTO_LIGHT,
        ROBOTO_MEDIUM,
        ROBOTO_BOLD
    }

    public static int dpToPx(Context context, int i) {
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    public static void loadFirebase(Context context) {
        PrefManager prefManager = new PrefManager(context);
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Util.showLog("Config params updated: " + updated);

                        } else {

                        }
                        prefManager.setString(Constant.api_key, mFirebaseRemoteConfig.getString("apiKey"));

                        Config.API_KEY = prefManager.getString(Constant.api_key);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Util.showErrorLog("Firebase", e);
                    }
                });
    }

    public static void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showErrorLog(e.getMessage(), e);
        }
    }

}
