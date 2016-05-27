package com.admin.mc;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.adutils.ABLogUtil;
import com.adutils.ABTextUtil;
import com.adutils.phone.ABDensityUtil;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;

/**
 * 项目名称：MistakesCollection
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/27  10:55
 * 修改人：Michael-hj
 * 修改时间：2016/5/27  10:55
 * 修改备注：
 */
public class ABMCScreenPopupWindow {
    private static Activity activity;
    private static ABMCScreenPopupWindow application;

    /**
     * @param activity Activity
     */
    public ABMCScreenPopupWindow(Activity activity) {
        ABMCScreenPopupWindow.activity = activity;
    }

    public static ABMCScreenPopupWindow getInstance(Activity c) {
        if (application == null)
            application = new ABMCScreenPopupWindow(c);
        activity = c;
        return application;
    }

    private View mBaseView;
    private ScreenPopWindow mPopupWindow;
    //字体大小 sp
    private int textSize = 14;
    private List<ScreenPopWindow.MyPopupWindowData> myPopupWindowDatas;

    /**
     * 字体大小 sp
     *
     * @param textSize
     */
    @SuppressWarnings("JavaDoc")
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        if (mPopupWindow != null)
            mPopupWindow.setTextSize(textSize);
    }

    public ABMCScreenPopupWindow(Activity activity, final View baseView, int textSize, List<String> listString, String splitType, MyItemClickListener myItemClickListener) {
        ABMCScreenPopupWindow.activity = activity;
        openPopupWindow(true, baseView, textSize, listString, splitType, myItemClickListener);
    }

    public ABMCScreenPopupWindow(Activity activity, final View baseView, int baseViewWidth, MyItemClickListener myItemClickListener) {
        ABMCScreenPopupWindow.activity = activity;
        openPopupWindow(true, baseView, textSize, new ArrayList<String>(), ";", myItemClickListener);
    }

    public ABMCScreenPopupWindow(Activity activity, final View baseView, int baseViewWidth, int baseViewHeight, MyItemClickListener myItemClickListener) {
        ABMCScreenPopupWindow.activity = activity;
        openPopupWindow(true, baseView, textSize, new ArrayList<String>(), ";", LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, myItemClickListener);
    }

    /**
     * 开启下拉框
     *
     * @param isNewPopopWindow    是否每次都新建一个pop
     * @param baseView            popupWindow停靠控件
     * @param textSize            默认单位（SP）
     * @param listString          显示的内容以分隔符分开的
     * @param splitType           字符串的分隔符
     * @param myItemClickListener 事件传出的接口
     */
    public void openPopupWindow(boolean isNewPopopWindow, final View baseView, int textSize, List<String> listString, String splitType, MyItemClickListener myItemClickListener) {
        openPopupWindow(isNewPopopWindow, baseView, textSize, listString, splitType, getMaxWidth(textSize, listString, splitType), LinearLayout.LayoutParams.WRAP_CONTENT, myItemClickListener);
    }

    /**
     * 开启下拉框
     *
     * @param isNewPopopWindow    是否每次都新建一个pop
     * @param baseView            popupWindow停靠控件
     * @param textSize            默认单位（SP）
     * @param listString          显示的内容以分隔符分开的
     * @param splitType           字符串的分隔符
     * @param baseViewWidth       popupwindow宽度
     * @param baseViewHeight      popupwindow高度
     * @param myItemClickListener 事件传出的接口
     */
    public void openPopupWindow(boolean isNewPopopWindow, final View baseView, int textSize, List<String> listString, String splitType, int baseViewWidth, int baseViewHeight, MyItemClickListener myItemClickListener) {
        this.textSize = textSize;
        List<ScreenPopWindow.MyPopupWindowData> myPopupWindowDatas = getEntityData(listString, splitType);
        if (myPopupWindowDatas == null || myPopupWindowDatas.size() < 1) {
            ABLogUtil.i("请添加数据 listString：" + listString);
            return;
        }
        isNewPopopWindow = isNewPopopWindow | !myPopupWindowDatas.equals(this.myPopupWindowDatas);
        int mBaseViewWidth = getMaxWidth(textSize, listString, splitType);
        baseViewWidth = mBaseViewWidth > baseViewHeight ? mBaseViewWidth : baseViewHeight;
        if (this.mBaseView == null || isNewPopopWindow || !this.mBaseView.equals(baseView)) {
            this.mBaseView = baseView;
            if (mPopupWindow != null && mPopupWindow.isShowing())
                mPopupWindow.dismiss();
            mPopupWindow = null;
            mPopupWindow = new ScreenPopWindow(activity, mBaseView, textSize, baseViewWidth, baseViewHeight, myItemClickListener);
        } else {
            if (mPopupWindow == null) {
                this.mBaseView = baseView;
                mPopupWindow = new ScreenPopWindow(activity, mBaseView, textSize, baseViewWidth, baseViewHeight, myItemClickListener);
            } else if (mPopupWindow.isShowing())
                mPopupWindow.dismiss();
        }
        //更新数据
        setStringData(listString, splitType);
    }

    /**
     * 获取最宽的一行的宽度
     *
     * @param textSize   字体大小
     * @param listString popupwindow显示内容
     * @param splitType  分隔符
     * @return MaxW
     */
    private int getMaxWidth(int textSize, List<String> listString, String splitType) {
        int MaxW = 0;
        int tSize = ABTextUtil.getFontWidth(ABDensityUtil.sp2px(activity, textSize), "正");
        ArrayList<ScreenPopWindow.MyPopupWindowData> ls = getEntityData(listString, splitType);
        if (ls.size() > 0)
            for (ScreenPopWindow.MyPopupWindowData it : ls) {
                if (it.getPopItemNames() == null) {
                    ABLogUtil.e("it.getPopItemNames() == null");
                    continue;
                }
                int ww ;
                String firstString = it.getPopItemNames().get(0);
                String name = "";
                if (firstString.contains(":"))
                    name = firstString.substring(0, firstString.indexOf(":"));
                ww = name.length() * tSize + ABTextUtil.getFontWidth(ABDensityUtil.sp2px(activity, textSize), ":") + ABDensityUtil.dip2px(activity, 10 * 2);//10位textView的padding
                for (int i = 1; i < it.getPopItemNames().size(); i++) {
                    ww += it.getPopItemNames().get(i).length() * tSize + ABDensityUtil.dip2px(activity, 10 * 2);//10位radiobutton的padding
                }
                if (ww > MaxW) {
                    MaxW = ww;
                }
            }
        return MaxW + ABDensityUtil.dip2px(activity, 3 * 2 + 5 * 2);//3为listview的padding
    }

    /**
     * 添加数据
     *
     * @param myPopupWindowDatas popupwindow显示数据
     */
    public void setEntityData(List<ScreenPopWindow.MyPopupWindowData> myPopupWindowDatas) {
        setData(myPopupWindowDatas);
    }

    /**
     * 添加数据
     *
     * @param listString popupwindow显示数据
     * @param splitType  分隔符
     */
    public void setStringData(List<String> listString, String splitType) {
        setData(getEntityData(listString, splitType));
    }

    /**
     * 获取数据（数据组装）
     *
     * @param listString popupwindow显示数据
     * @return ArrayList<ScreenPopWindow.MyPopupWindowData>
     */
    private ArrayList<ScreenPopWindow.MyPopupWindowData> getEntityData(List<String> listString, String splitType) {
        ArrayList<ScreenPopWindow.MyPopupWindowData> myPopupWindowDatas = new ArrayList<>();
        if (listString == null || listString.size() < 1) {
            ABLogUtil.i("请添加数据 listString：" + listString);
            return new ArrayList<>();
        }
        ScreenPopWindow.MyPopupWindowData data;
        for (int i = 0; i < listString.size(); i++) {
            data = new ScreenPopWindow.MyPopupWindowData();
            data.setPopItemNames(getStringSpitList(listString.get(i), splitType));
            myPopupWindowDatas.add(data);
        }
        return myPopupWindowDatas;
    }

    /**
     * 获取字符串分割后的集合
     *
     * @param arrayString popupwindow显示数据
     * @param splitType   分隔符
     * @return StringSpitList
     */
    private ArrayList<String> getStringSpitList(String arrayString, String splitType) {
        String[] ss;
        ArrayList<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(arrayString))
            return list;
        ss = arrayString.split(splitType);
        if (ss.length < 1)
            return list;
        addAll(list, ss);
        return list;
    }

    /**
     * 设置数据
     *
     * @param myPopupWindowDatas popupwindow显示数据
     */
    public void setData(List<ScreenPopWindow.MyPopupWindowData> myPopupWindowDatas) {
        if (mPopupWindow != null) {
            this.myPopupWindowDatas = myPopupWindowDatas;
            mPopupWindow.setData(myPopupWindowDatas);
        }
    }

    /**
     * 自定义事件监听接口
     */
    public interface MyItemClickListener {
        void onListItemIndexSelected(int line, int index);
    }

}
