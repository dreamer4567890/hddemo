/*
package com.example.demo.widget.draglayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.rich.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.evergrande.it.audiodevice.utils.bean.SyncPlayStatusInfo;
import cn.evergrande.it.common.ui.adapter.MultiItemTypeAdapter;
import cn.evergrande.it.common.ui.adapter.base.ViewHolder;
import cn.evergrande.it.hdhome.base.HomeConstants;
import cn.evergrande.it.hdhome.base.network.model.entity.device.CurtainAttrBean;
import cn.evergrande.it.hdhome.base.utils.DeviceCategoryType;
import cn.evergrande.it.hdhome.base.utils.DeviceCategoryTypeUtil;
import cn.evergrande.it.hdhome.base.utils.DeviceStatusUtil;
import cn.evergrande.it.hdhome.base.utils.RouteUtil;
import cn.evergrande.it.hdhome.entity.device.common.FolderBean;
import cn.evergrande.it.hdhome.entity.device.common.RoomDevice;
import cn.evergrande.it.hdhome.model.FamilyModel;
import cn.evergrande.it.hdhome.service.DeviceBiz;
import cn.evergrande.it.hdhome.service.FolderBiz;
import cn.evergrande.it.hdhome.service.event.AudioVolumeControlEvent;
import cn.evergrande.it.hdhome.service.event.DragViewEvent;
import cn.evergrande.it.hdhome.service.event.PackageChangeEvent;
import cn.evergrande.it.hdhome.temp.LoginManager;
import cn.evergrande.it.hdhome.util.EventBusUtils;
import cn.evergrande.it.hdhome.util.device.DeviceUtil;
import cn.evergrande.it.hdnetworklib.api.biz.GsonUtil;
import cn.evergrande.it.hdtoolkits.app.AppTrace;
import cn.evergrande.it.hdtoolkits.rx.RxTaskCallBack;
import cn.evergrande.it.hdtoolkits.rx.RxTaskScheduler;
import cn.evergrande.it.hdtoolkits.screen.ScreenUtils;
import cn.evergrande.it.hdtoolkits.thread.ThreadManager;
import cn.evergrande.it.hdtoolkits.toast.ToastUtil;
import cn.evergrande.it.logger.BHLog;
import cn.evergrande.it.phone.R;
import cn.evergrande.it.phone.anim.DeviceCardAnimator;
import cn.evergrande.it.phone.bus.FolderChangeEvent;
import cn.evergrande.it.phone.bus.UnLockRefreshEvent;
import cn.evergrande.it.phone.modularity.adapter.HomeAsyncAdapter;
import cn.evergrande.it.phone.modularity.adapter.control.AudioControl;
import cn.evergrande.it.phone.modularity.adapter.control.factory.CardControlFactory;
import cn.evergrande.it.phone.util.MarshallDeviceUtil;
import cn.evergrande.it.phone.view.DragLayout.helper.HomeItemBean;
import cn.evergrande.it.phone.view.aima.ActivityAnimationHelper;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static cn.evergrande.it.hdhome.base.HomeConstants.NORMAL_DEVICE;
import static cn.evergrande.it.hdhome.base.utils.DeviceCategorySubType.TYPE_NIGHT_LIGHT;
import static cn.evergrande.it.hdhome.base.utils.DeviceCategorySubType.TYPE_ROLLER_BLIND;
import static cn.evergrande.it.hdhome.base.utils.DeviceCategoryType.TYPE_AIR_CONDITIONER;
import static cn.evergrande.it.hdhome.base.utils.DeviceCategoryType.TYPE_AI_PAD;
import static cn.evergrande.it.hdhome.base.utils.DeviceCategoryType.TYPE_LIGHT;
import static cn.evergrande.it.hdhome.base.utils.DeviceCategoryType.TYPE_WINDOW_CURTAINS;
import static cn.evergrande.it.hdtoolkits.app.AppTrace.getContext;
import static cn.evergrande.it.phone.anim.DeviceCardAnimator.AnimatorType.EDIT_END;
import static cn.evergrande.it.phone.anim.DeviceCardAnimator.AnimatorType.EDIT_ING;
import static cn.evergrande.it.phone.anim.DeviceCardAnimator.AnimatorType.EDIT_INSERT;
import static cn.evergrande.it.phone.anim.DeviceCardAnimator.AnimatorType.EDIT_START;
import static cn.evergrande.it.phone.modularity.adapter.delegate.EmptyEnvironmentItemViewDelegateImpl.EMPTY_ENVIRONMENT_ID;
import static cn.evergrande.it.phone.modularity.adapter.delegate.EmptyItemViewDelegateImpl.EMPTY_DEVICE_ID;
import static cn.evergrande.it.phone.modularity.adapter.delegate.EmptySceneItemViewDelegateImpl.EMPTY_SCENE_ID;
import static cn.evergrande.it.phone.modularity.adapter.diff.DeviceItemDiffCallBack.DIFF_AUDIO;
import static cn.evergrande.it.phone.modularity.adapter.diff.DeviceItemDiffCallBack.DIFF_ROOM_INFO;
import static cn.evergrande.it.phone.modularity.adapter.diff.DeviceItemDiffCallBack.DIFF_SWITCH;
import static cn.evergrande.it.phone.modularity.fragment.home.HomeFragment.COUNT_EMPTY_HEADER;

*/
/**
 * Created by wuzhen
 * on 2020/7/7 14:18.
 * 设备拖拽、编组
 *//*

public class DeviceGroupsHelper implements NewPackageView.PackageViewBridge {
    public static final String TAG = cn.evergrande.it.phone.modularity.manager.DeviceGroupsHelper.class.getSimpleName();
    public static final int REFRESH_DELAY = 3 * 1000;
    public static final int GROUP_HINT_HIDE = -10;
    private static final long UPDATE_PACK_VIEW = 200;
    private static final long UPDATE_LAYOUT_TIME = 100;
    public static final int TYPE_HOME = 1;
    public static final int TYPE_PACKAGE = 2;
    private static final int HOME_BOTTOM_GAP = 0;
    private RecyclerView mRecyclerView;
    private HomeAsyncAdapter mAdapter;
    private NewPackageView mPackageView;
    private NewLauncherView mLauncherView;
    private DeviceCardAnimator mItemAnimator;
    private OnRecyclerItemTouchListener mOnRecyclerItemTouchListener;
    private boolean mIsPackViewShowing;
    private long mRoomId;
    private boolean mIsEditModel;
    private boolean mIsDraging;
    private Activity mActivity;
    private List<HomeItemBean> mNewDatas;
    private OnItemClickListener mOnItemClickListener;
    private HashMap<String, Subscription> mMapCurtainSub;     //延时刷新（窗帘）

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ViewHolder holder, int pos);
    }

    public DeviceGroupsHelper(Builder builder) {
        this.mActivity = builder.activity;
        this.mRecyclerView = builder.recyclerView;
        this.mAdapter = builder.adapter;
        this.mPackageView = builder.packageView;
        this.mLauncherView = builder.launcherView;
        this.mItemAnimator = builder.itemAnimator;
        this.mRoomId = builder.roomId;
        mMapCurtainSub = new HashMap<>();
        init();
    }

    public void init() {
        EventBusUtils.register(this);
        mOnRecyclerItemTouchListener = new OnRecyclerItemTouchListener(mRecyclerView) {
            @Override
            public void onShowPress(RecyclerView.ViewHolder vh, int pos) {
                doOnShowPress(pos);
            }
        };
        mRecyclerView.addOnItemTouchListener(mOnRecyclerItemTouchListener);
        if (mPackageView != null) {
            mPackageView.setLauncherView(mLauncherView);
            mPackageView.setHomeRecyclerView(mRecyclerView);
            mPackageView.setPackageViewBridge(this);
            updateDragLayout(TYPE_HOME, 0);
        }
        mAdapter.setOnItemClickListener(new OnItemClickedListener());
    }

    public void doOnShowPress(final int pos) {
        if (pos < COUNT_EMPTY_HEADER) {
            return;
        }
        int index = pos - mAdapter.getHeadersCount();
        if (index > 0 && index < mAdapter.getDatas().size() && (mAdapter.getItem(index).isBelongEmpty() ||
                mAdapter.getItem(index).isTitle())) {
            return;
        }

        int scrollState = mRecyclerView.getScrollState();
        if (scrollState != RecyclerView.SCROLL_STATE_IDLE) {
            BHLog.e(TAG, "RecyclerView   is not idle ,can not perform click event");
            return;
        }
        if (index < mAdapter.getDatas().size() && index >= 0) {
            RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(pos);
            if (viewHolder != null) {
                View view = mRecyclerView.findViewHolderForAdapterPosition(pos).itemView;
                view.clearAnimation();
                mLauncherView.setBottomOffset(ScreenUtils.dpToPx(mRoomId == HomeConstants.HOME_ROOM_ID ?
                        HOME_BOTTOM_GAP : 100));
                mLauncherView.startDrag(view, pos, false, 0, mRecyclerView);
                if (view.getVisibility() != View.VISIBLE) {
                    updateDragState(true);
                }
                updateGroupHint(index);
            } else {
                BHLog.e(TAG, "onItemLongClick viewHolder is null!!!");
            }
        }
    }

    public void hidePackageView(final boolean needUpdate) {
        if (!mIsPackViewShowing) {
            return;
        }
        mIsPackViewShowing = false;
        ValueAnimator animator = ObjectAnimator.ofFloat(1.0f, 0.1f);
        animator.setDuration(UPDATE_PACK_VIEW);
        //animator.setInterpolator(new DecelerateInterpolator());
        EventBusUtils.post(new PackageChangeEvent(false, mRoomId));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mPackageView.setAlpha(1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mPackageView.setVisibility(View.GONE);
                if (needUpdate) {
                    updateAll(TYPE_HOME, -1);
                }
                mPackageView.closeKeyBoard();
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPackageView.setAlpha(value);
                mPackageView.setScaleX(value);
                mPackageView.setScaleY(value);
            }
        });
        animator.start();
    }

    public void showPackageView(View view) {
        if (mIsPackViewShowing) {
            return;
        }
        final int[] location = new int[2];
        ActivityAnimationHelper.calculatePivotXY(view, location, mActivity);
        mIsPackViewShowing = true;
        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 1f);
        animator.setDuration(UPDATE_PACK_VIEW);
        //  animator.setInterpolator(new DecelerateInterpolator());
        mPackageView.setPivotX(location[0]);
        mPackageView.setPivotY(location[1]);
        EventBusUtils.post(new PackageChangeEvent(true, mRoomId));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mPackageView.setAlpha(0);
                mPackageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mPackageView.setAlpha(1);
                mPackageView.setScaleX(1);
                mPackageView.setScaleY(1);
                mPackageView.setVisibility(View.VISIBLE);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPackageView.setAlpha(value);
                mPackageView.setScaleX(value);
                mPackageView.setScaleY(value);
            }
        });
        animator.start();
    }

    */
/**
     * 首页拖动
     *//*

    public class LauncherDragListener implements NewLauncherView.IDragActionCallback {

        @Override
        public void onDrop(View dragView, int startPosition, int lastPosition, int currentPosition, boolean out) {
            dragView.setVisibility(View.VISIBLE);
            updateGroupHint(GROUP_HINT_HIDE);
            updateDragState(false);
            mNewDatas = mAdapter.getNewData();
            int index = currentPosition == -1 ? startPosition : currentPosition;
            if (index < mNewDatas.size()) {
                mNewDatas.get(index).setNeedHide(false);
                mAdapter.notifyItemDiffChanged(index);
            }
            if (mIsPackViewShowing) {
                mPackageView.updateDataVisible();
            }
        }

        @Override
        public boolean prepareInsert(int startPos, int targetPost) {
            int targetIndex = targetPost - mAdapter.getHeadersCount();
            int startIndex = startPos - mAdapter.getHeadersCount();
            if (targetIndex == startIndex || startIndex < 0 || targetIndex < 0 ||
                    startIndex >= mAdapter.getDatas().size() ||
                    targetIndex >= mAdapter.getDatas().size()) {
                return false;
            }
            HomeItemBean startItem = mAdapter.getItem(startIndex);
            HomeItemBean homeDataBean = mAdapter.getItem(targetIndex);
            if (!isGroupTagEquals(startItem, homeDataBean) ||
                    startItem.isScene() || homeDataBean.isScene() ||
                    startItem.isEnvironment() || homeDataBean.isEnvironment() ||
                    startItem.getFolderBean() != null) {
                return false;
            }

            return true;
        }

        @Override
        public void unPrepareInsert() {
        }

        @Override
        public boolean onSwap(View dragView, int startPosition, int lastPosition, int currentPosition, boolean out) {
            if (startPosition == currentPosition) {
                return false;
            }
            Log.i("mTest", "onSwap==startPosition=" + startPosition + " currentPosition=" + currentPosition);
            int fromIndex = startPosition - mAdapter.getHeadersCount();
            int toIndex = currentPosition - mAdapter.getHeadersCount();
            if (fromIndex < 0 || toIndex < 0 || toIndex > mAdapter.getDatas().size() - 1 || fromIndex > mAdapter.getDatas().size() - 1) {
                return false;
            }
            mNewDatas = mAdapter.getNewData();
            HomeItemBean fromDelegate = mNewDatas.get(fromIndex);
            HomeItemBean toDelegate = mNewDatas.get(toIndex);
            if (toDelegate.isBelongEmpty() || fromDelegate.isBelongEmpty() || toDelegate.isTitle()) {
                return false;
            }
            if ((fromDelegate.isScene() && !toDelegate.isScene()) || (!fromDelegate.isScene() && toDelegate.isScene()) ||
                    (fromDelegate.isEnvironment() && !toDelegate.isEnvironment()) || (!fromDelegate.isEnvironment() && toDelegate.isEnvironment())) {
                return false;
            }
            if (fromIndex < toIndex) {
                if (toIndex >= mNewDatas.size()) return false;
                for (int i = fromIndex; i < toIndex; i++) {
                    Collections.swap(mNewDatas, i, i + 1);
                }
            } else {
                if (fromIndex >= mNewDatas.size()) return false;
                for (int i = fromIndex; i > toIndex; i--) {
                    Collections.swap(mNewDatas, i, i - 1);
                }
            }
            toDelegate.setNeedHide(false);
            fromDelegate.setNeedHide(true);
            mAdapter.notifyItemMoved(startPosition, currentPosition);
            return true;
        }

        @Override
        public boolean onInsert(View dragView, int startPosition, int lastPosition, int currentPosition, boolean needOpenPack) {
            int headerCount = mAdapter.getHeadersCount();
            int fromIndex = startPosition - headerCount;
            int toIndex = currentPosition - headerCount;

            if (fromIndex < 0 || toIndex < 0) {
                return false;
            }
            if (toIndex > mAdapter.getDatas().size() - 1) {
                toIndex = mAdapter.getDatas().size() - 1;
            }
            mNewDatas = deepCopyList(mAdapter.getDatas());
            HomeItemBean toDelegate = mNewDatas.get(toIndex);
            if ((toDelegate.isBelongEmpty() || toDelegate.isTitle()) ||
                    isCurtainsLimit(toDelegate)) {
                return false;
            }

            HomeItemBean fromDelegate = mNewDatas.get(fromIndex);
            if (fromDelegate.getFolderBean() != null) {//文件夹不能合并
                return false;
            }
            fromDelegate.setNeedHide(false);
            RoomDevice fromRoomDevice = fromDelegate.getRoomDevice();
            if (fromRoomDevice == null) {
                return false;
            }
            if (toDelegate.isBelongEmpty()) {
                return false;
            }

            FolderBean folderBean;
            if (toDelegate.getFolderBean() != null) {
                //拖到文件夹里面
                folderBean = toDelegate.getFolderBean();
                List<RoomDevice> roomDeviceList = folderBean.getRoomDeviceList();
                roomDeviceList.add(fromRoomDevice);
                toDelegate.setMarshalling(false);
                mNewDatas.set(toIndex, toDelegate);
                mNewDatas.remove(fromIndex);
            } else {
                //拖到设备里面，需要创建文件夹
                mItemAnimator.setAnimatorType(EDIT_INSERT);
                RoomDevice toRoomDevice = toDelegate.getRoomDevice();
                List<RoomDevice> roomDeviceList = new ArrayList<>();
                roomDeviceList.add(toRoomDevice);
                roomDeviceList.add(fromRoomDevice);
                folderBean = createFolder(mRoomId, roomDeviceList);
                folderBean.setFolderName(DeviceCategoryTypeUtil.getCategoryByCid(fromRoomDevice.getDevice_category_id()));
                HomeItemBean itemBean = new HomeItemBean();
                itemBean.setEditModel(mIsEditModel);
                itemBean.setFolderBean(folderBean);
                mNewDatas.remove(fromIndex);
                int index = fromIndex < toIndex ? toIndex - 1 : toIndex;
                itemBean.setOrder(index + 1);
                mNewDatas.set(index, itemBean);
            }
            mAdapter.submitList(mNewDatas);
            insertOrReplaceFolder(folderBean);
            updateGroupHint(GROUP_HINT_HIDE);
            ThreadManager.getInstance().postDelayedUITask(new Runnable() {
                @Override
                public void run() {
                    mItemAnimator.setAnimatorType(EDIT_ING);
                }
            }, 200);
            if (!needOpenPack) {
                updateDragState(false);
                return true;
            }
            mPackageView.initData(folderBean, true, mRoomId);
            Utils.vibrate(mActivity);
            //1,显示package view
            showPackageView(mRecyclerView.getLayoutManager().findViewByPosition(currentPosition));
            //3,更新recyclerview
            ThreadManager.getInstance().postDelayedUITask(new Runnable() {
                @Override
                public void run() {
                    updateDragLayout(TYPE_PACKAGE, mPackageView.getPackRoomDevices().size() - 1 + mPackageView.getHeaderViewCount());
                }
            }, UPDATE_LAYOUT_TIME);
            return true;
        }

        @Override
        public void unPack(View dragView, int startPosition, boolean hidePack) {

        }
    }

    */
/**
     * 更新同类型item编组title
     *
     * @param index 当前拖拽的索引 GROUP_HINT_HIDE隐藏所有Hint
     *//*

    private void updateGroupHint(int index) {
        if (!mIsEditModel) return;
        if (index != GROUP_HINT_HIDE) {
            HomeItemBean dragBean = mAdapter.getItem(index);
            if (dragBean.isFolder() || dragBean.isScene()) return;
        }
        List<HomeItemBean> newList = mAdapter.getNewData();
        int size = newList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                HomeItemBean itemBean = newList.get(i);
                if (index == GROUP_HINT_HIDE) {
                    itemBean.setNeedHide(false);
                    itemBean.setMarshalling(false);
                    ConstraintLayout layoutGroupHint = (ConstraintLayout) mAdapter.getViewByPosition(i, R.id.layout_group_hint);
                    if (layoutGroupHint != null) {
                        layoutGroupHint.setVisibility(View.GONE);
                    }
                } else {
                    if (itemBean.isCommon() || itemBean.isFolder()) {
                        HomeItemBean curItemBean = newList.get(index);
                        if (isGroupTagEquals(curItemBean, itemBean) && index != i) {
                            itemBean.setMarshalling(mIsDraging);
                            ConstraintLayout layoutGroupHint = (ConstraintLayout) mAdapter.getViewByPosition(i, R.id.layout_group_hint);
                            if (layoutGroupHint != null) {
                                layoutGroupHint.setVisibility(mIsDraging ? View.VISIBLE : View.GONE);
                            }
                        }
                        itemBean.setNeedHide(index == i);
                    }
                }
            }
        }
    }

    */
/**
     * 更新音乐卡片信息
     *//*

    public void updateAudioItem(long deviceId) {
        mNewDatas = mAdapter.getNewData();
        int size = mNewDatas.size();
        for (int i = 0; i < size; i++) {
            HomeItemBean itemBean = mNewDatas.get(i);
            RoomDevice roomDevice = itemBean.getRoomDevice();
            if (roomDevice != null && roomDevice.getDevice_id() == deviceId - 1) {
                SyncPlayStatusInfo info = new AudioControl().asyncPlayStatus(itemBean.getRoomDevice());
                itemBean.setMusicName(info.getMusicName());
                itemBean.setPlaying(info.isPlay());
                if (!isVolumeLock(itemBean.getControlVolumeTime())) {
                    BHLog.d("VolumeLock", "" + itemBean.getControlVolumeTime());
                    itemBean.setVolume(info.getVolume());
                }
                mAdapter.notifyItemDiffChanged(i, DIFF_AUDIO);
                break;
            }
        }
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private boolean isVolumeLock(long lockTime) {
        return now() - lockTime < 3000L;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 1)
    public void onVolumeLock(AudioVolumeControlEvent event) {
        int size = mNewDatas.size();
        HomeItemBean itemBean;
        for (int i = 0; i < size; i++) {
            itemBean = mNewDatas.get(i);
            RoomDevice roomDevice = itemBean.getRoomDevice();
            if (roomDevice != null && TextUtils.equals(roomDevice.getDevice_uuid(), event.getDeviceUuid())) {
                itemBean.setControlVolumeTime(event.getLockTime());
                break;
            }
        }
    }

    */
/**
     * 更新解锁时的卡片：窗帘、热水器
     *//*

    public void updateUnLockItem(String key) {
        mNewDatas = mAdapter.getNewData();
        int size = mNewDatas.size();
        if (isPackViewShowing()) {
            mPackageView.notifyItem(key);
        }
        for (int i = 0; i < size; i++) {
            HomeItemBean itemBean = mNewDatas.get(i);
            RoomDevice roomDevice = itemBean.getRoomDevice();
            if (itemBean.isFolder() && TextUtils.equals(itemBean.getFolderBean().getFolderIdStr(), key)) {
                mAdapter.notifyItemDiffChanged(i);
                break;
            }
            if (roomDevice != null && TextUtils.equals(roomDevice.getDevice_uuid(), key)) {
                mAdapter.notifyItemDiffChanged(i);
                break;
            }
        }
    }

    */
/**
     * 更新摄像头卡片信息
     *//*

    public void updateCameraItem(String uuid) {
        mNewDatas = mAdapter.getNewData();
        int size = mNewDatas.size();
        for (int i = 0; i < size; i++) {
            HomeItemBean itemBean = mNewDatas.get(i);
            RoomDevice roomDevice = itemBean.getRoomDevice();
            if (roomDevice != null && TextUtils.equals(roomDevice.getDevice_uuid(), uuid)) {
                mAdapter.notifyItemDiffChanged(i);
                break;
            }
        }
    }

    */
/**
     * 更新授权状态
     *//*

    public void updateAudioItem(long deviceId, boolean authorize, final long audio_user_id) {
        for (HomeItemBean itemBean : mAdapter.getDatas()) {
            RoomDevice roomDevice = itemBean.getRoomDevice();
            if (roomDevice != null && roomDevice.getDevice_id() == deviceId - 1) {
                if (authorize) {
                    itemBean.getRoomDevice().setAudio_user_id(audio_user_id);
                } else {
                    itemBean.getRoomDevice().setAudio_user_id(0);
                }
                break;
            }
        }
    }

    */
/**
     * 设备移动了房间
     *
     * @param uuids
     * @param toRoomId
     *//*

    public void moveRoomDevice(List<String> uuids, long toRoomId) {
        mNewDatas = mAdapter.getNewData();
        int size = mNewDatas.size();
        for (int i = 0; i < size; i++) {
            HomeItemBean itemBean = mNewDatas.get(i);
            FolderBean folderBean = itemBean.getFolderBean();
            if (folderBean != null) {
                List<RoomDevice> roomDeviceList = folderBean.getRoomDeviceList();
                int folderDevSize = roomDeviceList.size();
                for (int j = 0; j < folderDevSize; j++) {
                    RoomDevice roomDevice = roomDeviceList.get(j);
                    if (roomDevice != null && uuids.contains(roomDevice.getDevice_uuid())) {
                        roomDevice.setRoom_id(toRoomId);
                        return;
                    }
                }
            }

            RoomDevice roomDevice = itemBean.getRoomDevice();
            if (roomDevice != null && uuids.contains(roomDevice.getDevice_uuid())) {
                roomDevice.setRoom_id(toRoomId);
                mAdapter.notifyItemDiffChanged(i, DIFF_ROOM_INFO);
                return;
            }
        }
    }

    */
/**
     * 更新设备信息
     *//*

    public void updateRoomDevice(RoomDevice newDeviceBean) {
        mNewDatas = mAdapter.getNewData();
        int size = mNewDatas.size();
        for (int i = 0; i < size; i++) {
            HomeItemBean itemBean = mNewDatas.get(i);
            RoomDevice oldDeviceBean = itemBean.getRoomDevice();
            FolderBean folderBean = itemBean.getFolderBean();
            if (oldDeviceBean != null && newDeviceBean.getDevice_uuid().equals(oldDeviceBean.getDevice_uuid())) {
                newDeviceBean.setRoom_name(oldDeviceBean.getRoom_name());
                itemBean.setRoomDevice(newDeviceBean);
                mNewDatas.set(i, itemBean);
                if (newDeviceBean.getDevice_category_id() == TYPE_WINDOW_CURTAINS) {
                    CurtainAttrBean attrBean = newDeviceBean.getAttribute(CurtainAttrBean.class);
                    if (attrBean != null) {
                        String key = newDeviceBean.getDevice_uuid();
                        CardControlFactory
                                .getInstance()
                                .putCurtainState(key, attrBean.getOpen_percentage());
                        if (!attrBean.isPercentageLimit()) {
                            delayRefresh(new UnLockRefreshEvent(key));
                        } else {
                            cancelDelayRefresh(key);
                        }
                    }
                }

                //优先判断在、离线
                if (DeviceUtil.deviceOffline(oldDeviceBean) != DeviceUtil.deviceOffline(newDeviceBean)) {
                    mAdapter.notifyItemDiffChanged(i);
                    break;
                }
                if (!CardControlFactory.getInstance().isLock(newDeviceBean, mRoomId, false)) {
                    if (DeviceUtil.isOpen(oldDeviceBean) != DeviceUtil.isOpen(newDeviceBean)) {
                        mAdapter.notifyItemDiffChanged(i, DIFF_SWITCH);
                    } else {
                        //默认刷全部
                        mAdapter.notifyItemDiffChanged(i);
                    }
                }
                break;
            } else if (folderBean != null) {
                List<RoomDevice> folderRoomDeviceList = folderBean.getRoomDeviceList();
                if (folderRoomDeviceList != null && folderRoomDeviceList.size() > 0) {
                    for (int j = 0; j < folderRoomDeviceList.size(); j++) {
                        RoomDevice oldFolderDeviceBean = folderRoomDeviceList.get(j);
                        if (newDeviceBean.getDevice_uuid().equals(oldFolderDeviceBean.getDevice_uuid())) {
                            newDeviceBean.setRoom_name(oldFolderDeviceBean.getRoom_name());
                            folderRoomDeviceList.set(j, newDeviceBean);
                            new FolderBiz().insertOrReplace(folderBean);
                            updatePackageView(j, folderBean);
                            mNewDatas.set(i, itemBean);

                            //虚拟灯的display变了
                            if (newDeviceBean.getVirtual_device_type() != NORMAL_DEVICE &&
                                    newDeviceBean.getDevice_category_id() == DeviceCategoryType.TYPE_LIGHT &&
                                    oldFolderDeviceBean.isShowCategorySub() != newDeviceBean.isShowCategorySub()) {
                                if (mIsPackViewShowing && mRoomId == HomeConstants.HOME_ROOM_ID) {
                                    //主页的文件夹中虚拟灯卡片隐藏
                                    if (folderBean.getRoomDeviceList().size() > 0) {
                                        mPackageView.setNewData(folderBean);
                                    } else {
                                        hidePackageView();
                                    }
                                }
                                break;
                            }

                            if (newDeviceBean.getDevice_category_id() == TYPE_WINDOW_CURTAINS) {
                                CurtainAttrBean attrBean = newDeviceBean.getAttribute(CurtainAttrBean.class);
                                if (attrBean != null) {
                                    String key = String.valueOf(folderBean.getFolderId());
                                    CardControlFactory
                                            .getInstance()
                                            .putCurtainState(key, attrBean.getOpen_percentage());
                                    if (!attrBean.isPercentageLimit()) {
                                        delayRefresh(new UnLockRefreshEvent(key));
                                    } else {
                                        cancelDelayRefresh(key);
                                    }
                                }
                            }

                            if (!CardControlFactory.getInstance().isLock(folderBean.getFolderId(), mRoomId, false)) {
                                mAdapter.notifyItemDiffChanged(i);
                            }
                            break;
                        }
                    }
                }
            }
        }

//        CardControlFactory
//                .getInstance()
//                .cancelLock(roomDevice.getDevice_category_id());
    }

    public class OnItemClickedListener extends MultiItemTypeAdapter.OnItemClickListener {

        @Override
        public void onItemClick(View view, ViewHolder holder, int pos) {
            if (pos >= 0 && pos < mAdapter.getDatas().size()) {
                //有需要可以实现OnItemClickListener
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, (ViewHolder) holder, pos);
                }

                HomeItemBean homeItemBean = mAdapter.getItem(pos);
                if (homeItemBean.isTitle() || homeItemBean.isScene()) {
                    return;
                }
                if (homeItemBean.isSceneEmpty()) {
                    RouteUtil.viewHomeAddScene(mActivity, HomeConstants.HOME_ADD_SCENE);
                    return;
                }
                if (homeItemBean.isEnvironmentEmpty()) {
                    RouteUtil.viewHomeAddScene(mActivity, HomeConstants.HOME_ADD_ENVIRONMENT);
                    return;
                }
                if (homeItemBean.isEmpty()) {
                    RouteUtil.viewAddRoomDevice(mActivity, mRoomId);
                }
            }
        }
    }

    public void gotoDetail(RecyclerView.ViewHolder holder, HomeItemBean homeItemBean) {
        FolderBean folderBean = homeItemBean.getFolderBean();
        final RoomDevice roomDevice = homeItemBean.getRoomDevice();
        if (folderBean != null) {
            updateDragLayout(TYPE_PACKAGE, 0);
            mPackageView.initData(folderBean, mIsEditModel, mRoomId);
            showPackageView(((ViewHolder) holder).getConvertView());
        } else if (roomDevice != null && !mIsEditModel && !DeviceStatusUtil.
                isNoDetail(roomDevice)) {
            if (roomDevice.getDevice_category_id() == TYPE_AI_PAD) {
                DeviceBiz deviceBiz = new DeviceBiz();
                RoomDevice deviceByUUID = deviceBiz.getDeviceByUUID(roomDevice.getDevice_uuid());
                //if (deviceByUUID != null && deviceByUUID.getAudio_user_id() == 0) {
                //   RouteUtil.viewAudioDialogAuthorize(getContext(), roomDevice);
                //  } else

                //    {
                gotoRoomDeviceActivity(roomDevice);
                //}
            } else {
                gotoRoomDeviceActivity(roomDevice);
            }
            MarshallDeviceUtil.removeNewMark(roomDevice);
        }
    }

    @Override
    public void updateDragLayout(int type, int startPos) {
        mLauncherView.updateType(type);
        switch (type) {
            case TYPE_HOME:
                //从文件夹中拖出索引得加上列表的头部
                startPos += COUNT_EMPTY_HEADER;
                mLauncherView.setDragPosition(new LauncherDragListener());
                mLauncherView.setBottomOffset(ScreenUtils.dpToPx(HOME_BOTTOM_GAP));
                mLauncherView.updateRecyclerView(mRecyclerView, startPos, FROMTYPE.FROM_PACKAGE);
                break;
            case TYPE_PACKAGE:
                mLauncherView.setDragPosition(mPackageView.getPackageDragListener());
                mLauncherView.setBottomOffset(ScreenUtils.dpToPx(100));
                mLauncherView.updateRecyclerView(mPackageView.getPackageRv(), startPos, FROMTYPE.FROM_HOME);
                break;
        }
    }

    @Override
    public List<HomeItemBean> currentHomeItemList() {
        return mAdapter.getDatas();
    }

    @Override
    public void updateAll(final int type, final int index) {
        if (type == TYPE_HOME) {
            FolderBean folderBean = mPackageView.getFolderBean();
            for (HomeItemBean itemBean : mAdapter.getDatas()) {
                if (itemBean.isFolder() && itemBean.getFolderBean().getFolderId() == folderBean.getFolderId()) {
                    itemBean.setFolderBean(folderBean);
                    break;
                }
            }
        }

        ThreadManager.getInstance().postDelayedUITask(new Runnable() {
            @Override
            public void run() {
                updateDragLayout(type, index);
            }
        }, UPDATE_LAYOUT_TIME);
    }


    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void hidePackageView() {
        hidePackageView(false);
    }

    @Override
    public void updateDragState(boolean isDraging) {
        this.mIsDraging = isDraging;
//        if (!isDraging) {
//            for (HomeItemBean homeDataBean : mAdapter.getDatas()) {
//                homeDataBean.setNeedHide(false);
//            }
//            mAdapter.notifyDataSetChanged();
//        }
        EventBusUtils.post(new DragViewEvent(mRoomId, isDraging));
    }

    @Override
    public void setEdit(boolean isEdit) {
        mIsEditModel = isEdit;
        mOnRecyclerItemTouchListener.setEditModel(mIsEditModel);
    }

    @Override
    public boolean isDragging() {
        return mIsDraging;
    }

    public FolderBean createFolder(long roomId, List<RoomDevice> folderDeviceList) {
        FolderBean folderBean = new FolderBean();
        folderBean.setFolderId(System.currentTimeMillis());
        folderBean.setFamilyId(FamilyModel.getInstance().getCurrentFamily().getFamily_id());
        folderBean.setRoomDeviceList(folderDeviceList);
        folderBean.setRoomId(roomId);
        return folderBean;
    }

    public void insertOrReplaceFolder(FolderBean folderBean) {
        new FolderBiz().insertOrReplace(folderBean);
    }

    public void notifyEditModelChanged(boolean isEditModel) {
        this.mIsEditModel = isEditModel;
        mItemAnimator.setAnimatorType(isEditModel ? EDIT_START : EDIT_END);
        int environmentCount = 0;     //环境item总数
        int sceneCount = 0;     //场景item总数
        int devCount = 0;       //设备item总数
        boolean isHaveSceneTitle = false;  //场景title
        boolean isHaveDevTitle = false;  //常用设备title
        mNewDatas = deepCopyList(mAdapter.getDatas());
        int size = mNewDatas.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                HomeItemBean homeItemBean = mNewDatas.get(i);
                homeItemBean.setEditModel(isEditModel);
//                homeItemBean.setRefresh(!homeItemBean.isRefresh());
                homeItemBean.setMarshalling(false);
                if (!isEditModel) {
                    homeItemBean.setNeedHide(false);
                }
                if (mRoomId == HomeConstants.HOME_ROOM_ID) {
                    if (homeItemBean.isEnvironment()) {
                        environmentCount += 1;
                    } else if (homeItemBean.isScene()) {
                        sceneCount += 1;
                    } else if (homeItemBean.isCommon()) {
                        devCount += 1;
                    }
                    if (homeItemBean.isTitle()) {
                        if (TextUtils.equals(homeItemBean.getTitle(), getContext().getString(
                                R.string.smart_scene))) {
                            isHaveSceneTitle = true;
                        } else if (TextUtils.equals(homeItemBean.getTitle(), getContext().getString(
                                R.string.common_dev))) {
                            isHaveDevTitle = true;
                        }
                    }
                }
            }

            if (mRoomId == HomeConstants.HOME_ROOM_ID) {
                //home tab
                if (isEditModel) {
                    //add environment empty
                    int environmentIndex = environmentCount == 0 ? COUNT_EMPTY_HEADER : environmentCount + COUNT_EMPTY_HEADER;
                    HomeItemBean emptyEnvironmentItem = getEmptyHomeItem();
                    if (!mNewDatas.contains(emptyEnvironmentItem)) {
                        mNewDatas.add(environmentIndex, emptyEnvironmentItem);
                    }
                    if (!isHaveSceneTitle) {
                        //add scene title
                        mNewDatas.add(environmentIndex + 1, getTitleHomeItem(true));
                    }
                    //add scene empty
                    int sceneIndex = sceneCount == 0 ? environmentIndex : sceneCount + environmentIndex;
                    //+2 = 1个空环境item + 1个场景title
                    sceneIndex += 2;
                    HomeItemBean emptySceneItem = getEmptyHomeItem(true);
                    if (!mNewDatas.contains(emptySceneItem)) {
                        mNewDatas.add(sceneIndex, emptySceneItem);
                    }
                    if (!isHaveDevTitle) {
                        //add common title
                        mNewDatas.add(sceneIndex + 1, getTitleHomeItem(false));
                    }
                    //add common empty
                    HomeItemBean emptyHomeItem = getEmptyHomeItem(false);
                    if (!mNewDatas.contains(emptyHomeItem)) {
                        mNewDatas.add(emptyHomeItem);
                    }

                } else {
                    //remove environment
                    int emptyEnvironmentPos = mNewDatas.indexOf(new HomeItemBean(EMPTY_ENVIRONMENT_ID));
                    if (emptyEnvironmentPos != -1) {
                        mNewDatas.remove(emptyEnvironmentPos);
                        if (environmentCount == 0) {
                            //移除场景title
                            mNewDatas.remove(emptyEnvironmentPos);
                        }
                    }
                    //remove scene
                    int emptyScenePos = mNewDatas.indexOf(new HomeItemBean(EMPTY_SCENE_ID));
                    if (emptyScenePos != -1) {
                        mNewDatas.remove(emptyScenePos);
                        if (sceneCount == 0) {
                            //移除常用设备title
                            mNewDatas.remove(emptyScenePos);
                        }
                    }
                    //remove device
                    int emptyDevicePos = mNewDatas.indexOf(new HomeItemBean(EMPTY_DEVICE_ID));
                    if (emptyDevicePos >= 0) {
                        mNewDatas.remove(emptyDevicePos);
                        if (devCount == 0) {
                            mNewDatas.remove(emptyDevicePos - 1);
                        }
                    }
                }

            } else {
                int emptyDevicePos = mNewDatas.indexOf(new HomeItemBean(EMPTY_DEVICE_ID));
                if (isEditModel) {
                    HomeItemBean emptyHomeItem = getEmptyHomeItem(false);
                    if (emptyDevicePos == -1 && !mNewDatas.contains(emptyHomeItem)) {
                        //添加房间设备页面空布局
                        mNewDatas.add(emptyHomeItem);
                    }
                } else {
                    if (emptyDevicePos >= 0) {
                        mNewDatas.remove(emptyDevicePos);
                    }
                }
            }
        } else {
            if (isEditModel) {
                if (mRoomId == HomeConstants.HOME_ROOM_ID) {
                    //没数据时
                    List<HomeItemBean> list = new ArrayList<>();
                    list.add(getEmptyHomeItem(true));
                    list.add(getTitleHomeItem(false));
                    list.add(getEmptyHomeItem(false));
                    mNewDatas.clear();
                    mNewDatas.addAll(list);
                } else {
                    //添加房间设备页面空布局
                    HomeItemBean emptyHomeItem = getEmptyHomeItem(false);
                    if (!mNewDatas.contains(emptyHomeItem)) {
                        mNewDatas.add(emptyHomeItem);
                    }
                }
            }
        }
        mAdapter.submitList(mNewDatas);
        mOnRecyclerItemTouchListener.setEditModel(isEditModel);
        if (isEditModel) {
            ThreadManager.getInstance().postDelayedUITask(new Runnable() {
                @Override
                public void run() {
                    mItemAnimator.setAnimatorType(EDIT_ING);
                }
            }, 200);
        }
    }

    */
/**
     * 异步刷新时，实时数据
     *
     * @return
     *//*

    public List<HomeItemBean> getNewDatas() {
        return mNewDatas;
    }

    public void updatePackageView(int index, FolderBean folderBean) {
        if (mIsPackViewShowing) {
            FolderBean old = mPackageView.getFolderBean();
            if (old != null && old.equals(folderBean)) {
                mPackageView.updateUI(index, folderBean);
            }
        }
    }

    private void gotoRoomDeviceActivity(RoomDevice roomDevice) {
        if (!LoginManager.getInstance().isCloudLogin()) {
            ToastUtil.toastError(R.string.phone_network_no_use);
            return;
        }
        RouteUtil.goToDeviceActivity(mActivity, roomDevice.getDevice_uuid(), roomDevice.getRoom_id());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void onFolderChanged(FolderChangeEvent event) {
        if (event.getRoomId() != this.mRoomId) {
            return;
        }
        switch (event.getType()) {
            case FolderChangeEvent.DEVICE_REMOVE:
                updateFolder(event);
                break;
            case FolderChangeEvent.FOLDER_DELETE:
                removeFolder(event);
                break;
            case FolderChangeEvent.NAME_CHANGE:
                changeFolder(event.getFolderBean());
                break;
            case FolderChangeEvent.FOLDER_DEVICE_ORDER_CHANGED:
                changeFolderInnerOrder(event);
                break;
        }
        updateDragState(false);
    }

    private void addNewItem(int index, RoomDevice roomDevice, boolean needHide) {
        HomeItemBean homeItemBean = new HomeItemBean();
        homeItemBean.setEditModel(isEditModel());
        homeItemBean.setRoomDevice(roomDevice);
        homeItemBean.setNeedHide(needHide);
        mNewDatas.add(index + 1, homeItemBean);
    }

    private void updateFolder(final FolderChangeEvent event) {
        RxTaskScheduler.postLogicMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                mAdapter.submitList(mNewDatas);
            }

            @Override
            public Boolean doWork() throws Exception {
                FolderBean folderBean = event.getFolderBean();
                int index = -1;
                mNewDatas = deepCopyList(mAdapter.getDatas());
                int size = mNewDatas.size();
                for (int i = size - 1; i >= 0; i--) {
                    HomeItemBean homeItemBean = mNewDatas.get(i);
                    if (homeItemBean.isFolder() && folderBean.getFolderId() ==
                            homeItemBean.getFolderBean().getFolderId()) {
                        homeItemBean.setNeedHide(false);
                        homeItemBean.setFolderBean(folderBean);
                        homeItemBean.setRefresh(!homeItemBean.isRefresh());
                        index = i;
                        break;
                    }
                }
                addNewItem(index, event.getRoomDevice(), event.isNeedHideNext());
                return true;
            }
        });
    }

    private void changeFolder(final FolderBean folderBean) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
//                mAdapter.submitList(mNewDatas);
            }

            @Override
            public Boolean doWork() {
                mNewDatas = mAdapter.getNewData();
                int size = mNewDatas.size();
                for (int i = size - 1; i >= 0; i--) {
                    HomeItemBean homeItemBean = mNewDatas.get(i);
                    if (homeItemBean.isFolder() && folderBean.getFolderId() ==
                            homeItemBean.getFolderBean().getFolderId()) {
                        homeItemBean.getFolderBean().setFolderName(folderBean.getFolderName());
                        mAdapter.notifyItemDiffChanged(i);
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void changeFolderInnerOrder(FolderChangeEvent event) {
        FolderBean folderBean = event.getFolderBean();
        int size = mNewDatas.size();
        for (int i = size - 1; i >= 0; i--) {
            HomeItemBean homeItemBean = mNewDatas.get(i);
            if (homeItemBean.isFolder() && folderBean.getFolderId() ==
                    homeItemBean.getFolderBean().getFolderId()) {
                homeItemBean.setFolderBean(folderBean);
                mAdapter.notifyItemDiffChanged(i);
                break;
            }
        }
    }

    private void removeFolder(final FolderChangeEvent event) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                mAdapter.submitList(mNewDatas);
            }

            @Override
            public Boolean doWork() {
                int index = -1;
                FolderBean folderBean = event.getFolderBean();
                mNewDatas = deepCopyList(mAdapter.getDatas());
                int size = mNewDatas.size();
                for (int i = size - 1; i >= 0; i--) {
                    HomeItemBean homeItemBean = mNewDatas.get(i);
                    if (homeItemBean.isFolder() && folderBean.getFolderId() ==
                            homeItemBean.getFolderBean().getFolderId()) {
                        index = i;
                        // homeItemBeanList.remove(homeItemBean);
                        break;
                    }
                }
                if (index != -1) {
                    List<RoomDevice> roomDevices = folderBean.getRoomDeviceList();
                    for (int j = 0; j < roomDevices.size(); j++) {
                        RoomDevice roomDevice = roomDevices.get(j);
                        HomeItemBean homeItemBean = new HomeItemBean();
                        homeItemBean.setRoomDevice(roomDevice);
                        homeItemBean.setEditModel(isEditModel());
                        if (j == 0) {
                            homeItemBean.setNeedHide(false);
                            homeItemBean.setRefresh(!homeItemBean.isRefresh());
                            mNewDatas.set(index, homeItemBean);
                        } else {
                            homeItemBean.setNeedHide(event.isNeedHideNext());
                            mNewDatas.add(index + j, homeItemBean);
                        }
                    }
                }
                return true;
            }
        });
    }

    public boolean isEditModel() {
        return mIsEditModel;
    }

    public boolean isPackViewShowing() {
        return mIsPackViewShowing;
    }

    */
/**
     * 创建一个添加设备的item 场景/设备
     *//*

    public HomeItemBean getEmptyHomeItem(boolean isSceneEmpty) {
        HomeItemBean emptyBean = new HomeItemBean();
        if (isSceneEmpty) {
            emptyBean.setSceneEmpty(true);
        } else {
            RoomDevice roomDevice = new RoomDevice();
            roomDevice.setRoom_id(mRoomId);
            emptyBean.setRoomDevice(roomDevice);
            emptyBean.setEmpty(true);
        }
        return emptyBean;
    }

    */
/**
     * 创建空环境item
     *
     * @return
     *//*

    public HomeItemBean getEmptyHomeItem() {
        HomeItemBean emptyBean = new HomeItemBean();
        emptyBean.setEnvironmentEmpty(true);
        return emptyBean;
    }

    public HomeItemBean getTitleHomeItem(boolean isSceneTitle) {
        HomeItemBean titleBean = new HomeItemBean();
        titleBean.setTitle(AppTrace.getContext().getString(isSceneTitle ? R.string.smart_scene :
                R.string.common_dev));
        return titleBean;
    }

    */
/**
     * 同种类型才能编组
     *//*

    private boolean isGroupTagEquals(HomeItemBean fromBean, HomeItemBean toBean) {
        int fromCid = fromBean.getCategoryId();
        int toCid = toBean.getCategoryId();
        if (!isGroupIds(fromCid) || !isGroupIds(toCid)) {
            return false;
        }
        return fromCid == toCid &&
                fromBean.getGroupTagId() == toBean.getGroupTagId() &&
                !isCurtainsLimit(toBean) &&
                isFilterSub(fromBean, toBean, TYPE_WINDOW_CURTAINS, TYPE_ROLLER_BLIND) &&
                isFilterSub(fromBean, toBean, TYPE_LIGHT, TYPE_NIGHT_LIGHT);
    }

    */
/**
     * 只有空调、灯、窗帘才能编组
     *//*

    private boolean isGroupIds(int cId) {
        return cId == TYPE_WINDOW_CURTAINS || cId == TYPE_LIGHT || cId == TYPE_AIR_CONDITIONER;
    }

    */
/**
     * 窗帘编组只能有两个设备
     *//*

    private boolean isCurtainsLimit(HomeItemBean itemBean) {
        return itemBean.isFolder() && itemBean.getDevice_category_id() == TYPE_WINDOW_CURTAINS &&
                itemBean.getFolderBean().getRoomDeviceList().size() >= 2;
    }

    */
/**
     * 卷帘只能跟卷帘编组，其它窗帘为一类
     * 夜灯只能跟夜灯编组、其它灯类是另一种
     *
     * @param fromBean  当前拖拽对象
     * @param toBean    需要编组的对象
     * @param filterCid 要过滤的cid
     * @param filterSub 要过滤的sub
     * @return
     *//*

    private boolean isFilterSub(HomeItemBean fromBean, HomeItemBean toBean, int filterCid,
                                int filterSub) {
        if (fromBean.getCategoryId() != filterCid) {
            return true;
        }
        int fromSub;
        if (fromBean.isFolder()) {
            fromSub = fromBean.getFolderBean().getRoomDeviceList().get(0).getCategory_sub();
        } else {
            fromSub = fromBean.getRoomDevice().getCategory_sub();
        }
        int toSub;
        if (toBean.isFolder()) {
            toSub = toBean.getFolderBean().getRoomDeviceList().get(0).getCategory_sub();
        } else {
            toSub = toBean.getRoomDevice().getCategory_sub();
        }
        return (fromSub == filterSub && toSub == filterSub) ||
                (fromSub != filterSub && toSub != filterSub);
    }

    */
/**
     * 深度复制list
     *//*

    public List<HomeItemBean> deepCopyList(List<HomeItemBean> oldList) {
        String json = new Gson().toJson(oldList);
        TypeToken<List<HomeItemBean>> typeToken = new TypeToken<List<HomeItemBean>>() {
        };
        return GsonUtil.jsonToListObject(json, typeToken);
    }

    @SuppressLint("CheckResult")
    public void delayRefresh(UnLockRefreshEvent refreshEvent) {
        final String key = refreshEvent.getKey();
        cancelDelayRefresh(key);
        Flowable.just(refreshEvent)
                .delay(REFRESH_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subs) {
                        mMapCurtainSub.put(key, subs);
                    }
                }).subscribe(new Consumer<UnLockRefreshEvent>() {
            @Override
            public void accept(UnLockRefreshEvent event) {
                CardControlFactory
                        .getInstance()
                        .putCurtainPauseState(event.getKey(), true);
                EventBusUtils.post(event);
                cancelDelayRefresh(event.getKey());
            }
        });
    }

    public void cancelDelayRefresh(String key) {
        if (mMapCurtainSub.containsKey(key)) {
            Subscription sub = mMapCurtainSub.get(key);
            if (sub != null) {
                sub.cancel();
            }
            mMapCurtainSub.remove(key);
        }
    }

    public static class Builder {
        public Activity activity;
        public RecyclerView recyclerView;
        public HomeAsyncAdapter adapter;
        public NewPackageView packageView;
        public NewLauncherView launcherView;
        public DeviceCardAnimator itemAnimator;
        public long roomId;

        public Builder activity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder adapter(HomeAsyncAdapter adapter) {
            this.adapter = adapter;
            this.recyclerView = adapter.getRecyclerView();
            return this;
        }

        public Builder packageView(NewPackageView packageView) {
            this.packageView = packageView;
            return this;
        }

        public Builder launcherView(NewLauncherView launcherView) {
            this.launcherView = launcherView;
            return this;
        }

        public Builder itemAnimator(DeviceCardAnimator itemAnimator) {
            this.itemAnimator = itemAnimator;
            return this;
        }

        public Builder roomId(long roomId) {
            this.roomId = roomId;
            return this;
        }

        public cn.evergrande.it.phone.modularity.manager.DeviceGroupsHelper create() {
            return new cn.evergrande.it.phone.modularity.manager.DeviceGroupsHelper(this);
        }
    }

    //释放
    public void release() {
        EventBusUtils.unRegister(this);
        mMapCurtainSub.clear();
    }
}
*/
