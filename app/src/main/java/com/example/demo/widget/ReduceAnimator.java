package com.example.demo.widget;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import java.util.ArrayList;
import java.util.List;

public class ReduceAnimator extends DefaultItemAnimator {

    List<RecyclerView.ViewHolder> removeHolders = new ArrayList<>();
    List<RecyclerView.ViewHolder> removeAnimators = new ArrayList<>();
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        removeHolders.add(holder);
        return true;
    }
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        return false;
    }
/*    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        holder.itemView.setTranslationY(fromY - toY);
        return true;
    }*/
    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }
    @Override
    public void runPendingAnimations() {
        if(!removeHolders.isEmpty()) {
            for(RecyclerView.ViewHolder holder : removeHolders) {
                remove(holder);
            }
            removeHolders.clear();
        }

    }
  /*  @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        removeHolders.clear();
        removeAnimators.clear();
    }
    @Override
    public void endAnimations() {
        removeHolders.clear();
        removeAnimators.clear();
    }*/

    @Override
    public boolean isRunning() {
        return !(removeHolders.isEmpty() && removeAnimators.isEmpty());
    }

    private void remove(final RecyclerView.ViewHolder holder){
        removeAnimators.add(holder);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1,0.5f,1,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                dispatchRemoveStarting(holder);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeAnimators.remove(holder);
                dispatchRemoveFinished(holder);
                if(!isRunning()){
                    dispatchAnimationsFinished();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        holder.itemView.startAnimation(scaleAnimation);
    }
}
