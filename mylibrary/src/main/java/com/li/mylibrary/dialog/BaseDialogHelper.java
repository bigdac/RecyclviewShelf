package com.li.mylibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;



import java.lang.ref.WeakReference;

/**
 * @author li
 * 版本：1.0
 * 创建日期：2020/4/23 20
 * 描述：
 */
    public class BaseDialogHelper {

    private View mContentView;
    private SparseArray<WeakReference<View>> mViews ;
    public BaseDialogHelper(Context mContext, int mViewLayoutResId) {
        this();
        mContentView = LayoutInflater.from(mContext).inflate(mViewLayoutResId,null);
    }

    public BaseDialogHelper() {
        mViews = new SparseArray<>();
    }

    public void setContentView(View mView) {
        this.mContentView = mView;
    }

    public void setText(int keyAt, CharSequence valueAt) {
        TextView textView = getView(keyAt);
        if (textView!=null)
            textView.setText(valueAt);

    }

    public void setClickListener(int keyAt, View.OnClickListener valueAt) {
        View view = getView(keyAt);
        if (view!=null)
            view.setOnClickListener(valueAt);
    }

    public  <T extends View>T getView (int viewId){
        WeakReference<View> views = mViews.get(viewId);
        View  view  = null;
        if (views!=null){
            view = views.get();
        }else {
            view = mContentView.findViewById(viewId);
            mViews.put(viewId,new WeakReference<>(view));
        }
        return (T) view;
    }

    public View getContentView() {
        return mContentView;
    }
}
