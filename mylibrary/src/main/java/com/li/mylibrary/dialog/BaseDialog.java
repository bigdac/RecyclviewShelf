package com.li.mylibrary.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.li.mylibrary.R;
import com.li.mylibrary.Utils.ScreenSizeUtils;

import java.lang.ref.WeakReference;

/**
 * @author li
 * 版本：1.0
 * 创建日期：2020/4/23 20
 * 描述：
 */
public class BaseDialog extends Dialog {

    private BaseDialogController mAlert;

    protected BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new BaseDialogController(this, getWindow());
    }

    public void setText(int keyAt, CharSequence valueAt) {
        mAlert.setText( keyAt,  valueAt);

    }

    public void setClickListener(int keyAt, View.OnClickListener valueAt) {
        View view = getView(keyAt);
        if (view!=null)
            view.setOnClickListener(valueAt);
    }

    public  <T extends View>T getView (int viewId){
       return   mAlert.getView( viewId);
    }


    //  builder  创建模式---->  BaseDialog内部的BaseParams
    public static class Builder {
        private final BaseDialogController.BaseParams P;

        public Builder(Context context) {
            this(context, R.style.MyDialog);
        }

        public Builder(Context context, int themeResId) {
            P = new BaseDialogController.BaseParams(context, themeResId);

        }

        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }


        public Builder setContentView(int layoutResId) {
            P.mView = null;
            P.mViewLayoutResId = layoutResId;
            return this;
        }

        public Builder setText(int viewId, CharSequence text) {
            P.mTextArray.put(viewId, text);
            return this;
        }


        public Builder setClickListener(int viewId, View.OnClickListener onClickListener,boolean dismiss) {
            P.mClickListenerSparseArray.put(viewId, onClickListener);
            P.mCancelClickListener.put(viewId, dismiss);
            return this;
        }
        public Builder setClickListener(int viewId, View.OnClickListener onClickListener) {
            setClickListener(viewId,onClickListener,false);
            return this;
        }
        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimation = R.style.BottomDialog_Animation;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * @author Li
         * 版本：1.0
         * 创建日期：2020/4/28
         * 描述：设置宽高 最好转成dp传过来
         */
        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }
        /**
         * @author Li
         * 版本：1.0
         * 创建日期：2020/4/28
         * 描述：屏幕百分比宽高
         *   百分比为 0-100
         */
        public Builder setPercentWidthAndHeight(int percentWidth, int percentHeight) {
            P.mWidth = (int)(ScreenSizeUtils.getInstance(P.mContext).getScreenWidth()*currentPercentValue(percentWidth));
            P.mHeight = (int)(ScreenSizeUtils.getInstance(P.mContext).getScreenHeight()*currentPercentValue(percentHeight));
            return this;
        }

        public Builder addAnimation(int isAnimation) {
            P.mAnimation = isAnimation;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }
        public Builder addDefaultAnimation() {
            P.mAnimation = R.style.BottomDialog_Animation;
            return this;
        }

        public BaseDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final BaseDialog dialog = new BaseDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public BaseDialog show() {
            final BaseDialog dialog = create();
            dialog.show();
            return dialog;
        }

        private float currentPercentValue(int value){
            if (value>100){
                value=100;
            }else if (value<=0){
                value = 1;
            }
            return (float) value/100;
        }
    }

}
