package com.opposs.tube.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

/**
 * Created by xcl on 16/2/29.
 */
public class CheckableLinearLayout extends ImageView implements Checkable {

    private static final int[] CHECKED_STATE_SET = new int[]{16842912};

    private static final String TAG = CheckableLinearLayout.class.getCanonicalName();

    private boolean mChecked;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private static class SavedState extends BaseSavedState{

        public static final Creator<SavedState> CREATOR;

        boolean checked;

        static class MyCreator implements Creator<SavedState> {

            MyCreator() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        static {
            CREATOR = new MyCreator();
        }


        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState createFromParcel(Parcel in){
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }

        private SavedState(Parcel in) {
            super(in);
            this.checked = ((Boolean) in.readValue(null)).booleanValue();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(Boolean.valueOf(this.checked));
        }

        public String toString() {
            return CheckableLinearLayout.TAG + ".SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + this.checked + "}";
        }




    }

    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (this.mChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            drawable.setState(getDrawableState());
            invalidate();
        }
    }

    public boolean performClick() {
        return super.performClick();
    }

    public boolean performLongClick() {
        return super.performLongClick();
    }

    @Override
    public void setChecked(boolean checked) {

        this.mChecked = checked;

        refreshDrawableState();

    }

    @Override
    public boolean isChecked() {
        return this.mChecked;

    }

    @Override
    public void toggle() {
        setChecked(!this.mChecked);
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.checked = this.mChecked;
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.checked);
        requestLayout();
    }
}
