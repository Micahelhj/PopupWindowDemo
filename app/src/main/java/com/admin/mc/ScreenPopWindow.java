package com.admin.mc;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adutils.ABLogUtil;
import com.adutils.popup.ABToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：MistakesCollection
 * 类描述：本popupWindow背景透明  上面的三角是TriangleView绘制的
 * 创建人：Michael-hj
 * 创建时间：2016/5/27  10:55
 * 修改人：Michael-hj
 * 修改时间：2016/5/27  10:55
 * 修改备注：
 */
public class ScreenPopWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    /**
     * 上下文
     */
    private Activity activity;
    /**
     * popupWindow停靠控件
     */
    private View mBaseView;
    /**
     * 数据适配器
     */
    private InsideListAdapter mInsideListAdapter;
    /**
     * 自定义事件监听接口
     */
    private ABMCScreenPopupWindow.MyItemClickListener myItemClickListener;
    /**
     * 字体大小
     */
    private int textSize;
    /**
     * 当前listView
     */
    private ListView popupwindowlist;

    /**
     * 字体大小获取
     *
     * @return textSize字体大小
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     * 字体大小设置
     *
     * @param textSize textSize字体大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * widget_mc_popupwindow_lay 构造器
     *
     * @param activity            上下文
     * @param mBaseView           popupWindow停靠控件
     * @param textSize            字体大小
     * @param baseViewWidth       popupwindow高
     * @param baseViewHeight      popupwindow高
     * @param myItemClickListener 自定义事件监听接口
     */
    public ScreenPopWindow(final Activity activity, View mBaseView, int textSize, int baseViewWidth, int baseViewHeight, ABMCScreenPopupWindow.MyItemClickListener myItemClickListener) {
        this.activity = activity;
        this.mBaseView = mBaseView;
        this.textSize = textSize;
        this.myItemClickListener = myItemClickListener;
        initPopup(activity, baseViewWidth, baseViewHeight);
    }

    /**
     * 初始化popupwindow
     *
     * @param activity       Activity
     * @param baseViewWidth  popupwindow宽度
     * @param baseViewHeight popupwindow高度
     */
    private void initPopup(Activity activity, int baseViewWidth, int baseViewHeight) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.setOnDismissListener(this);//添加dismiss监听
        View contentView = inflater.inflate(R.layout.widget_mc_popupwindow_lay, null);
        this.popupwindowlist = (ListView) contentView.findViewById(R.id.popupwindow_list);
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(baseViewWidth > 0 ? baseViewWidth : LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(baseViewHeight > 0 ? baseViewHeight : LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        //        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(new ColorDrawable());
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //            this.setAnimationStyle(R.style.AnimationPreview);
        //初始化列表
        mInsideListAdapter = new InsideListAdapter(activity);
        popupwindowlist.setAdapter(mInsideListAdapter);
    }


    /**
     * 设置数据
     *
     * @param popupWindowDatas popupwindow显示数据（List<ScreenPopWindow.MyPopupWindowData>）
     */
    public void setData(List<ScreenPopWindow.MyPopupWindowData> popupWindowDatas) {
        if (popupWindowDatas != null && popupWindowDatas.size() != 0) {
            mInsideListAdapter.setData(popupWindowDatas);
            mInsideListAdapter.notifyDataSetChanged();
            showPopupWindow(mBaseView);
        }
    }

    /**
     * 显示或关闭popupwindow
     *
     * @param parent 停靠的view
     */
    private void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            backgroundAlpha(0.8f);
            showAsDropDown(parent, parent.getWidth() / 2 - getWidth() / 2, 2);
        } else
            this.dismiss();

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }

    /**
     * MyPopupWindow数据实体类
     */
    public static class MyPopupWindowData implements Serializable {
        public ArrayList<String> mPopItemNames;

        public ArrayList<String> getPopItemNames() {
            return mPopItemNames;
        }

        public void setPopItemNames(ArrayList<String> mPopItemNames) {
            this.mPopItemNames = mPopItemNames;
        }
    }

    /**
     * MyPopupWindow列表数据适配器
     */
    public class InsideListAdapter extends BaseAdapter {
        private Context context;
        private int checkIndex = 1;
        private List<MyPopupWindowData> myPopupWindowDatas;

        public InsideListAdapter(Context context) {
            this.context = context;
        }

        /**
         * 更新数据
         *
         * @param myPopupWindowDatas List<MyPopupWindowData>
         */
        public void setData(List<MyPopupWindowData> myPopupWindowDatas) {
            this.myPopupWindowDatas = myPopupWindowDatas;
        }

        @Override
        public int getCount() {
            if (myPopupWindowDatas == null)
                return 0;
            return myPopupWindowDatas.size();
        }

        @Override
        public MyPopupWindowData getItem(int position) {
            return myPopupWindowDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHoler {
            SparseArray<RadioButton> sparseArray;
            LinearLayout convertLayout;
            TextView tv_item;
            RadioGroup group_item;
        }

        @SuppressWarnings("SpellCheckingInspection")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoler viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHoler();
                viewHolder.sparseArray = new SparseArray<>();
                //定义一个线性布局
                viewHolder.convertLayout = new LinearLayout(context);
                viewHolder.convertLayout.setPadding(5, 5, 5, 5);
                //TextView
                viewHolder.tv_item = new TextView(context);
                viewHolder.tv_item.setTextSize(textSize);
                viewHolder.tv_item.setPadding(10, 5, 10, 5);
                viewHolder.convertLayout.addView(viewHolder.tv_item);
                //RadioGroup
                viewHolder.group_item = new RadioGroup(context);
                viewHolder.group_item.setOrientation(LinearLayout.HORIZONTAL);
                viewHolder.convertLayout.addView(viewHolder.group_item);
                //定义一个布局参数类（用于线性布局中的参数）
                convertView = viewHolder.convertLayout;
                convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHoler) convertView.getTag();
            }
            if (getItem(position) != null && viewHolder.group_item != null) {
                ArrayList<String> mPopItemNames = getItem(position).getPopItemNames();
                viewHolder.tv_item.setText(mPopItemNames.get(0));
                if (viewHolder.group_item != null)
                    viewHolder.group_item.removeAllViews();
                //noinspection ConstantConditions 添加radiobutton
                if (mPopItemNames != null) {
                    for (int i = 1; i < getItem(position).getPopItemNames().size(); i++) {
                        viewHolder.group_item.addView(getRadioBtn(viewHolder, position, i, getItem(position).getPopItemNames().get(i)));
                    }
                }
            } else ABLogUtil.e("getItem(position) != null");
            return convertView;
        }

        /**
         * 获取一个RadioButton
         *
         * @param viewHolder ViewHoler
         * @param position   the adapter position
         * @param index      当前的序列
         * @param str        当前radio的显示内容
         * @return radioButton
         */
        private RadioButton getRadioBtn(ViewHoler viewHolder, final int position, int index, String str) {
            RadioButton radioButton = viewHolder.sparseArray.get((position + 1) * (index + 1));
            if (radioButton == null) {
                viewHolder.sparseArray.put((position + 1) * (index + 1), new RadioButton(context));
                radioButton = viewHolder.sparseArray.get((position + 1) * (index + 1));
            } else {
                return viewHolder.sparseArray.get((position + 1) * (index + 1));
            }
            radioButton.setTag(index);
            radioButton.setId((position + 1) * (index + 1));
            //以像素为单位
            radioButton.setPadding(10, 3, 10, 3);
            radioButton.setChecked(index == checkIndex);
            //本身设置的就是SP不用转化
            radioButton.setTextSize(textSize);
            radioButton.setBackgroundResource(R.drawable.mc_pop_yuanjiao_btn_bg);
            //noinspection deprecation
            radioButton.setTextColor(radioButton.isChecked() ? context.getResources().getColor(R.color.mc_white) : context.getResources().getColor(R.color.mc_black));
            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setText(str);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int tag = (int) (buttonView).getTag();
                        if (myItemClickListener != null)
                            myItemClickListener.onListItemIndexSelected(position, tag);
                    }
                    buttonView.setChecked(isChecked);
                    //noinspection deprecation
                    buttonView.setTextColor(buttonView.isChecked() ? context.getResources().getColor(R.color.mc_white) : context.getResources().getColor(R.color.mc_black));
                }
            });
            return radioButton;
        }
    }
}
