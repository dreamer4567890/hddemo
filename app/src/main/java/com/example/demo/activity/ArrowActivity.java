package com.example.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.widget.PopupMenu;

public class ArrowActivity extends BaseUiActivity {
    private PopupMenu mPopupMenu;

    @Override
    protected int getLayoutId(){
        return R.layout.activity_arrow;
    }

    @Override
    protected void initData() {
        // initialize popup menu (force measure to get width)
        View menuLayout = getLayoutInflater().inflate(R.layout.toolbar_menu, null);
        menuLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPopupMenu = new PopupMenu((ViewGroup)menuLayout);
        mPopupMenu.setMenuItemBackgroundColor(0xffb1df83);
        mPopupMenu.setMenuItemHoverBackgroundColor(0x22000000);
        mPopupMenu.setOnMenuItemSelectedListener(new PopupMenu.OnMenuItemSelectedListener() {
            @Override
            public void onMenuItemSelected(View menuItem) {
                Toast.makeText(ArrowActivity.this, "Menu item clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // show or dismiss popup menu when clicked
        final float offsetX = 0;
        final float offsetY = 0;
        final float menuWidth = menuLayout.getMeasuredWidth();
        final View menu = findViewById(R.id.toolbar_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupMenu.isShowing()) {
                    mPopupMenu.dismiss();
                } else {
                    // based on bottom-left, need take menu width and menu icon width into account
                    mPopupMenu.show(menu, (int) (menu.getWidth() - offsetX - menuWidth), (int) offsetY);
                }
            }
        });
    }
}
