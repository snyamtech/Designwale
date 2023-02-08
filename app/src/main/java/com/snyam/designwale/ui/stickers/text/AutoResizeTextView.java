package com.snyam.designwale.ui.stickers.text;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.internal.view.SupportMenu;

import com.snyam.designwale.R;

public class AutoResizeTextView extends AppCompatTextView {

    public static float _minTextSize;
    private final RectF _availableSpaceRect;
    private final SizeTester _sizeTester;
    public TextPaint _paint;
    public float _spacingAdd;
    public float _spacingMult;
    public int _widthLimit;
    private boolean _initialized;
    private int _maxLines;
    private float _maxTextSize;
    private int mOutlineColor;
    private int mOutlineSize;
    private int mShadowColor;
    private float mShadowDx;
    private float mShadowDy;
    private float mShadowRadius;
    private int mTextColor;

    public AutoResizeTextView(Context context) {
        this(context, (AttributeSet) null, 16842884);
    }

    public AutoResizeTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842884);
    }

    @SuppressLint("ResourceType")
    public AutoResizeTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOutlineSize = 5;
        this.mOutlineColor = -256;
        this.mTextColor = getCurrentTextColor();
        if (attributeSet != null) {

            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.TextViewOutline);
            if (obtainStyledAttributes.hasValue(5)) {
                this.mOutlineSize = (int) obtainStyledAttributes.getDimension(R.styleable.TextViewOutline_mOutlineSize, 5.0f);
            }
            if (obtainStyledAttributes.hasValue(4)) {
                this.mOutlineColor = obtainStyledAttributes.getColor(R.styleable.TextViewOutline_mOutlineColor, -256);
            }
            if (obtainStyledAttributes.hasValue(3) || obtainStyledAttributes.hasValue(1) || obtainStyledAttributes.hasValue(2) || obtainStyledAttributes.hasValue(0)) {
                this.mShadowRadius = obtainStyledAttributes.getFloat(R.styleable.TextViewOutline_mShadowRadius, 0.0f);
                this.mShadowDx = obtainStyledAttributes.getFloat(R.styleable.TextViewOutline_mShadowDx, 0.0f);
                this.mShadowDy = obtainStyledAttributes.getFloat(R.styleable.TextViewOutline_mShadowDy, 0.0f);
                this.mShadowColor = obtainStyledAttributes.getColor(R.styleable.TextViewOutline_mShadowColor, 0);
            }
            obtainStyledAttributes.recycle();
        }

        this._availableSpaceRect = new RectF();
        this._spacingMult = 1.0f;
        this._spacingAdd = 0.0f;
        this._initialized = false;
        _minTextSize = TypedValue.applyDimension(2, 12.0f, getResources().getDisplayMetrics());
        this._maxTextSize = getTextSize();
        this._paint = new TextPaint(getPaint());
        if (this._maxLines == 0) {
            this._maxLines = -1;
        }
        this._sizeTester = new TextSize();
        this._initialized = true;
    }

    public boolean isValidWordWrap(char c, char c2) {
        return c == ' ' || c == '-';
    }

    @Override
    public void setAllCaps(boolean z) {
        super.setAllCaps(z);
        adjustTextSize();
    }



    @Override
    public void setTypeface(Typeface typeface) {
        super.setTypeface(typeface);
        adjustTextSize();
    }

    @Override
    public void setTextSize(float f) {
        this._maxTextSize = f;
        adjustTextSize();
    }

    @Override
    public int getMaxLines() {
        return this._maxLines;
    }

    @Override
    public void setMaxLines(int i) {
        super.setMaxLines(i);
        this._maxLines = i;
        adjustTextSize();
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        this._maxLines = 1;
        adjustTextSize();
    }

    @Override
    public void setSingleLine(boolean z) {
        super.setSingleLine(z);
        if (z) {
            this._maxLines = 1;
        } else {
            this._maxLines = -1;
        }
        adjustTextSize();
    }

    @Override
    public void setLines(int i) {
        super.setLines(i);
        this._maxLines = i;
        adjustTextSize();
    }

    @Override
    public void setTextSize(int i, float f) {
        Resources resources;
        Context context = getContext();
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        this._maxTextSize = TypedValue.applyDimension(i, f, resources.getDisplayMetrics());
        adjustTextSize();
    }

    @Override
    public void setLineSpacing(float f, float f2) {
        super.setLineSpacing(f, f2);
        this._spacingMult = f2;
        this._spacingAdd = f;
    }

    public void setMinTextSize(float f) {
        _minTextSize = f;
        adjustTextSize();
    }

    private void setPaintToOutline() {
        TextPaint paint = getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth((float) this.mOutlineSize);
        super.setTextColor(this.mOutlineColor);
        super.setShadowLayer(this.mShadowRadius, this.mShadowDx, this.mShadowDy, this.mShadowColor);
    }

    private void setPaintToRegular() {
        TextPaint paint = getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0.0f);
        super.setTextColor(this.mTextColor);
        if (this.mOutlineSize > 0) {
            super.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        } else {
            super.setShadowLayer(this.mShadowRadius, this.mShadowDx, this.mShadowDy, this.mShadowColor);
        }
    }

    private void setShader() {
        TextPaint paint = getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(new LinearGradient(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), -16711936, SupportMenu.CATEGORY_MASK, Shader.TileMode.REPEAT));
    }

    public void setGradient(Shader shader){
        TextPaint paint = getPaint();
        paint.setShader(shader);
    }


    @Override
    public void onMeasure(int i, int i2) {
        setPaintToOutline();
        super.onMeasure(i, i2);
    }

    @Override
    public void setTextColor(int i) {
        TextPaint paint = getPaint();
        paint.setShader(null);
        super.setTextColor(i);
        this.mTextColor = i;
    }

    @Override
    public void setShadowLayer(float radius, float dx, float dy, int color) {
        super.setShadowLayer(radius, dx, dy, color);
        this.mShadowRadius = radius;
        this.mShadowDx = dx;
        this.mShadowDy = dy;
        this.mShadowColor = color;
    }

    public void setOutlineSize(int i) {
        this.mOutlineSize = i;
    }

    public void setOutlineColor(int i) {
        this.mOutlineColor = i;
    }

    @Override
    public void onDraw(Canvas canvas) {
        setPaintToOutline();
        super.onDraw(canvas);
        setPaintToRegular();
        super.onDraw(canvas);
    }

    private void adjustTextSize() {
        if (this._initialized) {
            int i = (int) _minTextSize;
            int measuredHeight = (getMeasuredHeight() - getCompoundPaddingBottom()) - getCompoundPaddingTop();
            this._widthLimit = (getMeasuredWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight();
            if (this._widthLimit > 0) {
                this._paint = new TextPaint(getPaint());
                RectF rectF = this._availableSpaceRect;
                rectF.right = (float) this._widthLimit;
                rectF.bottom = (float) measuredHeight;
                superSetTextSize(i);
            }else {
//                setScaleX(getScaleX() + i/100);
//                setScaleY(getScaleY() + i/100);
            }
        }
    }

    private void superSetTextSize(int i) {
        super.setTextSize(0, (float) binarySearch(i, (int) this._maxTextSize, this._sizeTester, this._availableSpaceRect));
    }

    private int binarySearch(int i, int i2, SizeTester sizeTester, RectF rectF) {
        int i3 = i2 - 1;
        int i4 = i;
        while (i <= i3) {
            int i5 = (i + i3) >>> 1;
            int onTestSize = sizeTester.onTestSize(i5, rectF);
            if (onTestSize < 0) {
                int i6 = i5 + 1;
                i4 = i;
                i = i6;
            } else if (onTestSize <= 0) {
                return i5;
            } else {
                i4 = i5 - 1;
                i3 = i4;
            }
        }
        return i4;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        super.onTextChanged(charSequence, i, i2, i3);
        adjustTextSize();
    }

    @Override

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3 || i2 != i4) {
            adjustTextSize();
        }
    }

    public void setPattern(Bitmap bitmap) {
        TextPaint paint = getPaint();
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
    }

    @Override
    public float getTextSize() {
        return super.getTextSize();
    }

    private interface SizeTester {
        int onTestSize(int i, RectF rectF);
    }

    class TextSize implements SizeTester {
        final RectF textRect = new RectF();

        TextSize() {
        }

        @TargetApi(16)
        public int onTestSize(int i, RectF rectF) {
            String str;
            AutoResizeTextView.this._paint.setTextSize((float) i);
            TransformationMethod transformationMethod = AutoResizeTextView.this.getTransformationMethod();
            if (transformationMethod != null) {
                str = transformationMethod.getTransformation(AutoResizeTextView.this.getText(), AutoResizeTextView.this).toString();
            } else {
                str = AutoResizeTextView.this.getText().toString();
            }
            if (AutoResizeTextView.this.getMaxLines() == 1) {
                this.textRect.bottom = AutoResizeTextView.this._paint.getFontSpacing();
                this.textRect.right = AutoResizeTextView.this._paint.measureText(str);
            } else {
                StaticLayout staticLayout = new StaticLayout(str, AutoResizeTextView.this._paint, AutoResizeTextView.this._widthLimit, Layout.Alignment.ALIGN_NORMAL, AutoResizeTextView.this._spacingMult, AutoResizeTextView.this._spacingAdd, true);
                if (AutoResizeTextView.this.getMaxLines() != -1 && staticLayout.getLineCount() > AutoResizeTextView.this.getMaxLines()) {
                    return 1;
                }
                this.textRect.bottom = (float) staticLayout.getHeight();
                int lineCount = staticLayout.getLineCount();
                int i2 = -1;
                for (int i3 = 0; i3 < lineCount; i3++) {
                    int lineEnd = staticLayout.getLineEnd(i3);
                    if (i3 < lineCount - 1 && lineEnd > 0 && !AutoResizeTextView.this.isValidWordWrap(str.charAt(lineEnd - 1), str.charAt(lineEnd))) {
                        return 1;
                    }
                    if (((float) i2) < staticLayout.getLineRight(i3) - staticLayout.getLineLeft(i3)) {
                        i2 = ((int) staticLayout.getLineRight(i3)) - ((int) staticLayout.getLineLeft(i3));
                    }
                }
                this.textRect.right = (float) i2;
            }
            this.textRect.offsetTo(0.0f, 0.0f);
            return rectF.contains(this.textRect) ? -1 : 1;
        }
    }


}
