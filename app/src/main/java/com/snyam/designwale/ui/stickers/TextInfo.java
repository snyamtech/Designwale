package com.snyam.designwale.ui.stickers;

import androidx.core.view.ViewCompat;

public class TextInfo {
    public String name = "";
    int CurveRotateProg;
    int FIELD_ONE = 0;
    int XRotateProg;
    int YRotateProg;
    int ZRotateProg;
    private int BG_ALPHA = 255;
    private int BG_COLOR = 0;
    private String BG_DRAWABLE = "0";
    private String FIELD_FOUR = "";
    private String FIELD_THREE = "";
    private String FIELD_TWO = "";
    private String FONT_NAME = "";
    private int HEIGHT;
    private int ORDER;
    private float POS_X = 0.0f;
    private float POS_Y = 0.0f;
    private float ROTATION;
    private int SHADOW_COLOR = 0;
    private int SHADOW_PROG = 0;
    private int TEMPLATE_ID;
    private String TEXT = "";
    private int TEXT_ALPHA = 100;
    private int TEXT_COLOR = ViewCompat.MEASURED_STATE_MASK;
    private int TEXT_ID;
    private String TYPE = "";
    private int WIDTH;
    private float leftRighShadow;
    private int outLineColor;
    private int outLineSize;
    private float topBottomShadow;
    private int outLineOpacity = 100;

    public TextInfo(String name, int i, String str, String str2, int i2, int i3, int i4, int i5, String str3, int i6,
                    int i7, float f, float f2, int i8, int i9, float f3, String str4, int i10, int i11, int i12, int i13,
                    int i14, int i15, String str5, String str6, String str7, float f4, float f5, int i16, int i17, int op) {
        this.name = name;
        this.TEMPLATE_ID = i;
        this.TEXT = str;
        this.FONT_NAME = str2;
        this.TEXT_COLOR = i2;
        this.TEXT_ALPHA = i3;
        this.SHADOW_COLOR = i4;
        this.SHADOW_PROG = i5;
        this.BG_DRAWABLE = str3;
        this.BG_COLOR = i6;
        this.BG_ALPHA = i7;
        this.POS_X = f;
        this.POS_Y = f2;
        this.WIDTH = i8;
        this.HEIGHT = i9;
        this.ROTATION = f3;
        this.TYPE = str4;
        this.ORDER = i10;
        this.XRotateProg = i11;
        this.YRotateProg = i12;
        this.ZRotateProg = i13;
        this.CurveRotateProg = i14;
        this.FIELD_ONE = i15;
        this.FIELD_TWO = str5;
        this.FIELD_THREE = str6;
        this.FIELD_FOUR = str7;
        this.leftRighShadow = f4;
        this.topBottomShadow = f5;
        this.outLineSize = i16;
        this.outLineColor = i17;
        this.outLineOpacity=op;
    }

    public TextInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextInfo(String name, int curveRotateProg, int FIELD_ONE, int XRotateProg, int YRotateProg, int ZRotateProg, int BG_ALPHA, int BG_COLOR, String BG_DRAWABLE, String FIELD_FOUR, String FIELD_THREE, String FIELD_TWO, String FONT_NAME, int HEIGHT, int ORDER, float POS_X, float POS_Y, float ROTATION, int SHADOW_COLOR, int SHADOW_PROG, int TEMPLATE_ID, String TEXT, int TEXT_ALPHA, int TEXT_COLOR, int TEXT_ID, String TYPE, int WIDTH, float leftRighShadow, int outLineColor, int outLineSize, float topBottomShadow, int outLineOpacity) {
        this.name = name;
        CurveRotateProg = curveRotateProg;
        this.FIELD_ONE = FIELD_ONE;
        this.XRotateProg = XRotateProg;
        this.YRotateProg = YRotateProg;
        this.ZRotateProg = ZRotateProg;
        this.BG_ALPHA = BG_ALPHA;
        this.BG_COLOR = BG_COLOR;
        this.BG_DRAWABLE = BG_DRAWABLE;
        this.FIELD_FOUR = FIELD_FOUR;
        this.FIELD_THREE = FIELD_THREE;
        this.FIELD_TWO = FIELD_TWO;
        this.FONT_NAME = FONT_NAME;
        this.HEIGHT = HEIGHT;
        this.ORDER = ORDER;
        this.POS_X = POS_X;
        this.POS_Y = POS_Y;
        this.ROTATION = ROTATION;
        this.SHADOW_COLOR = SHADOW_COLOR;
        this.SHADOW_PROG = SHADOW_PROG;
        this.TEMPLATE_ID = TEMPLATE_ID;
        this.TEXT = TEXT;
        this.TEXT_ALPHA = TEXT_ALPHA;
        this.TEXT_COLOR = TEXT_COLOR;
        this.TEXT_ID = TEXT_ID;
        this.TYPE = TYPE;
        this.WIDTH = WIDTH;
        this.leftRighShadow = leftRighShadow;
        this.outLineColor = outLineColor;
        this.outLineSize = outLineSize;
        this.topBottomShadow = topBottomShadow;
        this.outLineOpacity = outLineOpacity;
    }

    public int getOutLineOpacity() {
        return outLineOpacity;
    }

    public void setOutLineOpacity(int outLineOpacity) {
        this.outLineOpacity = outLineOpacity;
    }

    public int getWIDTH() {
        return this.WIDTH;
    }

    public void setWIDTH(int i) {
        this.WIDTH = i;
    }

    public int getHEIGHT() {
        return this.HEIGHT;
    }

    public void setHEIGHT(int i) {
        this.HEIGHT = i;
    }

    public String getFONT_NAME() {
        return this.FONT_NAME;
    }

    public void setFONT_NAME(String str) {
        this.FONT_NAME = str;
    }

    public String getTEXT() {
        return this.TEXT;
    }

    public void setTEXT(String str) {
        this.TEXT = str;
    }

    public int getTEXT_COLOR() {
        return this.TEXT_COLOR;
    }

    public void setTEXT_COLOR(int i) {
        this.TEXT_COLOR = i;
    }

    public int getTEXT_ALPHA() {
        return this.TEXT_ALPHA;
    }

    public void setTEXT_ALPHA(int i) {
        this.TEXT_ALPHA = i;
    }

    public int getSHADOW_PROG() {
        return this.SHADOW_PROG;
    }

    public void setSHADOW_PROG(int i) {
        this.SHADOW_PROG = i;
    }

    public int getSHADOW_COLOR() {
        return this.SHADOW_COLOR;
    }

    public void setSHADOW_COLOR(int i) {
        this.SHADOW_COLOR = i;
    }

    public String getBG_DRAWABLE() {
        return this.BG_DRAWABLE;
    }

    public void setBG_DRAWABLE(String str) {
        this.BG_DRAWABLE = str;
    }

    public int getBG_COLOR() {
        return this.BG_COLOR;
    }

    public void setBG_COLOR(int i) {
        this.BG_COLOR = i;
    }

    public int getBG_ALPHA() {
        return this.BG_ALPHA;
    }

    public void setBG_ALPHA(int i) {
        this.BG_ALPHA = i;
    }

    public float getPOS_X() {
        return this.POS_X;
    }

    public void setPOS_X(float f) {
        this.POS_X = f;
    }

    public float getPOS_Y() {
        return this.POS_Y;
    }

    public void setPOS_Y(float f) {
        this.POS_Y = f;
    }

    public float getROTATION() {
        return this.ROTATION;
    }

    public void setROTATION(float f) {
        this.ROTATION = f;
    }

    public String getTYPE() {
        return this.TYPE;
    }

    public void setTYPE(String str) {
        this.TYPE = str;
    }

    public int getORDER() {
        return this.ORDER;
    }

    public void setORDER(int i) {
        this.ORDER = i;
    }

    public int getTEXT_ID() {
        return this.TEXT_ID;
    }

    public void setTEXT_ID(int i) {
        this.TEXT_ID = i;
    }

    public int getTEMPLATE_ID() {
        return this.TEMPLATE_ID;
    }

    public void setTEMPLATE_ID(int i) {
        this.TEMPLATE_ID = i;
    }

    public int getXRotateProg() {
        return this.XRotateProg;
    }

    public void setXRotateProg(int i) {
        this.XRotateProg = i;
    }

    public int getYRotateProg() {
        return this.YRotateProg;
    }

    public void setYRotateProg(int i) {
        this.YRotateProg = i;
    }

    public int getZRotateProg() {
        return this.ZRotateProg;
    }

    public void setZRotateProg(int i) {
        this.ZRotateProg = i;
    }

    public int getCurveRotateProg() {
        return this.CurveRotateProg;
    }

    public void setCurveRotateProg(int i) {
        this.CurveRotateProg = i;
    }

    public int getFIELD_ONE() {
        return this.FIELD_ONE;
    }

    public void setFIELD_ONE(int i) {
        this.FIELD_ONE = i;
    }

    public String getFIELD_TWO() {
        return this.FIELD_TWO;
    }

    public void setFIELD_TWO(String str) {
        this.FIELD_TWO = str;
    }

    public String getFIELD_THREE() {
        return this.FIELD_THREE;
    }

    public void setFIELD_THREE(String str) {
        this.FIELD_THREE = str;
    }

    public String getFIELD_FOUR() {
        return this.FIELD_FOUR;
    }

    public void setFIELD_FOUR(String str) {
        this.FIELD_FOUR = str;
    }

    public float getTopBottomShadow() {
        return this.topBottomShadow;
    }

    public void setTopBottomShadow(float f) {
        this.topBottomShadow = f;
    }

    public float getLeftRighShadow() {
        return this.leftRighShadow;
    }

    public void setLeftRighShadow(float f) {
        this.leftRighShadow = f;
    }

    public int getOutLineSize() {
        return this.outLineSize;
    }

    public void setOutLineSize(int i) {
        this.outLineSize = i;
    }

    public int getOutLineColor() {
        return this.outLineColor;
    }

    public void setOutLineColor(int i) {
        this.outLineColor = i;
    }
}
