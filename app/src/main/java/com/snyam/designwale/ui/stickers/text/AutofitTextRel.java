package com.snyam.designwale.ui.stickers.text;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;

import com.snyam.designwale.R;
import com.snyam.designwale.ui.stickers.TextInfo;
import com.snyam.designwale.utils.StorageUtils;

import java.io.File;


public class AutofitTextRel extends RelativeLayout implements MultiTouchListener.TouchCallbackListener {
    private static final String TAG = "AutofitTextRel";
    public ImageView background_iv;
    public String bgDrawable = "0";
    public int f27s;
    public String field_two = "0,0";
    public int he;
    public boolean isMultiTouchEnabled = true;
    public int leftMargin = 0;
    public TouchEventListener listener = null;
    public AutoResizeTextView autoResizeTextView;
    public int topMargin = 0;
    public int wi;
    double angle = 0.0d;
    int baseh;
    int basew;
    int basex;
    int basey;
    float cX = 0.0f;
    float cY = 0.0f;
    int currentState = 0;
    double dAngle = 0.0d;
    int height;
    float heightMain = 0.0f;
    boolean isUndoRedo = false;
    int margl;
    int margt;
    float ratio;
    Animation scale;
    int sh = 1794;
    int sw = 1080;
    double tAngle = 0.0d;
    double vAngle = 0.0d;
    int width;
    float widthMain = 0.0f;
    Animation zoomInScale;
    Animation zoomOutScale;
    private int bgAlpha = 255;
    private int bgColor = 0;
    private ImageView border_iv;
    private int capitalFlage;
    private Context context;
    private ImageView delete_iv;
    private String field_four = "";
    private int field_one = 0;
    private String field_three = "";
    private String fontName = "";
    private GestureDetector gd = null;
    private boolean isBold;
    private boolean isBorderVisible = false;
    private boolean isItalic = false;
    private boolean isUnderLine = false;
    private boolean isCenterLine = false;
    private float leftRightShadow = 0.0f;
    private OnTouchListener mTouchListener1 = new Touch();
    private int outercolor = 0;
    private int outersize = 0;
    private int progress = 0;
    public String name;
    private int outLineOpacity = 0;
    private OnTouchListener rTouchListener = (view, motionEvent) -> {


        AutofitTextRel autofitTextRel = (AutofitTextRel) view.getParent();

        int action = motionEvent.getAction();
        if (action == 0) {
            if (autofitTextRel != null) {
                autofitTextRel.requestDisallowInterceptTouchEvent(true);
            }
            if (AutofitTextRel.this.listener != null) {
                AutofitTextRel.this.listener.onRotateDown(AutofitTextRel.this);
            }
            Rect rect = new Rect();
            ((View) view.getParent()).getGlobalVisibleRect(rect);
            AutofitTextRel.this.cX = rect.exactCenterX();
            AutofitTextRel.this.cY = rect.exactCenterY();
            AutofitTextRel.this.vAngle = (double) ((View) view.getParent()).getRotation();
            AutofitTextRel autofitTextRel2 = AutofitTextRel.this;
            autofitTextRel2.tAngle = (Math.atan2((double) (autofitTextRel2.cY - motionEvent.getRawY()), (double) (AutofitTextRel.this.cX - motionEvent.getRawX())) * 180.0d) / 3.141592653589793d;
            AutofitTextRel autofitTextRel3 = AutofitTextRel.this;
            autofitTextRel3.dAngle = autofitTextRel3.vAngle - AutofitTextRel.this.tAngle;
            AutofitTextRel.this.currentState = 1;
        } else if (action != 1) {
            if (action == 2) {
                if (autofitTextRel != null) {
                    autofitTextRel.requestDisallowInterceptTouchEvent(true);
                }
                if (AutofitTextRel.this.listener != null) {
                    AutofitTextRel.this.listener.onRotateMove(AutofitTextRel.this);
                }
                AutofitTextRel autofitTextRel4 = AutofitTextRel.this;
                autofitTextRel4.angle = (Math.atan2((double) (autofitTextRel4.cY - motionEvent.getRawY()), (double) (AutofitTextRel.this.cX - motionEvent.getRawX())) * 180.0d) / 3.141592653589793d;
                ((View) view.getParent()).setRotation((float) (AutofitTextRel.this.angle + AutofitTextRel.this.dAngle));
                ((View) view.getParent()).invalidate();
                ((View) view.getParent()).requestLayout();
                AutofitTextRel.this.currentState = 3;
            }
        } else if (AutofitTextRel.this.listener != null) {
            AutofitTextRel.this.listener.onRotateUp(AutofitTextRel.this);
            if (AutofitTextRel.this.currentState == 3) {
                AutofitTextRel.this.clickToSaveWork();
            }
            AutofitTextRel.this.currentState = 2;
        }
        return true;
    };
    private ImageView rotate_iv;
    private float rotation;
    private ImageView scale_iv;
    private int shadowColor = 0;
    private int shadowColorProgress = 255;
    private int shadowProg = 0;
    private int tAlpha = 100;
    private int tColor = ViewCompat.MEASURED_STATE_MASK;
    private String text = "";
    private Path textPath;
    private float topBottomShadow = 0.0f;
    private int xRotateProg = 0;
    private int yRotateProg = 0;
    private int zRotateProg = 0;


    public AutofitTextRel(Context context2) {
        super(context2);
        init(context2);
    }

    public AutofitTextRel(Context context2, boolean z) {
        super(context2);
        this.isUndoRedo = z;
        init(context2);
    }

    public AutofitTextRel(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        init(context2);
    }

    public AutofitTextRel(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        init(context2);
    }

    public void setViewWH(float f, float f2) {
        this.widthMain = f;
        this.heightMain = f2;
    }

    public String getName() {
        return this.name;
    }

    public float getMainWidth() {
        return this.widthMain;
    }

    public float getMainHeight() {
        return this.heightMain;
    }

    public AutofitTextRel setOnTouchCallbackListener(TouchEventListener touchEventListener) {
        this.listener = touchEventListener;
        return this;
    }

    public void setDrawParams() {
        invalidate();
    }

    public void init(Context context2) {
        try {
            this.context = context2;
            Display defaultDisplay = ((Activity) this.context).getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            this.width = point.x;
            this.height = point.y;
            this.ratio = ((float) this.width) / ((float) this.height);


            this.autoResizeTextView = new AutoResizeTextView(this.context);


            this.scale_iv = new ImageView(this.context);
            this.border_iv = new ImageView(this.context);
            this.background_iv = new ImageView(this.context);
            this.delete_iv = new ImageView(this.context);
            this.rotate_iv = new ImageView(this.context);
            this.f27s = dpToPx(this.context, 20);
            this.wi = dpToPx(this.context, 200);
            this.he = dpToPx(this.context, 200);


            this.scale_iv.setImageResource(R.drawable.sticker_scale);
            this.background_iv.setImageResource(0);
            this.rotate_iv.setImageResource(R.drawable.sticker_rotate);
            this.delete_iv.setImageResource(R.drawable.sticker_delete1);


            LayoutParams layoutParams = new LayoutParams(this.wi, this.he);
            LayoutParams layoutParams2 = new LayoutParams(this.f27s, this.f27s);
            layoutParams2.addRule(12);
            layoutParams2.addRule(11);
            LayoutParams layoutParams3 = new LayoutParams(this.f27s, this.f27s);
            layoutParams3.addRule(12);
            layoutParams3.addRule(9);
            LayoutParams layoutParams4 = new LayoutParams(-1, -1);
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams4.addRule(17);
            } else {
                layoutParams4.addRule(13);
            }
            LayoutParams layoutParams5 = new LayoutParams(this.f27s, this.f27s);
            layoutParams5.addRule(10);
            layoutParams5.addRule(9);
            LayoutParams layoutParams6 = new LayoutParams(-1, -1);
            LayoutParams layoutParams7 = new LayoutParams(-1, -1);
            setLayoutParams(layoutParams);
            setBackgroundResource(R.drawable.border_gray);
            addView(this.background_iv);
            this.background_iv.setLayoutParams(layoutParams7);
            this.background_iv.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(this.border_iv);
            this.border_iv.setLayoutParams(layoutParams6);
            this.border_iv.setTag("border_iv");
            addView(this.autoResizeTextView);

            this.autoResizeTextView.setTextSize(400.0f);
            this.autoResizeTextView.setText(this.text);
            this.autoResizeTextView.setTextColor(this.tColor);
            this.autoResizeTextView.setOutlineSize(0);
            this.autoResizeTextView.setOutlineColor(Color.TRANSPARENT);
            this.autoResizeTextView.setShadowLayer(10.6f, 5.5f, 5.3f, ViewCompat.MEASURED_STATE_MASK);

            this.autoResizeTextView.setLayoutParams(layoutParams4);
            this.autoResizeTextView.setGravity(17);

            this.autoResizeTextView.setMinTextSize(10.0f);


            addView(this.delete_iv);
            this.delete_iv.setLayoutParams(layoutParams5);
            this.delete_iv.setOnClickListener(view -> {
                final ViewGroup viewGroup = (ViewGroup) AutofitTextRel.this.getParent();
                AutofitTextRel.this.zoomInScale.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        viewGroup.removeView(AutofitTextRel.this);
                    }
                });
                AutofitTextRel.this.autoResizeTextView.startAnimation(AutofitTextRel.this.zoomInScale);
                AutofitTextRel.this.background_iv.startAnimation(AutofitTextRel.this.zoomInScale);
                AutofitTextRel.this.setBorderVisibility(false);
                if (AutofitTextRel.this.listener != null) {
                    AutofitTextRel.this.listener.onDelete();
                }
            });
            addView(this.rotate_iv);
            this.rotate_iv.setLayoutParams(layoutParams3);
            this.rotate_iv.setOnTouchListener(this.rTouchListener);
            addView(this.scale_iv);
            this.scale_iv.setLayoutParams(layoutParams2);
            this.scale_iv.setTag("scale_iv");
            this.scale_iv.setOnTouchListener(this.mTouchListener1);
            this.rotation = getRotation();
            this.scale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_anim);
            this.zoomOutScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_out_view);
            this.zoomInScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_in_view);
            initGD();
            this.isMultiTouchEnabled = setDefaultTouchListener(true);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    float letterSpacing = 0.0f;

    public void applyLetterSpacing(float f) {
        this.letterSpacing = f;
        if (this.text != null) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < this.text.length()) {
                sb.append("" + this.text.charAt(i));
                i++;
                if (i < this.text.length()) {
                    sb.append(" ");
                }
            }
            SpannableString spannableString = new SpannableString(sb.toString());
            if (sb.toString().length() > 1) {
                for (int i2 = 1; i2 < sb.toString().length(); i2 += 2) {
                    spannableString.setSpan(new ScaleXSpan((1.0f + f) / 10.0f), i2, i2 + 1, 33);
                }
            }
            this.autoResizeTextView.setText(spannableString, TextView.BufferType.SPANNABLE);
        }
    }

    public float getLetterSpacing() {
        return letterSpacing;
    }

    public float getLineSpacing() {
        return autoResizeTextView.getLineSpacingExtra();
    }

    public void applyLineSpacing(float f) {
        this.autoResizeTextView.setLineSpacing(f, 1.0f);
    }

    public void setBoldFont() {
        if (this.isBold) {
            this.isBold = false;
//            this.autoResizeTextView.setTypeface(Typeface.DEFAULT);
            this.autoResizeTextView.setText(Html.fromHtml(this.text.replace("<b>", "").replace("</b>", "")));
            return;
        }
        this.isBold = true;
//        this.autoResizeTextView.setTypeface(Typeface.DEFAULT_BOLD);
        AutoResizeTextView autoResizeTextView = this.autoResizeTextView;
        autoResizeTextView.setText(Html.fromHtml("<b>" + this.text + "</b>"));

    }

    public void setGradientColors(String[] gradientColors, String gradientType, String gradientDirection) {
        autoResizeTextView.setGradient(generateGradient(gradientColors, gradientType, gradientDirection));
    }

    private Shader generateGradient(String[] gradientColors, String gradientType, String gradientDirection) {
        String[] gradient = gradientColors;
        Shader.TileMode tileMode = Shader.TileMode.MIRROR;
        int[] iArr = new int[gradient.length];
        for (int i = 0; i < gradient.length; i++) {
            iArr[i] = Color.parseColor(gradient[i]);
        }
        LinearGradient linearGradient = null;
        if (gradientType.equals("Linear")) {
            if (gradientDirection.equals("Horizontal")) {
                linearGradient = new LinearGradient(0.0f, 0.0f, (float) getWidth(), 0.0f, iArr, null, tileMode);
            } else if (gradientDirection.equals("Vertical")) {
                linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, (float) getHeight(), iArr, null, tileMode);
            }
        } else if (gradientType.equals("Radial")) {
            RadialGradient radialGradient = new RadialGradient((float) (getWidth() / 2), (float) (getHeight() / 2), (float) getWidth(), iArr, null, tileMode);
            return radialGradient;
        } else if (gradientType.equals("Sweep")) {
            return new SweepGradient((float) (getWidth() / 2), (float) (getHeight() / 2), iArr, null);
        }
        return linearGradient;
    }

    public void setBGGradient(String[] gradientColors, String gradientType, String gradientDirection) {
        background_iv.setImageBitmap(addGradient(gradientColors, gradientType, gradientDirection));
        background_iv.setBackgroundColor(0);
    }

    public Bitmap addGradient(String[] gradientColors, String gradientType, String gradientDirection) {
        int w = background_iv.getWidth();
        int h = background_iv.getHeight();
        Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

//        canvas.drawBitmap(result, 0, 0, null);

        Paint paint = new Paint();
        paint.setShader(generateBGGradient(gradientColors, gradientType, gradientDirection));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawPaint(paint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        return result;
    }


    private Shader generateBGGradient(String[] gradientColors, String gradientType, String gradientDirection) {
        String[] gradient = gradientColors;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        int[] iArr = new int[gradient.length];
        for (int i = 0; i < gradient.length; i++) {
            iArr[i] = Color.parseColor(gradient[i]);
        }
        LinearGradient linearGradient = null;
        if (gradientType.equals("Linear")) {
            if (gradientDirection.equals("Horizontal")) {
                linearGradient = new LinearGradient(0.0f, 0.0f, (float) getWidth(), 0.0f, iArr, null, tileMode);
            } else if (gradientDirection.equals("Vertical")) {
                linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, (float) getHeight(), iArr, null, tileMode);
            }
        } else if (gradientType.equals("Radial")) {
            RadialGradient radialGradient = new RadialGradient((float) (getWidth() / 2), (float) (getHeight() / 2), (float) getWidth(), iArr, null, tileMode);
            return radialGradient;
        } else if (gradientType.equals("Sweep")) {
            return new SweepGradient((float) (getWidth() / 2), (float) (getHeight() / 2), iArr, null);
        }
        return linearGradient;
    }

    public void setCapitalFont() {
        AutoResizeTextView autoResizeTextView = this.autoResizeTextView;
        autoResizeTextView.setText(autoResizeTextView.getText().toString().toUpperCase());
    }

    public void setLowerFont() {
        AutoResizeTextView autoResizeTextView2 = this.autoResizeTextView;
        autoResizeTextView2.setText(autoResizeTextView2.getText().toString().toLowerCase());
    }

    public void setFirstLetterCap() {
        AutoResizeTextView autoResizeTextView = this.autoResizeTextView;
        autoResizeTextView.setText(getCapsSentences(autoResizeTextView.getText().toString()));
    }

    private String getCapsSentences(String tagName) {
        String[] splits = tagName.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }

    public void setUnderLineFont() {
        if (this.isUnderLine) {
            this.isUnderLine = false;
            this.autoResizeTextView.setText(Html.fromHtml(this.text.replace("<u>", "").replace("</u>", "")));
            return;
        }
        this.isUnderLine = true;
        AutoResizeTextView autoResizeTextView = this.autoResizeTextView;
        autoResizeTextView.setText(Html.fromHtml("<u>" + this.text + "</u>"));
    }

    public void setCenterLineFont() {
        if (this.isCenterLine) {
            this.isCenterLine = false;
            this.autoResizeTextView.setText(Html.fromHtml(this.text.replace("<s>", "").replace("</s>", "")));
            return;
        }
        this.isCenterLine = true;
        AutoResizeTextView autoResizeTextView = this.autoResizeTextView;
        autoResizeTextView.setText(Html.fromHtml("<s>" + this.text + "</s>"));
    }

    public void setItalicFont() {
        if (this.isItalic) {
            this.isItalic = false;
            this.autoResizeTextView.setText(Html.fromHtml(this.text.replace("<i>", "").replace("</i>", "")));
            return;
        }
        this.isItalic = true;
        AutoResizeTextView autoResizeTextView = this.autoResizeTextView;
        autoResizeTextView.setText(Html.fromHtml("<i>" + this.text + "</i>"));
    }

    public void setLeftAlignMent() {
        this.autoResizeTextView.setGravity(19);
    }

    public void setCenterAlignMent() {
        this.autoResizeTextView.setGravity(17);
    }

    public void setRightAlignMent() {
        this.autoResizeTextView.setGravity(21);
    }

    public boolean setDefaultTouchListener(boolean z) {
        if (z) {
            setOnTouchListener(new MultiTouchListener().enableRotation(true).setOnTouchCallbackListener(this).setGestureListener(this.gd));
            return true;
        }
        setOnTouchListener(null);
        return false;
    }

    public boolean getBorderVisibility() {
        return this.isBorderVisible;
    }

    public void setBorderVisibility(boolean z) {
        this.isBorderVisible = z;
        if (!z) {
            this.border_iv.setVisibility(GONE);
            this.scale_iv.setVisibility(GONE);
            this.delete_iv.setVisibility(GONE);
            this.rotate_iv.setVisibility(GONE);
            setBackgroundResource(0);
        } else if (this.border_iv.getVisibility() != VISIBLE) {
            this.border_iv.setVisibility(VISIBLE);
            this.scale_iv.setVisibility(VISIBLE);
            this.delete_iv.setVisibility(VISIBLE);
            this.rotate_iv.setVisibility(VISIBLE);
            setBackgroundResource(R.drawable.border_gray);
            this.autoResizeTextView.startAnimation(this.scale);
        }
    }

    public String getText() {
        return this.autoResizeTextView.getText().toString();
    }

    public void setText(String str) {
        try {
            this.autoResizeTextView.setText(str);
            this.text = str;
            if (!this.isUndoRedo) {
                this.autoResizeTextView.startAnimation(this.zoomOutScale);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setTextFont(String str) {
        try {
            if (str.equals("default")) {
                this.autoResizeTextView.setTypeface(Typeface.createFromAsset(this.context.getAssets(), "font/1.ttf"));
                this.fontName = str;
                return;
            }

            String str1 = str;

            String directory = new StorageUtils(context).getPackageStorageDir("/." + "font" + "/").getAbsolutePath();

            File file1 = new File(directory + "/" + str1);

            str1 = str;

            if (file1.exists()) {
                this.autoResizeTextView.setTypeface(Typeface.createFromFile(file1.getAbsolutePath()));
            } else {
                str1 = str;
//                    this.autoResizeTextView.setTypeface(Typeface.createFromFile(file1.getAbsolutePath() + "/" + str1));
                this.autoResizeTextView.setTypeface(Typeface.createFromAsset(context.getAssets(), "font" + "/" + str1));
            }

            this.fontName = str;

        } catch (Exception e) {
            Log.e(TAG, "setTextFont: ");
        }
    }

    public int getTextColor() {
        return this.tColor;
    }

    public void setTextColor(int i) {
        this.autoResizeTextView.setTextColor(i);
        this.tColor = i;
    }

    public void setTextOutlLine(int i) {
        this.outersize = i;
        this.autoResizeTextView.setOutlineSize(i);
    }

    boolean isStroke = false;

    public void setTextOutlineColor(int i) {
        if (!isStroke) {
            setTextOutlLine(5);
            isStroke = true;
        }
        this.outercolor = i;
        this.autoResizeTextView.setOutlineColor(i);
    }

    public int getTextAlpha() {
        return this.tAlpha;
    }

    public void setTextAlpha(int i) {
        this.autoResizeTextView.setAlpha(((float) i) / 100.0f);
        this.tAlpha = i;
    }

    public int getTextShadowColor() {
        return this.shadowColor;
    }

    public void setTextShadowColor(int i) {
        this.shadowColor = i;
        this.shadowColor = ColorUtils.setAlphaComponent(this.shadowColor, this.shadowColorProgress);
        this.autoResizeTextView.setShadowLayer((float) this.shadowProg, this.leftRightShadow, this.topBottomShadow, this.shadowColor);
    }

    public int getShadowOpacity() {
        return shadowColorProgress / 2;
    }

    public void setTextShadowOpacity(int i) {
        this.shadowColorProgress = i;
        this.shadowColor = ColorUtils.setAlphaComponent(this.shadowColor, i);
        this.autoResizeTextView.setShadowLayer((float) this.shadowProg, this.leftRightShadow, this.topBottomShadow, this.shadowColor);
    }

    public void setLeftRightShadow(float f) {
        this.leftRightShadow = f;
        this.autoResizeTextView.setShadowLayer((float) this.shadowProg, this.leftRightShadow, this.topBottomShadow, this.shadowColor);
    }

    public void setTopBottomShadow(float f) {
        this.topBottomShadow = f;
        this.autoResizeTextView.setShadowLayer((float) this.shadowProg, this.leftRightShadow, this.topBottomShadow, this.shadowColor);
    }

    public int getTextShadowProg() {
        return this.shadowProg;
    }

    public void setTextShadowProg(int i) {
        this.shadowProg = i;
        this.autoResizeTextView.setShadowLayer((float) this.shadowProg, this.leftRightShadow, this.topBottomShadow, this.shadowColor);
    }

    public String getBgDrawable() {
        return this.bgDrawable;
    }

    public void setBgDrawable(String str) {
        this.bgDrawable = str;
        this.bgColor = 0;
        this.background_iv.setImageBitmap(getTiledBitmap(this.context, getResources().getIdentifier(str, "drawable", this.context.getPackageName()), this.wi, this.he));
        this.background_iv.setBackgroundColor(this.bgColor);
    }

    public void setBGPattern(Bitmap bitmap) {
        this.background_iv.setImageBitmap(getTiledBitmap(bitmap));
        this.background_iv.setBackgroundColor(0);
    }

    public int getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(int i) {
        this.bgDrawable = "0";
        this.bgColor = i;
        this.background_iv.setImageBitmap((Bitmap) null);
        this.background_iv.setBackgroundColor(i);
    }

    public int getLeftSadow() {
        return (int) this.leftRightShadow;
    }

    public int getTopBottomSadow() {
        return (int) this.topBottomShadow;
    }

    public int getOutercolor() {
        return this.outercolor;
    }

    public int getOutersize() {
        return this.outersize;
    }

    public int getBgAlpha() {
        return this.bgAlpha;
    }

    public void setBgAlpha(int i) {
        this.background_iv.setAlpha(((float) i) / 100.0f);
        this.bgAlpha = i;
    }

    public TextInfo getTextInfo() {
        TextInfo textInfo = new TextInfo();
        textInfo.setName(name);
        textInfo.setPOS_X(getX());
        textInfo.setPOS_Y(getY());
        textInfo.setWIDTH(this.wi);
        textInfo.setHEIGHT(this.he);
        textInfo.setTEXT(this.text);
        textInfo.setFONT_NAME(this.fontName);
        textInfo.setTEXT_COLOR(this.tColor);
        textInfo.setTEXT_ALPHA(this.tAlpha);
        textInfo.setSHADOW_COLOR(this.shadowColor);
        textInfo.setSHADOW_PROG(this.shadowProg);
        textInfo.setBG_COLOR(this.bgColor);
        textInfo.setBG_DRAWABLE(this.bgDrawable);
        textInfo.setBG_ALPHA(this.bgAlpha);
        textInfo.setROTATION(getRotation());
        textInfo.setXRotateProg(this.xRotateProg);
        textInfo.setYRotateProg(this.yRotateProg);
        textInfo.setZRotateProg(this.zRotateProg);
        textInfo.setCurveRotateProg(this.progress);
        textInfo.setFIELD_ONE(this.field_one);
        textInfo.setFIELD_TWO(this.field_two);
        textInfo.setFIELD_THREE(this.field_three);
        textInfo.setFIELD_FOUR(this.field_four);
        textInfo.setLeftRighShadow(this.leftRightShadow);
        textInfo.setTopBottomShadow(this.topBottomShadow);
        textInfo.setOutLineSize(this.outersize);
        textInfo.setOutLineColor(this.outercolor);
        textInfo.setOutLineOpacity(outLineOpacity / 2);

        return textInfo;
    }

    public void setTextInfo(TextInfo textInfo, boolean z) {
        this.wi = textInfo.getWIDTH();
        this.he = textInfo.getHEIGHT();
        this.text = textInfo.getTEXT();
        this.fontName = textInfo.getFONT_NAME();
        this.tColor = textInfo.getTEXT_COLOR();
        this.tAlpha = textInfo.getTEXT_ALPHA();
        this.shadowColor = textInfo.getSHADOW_COLOR();
        this.shadowProg = textInfo.getSHADOW_PROG();
        this.bgColor = textInfo.getBG_COLOR();
        this.bgDrawable = textInfo.getBG_DRAWABLE();
        this.bgAlpha = textInfo.getBG_ALPHA();
        this.rotation = textInfo.getROTATION();
        this.field_two = textInfo.getFIELD_TWO();
        this.zRotateProg = (int) textInfo.getROTATION();
        this.outLineOpacity = textInfo.getOutLineOpacity();
        this.name = textInfo.getName();

        setText(this.text);
        setTextFont(this.fontName);

        setTextColor(this.tColor);
        setTextAlpha(this.tAlpha);

        this.outersize = textInfo.getOutLineSize();
        setTextOutlLine(this.outersize);
        this.outercolor = textInfo.getOutLineColor();
        setTextOutlineColor(this.outercolor);
        setTextShadowColor(this.shadowColor);
        this.leftRightShadow = textInfo.getLeftRighShadow();
        this.topBottomShadow = textInfo.getTopBottomShadow();
        setTextShadowProg(this.shadowProg);
        int i = this.bgColor;
        if (i != 0) {
            setBgColor(i);
        } else {
            this.background_iv.setBackgroundColor(0);
        }
        if (this.bgDrawable.equals("0")) {
            this.background_iv.setImageBitmap((Bitmap) null);
        } else {
            setBgDrawable(this.bgDrawable);
        }
        setBgAlpha(this.bgAlpha);
        setRotation(textInfo.getROTATION());

        if (this.field_two.equals("")) {
            getLayoutParams().width = this.wi;
            getLayoutParams().height = this.he;
            setX(textInfo.getPOS_X());
            setY(textInfo.getPOS_Y());
            return;
        } else {
            String[] split = this.field_two.split(",");
            int parseInt = Integer.parseInt(split[0]);
            int parseInt2 = Integer.parseInt(split[1]);
            ((LayoutParams) getLayoutParams()).leftMargin = parseInt;
            ((LayoutParams) getLayoutParams()).topMargin = parseInt2;
            getLayoutParams().width = this.wi;
            getLayoutParams().height = this.he;

            setX(textInfo.getPOS_X() + ((float) (parseInt * -1)));
            setY(textInfo.getPOS_Y() + ((float) (parseInt2 * -1)));
        }
    }

    public void optimize(float f, float f2) {
        setX(getX() * f);
        setY(getY() * f2);
        getLayoutParams().width = (int) (((float) this.wi) * f);
        getLayoutParams().height = (int) (((float) this.he) * f2);
    }

    public void setRotateProg(String axis, int progress) {
        if (axis.equals("Z")) {
            zRotateProg = progress / 2;
            setRotation(progress);
        } else if (axis.equals("Y")) {
            yRotateProg = progress / 2;
            setRotationY(progress);
        } else {
            xRotateProg = progress / 2;
            setRotationX(progress);
        }
    }

    public void flipView(String axis) {
        AutoResizeTextView textView = this.autoResizeTextView;

        if (axis.equals("Y")) {
            float f = -180.0f;
            if (textView.getRotationY() == -180.0f) {
                f = 0.0f;
            }
            textView.setRotationY(f);
            textView.invalidate();
            textView.requestLayout();
        } else {
            float f = -180.0f;
            if (textView.getRotationX() == -180.0f) {
                f = 0.0f;
            }
            textView.setRotationX(f);
            textView.invalidate();
            textView.requestLayout();
        }
    }

    public void incrX() {
        setX(getX() + 2.0f);
    }

    public void decX() {
        setX(getX() - 2.0f);
    }

    public void incrY() {
        setY(getY() + 2.0f);
    }

    public void decY() {
        setY(getY() - 2.0f);
    }

    public int dpToPx(Context context2, int i) {
        context2.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    private Bitmap getTiledBitmap(Context context2, int i, int i2, int i3) {
        Rect rect = new Rect(0, 0, i2, i3);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(context2.getResources(), i,
                new BitmapFactory.Options()), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawRect(rect, paint);
        return createBitmap;
    }

    private Bitmap getTiledBitmap(Bitmap bitmap) {
        Rect rect = new Rect(0, 0, background_iv.getWidth(), background_iv.getHeight());
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        Bitmap createBitmap = Bitmap.createBitmap(background_iv.getWidth(), background_iv.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawRect(rect, paint);
        return createBitmap;
    }

    public void setTextPattern(Bitmap bitmap) {
        autoResizeTextView.setPattern(bitmap);
    }

    private void initGD() {
        this.gd = new GestureDetector(this.context, new SimpleListner());
    }

    public void onTouchCallback(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchDown(view);
        }
    }

    public void onTouchUpCallback(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchUp(view);
        }
    }

    public void onTouchMoveCallback(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchMove(view);
        }
    }

    public void onTouchUpClick(View view) {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchMoveUpClick(view);
        }
    }

    public float getNewX(float f) {
        return ((float) this.width) * (f / ((float) this.sw));
    }

    public float getNewY(float f) {
        return ((float) this.height) * (f / ((float) this.sh));
    }

    public void clickToSaveWork() {
        TouchEventListener touchEventListener = this.listener;
        if (touchEventListener != null) {
            touchEventListener.onTouchMoveUpClick(this);
        }
    }

    public void setOutLineOpacity(int progress) {
        this.outLineOpacity = progress;
    }

    public void setFontSize(int progress) {
//        autoResizeTextView.setTextSize(progress);
        LayoutParams layoutParams = (LayoutParams) AutofitTextRel.this.getLayoutParams();
        if (progress > 50) {
            layoutParams.width = layoutParams.width + 20;
            layoutParams.height = layoutParams.height + 20;
            AutofitTextRel.this.setLayoutParams(layoutParams);
        } else {
            layoutParams.width = layoutParams.width - 20;
            layoutParams.height = layoutParams.height - 20;
            AutofitTextRel.this.setLayoutParams(layoutParams);
        }
    }

    public int getTextSize() {
        return (int) autoResizeTextView.getTextSize();
    }

    public void plusSize() {
        LayoutParams layoutParams = (LayoutParams) AutofitTextRel.this.getLayoutParams();
        layoutParams.width = layoutParams.width + 20;
        layoutParams.height = layoutParams.height + 20;
        AutofitTextRel.this.setLayoutParams(layoutParams);
    }

    public void minusSize() {
        LayoutParams layoutParams = (LayoutParams) AutofitTextRel.this.getLayoutParams();
        layoutParams.width = layoutParams.width - 20;
        layoutParams.height = layoutParams.height - 20;
        AutofitTextRel.this.setLayoutParams(layoutParams);
    }

    public interface TouchEventListener {
        void onDelete();

        void onDoubleTap();

        void onEdit(View view, Uri uri);

        void onRotateDown(View view);

        void onRotateMove(View view);

        void onRotateUp(View view);

        void onScaleDown(View view);

        void onScaleMove(View view);

        void onScaleUp(View view);

        void onTouchDown(View view);

        void onTouchMove(View view);

        void onTouchMoveUpClick(View view);

        void onTouchUp(View view);
    }

    class Touch implements OnTouchListener {
        Touch() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {


            AutofitTextRel autofitTextRel = (AutofitTextRel) view.getParent();


            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            LayoutParams layoutParams = (LayoutParams) AutofitTextRel.this.getLayoutParams();
            int action = motionEvent.getAction();

            if (action == 0) {
                if (autofitTextRel != null) {
                    autofitTextRel.requestDisallowInterceptTouchEvent(true);
                }
                if (AutofitTextRel.this.listener != null) {
                    AutofitTextRel.this.listener.onScaleDown(AutofitTextRel.this);
                }
                AutofitTextRel.this.invalidate();
                AutofitTextRel autofitTextRel2 = AutofitTextRel.this;
                autofitTextRel2.basex = rawX;
                autofitTextRel2.basey = rawY;
                autofitTextRel2.basew = autofitTextRel2.getWidth();
                AutofitTextRel autofitTextRel3 = AutofitTextRel.this;
                autofitTextRel3.baseh = autofitTextRel3.getHeight();
                AutofitTextRel.this.getLocationOnScreen(new int[2]);
                AutofitTextRel.this.margl = layoutParams.leftMargin;
                AutofitTextRel.this.margt = layoutParams.topMargin;
                AutofitTextRel.this.currentState = 0;
            } else if (action == 1) {
                AutofitTextRel autofitTextRel4 = AutofitTextRel.this;
                autofitTextRel4.wi = autofitTextRel4.getLayoutParams().width;
                AutofitTextRel autofitTextRel5 = AutofitTextRel.this;
                autofitTextRel5.he = autofitTextRel5.getLayoutParams().height;
                AutofitTextRel autofitTextRel6 = AutofitTextRel.this;
                autofitTextRel6.leftMargin = ((LayoutParams) autofitTextRel6.getLayoutParams()).leftMargin;
                AutofitTextRel autofitTextRel7 = AutofitTextRel.this;
                autofitTextRel7.topMargin = ((LayoutParams) autofitTextRel7.getLayoutParams()).topMargin;
                AutofitTextRel autofitTextRel8 = AutofitTextRel.this;
                autofitTextRel8.field_two = String.valueOf(AutofitTextRel.this.leftMargin) + "," + String.valueOf(AutofitTextRel.this.topMargin);
                if (AutofitTextRel.this.currentState == 3) {
                    AutofitTextRel.this.clickToSaveWork();
                }
                AutofitTextRel autofitTextRel9 = AutofitTextRel.this;
                autofitTextRel9.currentState = 2;
                if (autofitTextRel9.listener != null) {
                    AutofitTextRel.this.listener.onScaleUp(AutofitTextRel.this);
                }
            } else if (action == 2) {
                if (autofitTextRel != null) {
                    autofitTextRel.requestDisallowInterceptTouchEvent(true);
                }
                if (AutofitTextRel.this.listener != null) {
                    AutofitTextRel.this.listener.onScaleMove(AutofitTextRel.this);
                }
                float degrees = (float) Math.toDegrees(Math.atan2((double) (rawY - AutofitTextRel.this.basey), (double) (rawX - AutofitTextRel.this.basex)));
                if (degrees < 0.0f) {
                    degrees += 360.0f;
                }
                int i = rawX - AutofitTextRel.this.basex;
                int i2 = rawY - AutofitTextRel.this.basey;
                int i3 = i2 * i2;
                int sqrt = (int) (Math.sqrt((double) ((i * i) + i3)) * Math.cos(Math.toRadians((double) (degrees - AutofitTextRel.this.getRotation()))));
                int sqrt2 = (int) (Math.sqrt((double) ((sqrt * sqrt) + i3)) * Math.sin(Math.toRadians((double) (degrees - AutofitTextRel.this.getRotation()))));
                int i4 = (sqrt * 2) + AutofitTextRel.this.basew;
                int i5 = (sqrt2 * 2) + AutofitTextRel.this.baseh;
                if (i4 > AutofitTextRel.this.f27s) {
                    layoutParams.width = i4;
                    layoutParams.leftMargin = AutofitTextRel.this.margl - sqrt;
                }
                if (i5 > AutofitTextRel.this.f27s) {
                    layoutParams.height = i5;
                    layoutParams.topMargin = AutofitTextRel.this.margt - sqrt2;
                }
                AutofitTextRel.this.setLayoutParams(layoutParams);
                AutofitTextRel autofitTextRel10 = AutofitTextRel.this;
                autofitTextRel10.currentState = 3;
                if (!autofitTextRel10.bgDrawable.equals("0")) {
                    AutofitTextRel autofitTextRel11 = AutofitTextRel.this;
                    autofitTextRel11.wi = autofitTextRel11.getLayoutParams().width;
                    AutofitTextRel autofitTextRel12 = AutofitTextRel.this;
                    autofitTextRel12.he = autofitTextRel12.getLayoutParams().height;
                    AutofitTextRel autofitTextRel13 = AutofitTextRel.this;
                    autofitTextRel13.setBgDrawable(autofitTextRel13.bgDrawable);
                }
            }
            return true;
        }
    }

    class SimpleListner extends GestureDetector.SimpleOnGestureListener {
        SimpleListner() {
        }

        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return true;
        }

        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (AutofitTextRel.this.listener == null) {
                return true;
            }
            AutofitTextRel.this.listener.onDoubleTap();
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
            super.onLongPress(motionEvent);
        }
    }

    public void setNewRotation(float rotation) {
        this.setRotation(rotation);
    }
}
