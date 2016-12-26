package com.jdjz.androidnews.ui.news.presenter;

import com.jdjz.androidnews.bean.NewsChannelTable;
import com.jdjz.androidnews.ui.news.contract.NewsChannelCtontract;
import com.jdjz.common.baserx.RxSubscriber;
import com.jdjz.common.commonutils.LogUtils;

import java.util.List;

/**
 * Created by tchl on 2016-12-23.
 */
public class NewsChannelPresenter extends NewsChannelCtontract.Presenter {
    @Override
    public void loadChannelsRequest() {
        mRxManager.add(mModel.lodeMineNewsChannels().subscribe(
                        new RxSubscriber<List<NewsChannelTable>>(mContext,false)
                        {
            @Override
            protected void _onNext(List<NewsChannelTable> newsChannelTables) {
                mView.returnMineNewsChannels(newsChannelTables);
            }

            @Override
            protected void _onError(String t) {

            }
        }));

        mRxManager.add(mModel.lodeMoreNewsChannels().subscribe(new RxSubscriber<List<NewsChannelTable>>(mContext,false) {
            @Override
            protected void _onNext(List<NewsChannelTable> newsChannelTables) {
                mView.returnMoreNewsChannels(newsChannelTables);
            }

            @Override
            protected void _onError(String t) {

            }
        }));
    }
}
