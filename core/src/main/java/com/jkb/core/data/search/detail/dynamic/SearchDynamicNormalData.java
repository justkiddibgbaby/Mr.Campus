package com.jkb.core.data.search.detail.dynamic;

import com.jkb.api.entity.search.SearchEntity;
import com.jkb.core.data.info.dynamic.content.DynamicContentNormalInfo;
import com.jkb.core.data.search.detail.SearchData;

/**
 * 搜索的普通数据类
 * Created by JustKiddingBaby on 2016/11/26.
 */

public final class SearchDynamicNormalData extends SearchDynamicData {

    private DynamicContentNormalInfo normal;

    public SearchDynamicNormalData(
            SearchEntity.DataBean.ContentBean.NormalBean normalBean) {
        handleNormalData(normalBean);
    }

    @Override
    public SearchData getSearchData() {
        if (isDataEffective) {
            return this;
        }
        return null;
    }

    /**
     * 处理文章数据
     */
    private void handleNormalData(SearchEntity.DataBean.ContentBean.NormalBean normalBean) {
        if (normalBean == null) {
            isDataEffective = false;
            return;
        }
        normal = new DynamicContentNormalInfo();
        normal.setImg(normalBean.getImg());
        normal.setDoc(normalBean.getDoc());
        setNormal(normal);
    }

    public DynamicContentNormalInfo getNormal() {
        return normal;
    }

    public void setNormal(DynamicContentNormalInfo normal) {
        this.normal = normal;
    }
}
