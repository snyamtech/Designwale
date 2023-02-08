package com.snyam.designwale.ui.stickers;

import android.graphics.Bitmap;
import android.net.Uri;

public class ElementInfo {
    public String name;
    int FIELD_ONE = 0;
    int ScaleProg;
    int XRotateProg;
    int YRotateProg;
    int ZRotateProg;
    private Bitmap BITMAP;
    private String COLORTYPE;
    private int COMP_ID;
    private String FIELD_FOUR = "";
    private String FIELD_THREE = "";
    private String FIELD_TWO = "";
    private int HEIGHT;
    private int ORDER;
    private float POS_X;
    private float POS_Y;
    private String RES_ID;
    private Uri RES_URI;
    private float ROTATION;
    private int STC_COLOR;
    private int STC_HUE;
    private int STC_OPACITY;
    private String STKR_PATH = "";
    private int TEMPLATE_ID;
    private String TYPE = "";
    private int WIDTH;
    private float Y_ROTATION;

    public ElementInfo(String name, int mTEMPLATE_ID, float mPOS_X, float mPOS_Y, int mWIDTH, int mHEIGHT,
                       float mROTATION, float mY_ROTATION, String mRES_ID, String mTYPE,
                       int mORDER, int mSTC_COLOR, int mSTC_OPACITY, int mXRotateProg, int mYRotateProg,
                       int mZRotateProg, int mScaleProg, String mSTKR_PATH, String mCOLORTYPE,
                       int mSTC_HUE, int mFIELD_ONE, String mFIELD_TWO, String mFIELD_THREE,
                       String mFIELD_FOUR, Uri mRES_URI, Bitmap mBITMAP) {
        this.TEMPLATE_ID = mTEMPLATE_ID;
        this.POS_X = mPOS_X;
        this.POS_Y = mPOS_Y;
        this.WIDTH = mWIDTH;
        this.HEIGHT = mHEIGHT;
        this.ROTATION = mROTATION;
        this.Y_ROTATION = mY_ROTATION;
        this.RES_ID = mRES_ID;
        this.RES_URI = mRES_URI;
        this.BITMAP = mBITMAP;
        this.TYPE = mTYPE;
        this.ORDER = mORDER;
        this.STC_COLOR = mSTC_COLOR;
        this.COLORTYPE = mCOLORTYPE;
        this.STC_OPACITY = mSTC_OPACITY;
        this.XRotateProg = mXRotateProg;
        this.YRotateProg = mYRotateProg;
        this.ZRotateProg = mZRotateProg;
        this.ScaleProg = mScaleProg;
        this.STKR_PATH = mSTKR_PATH;
        this.STC_HUE = mSTC_HUE;
        this.FIELD_ONE = mFIELD_ONE;
        this.FIELD_TWO = mFIELD_TWO;
        this.FIELD_THREE = mFIELD_THREE;
        this.FIELD_FOUR = mFIELD_FOUR;
        this.name = name;
    }

    public ElementInfo() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ElementInfo{" +
                "name='" + name + '\'' +
                ", FIELD_ONE=" + FIELD_ONE +
                ", ScaleProg=" + ScaleProg +
                ", XRotateProg=" + XRotateProg +
                ", YRotateProg=" + YRotateProg +
                ", ZRotateProg=" + ZRotateProg +
                ", BITMAP=" + BITMAP +
                ", COLORTYPE='" + COLORTYPE + '\'' +
                ", COMP_ID=" + COMP_ID +
                ", FIELD_FOUR='" + FIELD_FOUR + '\'' +
                ", FIELD_THREE='" + FIELD_THREE + '\'' +
                ", FIELD_TWO='" + FIELD_TWO + '\'' +
                ", HEIGHT=" + HEIGHT +
                ", ORDER=" + ORDER +
                ", POS_X=" + POS_X +
                ", POS_Y=" + POS_Y +
                ", RES_ID='" + RES_ID + '\'' +
                ", RES_URI=" + RES_URI +
                ", ROTATION=" + ROTATION +
                ", STC_COLOR=" + STC_COLOR +
                ", STC_HUE=" + STC_HUE +
                ", STC_OPACITY=" + STC_OPACITY +
                ", STKR_PATH='" + STKR_PATH + '\'' +
                ", TEMPLATE_ID=" + TEMPLATE_ID +
                ", TYPE='" + TYPE + '\'' +
                ", WIDTH=" + WIDTH +
                ", Y_ROTATION=" + Y_ROTATION +
                '}';
    }

    public int getCOMP_ID() {
        return this.COMP_ID;
    }

    public void setCOMP_ID(int i) {
        this.COMP_ID = i;
    }

    public int getTEMPLATE_ID() {
        return this.TEMPLATE_ID;
    }

    public void setTEMPLATE_ID(int i) {
        this.TEMPLATE_ID = i;
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

    public String getRES_ID() {
        return this.RES_ID;
    }

    public void setRES_ID(String str) {
        this.RES_ID = str;
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

    public float getROTATION() {
        return this.ROTATION;
    }

    public void setROTATION(float f) {
        this.ROTATION = f;
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

    public float getY_ROTATION() {
        return this.Y_ROTATION;
    }

    public void setY_ROTATION(float f) {
        this.Y_ROTATION = f;
    }

    public Uri getRES_URI() {
        return this.RES_URI;
    }

    public void setRES_URI(Uri uri) {
        this.RES_URI = uri;
    }

    public Bitmap getBITMAP() {
        return this.BITMAP;
    }

    public void setBITMAP(Bitmap bitmap) {
        this.BITMAP = bitmap;
    }

    public int getSTC_COLOR() {
        return this.STC_COLOR;
    }

    public void setSTC_COLOR(int i) {
        this.STC_COLOR = i;
    }

    public String getCOLORTYPE() {
        return this.COLORTYPE;
    }

    public void setCOLORTYPE(String str) {
        this.COLORTYPE = str;
    }

    public int getSTC_OPACITY() {
        return this.STC_OPACITY;
    }

    public void setSTC_OPACITY(int i) {
        this.STC_OPACITY = i;
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

    public int getScaleProg() {
        return this.ScaleProg;
    }

    public void setScaleProg(int i) {
        this.ScaleProg = i;
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

    public String getSTKR_PATH() {
        return this.STKR_PATH;
    }

    public void setSTKR_PATH(String str) {
        this.STKR_PATH = str;
    }

    public int getSTC_HUE() {
        return this.STC_HUE;
    }

    public void setSTC_HUE(int i) {
        this.STC_HUE = i;
    }
}
