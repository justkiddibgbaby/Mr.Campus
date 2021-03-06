package com.jkb.core.contract.map;

import com.jkb.core.base.BasePresenter;
import com.jkb.core.base.BaseView;
import com.jkb.core.data.info.map.MapMarkCircleInfo;
import com.jkb.core.data.info.map.MapMarkUserInfo;
import com.jkb.model.info.LocationInfoSingleton;

/**
 * MapFragment的页面协议类
 * Created by JustKiddingBaby on 2016/7/26.
 */
public interface MapContract {

    interface View extends BaseView<Presenter> {
        /**
         * 选择学校
         */
        void chooseSchool();

        /**
         * 定位到当前
         */
        void location();

        /**
         * 位置暴露开关
         */
        void locationSwitch();

        /**
         * 附近的人开关
         */
        void nearUserSwitch();

        /**
         * 筛选性别
         */
        void filterSex();

        /**
         * 显示选择学校的视图
         */
        void showSchoolSelectorView();

        /**
         * 设置是否允许周边雷达的视图
         */
        void setNearSearchSwitchView(boolean isAble);

        /**
         * 显示学校的视图
         *
         * @param schoolBadge 学校校徽Url
         */
        void showSchoolView(String schoolBadge);

        /**
         * 显示用户位置视图
         *
         * @param userAvatar 用户头像
         */
        void showUserLocation(String userAvatar);

        /**
         * 移动地图到坐标点
         *
         * @param latitude  经度
         * @param longitude 纬度
         */
        void moveMapToLng(double latitude, double longitude);

        /**
         * 设置地图标记：圈子
         *
         * @param mapMarkCircles 地图标记：圈子的数据集合类
         */
        void setMapMarkCircles(MapMarkCircleInfo mapMarkCircles);

        /**
         * 设置地图标记：用户
         *
         * @param mapMarkUsers 地图标记：用户的数据集合类
         */
        void setMapMarkUsers(MapMarkUserInfo mapMarkUsers);

        /**
         * 开始搜索附近的用户信息
         */
        void startSearchNearUserInfo();

        /**
         * 停止搜索附近的用户信息
         */
        void stopSearchNearUserInfo();

        /**
         * 清除周边雷达相关
         */
        void clearRadarSearch();
    }

    interface Presenter extends BasePresenter {

        /**
         * 初始化位置和学校的数据
         */
        void initLocation$SchoolData();

        /**
         * 初始化用户数据
         */
        void initUserData();

        /**
         * 初始化地圖的位置：学校的坐标位置
         */
        void initMapLocation();

        /**
         * 移动地图到学校中心点
         */
        void moveMapToSchoolCenter();

        /**
         * 移动地图到用户坐标位置
         */
        void moveMapToUserLocation();

        /**
         * 绑定本地坐标信息类
         *
         * @return 本地坐标信息类的对象
         */
        LocationInfoSingleton bindLocationInfo();

        /**
         * 刷新数据
         */
        void onRefresh();

        /**
         * 初始化地图标记数据
         */
        void initMapMarkInfo();

        /**
         * 绑定数据到视图中
         */
        void bindDataToView();
    }
}
