package com.li.mylibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;



import java.lang.ref.WeakReference;

/**
 * @author li
 * 版本：1.0
 * 创建日期：2020/4/23 20
 * 描述：
 */
public class BaseDialogController {
    private BaseDialog mBaseDialog;
    private Window mWindow;
    private BaseDialogHelper mBaseDialogHelper;
    private Handler mHandler;

    public BaseDialogController(BaseDialog baseDialog, Window window) {
        this.mBaseDialog = baseDialog;
        this.mWindow = window;
        mHandler = new BaseDialogController.ButtonHandler(baseDialog);
    }

    public BaseDialog getBaseDialog() {
        return mBaseDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public void setBaseDialogHelper(BaseDialogHelper mBaseDialogHelper) {
        this.mBaseDialogHelper = mBaseDialogHelper;
    }

    public <T extends View> T getView(int viewId) {
        return mBaseDialogHelper.getView(viewId);
    }

    public void setText(int keyAt, CharSequence valueAt) {
        mBaseDialogHelper.setText(keyAt,valueAt);
    }
    private final View.OnClickListener mButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Post a message so we dismiss after the above handlers are executed
            mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, mBaseDialog)
                    .sendToTarget();
        }
    };


    public static class BaseParams{
        public Context mContext;
        public int mThemeResId;
//        点击能否取消
        public boolean mCancelable = true;
//       取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
//        消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;
//        按键监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public int mViewLayoutResId;
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        public SparseArray<View.OnClickListener> mClickListenerSparseArray = new SparseArray<>();
        public SparseArray<Boolean> mCancelClickListener= new SparseArray<>();
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mHeight= ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mGravity;
        public int mAnimation = 0;

        public BaseParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        public void apply(BaseDialogController dialog) {

            BaseDialogHelper helper = null;
            if (mViewLayoutResId!=0){
                helper = new BaseDialogHelper(mContext,mViewLayoutResId);
            }
            if (mView!=null){
                helper = new BaseDialogHelper();
                helper.setContentView(mView);
            }
            if (helper==null){
                throw new IllegalArgumentException("请设置布局setContentView");
            }
            dialog.getBaseDialog().setContentView(helper.getContentView());
            dialog.setBaseDialogHelper(helper);

            for(int i=0;i<mTextArray.size();i++){
                helper.setText(mTextArray.keyAt(i),mTextArray.valueAt(i));
            }

            for(int i=0;i<mClickListenerSparseArray.size();i++){
                helper.setClickListener(mClickListenerSparseArray.keyAt(i),mClickListenerSparseArray.valueAt(i));
                if (mCancelClickListener.valueAt(i))
                helper.getView(mCancelClickListener.keyAt(i)).setOnClickListener(dialog.mButtonHandler);
            }

            Window window = dialog.getWindow();
            window.setGravity(mGravity);
            if (mAnimation!=0){
                window.setWindowAnimations(mAnimation);
            }
            WindowManager.LayoutParams params =window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }

    private static final class ButtonHandler extends Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;

                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }
}
