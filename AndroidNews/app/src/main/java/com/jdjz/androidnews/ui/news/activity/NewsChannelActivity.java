package com.jdjz.androidnews.ui.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;

import com.jdjz.androidnews.R;
import com.jdjz.androidnews.app.AppConstant;
import com.jdjz.androidnews.bean.NewsChannelTable;
import com.jdjz.androidnews.ui.news.adapter.ChannelAdapter;
import com.jdjz.androidnews.ui.news.adapter.ChannelAdapter.OnItemClickListener;
import com.jdjz.androidnews.ui.news.contract.NewsChannelCtontract;
import com.jdjz.androidnews.ui.news.event.ChannelItemMoveEvent;
import com.jdjz.androidnews.ui.news.model.NewsChannelModel;
import com.jdjz.androidnews.ui.news.presenter.NewsChannelPresenter;
import com.jdjz.androidnews.widget.ItemDragHelperCallback;
import com.jdjz.common.base.BaseActivity;
import com.jdjz.common.commonutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by tchl on 2016-12-22.
 */
public class NewsChannelActivity extends BaseActivity<NewsChannelPresenter,NewsChannelModel>implements NewsChannelCtontract.View {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.news_channel_mine_rv)
    RecyclerView newsChannelMineRv;
    @Bind(R.id.news_channel_more_rv)
    RecyclerView newsChannelMoreRv;//<NewsChanelPresenter,NewsChannelModel> implements NewsChannelContract.View

    private ChannelAdapter channelAdapterMine;
    private ChannelAdapter channelAdapterMore;
    @Override
    public int getLayoutId() {
        return R.layout.act_news_channel;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mRxManager.on(AppConstant.CHANNEL_SWAP, new Action1<ChannelItemMoveEvent>() {

            @Override
            public void call(ChannelItemMoveEvent channelItemMoveEvent) {
                if(channelItemMoveEvent !=null){
                    mPresenter.onItemSwap((ArrayList<NewsChannelTable>) channelAdapterMine.getAll(),channelItemMoveEvent.getFromPosition(),channelItemMoveEvent.getToPosition());
                }
            }
        });
    }

    @Override
    public void initView() {


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        mPresenter.loadChannelsRequest();

    }

    /**
     * 入口
     *
     * @param context
     */
    public static void startAction(Context context) {
        Intent intent = new Intent(context, NewsChannelActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void returnMineNewsChannels(List<NewsChannelTable> newsChannelsMine) {
        LogUtils.logd("returnMineNewsChannels");
        channelAdapterMine = new ChannelAdapter(mContext,R.layout.item_news_channel);
        newsChannelMineRv.setLayoutManager(new GridLayoutManager(this,4, LinearLayout.VERTICAL,false));
        newsChannelMineRv.setItemAnimator(new DefaultItemAnimator());
        newsChannelMineRv.setAdapter(channelAdapterMine);
        newsChannelMineRv.setItemAnimator(new DefaultItemAnimator());
        channelAdapterMine.replaceAll(newsChannelsMine);

        channelAdapterMine.setOnItemClickListener(new ChannelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannelTable newsChannel = channelAdapterMine.get(position);
                channelAdapterMore.add(newsChannel);
                channelAdapterMine.removeAt(position);
                mPresenter.onItemAddOrRemove((ArrayList<NewsChannelTable>) channelAdapterMine.getAll(), (ArrayList<NewsChannelTable>)channelAdapterMore.getAll());

            }
        });

        ItemDragHelperCallback itemDragHelperCallback = new ItemDragHelperCallback(channelAdapterMine);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragHelperCallback);
        itemTouchHelper.attachToRecyclerView(newsChannelMineRv);
        channelAdapterMine.setItemDragHelperCallback(itemDragHelperCallback);

    }

    @Override
    public void returnMoreNewsChannels(List<NewsChannelTable> newsChannelsMore){
        LogUtils.logd("returnMoreNewsChannels");
        channelAdapterMore = new ChannelAdapter(mContext,R.layout.item_news_channel);
        if(channelAdapterMore == null){LogUtils.logd("channelAdapterMore == null");}
        newsChannelMoreRv.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        newsChannelMoreRv.setItemAnimator(new DefaultItemAnimator());
        newsChannelMoreRv.setAdapter(channelAdapterMore);
        channelAdapterMore.replaceAll(newsChannelsMore);
        channelAdapterMore.setOnItemClickListener(new ChannelAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                LogUtils.logd("returnMoreNewsChannels->onItemClick");
                NewsChannelTable newsChannelTable = channelAdapterMore.get(position);
                channelAdapterMine.add(newsChannelTable);
                channelAdapterMore.removeAt(position);
                mPresenter.onItemAddOrRemove((ArrayList<NewsChannelTable>)channelAdapterMine.getAll(),
                        (ArrayList<NewsChannelTable>)channelAdapterMore.getAll());
            }
        });
    }
}
