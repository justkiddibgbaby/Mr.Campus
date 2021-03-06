package com.jkb.mrcampus.fragment.function.index.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkb.api.config.Config;
import com.jkb.core.Injection;
import com.jkb.core.contract.function.index.DynamicContract;
import com.jkb.core.control.messageState.MessageObservable;
import com.jkb.core.control.userstate.LoginContext;
import com.jkb.core.data.dynamic.dynamic.DynamicBaseData;
import com.jkb.core.presenter.function.index.dynamic.DynamicPresenter;
import com.jkb.model.utils.LogUtils;
import com.jkb.mrcampus.R;
import com.jkb.mrcampus.activity.DynamicCreateActivity;
import com.jkb.mrcampus.activity.DynamicDetailActivity;
import com.jkb.mrcampus.activity.MainActivity;
import com.jkb.mrcampus.activity.MessageActivity;
import com.jkb.mrcampus.adapter.recycler.NoAlphaItemAnimator;
import com.jkb.mrcampus.adapter.recycler.dynamic.DynamicAdapter;
import com.jkb.mrcampus.adapter.recycler.itemDecoration.DividerItemDecoration;
import com.jkb.mrcampus.base.BaseFragment;
import com.jkb.mrcampus.fragment.dialog.ShareDynamicDialogFragment;
import com.jkb.mrcampus.fragment.dialog.WriteDynamicDialogFragment;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 首页——动态的View层
 * Created by JustKiddingBaby on 2016/8/22.
 */

public class DynamicFragment extends BaseFragment implements DynamicContract.View,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, Observer {

    public static DynamicFragment newInstance() {
        return new DynamicFragment();
    }

    //View
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    //Data
    private MainActivity mainActivity;
    private DynamicPresenter mPresenter;
    private DynamicAdapter dynamicAdapter;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity = (MainActivity) mActivity;
        setRootView(R.layout.frg_homepage_dynamic);
        super.onCreateView(inflater, container, savedInstanceState);
        init(savedInstanceState);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.start();
        }
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshListener(this);

        //设置添加动态按钮监听器
        rootView.findViewById(R.id.fhd_iv_floatBt).setOnClickListener(this);
        rootView.findViewById(R.id.fhd_iv_floatBt_top).setOnClickListener(this);
        rootView.findViewById(R.id.fhd_content_unReadMessage).setOnClickListener(this);

        //设置下拉加载
        recyclerView.addOnScrollListener(onScrollListener);//设置滑动监听，设置是否下拉刷新

        //设置动态条目相关监听器
        dynamicAdapter.setOnUserClickListener(onUserClickListener);
        dynamicAdapter.setOnLikeActionClickListener(onLikeActionClickListener);
        dynamicAdapter.setOnOriginatorUserClickListener(onOriginatorUserClickListener);
        dynamicAdapter.setOnCircleClickListener(onCircleClickListener);
        dynamicAdapter.setOnShareClickListener(onShareClickListener);
        dynamicAdapter.setOnCommentClickListener(onCommentClickListener);
        dynamicAdapter.setOnDynamicClickListener(onDynamicClickListener);

        //登录状态变化的监听器
        LoginContext.getInstance().addObserver(loginObserver);

        //消息的观察者模式
        MessageObservable.newInstance().addObserver(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        initPresenter();//初始化P层
        if (savedInstanceState == null) {

        } else {

        }
        dynamicAdapter = new DynamicAdapter(context, null);
        recyclerView.setAdapter(dynamicAdapter);
    }

    @Override
    protected void initView() {
        //刷新控件
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fhd_srl);
        //初始化列表栏
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fhd_rv);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new NoAlphaItemAnimator());
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(context,
                        LinearLayoutManager.VERTICAL,
                        getResources().getColor(R.color.line), 1));//添加分割线
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fhd_iv_floatBt:
                showWriteDynamicView();
                break;
            case R.id.fhd_iv_floatBt_top://滚动到顶部
                scrollToTop();
                break;
            case R.id.fhd_content_unReadMessage://未读消息的点击事件
                startDynamicMessageActivity();
                break;
        }
    }

    /**
     * 滑动的监听器
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = (linearLayoutManager).findLastVisibleItemPosition();
            int totalItemCount = linearLayoutManager.getItemCount();
            if (lastVisibleItem > 5) {
                rootView.findViewById(R.id.fhd_iv_floatBt_top).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.fhd_iv_floatBt_top).setVisibility(View.GONE);
            }
            if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                mPresenter.onLoadMore();//设置下拉加载
            }
        }
    };
    /**
     * 圈子的点击监听事件
     */
    private DynamicAdapter.OnCircleClickListener onCircleClickListener =
            new DynamicAdapter.OnCircleClickListener() {
                @Override
                public void onCircleClick(int position) {
                    Log.d(TAG, "position=" + position);
                    mainActivity.startCircleActivity(mPresenter.getCircleId(position));
                }
            };
    /**
     * 用户的点击监听事件
     */
    private DynamicAdapter.OnUserClickListener onUserClickListener =
            new DynamicAdapter.OnUserClickListener() {
                @Override
                public void onUserClick(int position) {
                    Log.d(TAG, "position=" + position);
                    mainActivity.startPersonalCenterActivity(mPresenter.getCreator_id(position));
                }
            };
    /**
     * 原创用户的点击监听事件
     */
    private DynamicAdapter.OnOriginatorUserClickListener onOriginatorUserClickListener =
            new DynamicAdapter.OnOriginatorUserClickListener() {
                @Override
                public void onOriginatorUserClick(int position) {
                    Log.d(TAG, "position=" + position);
                    mainActivity.startPersonalCenterActivity(mPresenter.getOriginator_user_id(position));
                }
            };
    /**
     * 喜欢操作的点击监听事件
     */
    private DynamicAdapter.OnLikeActionClickListener onLikeActionClickListener =
            new DynamicAdapter.OnLikeActionClickListener() {
                @Override
                public void onLikeClick(int position) {
                    Log.d(TAG, "position=" + position);
                    mPresenter.likeDynamic(position);
                }
            };
    /**
     * 评论操作的点击监听事件
     */
    private DynamicAdapter.OnCommentClickListener onCommentClickListener =
            new DynamicAdapter.OnCommentClickListener() {
                @Override
                public void onCommentClick(int position) {
                    Log.d(TAG, "position=" + position);
                    mPresenter.onCommentClick(position);
                }
            };
    /**
     * 分享的点击监听事件
     */
    private DynamicAdapter.OnShareClickListener onShareClickListener =
            new DynamicAdapter.OnShareClickListener() {
                @Override
                public void onShareClick(int position) {
                    Log.d(TAG, "position=" + position);
                    showShareItemView(position);
                }
            };
    /**
     * 动态的点击监听事件
     */
    private DynamicAdapter.OnDynamicClickListener onDynamicClickListener =
            new DynamicAdapter.OnDynamicClickListener() {
                @Override
                public void OnDynamicClick(int position) {
                    //打开动态页面
                    mPresenter.startDynamicDetailView(position);
                }
            };

    /**
     * 显示分享数据
     *
     * @param position 条目
     */
    private void showShareItemView(int position) {
        mainActivity.showShareDynamicView(new ShareDynamicDialogFragment
                .OnShareItemClickListener() {
            @Override
            public void onWechatClick() {

            }

            @Override
            public void onWechatCircleClick() {

            }

            @Override
            public void onQQClick() {

            }

            @Override
            public void onQQZoneClick() {

            }

            @Override
            public void onSinaClick() {

            }

            @Override
            public void onCircleClick() {

            }
        });
    }

    /**
     * 初始化Presenter层
     */
    private void initPresenter() {
        if (mPresenter == null) {
            mPresenter = new DynamicPresenter(this,
                    Injection.provideDynamicDataResponsitory(context.getApplicationContext()));
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
    }

    @Override
    public void scrollToTop() {
        int totalItemCount = linearLayoutManager.getItemCount();
        if (totalItemCount > 0) {
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void showLoginedView() {
        rootView.findViewById(R.id.fhd_srl).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.fhd_content_unLoginView).setVisibility(View.GONE);
    }

    @Override
    public void showUnLoginView() {
        rootView.findViewById(R.id.fhd_srl).setVisibility(View.GONE);
        rootView.findViewById(R.id.fhd_content_unLoginView).setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshingView() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshingView() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showWriteDynamicView() {
        mainActivity.showWriteDynamicView(onWriteDynamicClickListener);
    }

    @Override
    public void showCommentView() {

    }

    @Override
    public void showShareView() {

    }

    @Override
    public void startDynamicDetailView() {

    }

    @Override
    public void like() {

    }

    @Override
    public void setDynamicDataToView(List<DynamicBaseData> dynamicBaseDatas) {
        dynamicAdapter.dynamicBaseDatas = dynamicBaseDatas;
        dynamicAdapter.notifyDataSetChanged();
    }

    @Override
    public void startDynamicActivity(@NonNull int dynamic_id, @NonNull String dynamicType) {
        switch (dynamicType) {
            case Config.D_TYPE_ARTICLE:
                dynamicType = DynamicDetailActivity.SHOW_DYNAMIC_TYPE_ARTICLE;
                break;
            case Config.D_TYPE_NORMAL:
                dynamicType = DynamicDetailActivity.SHOW_DYNAMIC_TYPE_NORMAL;
                break;
            case Config.D_TYPE_TOPIC:
                dynamicType = DynamicDetailActivity.SHOW_DYNAMIC_TYPE_TOPIC;
                break;
        }
        mainActivity.startDynamicActivity(dynamic_id, dynamicType);
    }

    @Override
    public void startCommentActivity(@NonNull int dynamic_id) {
        mainActivity.startCommentListActivity(dynamic_id);
    }

    @Override
    public void startDynamicMessageActivity() {
        mainActivity.startMessageActivity(MessageActivity.MESSAGE_TYPE_DYNAMIC);
    }

    @Override
    public void setPresenter(DynamicContract.Presenter presenter) {
        mPresenter = (DynamicPresenter) presenter;
    }

    @Override
    public void showLoading(String value) {
        mainActivity.showLoading(value, this);
    }

    @Override
    public void dismissLoading() {
        mainActivity.dismissLoading();
    }

    @Override
    public void showReqResult(String value) {
        mainActivity.showShortToast(value);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity = null;
        MessageObservable.newInstance().deleteObserver(this);
        LoginContext.getInstance().deleteObserver(loginObserver);
        refreshLayout = null;
        linearLayoutManager = null;
        recyclerView = null;
        onScrollListener = null;
        dynamicAdapter = null;
    }

    /**
     * 登录状态改变时候的监听器
     */
    private Observer loginObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            boolean logined = LoginContext.getInstance().isLogined();
            if (logined) {
                showLoginedView();
            } else {
                showUnLoginView();
                mPresenter.setCacheExpired();
            }
        }
    };
    /**
     * 写动态的监听器
     */
    private WriteDynamicDialogFragment.OnWriteDynamicClickListener onWriteDynamicClickListener
            = new WriteDynamicDialogFragment.OnWriteDynamicClickListener() {
        @Override
        public void onTopicClick() {
            mainActivity.startDynamicCreateActivity(
                    DynamicCreateActivity.DYNAMIC_CREATE_TYPE_TOPIC, 0);
        }

        @Override
        public void onArticleClick() {
            mainActivity.startDynamicCreateActivity(
                    DynamicCreateActivity.DYNAMIC_CREATE_TYPE_ARTICLE, 0);
        }

        @Override
        public void onNormalClick() {
            mainActivity.startDynamicCreateActivity(
                    DynamicCreateActivity.DYNAMIC_CREATE_TYPE_NORMAL, 0);
        }
    };

    @Override
    public void update(Observable o, Object arg) {
        //订阅消息数目
        int count = MessageObservable.newInstance().getAllUnReadDynamicMessageCount();
        LogUtils.d(TAG, "functionMenu---unReadMessageCount->" + count);
        if (count > 0) {
            rootView.findViewById(R.id.fhd_content_unReadMessage).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.fhd_tv_unReadMessageCount)).setText(count + "");
        } else {
            rootView.findViewById(R.id.fhd_content_unReadMessage).setVisibility(View.GONE);
        }
    }
}
