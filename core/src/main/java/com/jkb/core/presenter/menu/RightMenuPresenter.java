package com.jkb.core.presenter.menu;

import com.jkb.core.contract.menu.RightMenuContract;
import com.jkb.core.control.userstate.LoginContext;
import com.jkb.core.control.userstate.LogoutState;
import com.jkb.core.control.userstate.UserState;
import com.jkb.model.info.UserInfoSingleton;

import jkb.mrcampus.db.entity.Users;

/**
 * 右滑菜单的P层
 * Created by JustKiddingBaby on 2016/8/16.
 */

public class RightMenuPresenter implements RightMenuContract.Presenter {

    private RightMenuContract.View view;

    public RightMenuPresenter(RightMenuContract.View view) {
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void getCountData() {
        //设置计数的数据
        UserInfoSingleton info = UserInfoSingleton.getInstance();
        Users users = info.getUsers();
        if (users == null) {
            //切换身份
            LoginContext.getInstance().setUserState(new LogoutState());
            return;
        }
        view.setAttentionCount(users.getAttentionCount());
        view.setFansCount(users.getFansCount());
        view.setVisitorCount(users.getVisitorCount());
    }

    @Override
    public void setOnUsersDataChangedListener(UserState.UsersChangedListener listener) {
        LoginContext.getInstance().setRightSlideMenuDataViewChangedListener(listener);
    }

    @Override
    public void start() {
        setOnUsersDataChangedListener(view.onUserDataChangedListener());
    }
}