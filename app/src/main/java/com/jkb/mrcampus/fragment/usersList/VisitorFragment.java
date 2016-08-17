package com.jkb.mrcampus.fragment.usersList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkb.mrcampus.R;
import com.jkb.mrcampus.adapter.recycler.AttentionListAdapter;
import com.jkb.mrcampus.adapter.recycler.VisitorListAdapter;
import com.jkb.mrcampus.base.BaseFragment;

/**
 * 访客页面
 * Created by JustKiddingBaby on 2016/8/17.
 */

public class VisitorFragment extends BaseFragment {

    public VisitorFragment() {
    }

    public static VisitorFragment INSTANCE = null;

    public static VisitorFragment newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VisitorFragment();
        }
        return INSTANCE;
    }

    private RecyclerView rv;
    private VisitorListAdapter visitorListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRootView(R.layout.frg_userslist_visitor);
        init(savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        visitorListAdapter = new VisitorListAdapter(mActivity);
        rv.setAdapter(visitorListAdapter);
    }

    @Override
    protected void initView() {
        //初始化标题栏
        ((TextView) rootView.findViewById(R.id.ts4_tv_name)).setText(R.string.Visitors);
        //设置布局方向等
        rv = (RecyclerView) rootView.findViewById(R.id.fuv_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
    }
}
