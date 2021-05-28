package com.example.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.demo.R;


/**
 * 吸顶标题
 */

public class StickyItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "DividerItemDecoration";
    private Context context;
    private int groupDividerHeight;
    private int itemDividerHeight;
    private int dividerPaddingLeft;
    private int dividerPaddingRight;
    private int textPaddingLeft;   // 文字左边的间距
    private Paint dividerPaint;
    private Paint textPaint;
    private int dividerPaintColor;
    private int textPaintColor;

    public StickyItemDecoration(Context context, OnGroupListener listener) {
        this.context = context;
        this.listener = listener;
        dividerPaintColor = R.color.colorRed;
        textPaintColor = R.color.common_text_white_gray;
        groupDividerHeight = dp2Px(30);
        itemDividerHeight = dp2Px(0);
        textPaddingLeft = dp2Px(0);
        initPaint();
    }

    public StickyItemDecoration(Context context, int dividerPaintColor, int textPaintColor, OnGroupListener listener) {
        this.context = context;
        this.listener = listener;
        this.dividerPaintColor = dividerPaintColor;
        this.textPaintColor = textPaintColor;
        groupDividerHeight = dp2Px(30);
        itemDividerHeight = dp2Px(0);
        textPaddingLeft = dp2Px(0);
        initPaint();
    }

    private void initPaint() {
        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(context.getResources().getColor(dividerPaintColor));
        dividerPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(context.getResources().getColor(textPaintColor));
        textPaint.setTextSize(sp2Px(12));

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        // 获取组名
        String groupName = getGroupName(position);
        if (TextUtils.isEmpty(groupName)) {
            return;
        }
        if (isGroupFirst(position)) {
            outRect.top = groupDividerHeight;
        } else {
            outRect.top = dp2Px(0);
        }
    }


    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        // getChildCount() 获取的是当前屏幕可见 item 数量，而不是 RecyclerView 所有的 item 数量
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            // 获取当前itemview在adapter中的索引
            int childAdapterPosition = parent.getChildAdapterPosition(childView);
            Log.e("onDraw", childAdapterPosition + "");
            String groupName = getGroupName(childAdapterPosition);
            if (TextUtils.isEmpty(groupName)) {
                continue;
            }
            /**
             * 由于分割线是绘制在每一个 itemview 的顶部，所以分割线矩形 rect.bottom = itemview.top,
             * rect.top = itemview.top - groupDividerHeight
             */
            int bottom = childView.getTop();
            int left = parent.getPaddingLeft();
            int right = parent.getPaddingRight();
            if (isGroupFirst(childAdapterPosition)) {   // 是分组第一个，则绘制分组分割线
                int top = bottom - groupDividerHeight;
                // 绘制分组分割线矩形
                canvas.drawRect(left + dividerPaddingLeft, top,
                        childView.getWidth() - right - dividerPaddingRight, bottom, dividerPaint);
                // 绘制分组分割线中的文字
                float baseLine = (top + bottom) / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
                canvas.drawText(groupName, left + textPaddingLeft,
                        baseLine, textPaint);
            } else {    // 不是分组中第一个，则绘制常规分割线
                int top = bottom - dp2Px(1);
                canvas.drawRect(left + dividerPaddingLeft, top,
                        childView.getWidth() - right - dividerPaddingRight, bottom, dividerPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        View firstVisibleView = parent.getChildAt(0);
        if (firstVisibleView == null) return;
        int firstVisiblePosition = parent.getChildAdapterPosition(firstVisibleView);
        Log.e("onDrawOver", firstVisiblePosition + "");
        String groupName = getGroupName(firstVisiblePosition);
        if (TextUtils.isEmpty(groupName)) {
            return;
        }
        int left = parent.getPaddingLeft();
        int right = firstVisibleView.getWidth() - parent.getPaddingRight();
        // 第一个itemview(firstVisibleView) 的 bottom 值小于分割线高度，分割线随着 recyclerview 滚动，
        // 分割线top固定不变，bottom=firstVisibleView.bottom
        if (firstVisibleView.getBottom() <= groupDividerHeight && isGroupFirst(firstVisiblePosition + 1)) {
            canvas.drawRect(left, 0, right, firstVisibleView.getBottom(), dividerPaint);
            float baseLine = firstVisibleView.getBottom() / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
            canvas.drawText(groupName, left + textPaddingLeft,
                    baseLine, textPaint);
        } else {
            canvas.drawRect(left, 0, right, groupDividerHeight, dividerPaint);
            float baseLine = groupDividerHeight / 2f - (textPaint.descent() + textPaint.ascent()) / 2f;
            canvas.drawText(groupName, left + textPaddingLeft, baseLine, textPaint);
        }
    }

    private OnGroupListener listener;

    public interface OnGroupListener {
        String getGroupName(int position);

        boolean isDrawSticky(int position);
    }

    public String getGroupName(int position) {
        if (listener != null) {
            return listener.getGroupName(position);
        }
        return null;
    }

    /**
     * 是否是某组中第一个item
     *
     * @param position
     * @return
     */
    private boolean isGroupFirst(int position) {
        return listener.isDrawSticky(position);
    }

    private int dp2Px(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    private int sp2Px(int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
}
