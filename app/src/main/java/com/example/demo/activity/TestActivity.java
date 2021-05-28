package com.example.demo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.demo.R;
import com.example.demo.adapter.DiffAdapter;
import com.example.demo.adapter.common.CommonAdapter;
import com.example.demo.adapter.common.base.ViewHolder;
import com.example.demo.adapter.common.callback.PageItemCallback;
import com.example.demo.bean.DiffBean;
import com.example.demo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseUiActivity {
    private EditText editText;
    private RecyclerView mRecyclerView;
    private CommonAdapter<DiffBean> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.test_activity;
    }

    @Override
    protected void initData() {
        super.initData();
        editText = findViewById(R.id.edit_text);
        mRecyclerView = findViewById(R.id.recyclerView);
        editText.setOnFocusChangeListener(inputCharacterLengthListener("三点几，饮茶先啦"));
        mAdapter = new CommonAdapter<DiffBean>(this, R.layout.h_item) {
            @Override
            protected void convert(ViewHolder holder, DiffBean diffBean, int position) {
                holder.setText(R.id.name, diffBean.getName());
                holder.setText(R.id.value, diffBean.getValue());
                holder.setText(R.id.title, diffBean.getTitle());
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemTouchCallback(new PageItemCallback(mAdapter));
        mAdapter.bindRecyclerView(mRecyclerView);
        mAdapter.enableDragItem(true);
        setData();
    }

    private void setData() {
        List<DiffBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new DiffBean(i, i + "", i + "", i + "",
                    i < 5 ? DiffAdapter.VIEW_TYPE_H : DiffAdapter.VIEW_TYPE_V));
        }
        mAdapter.setNewData(list);
    }

    /**
     * 输入字符长度监听
     * ToastUtil.toastError(R.string.new_pwd_invalid);
     *
     * @param errorTips 字符长度不符合规定时的提示语
     * @return
     */
    public View.OnFocusChangeListener inputCharacterLengthListener(final String errorTips) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    int length = ((EditText) view).getText().length();
                    if (length < 6 || length > 20) {
                        ToastUtil.toastError(errorTips);
                    }
                }
            }
        };
    }
}
