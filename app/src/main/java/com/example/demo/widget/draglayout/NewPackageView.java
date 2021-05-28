/*
package com.example.demo.widget.draglayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.TextUtils;
import com.google.gson.reflect.TypeToken;
import com.rich.gson.Gson;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.evergrande.it.audiodevice.utils.InputMethodUtils;
import cn.evergrande.it.common.ui.NameInputFilter;
import cn.evergrande.it.common.ui.adapter.MultiItemTypeAdapter;
import cn.evergrande.it.common.ui.adapter.base.ViewHolder;
import cn.evergrande.it.common.ui.widget.MaxHeightRecyclerView;
import cn.evergrande.it.common.ui.widget.diaglog.CommonDialog;
import cn.evergrande.it.common.ui.widget.diaglog.blur.BlurUtils;
import cn.evergrande.it.hdhome.base.HomeConstants;
import cn.evergrande.it.hdhome.base.network.model.entity.device.CurtainAttrBean;
import cn.evergrande.it.hdhome.base.utils.DeviceStatusUtil;
import cn.evergrande.it.hdhome.base.utils.RouteUtil;
import cn.evergrande.it.hdhome.entity.device.common.FolderBean;
import cn.evergrande.it.hdhome.entity.device.common.RoomDevice;
import cn.evergrande.it.hdhome.service.DeviceSortBiz;
import cn.evergrande.it.hdhome.service.FolderBiz;
import cn.evergrande.it.hdhome.temp.LoginManager;
import cn.evergrande.it.hdhome.util.EventBusUtils;
import cn.evergrande.it.hdhome.util.device.DeviceUtil;
import cn.evergrande.it.hdnetworklib.api.biz.GsonUtil;
import cn.evergrande.it.hdtoolkits.regex.RegexUtils;
import cn.evergrande.it.hdtoolkits.screen.ScreenUtils;
import cn.evergrande.it.hdtoolkits.toast.ToastUtil;
import cn.evergrande.it.logger.BHLog;
import cn.evergrande.it.phone.R;
import cn.evergrande.it.phone.anim.DeviceCardAnimator;
import cn.evergrande.it.phone.bus.FolderChangeEvent;
import cn.evergrande.it.phone.bus.UnLockRefreshEvent;
import cn.evergrande.it.phone.modularity.adapter.HomeAsyncAdapter;
import cn.evergrande.it.phone.modularity.adapter.control.factory.CardControlFactory;
import cn.evergrande.it.phone.modularity.manager.HomeDeviceSortManager;
import cn.evergrande.it.phone.modularity.manager.RoomDeviceSortManager;
import cn.evergrande.it.phone.modularity.proxy.IMainProvider;
import cn.evergrande.it.phone.view.DragLayout.helper.HomeItemBean;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static cn.evergrande.it.hdhome.base.utils.DeviceCategoryType.TYPE_WINDOW_CURTAINS;
import static cn.evergrande.it.phone.anim.DeviceCardAnimator.AnimatorType.EDIT_ING;
import static cn.evergrande.it.phone.anim.DeviceCardAnimator.AnimatorType.EDIT_START;
import static cn.evergrande.it.phone.modularity.adapter.diff.DeviceItemDiffCallBack.DIFF_SWITCH;
import static cn.evergrande.it.phone.modularity.manager.DeviceGroupsHelper.TYPE_HOME;
import static cn.evergrande.it.phone.modularity.manager.DeviceGroupsHelper.TYPE_PACKAGE;

public class NewPackageView extends LinearLayout {
    public static String TAG = cn.evergrande.it.phone.view.DragLayout.NewPackageView.class.getSimpleName();
    public static final int REFRESH_DELAY = 3 * 1000;
    public static final int HIDE_VIEW_COMMON_TYPE = 100;
    public static final int HIDE_VIEW_LONG_TOUCH_TYPE = 200;
    private Context mContext;
    private NewLauncherView mLauncherView;
    private RecyclerView mHomeRecyclerView;
    private EditText nameEt;
    private ImageView clearIv;
    private MaxHeightRecyclerView packageRv;
    private ImageView ivBlurBg;
    private boolean hasSwap = false;
    private ViewGroup clickView;
    private HomeAsyncAdapter mAdapter;
    private FolderBean mFolderBean;
    private boolean isDeleteFolder;
    private long roomId;
    private PackageViewBridge packageViewBridge;
    private boolean isEdit = false;
    private int mScreenHeight;
    private int mScreenWidth;
    private OnRecyclerItemTouchListener mOnRecyclerItemTouchListener;
    private IMainProvider mIMainProvider;
    private List<HomeItemBean> mNewData;
    protected DeviceCardAnimator mItemAnimator;
    protected Bitmap mShotBitmap;
    protected Bitmap mBlurBitmap;
    private HashMap<String, Subscription> mMapCurtainSub;     //延时刷新（窗帘）
    private CommonDialog mDeleteCardDialog;

    public NewPackageView(Context context) {
        this(context, null);
    }

    public NewPackageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewPackageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        Activity activity = ((Activity) context);
        mMapCurtainSub = new HashMap<>();
        Point outSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(outSize);
        mScreenWidth = outSize.x;
        mScreenHeight = outSize.y;
        View.inflate(context, R.layout.new_package_view_layout, this);
        mIMainProvider = (IMainProvider) ARouter
                .getInstance()
                .build(RouteUtil.PATH_MAIN_ACTIVITY)
                .navigation();
    }

    public FolderBean getFolderBean() {
        return mFolderBean;
    }

    public void initData(FolderBean folderBean, boolean isEdit, long roomId) {
        this.mFolderBean = folderBean;
        this.roomId = roomId;
        this.isEdit = isEdit;
        mMapCurtainSub.clear();
        nameEt.post(new Runnable() {
            @Override
            public void run() {
                int margin = ScreenUtils.dpToPx(240);     //dp
                int maxHeight = mScreenHeight - nameEt.getHeight() - margin;
                packageRv.setMaxHeight(maxHeight);
            }
        });

        mOnRecyclerItemTouchListener.setEditModel(isEdit);
        mItemAnimator.setAnimatorType(isEdit ? EDIT_ING : EDIT_START);
        if (mFolderBean != null && mFolderBean.getRoomDeviceList().size() > 0) {
            transfer2HomeBean();
            nameEt.setText(folderBean.getFolderName());
        } else {
            ToastUtil.showLong(R.string.folder_data_null);
        }
        if (isEdit) {
            nameEt.setEnabled(true);
            clearIv.setVisibility(VISIBLE);
        } else {
            nameEt.setEnabled(false);
            clearIv.setVisibility(GONE);
        }

        initItemClick();
        //高斯模糊
        mShotBitmap = BlurUtils.activityShot((Activity) mContext, mIMainProvider.getRootLayout());
        mBlurBitmap = BlurUtils.blur(mContext, mShotBitmap, 15, 0.15f);
        ivBlurBg.setImageBitmap(mBlurBitmap);
    }

    //更新面板音乐状态
    public void updateMusicItem(boolean isPlay, String musicName) {
//        for (int i = 0; i < homeItemBeanList.size(); i++) {
//            HomeItemBean itemBean = homeItemBeanList.get(i);
//            RoomDevice roomDevice = itemBean.getRoomDevice();
//            if (roomDevice != null && MarshallDeviceUtil.isAIPlayer(roomDevice.getDevice_category_id())) {
//                itemBean.setMusicName(musicName);
//                itemBean.setPlaying(isPlay);
//                homeItemBeanList.set(i, itemBean);
//                if (!packageViewBridge.isDragging()) {
//                    adapter.notifyDataSetChanged();
//                }
//                break;
//            }
//        }
    }

    public void updateDataVisible() {
        mNewData = deepCopyList(mAdapter.getNewData());
        for (HomeItemBean itemBean : mNewData) {
            itemBean.setNeedHide(false);
        }
        mAdapter.submitList(mNewData);
    }

    public void setNewData(FolderBean folderBean) {
        this.mFolderBean = folderBean;
        transfer2HomeBean();
    }

    private void transfer2HomeBean() {
        List<HomeItemBean> homeItemBeanList = new ArrayList<>();
        List<RoomDevice> devices = mFolderBean.getRoomDeviceList();
        int size = devices.size();
        for (int i = 0; i < size; i++) {
            RoomDevice roomDevice = devices.get(i);
            if (DeviceUtil.isFilter(roomDevice)) {
                continue;
            }
            HomeItemBean homeItemBean = new HomeItemBean();
            homeItemBean.setEditModel(isEdit);
            homeItemBean.setRoomDevice(roomDevice);
            homeItemBean.setNeedHide(packageViewBridge.isDragging() && i == size - 1);
            homeItemBeanList.add(homeItemBean);
        }
        if (homeItemBeanList.size() <= 1) {
            hidePackageView(-1);
        }
        if (mAdapter == null) {
            String pageType = roomId == HomeConstants.HOME_ROOM_ID ? HomeAsyncAdapter.TYPE_HOME :
                    HomeAsyncAdapter.TYPE_HOME;
            mAdapter = new HomeAsyncAdapter(getContext(), homeItemBeanList, pageType, true);
            packageRv.setAdapter(mAdapter);
        } else {
            mAdapter.submitList(deepCopyList(homeItemBeanList));
        }
    }

    public void showDeleteCardDialog(final RoomDevice roomDevice, final int position) {
        if (mDeleteCardDialog == null) {
            mDeleteCardDialog = new CommonDialog(getContext());
        }
        mDeleteCardDialog.setDialogTitle(R.string.delete_home_card)
                .setDialogCancel(R.string.cancel_action)
                .setDialogMessage(mContext.getString(R.string.delete_home_card_device))
                .setDialogEnsure(R.string.delete_card_action)
                .setDialogListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onEnsureClick(View view) {
                        if (roomDevice != null) {
                            DeviceSortBiz biz = new DeviceSortBiz();
                            String uuid = roomDevice.getDevice_uuid();
                            if (roomId == HomeConstants.HOME_ROOM_ID) {
                                //常用设备编组移除
                                biz.deleteHomeDeviceSort(uuid);
                                HomeDeviceSortManager
                                        .getInstance()
                                        .delDeviceSort(uuid);
                            } else {
                                //房间设备编组移除
                                biz.deleteRoomDeviceSort(uuid);
                                RoomDeviceSortManager
                                        .getInstance()
                                        .delDeviceSort(uuid);
                            }
                            mFolderBean.getRoomDeviceList().remove(roomDevice);
                            updateFolder();
                        }
                        mAdapter.remove(position);
                        FolderChangeEvent event = new FolderChangeEvent(roomId);
                        if (mAdapter.getNewData().size() > 1) {
                            //更新设备列表文件夹里面设备个数
                            event.setType(FolderChangeEvent.FOLDER_DEVICE_ORDER_CHANGED);
                            event.setFolderBean(mFolderBean);
                            isDeleteFolder = false;
                        } else {
                            //删除文件夹
                            event.setType(FolderChangeEvent.FOLDER_DELETE);
                            event.setFolderBean(mFolderBean);
                            removeFolder();
                            isDeleteFolder = true;
                            changeUI(HIDE_VIEW_COMMON_TYPE);
                        }
                        EventBusUtils.post(event);
                    }

                    @Override
                    public void onCancelClick(View view) {

                    }
                }).show();
    }

    private void initItemClick() {
        mAdapter.setOnItemChildClickListener(new MultiItemTypeAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, ViewHolder holder, int position) {
                HomeItemBean bean = mAdapter.getDatas().get(position);
                RoomDevice roomDevice = bean.getRoomDevice();
                if (view.getId() == R.id.iv_arrow) {
                    if (isEdit) {
                        showDeleteCardDialog(roomDevice, position);
                    } else {
                        if (DeviceStatusUtil.isNoDetail(roomDevice)) {
                            return;
                        }
                        BHLog.i(TAG, "onItemClick roomDevice " + roomDevice.toString());
                        if (!LoginManager.getInstance().isCloudLogin()) {
                            ToastUtil.toastError(R.string.phone_network_no_use);
                            return;
                        }
                        gotoRoomDeviceActivity(roomDevice);
                    }
                } else {
                    if (!isEdit) {
                        dealControl(view, (ViewHolder) holder, bean);
                    }
                }
            }
        });
    }

    */
/**
     * 卡片上面的icon控制
     *
     * @param view
     * @param holder
     * @param itemBean
     *//*

    private void dealControl(View view, ViewHolder holder, HomeItemBean itemBean) {
        CardControlFactory
                .getInstance()
                .createControl(holder, itemBean, roomId, true, false)
                .click(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameEt = findViewById(R.id.et_folder_name);
        clearIv = findViewById(R.id.iv_clear);
        clickView = findViewById(R.id.click_view);
        packageRv = findViewById(R.id.rv_package);
        ivBlurBg = findViewById(R.id.iv_blur_bg);
        initRecyclerView();
        clearIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEt.setText("");
            }
        });
        clickView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isDeleteFolder = false;
                for (RoomDevice roomDevice : mFolderBean.getRoomDeviceList()) {
                    if (roomDevice.getDevice_category_id() == TYPE_WINDOW_CURTAINS) {
                        CardControlFactory
                                .getInstance()
                                .putCurtainPauseState(roomDevice.getDevice_uuid(), true);
                        cancelDelayRefresh(roomDevice.getDevice_uuid());
                    }
                }
                hidePackageView(-1);
            }
        });

        InputFilter filterJustCNENNumber = new NameInputFilter(NameInputFilter.signCNNumber);
        InputFilter filterLength = new InputFilter.LengthFilter(HomeConstants.FOLDER_NAME_LENGTH);
        nameEt.setFilters(new InputFilter[]{filterLength, filterJustCNENNumber});
    }

    private void initRecyclerView() {
        packageRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mItemAnimator = new DeviceCardAnimator();
        packageRv.setItemAnimator(mItemAnimator);
        mOnRecyclerItemTouchListener = new OnRecyclerItemTouchListener(packageRv) {
            @Override
            public void onShowPress(RecyclerView.ViewHolder vh, int pos) {
                doOnShowPress(pos);
            }
        };
        packageRv.addOnItemTouchListener(mOnRecyclerItemTouchListener);
        DefaultItemAnimator itemAnimator = (DefaultItemAnimator) packageRv.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setSupportsChangeAnimations(false);
        }
    }

    private void doOnShowPress(int pos) {
        if (pos < mAdapter.getDatas().size() && pos >= 0) {
            RecyclerView.ViewHolder viewHolder = packageRv.findViewHolderForAdapterPosition(pos);
            if (viewHolder != null) {
                View childView = packageRv.findViewHolderForAdapterPosition(pos).itemView;
                childView.clearAnimation();
                mLauncherView.startDrag(childView, pos, false, 0, packageRv);
                if (childView.getVisibility() != View.VISIBLE) {
                    packageViewBridge.updateDragState(true);
                }
            } else {
                BHLog.e(TAG, "onItemLongClick viewHolder is null!!!");
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private void hidePackageView(int pos) {
        mHomeRecyclerView.stopNestedScroll();
        String folderName = nameEt.getText().toString().trim();

        if (TextUtils.isEmpty(folderName) && !isDeleteFolder) {
            ToastUtil.toastError(R.string.group_name_cannot__empty);
            packageViewBridge.updateAll(TYPE_PACKAGE, pos);
            return;
        }
        if (folderName.length() < 2) {
            ToastUtil.toastError(R.string.at_least_2_char);
            packageViewBridge.updateAll(TYPE_PACKAGE, pos);
            return;
        }
        if (folderName.length() > HomeConstants.FOLDER_NAME_LENGTH && !isDeleteFolder) {
            showToast(String.format(mContext.getString(R.string.at_most_char), HomeConstants.FOLDER_NAME_LENGTH));
            packageViewBridge.updateAll(TYPE_PACKAGE, pos);
            return;
        }
        if (!RegexUtils.isNUM_ZH(folderName) && !isDeleteFolder) {
            ToastUtil.toastError(R.string.support_chinese_number);
            packageViewBridge.updateAll(TYPE_PACKAGE, pos);
            return;
        }
        if (!TextUtils.isEmpty(mFolderBean.getFolderName()) && !mFolderBean.getFolderName().equals(folderName)) {
            mFolderBean.setFolderName(folderName);
            updateFolder();
            FolderChangeEvent event = new FolderChangeEvent(roomId);
            event.setType(FolderChangeEvent.NAME_CHANGE);
            event.setFolderBean(mFolderBean);
            EventBusUtils.post(event);
        }
        if (hasSwap) {
            notifyFolderChanged();
            hasSwap = false;
        }
        packageViewBridge.hidePackageView();
        packageViewBridge.updateAll(TYPE_HOME, pos);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            mAdapter.submitList(new ArrayList<HomeItemBean>());
            if (mBlurBitmap != null) {
                mBlurBitmap.recycle();
                mBlurBitmap = null;
            }
            if (mShotBitmap != null) {
                mShotBitmap.recycle();
                mShotBitmap = null;
            }
        }
    }

    public MaxHeightRecyclerView getPackageRv() {
        return packageRv;
    }

    public PackageDragListener getPackageDragListener() {
        return new PackageDragListener();
    }

    public void showToast(String message) {
        ToastUtil.toastError(message);
    }

    private void gotoRoomDeviceActivity(RoomDevice roomDevice) {
        RouteUtil.goToDeviceActivity((Activity) getContext(), roomDevice.getDevice_uuid(), roomDevice.getRoom_id());
    }

    public void setLauncherView(NewLauncherView mLauncherView) {
        this.mLauncherView = mLauncherView;
    }

    public void setHomeRecyclerView(RecyclerView mHomeRecyclerView) {
        this.mHomeRecyclerView = mHomeRecyclerView;
    }

    public List<RoomDevice> getPackRoomDevices() {
        return mFolderBean.getRoomDeviceList();
    }

    public int getHeaderViewCount() {
        return 0;
    }

    public void setPackageViewBridge(PackageViewBridge packageViewBridge) {
        this.packageViewBridge = packageViewBridge;
    }

    */
/**
     * 文件夹拖动
     *//*

    public class PackageDragListener implements NewLauncherView.IDragActionCallback {

        @Override
        public void onDrop(View dragView, int startPosition, int lastPosition, int currentPosition, boolean out) {
            dragView.setVisibility(View.VISIBLE);
            mNewData = deepCopyList(mAdapter.getDatas());
            int index = currentPosition == -1 ? startPosition : currentPosition;
            if (index < mNewData.size()) {
                mNewData.get(index).setNeedHide(false);
                mAdapter.submitList(mNewData);
            }
            packageViewBridge.updateDragState(false);
        }

        @Override
        public boolean prepareInsert(int startPos, int targetPost) {
            return false;
        }

        @Override
        public void unPrepareInsert() {

        }

        @Override
        public boolean onSwap(View dragView, int startPosition, int lastPosition, int currentPosition, boolean out) {
            BHLog.i("mTest", "onSwap==startPosition=" + startPosition + " currentPosition=" + currentPosition);
            if (startPosition == currentPosition) {
                return false;
            }
            mNewData = mAdapter.getNewData();
            List<RoomDevice> roomDeviceList = mFolderBean.getRoomDeviceList();
            if (startPosition < currentPosition) {
                if (currentPosition >= mNewData.size()) return false;
                for (int i = startPosition; i < currentPosition; i++) {
                    Collections.swap(mNewData, i, i + 1);
                    Collections.swap(roomDeviceList, i, i + 1);
                }
            } else {
                if (startPosition >= mNewData.size()) return false;
                for (int i = startPosition; i > currentPosition; i--) {
                    Collections.swap(mNewData, i, i - 1);
                    Collections.swap(roomDeviceList, i, i - 1);
                }
            }

            int size = roomDeviceList.size();
            for (int i = 0; i < size; i++) {
                RoomDevice roomDevice = roomDeviceList.get(i);
                roomDevice.setFolderOrder(i + 1);
            }
            mAdapter.notifyItemMoved(startPosition, currentPosition);
//            mAdapter.submitList(mNewDatas);
            hasSwap = true;
            return true;
        }

        @Override
        public boolean onInsert(View dragView, int startPosition, int lastPosition, int currentPosition, boolean needOpenPack) {
            return false;
        }

        @Override
        public synchronized void unPack(View dragView, int startPosition, final boolean hidePack) {
            if (mAdapter.getItemCount() == 0 || startPosition >= mAdapter.getItemCount()) {
                return;
            }
            FolderChangeEvent event = new FolderChangeEvent(roomId);
            //要移出的device
            event.setRoomDevice(mAdapter.getItem(startPosition).getRoomDevice());
            mAdapter.remove(startPosition);
            if (mAdapter.getNewData().size() > 1) {
                //更新设备列表文件夹里面设备个数
                event.setType(FolderChangeEvent.DEVICE_REMOVE);
                mFolderBean.getRoomDeviceList().remove(startPosition);
                updateFolder();
                isDeleteFolder = false;
                if (hidePack) {
                    changeUI(HIDE_VIEW_LONG_TOUCH_TYPE);
                } else {
                    packageViewBridge.updateDragState(false);
                }
            } else {
                //删除文件夹
                event.setType(FolderChangeEvent.FOLDER_DELETE);
                removeFolder();
                isDeleteFolder = true;
                changeUI(HIDE_VIEW_COMMON_TYPE);
                packageViewBridge.updateDragState(false);
            }
            event.setFolderBean(mFolderBean);
            EventBusUtils.post(event);
        }
    }

    */
/**
     * type==HIDE_VIEW_COMMON_TYPE 普通拖出边界隐藏文件夹
     * type==HIDE_VIEW_LONG_TOUCH_TYPE 拖出边界没松手情况下，隐藏文件夹
     *
     * @param type
     *//*

    private void changeUI(int type) {
        List<HomeItemBean> homeList = packageViewBridge.currentHomeItemList();
        for (int i = 0; i < homeList.size(); i++) {
            HomeItemBean homeItemBean = homeList.get(i);
            if (homeItemBean.isFolder() && homeItemBean.getFolderBean().getFolderId()
                    == mFolderBean.getFolderId()) {
                //当前操作文件夹所在设备列表的pos
                if (TextUtils.isEmpty(nameEt.getText().toString().trim())) {
                    nameEt.setText(mFolderBean.getFolderName());
                }
                if (type == HIDE_VIEW_COMMON_TYPE) {
                    hidePackageView(i);
                } else if (type == HIDE_VIEW_LONG_TOUCH_TYPE) {
                    hidePackageView(i + 1);
                }
                break;
            }
        }
    }

    public void updateUI(int index, FolderBean folderBean) {
        this.mFolderBean = folderBean;
        nameEt.setText(folderBean.getFolderName());
        mNewData = mAdapter.getNewData();
        HomeItemBean itemBean = mNewData.get(index);
        RoomDevice roomDeviceNew = folderBean.getRoomDeviceList().get(index);
        RoomDevice roomDeviceOld = itemBean.getRoomDevice();
        itemBean.setRoomDevice(roomDeviceNew);
        mNewData.set(index, itemBean);
        if (roomDeviceNew.getDevice_category_id() == TYPE_WINDOW_CURTAINS) {
            CurtainAttrBean attrBean = roomDeviceNew.getAttribute(CurtainAttrBean.class);
            if (attrBean != null) {
                String key = roomDeviceNew.getDevice_uuid();
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
        if (!CardControlFactory.getInstance().isLock(roomDeviceNew, roomId, true)) {
            if (DeviceUtil.deviceOffline(roomDeviceOld) != DeviceUtil.deviceOffline(roomDeviceNew)) {
                mAdapter.notifyItemDiffChanged(index);
            } else if (DeviceUtil.isOpen(roomDeviceOld) != DeviceUtil.isOpen(roomDeviceNew)) {
                mAdapter.notifyItemDiffChanged(index, DIFF_SWITCH);
            } else {
                //默认刷全部
                mAdapter.notifyItemDiffChanged(index);
            }
        }
    }

    public void notifyItem(String deviceUuid) {
        mNewData = mAdapter.getNewData();
        int size = mNewData.size();
        for (int i = 0; i < size; i++) {
            HomeItemBean itemBean = mNewData.get(i);
            RoomDevice roomDevice = itemBean.getRoomDevice();
            if (roomDevice != null && android.text.TextUtils.equals(roomDevice.getDevice_uuid(), deviceUuid)) {
                mAdapter.notifyItemDiffChanged(i);
                break;
            }
        }
    }

    public void closeKeyBoard() {
        nameEt.clearFocus();
        InputMethodUtils.closeKeyBoard(getContext(), nameEt);
    }

    public void removeFolder() {
        new FolderBiz().deleteFolder(mFolderBean.getFolderId());
    }

    public void updateFolder() {
        new FolderBiz().insertOrReplace(mFolderBean);
    }

    public interface PackageViewBridge {
        List<HomeItemBean> currentHomeItemList();

        void updateAll(int type, int pos);


        void updateDragLayout(int type, int pos);

        void notifyDataSetChanged();

        void hidePackageView();

        void updateDragState(boolean isDraging);

        void setEdit(boolean isEdit);

        boolean isDragging();

    }

    private void notifyFolderChanged() {
        FolderChangeEvent event = new FolderChangeEvent(roomId);
        event.setType(FolderChangeEvent.FOLDER_DEVICE_ORDER_CHANGED);
        event.setFolderBean(mFolderBean);
        EventBusUtils.post(event);
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }

    //深度复制list
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
}
*/
