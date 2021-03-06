package com.jkb.model.dataSource.entering.personmessage;

import android.graphics.Bitmap;

import com.jkb.api.ApiCallback;
import com.jkb.api.ApiResponse;
import com.jkb.api.entity.auth.RegisterEntity;

import java.util.Date;

import jkb.mrcampus.db.entity.UserAuths;
import jkb.mrcampus.db.entity.Users;
import okhttp3.MultipartBody;

/**
 * 个人数据获取的通用接口
 * Created by JustKiddingBaby on 2016/8/1.
 */
public interface PersonMessageDataSource {
    /**
     * 请求注册接口
     * 请求方式：异步请求
     * 使用邮箱注册
     *
     * @param nickName      昵称
     * @param code          验证码
     * @param credential    密码
     * @param identity_type 认证类型
     * @param identifier    认证帐号
     * @param image         图片
     * @param apiCallback   回调接口
     */
    void registerWithEmail(String nickName, String code, String credential,
                           String identity_type, String identifier, MultipartBody.Part image,
                           ApiCallback<ApiResponse<RegisterEntity>> apiCallback);

    /**
     * 请求注册接口
     * 请求方式：异步请求
     * 使用手机号注册
     *
     * @param nickName      昵称
     * @param code          验证码
     * @param credential    密码
     * @param identity_type 认证类型
     * @param identifier    认证帐号
     * @param image         图片
     * @param apiCallback   回调接口
     */
    void registerWithPhone(String nickName, String code, String credential,
                           String identity_type, String identifier, MultipartBody.Part image,
                           ApiCallback<ApiResponse<RegisterEntity>> apiCallback);

    /**
     * 保存数据到数据库中
     */
    void saveUserToDb(Users users);

    /**
     * 保存用户Auth信息到数据库中
     */
    void saveUserAuthToDb(UserAuths userAuths);


    /**
     * 保存系统状态到数据库中
     */
    void saveStatusToDb(int userId, String version, boolean isLogin,
                        boolean isSelectedSchool, int schoolId, Date date);


    /**
     * 转换Bitmap为文件
     *
     * @param bitmap 图像位图对象
     * @param path   保存路径
     * @return filePath
     */
    String saveBitmapToFile(Bitmap bitmap, String path, String fileName);

    /**
     * 通过路径名得到Bitmap对象
     *
     * @param urlPath 图片路径
     */
    Bitmap getBitmapFromFile(String urlPath);

    /**
     * 得到系统的当前版本号
     */
    String getCurrentVersion();
}
