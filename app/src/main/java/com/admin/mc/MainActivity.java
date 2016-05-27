package com.admin.mc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.adutils.ABLogUtil;
import com.adutils.phone.ABDensityUtil;
import com.adutils.popup.ABToastUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.helo_word_0:
                ArrayList<String> list = new ArrayList<>();
                list.add("年级:;全部;高一;高二;高三");
                list.add("题型:;全部");
                list.add("类型:;全部;错题;收藏");
                ABMCScreenPopupWindow.getInstance(this).openPopupWindow(false, findViewById(R.id.helo_word_0), 18, list, ";", new ABMCScreenPopupWindow.MyItemClickListener() {
                    @Override
                    public void onListItemIndexSelected(int line, int index) {
                        ABLogUtil.i("line=====" + line + "\r\n" + "index=======" + index);
                        ABToastUtils.showToast(MainActivity.this, "line=====" + line + "\r\n" + "index=======" + index);
                    }
                });
                break;
            default:
                break;
        }
    }
}
