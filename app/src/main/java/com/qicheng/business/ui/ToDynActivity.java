/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.DynLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.DynEventAargs;
import com.qicheng.business.module.Dyn;
import com.qicheng.business.ui.chat.activity.ShowBigImage;
import com.qicheng.business.ui.component.DynSearch;
import com.qicheng.business.ui.component.GeneralListView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.framework.util.UIUtil;
import com.qicheng.util.Const;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static com.qicheng.util.Const.Intent.DYN_QUERY_NAME;
import static com.qicheng.util.Const.Intent.DYN_QUERY_TYPE;
import static com.qicheng.util.Const.Intent.DYN_QUERY_VALUE;

public class ToDynActivity extends BaseActivity {

    /*动态的ListView视图*/
    private GeneralListView dynListView = null;
    /*动态的ListView的Adapter*/
    private DynListViewAdapter listAdapter;
    /*搜索选项的GridView视图*/
    private List<Dyn> dynSearchList = new ArrayList<Dyn>();
    /*搜索条件*/
    private List<Dyn> newData;
    /*动态的类型*/
    private DynSearch dynSearch = new DynSearch();
    private static final int ADD_SUCCESS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_to_dyn);

        dynListView = (GeneralListView) findViewById(R.id.dynlist);
        listAdapter = new DynListViewAdapter(getActivity().getApplicationContext());
        Bundle extras = getIntent().getExtras();
        String queryValue = extras.getString(DYN_QUERY_VALUE);
        String queryName = extras.getString(DYN_QUERY_NAME);
        Byte queryType = extras.getByte(DYN_QUERY_TYPE);
        if (queryName != null) {
            setTitle(queryName);
        } else {
            setTitle(queryValue);
        }
        dynSearch.setQueryType(queryType);
        if (queryType != Const.QUERY_TYPE_MY) {
            dynSearch.setQueryValue(queryValue);
        }
        getDynList(dynSearch);
        dynListView.setAdapter(listAdapter);

        dynListView.setonRefreshListener(new GeneralListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dynSearch.setOrderBy(Const.ORDER_BY_NEWEST);
                        dynSearch.setOrderNum(dynSearchList.get(0).getOrderNum());
                        getDynList(dynSearch);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        listAdapter.notifyDataSetChanged();
                        dynListView.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }


            /*获取更多动态信息*/
            @Override
            public void toLastRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dynSearch.setOrderNum(dynSearchList.get(dynSearchList.size() - 1).getOrderNum());
                        dynSearch.setOrderBy(Const.ORDER_BY_EARLIEST);
                        getDynList(dynSearch);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        listAdapter.notifyDataSetChanged();
                        dynListView.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trip_to_dyn, menu);
        if (dynSearch.getQueryType() == Const.QUERY_TYPE_MY || dynSearch.getQueryType() == Const.QUERY_TYPE_USER) {
            menu.findItem(R.id.activity_add).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.activity_add) {
            Intent intent = new Intent(getActivity(), DynPublishActivity.class);
            if (dynSearch.getQueryType() != null) {
                Bundle bundle = new Bundle();
                bundle.putByte("query_type", dynSearch.getQueryType());
                bundle.putString("query_value", dynSearch.getQueryValue());
                intent.putExtras(bundle);
            }
            startActivityForResult(intent, 0);
        }
        return super.onOptionsItemSelected(item);
    }


    /*添加动态完成后，捕获返回值后的操作*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ADD_SUCCESS:
                dynSearch.setOrderBy(Const.ORDER_BY_NEWEST);
                dynSearch.setOrderNum(dynSearchList.get(0).getOrderNum());
                getDynList(dynSearch);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /*获取动态列表*/
    public void getDynList(final DynSearch dynSearchCondition) {

        DynLogic dynLogic = (DynLogic) LogicFactory.self().get(LogicFactory.Type.Dyn);
        dynLogic.getDynList(dynSearchCondition, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                DynEventAargs dynEventAargs = (DynEventAargs) args;
                OperErrorCode errCode = dynEventAargs.getErrCode();
                int orderBy = dynSearchCondition.getOrderBy();
                Byte type = dynSearchCondition.getQueryType();
                switch (errCode) {
                    case Success:
                        newData = dynEventAargs.getDynList();
                        refreshSearchList(orderBy);
                        break;
                    case NoDataFound:
                        Alert.Toast(getResources().getString(R.string.activity_noMoreData));
                        listAdapter.notifyDataSetChanged();
                        break;
                    default:
                        Alert.Toast(getResources().getString(R.string.activity_reject));
                        break;
                }
            }
        }));
    }

    /**
     * 刷新动态，判断是上拉刷新还是下拉更多
     *
     * @param orderBy
     */
    public void refreshSearchList(int orderBy) {

        if (dynSearchList.size() == 0 && orderBy == Const.ORDER_BY_NEWEST) {
            /**
             * 场景刚刚进入动态列表，当动态的列表为空，并且查询类型为空，并且查询方向为最新
             */
            dynSearchList = newData;
            listAdapter.setDataList(dynSearchList);
            listAdapter.notifyDataSetChanged();
        } else if (dynSearchList.size() > 0 && orderBy == Const.ORDER_BY_NEWEST) {
                            /*
                            场景最新动态列表，上拉刷新
                             */
            dynSearchList.addAll(0, newData);
            listAdapter.notifyDataSetChanged();
        } else if (dynSearchList.size() > 0 && orderBy == Const.ORDER_BY_EARLIEST) {
                            /*
                            场景：下拉刷新动态列表
                             */
            dynSearchList.addAll(newData);
            listAdapter.notifyDataSetChanged();
        }

    }


    /*shareSDK分享*/
    private void showShare(String msg, String url) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        oks.setText(msg);
        oks.setImageUrl(url);
        // 启动分享GUI
        oks.show(getActivity());
    }


    /*点赞分享次数*/
    public void interact(String id, byte action) {
        DynLogic dynLogic = (DynLogic) LogicFactory.self().get(LogicFactory.Type.Dyn);
        dynLogic.interact(id, action, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                DynEventAargs dynEventAargs = (DynEventAargs) args;
                OperErrorCode errCode = dynEventAargs.getErrCode();
                switch (errCode) {
                    case Success:
                        break;
                    default:
                        Alert.Toast(getResources().getString(R.string.activity_reject));
                        break;
                }
            }
        }));
    }

    /**
     * 删除动态
     *
     * @param activityId
     */
    private void deleteDyn(String activityId) {
        DynLogic dynLogic = (DynLogic) LogicFactory.self().get(LogicFactory.Type.Dyn);
        dynLogic.deleteDyn(activityId, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                DynEventAargs dynEventAargs = (DynEventAargs) args;
                OperErrorCode errCode = dynEventAargs.getErrCode();
                switch (errCode) {
                    case Success:
                        listAdapter.notifyDataSetChanged();
                        break;
                    default:
                        Alert.Toast(getResources().getString(R.string.activity_delete_reject));
                        break;
                }
            }
        }));
    }


    /**
     * ListView的Adapter
     */
    public class DynListViewAdapter extends BaseAdapter {
        private Context mContext;
        private List<Dyn> dataList;
        /*是否赞过的标志*/
        private boolean flag;

        public DynListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void setDataList(List<Dyn> dataList) {
            this.dataList = dataList;
            this.notifyDataSetChanged();
        }

        public void clear() {
            if (null != dataList)
                dataList.clear();
        }

        @Override
        public int getCount() {
            if (null == dataList)
                return 0;
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final int index = position;
            final Dyn bean = dataList.get(position);
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dyn_item, null);
                holder = new ViewHolder();
                holder.portraitl = (ImageView) convertView.findViewById(R.id.portrait);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.pasttime = (TextView) convertView.findViewById(R.id.pasttime);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.photo = (ImageView) convertView.findViewById(R.id.photo);
                holder.likeNum = (TextView) convertView.findViewById(R.id.liketime);
                holder.shareNum = (TextView) convertView.findViewById(R.id.sharetime);
                holder.likeimg = (ImageView) convertView.findViewById(R.id.likeimg);
                holder.shareimg = (ImageView) convertView.findViewById(R.id.shareimg);
                holder.weixin = (ImageView) convertView.findViewById(R.id.weixin);
                holder.likeCollect = (LinearLayout) convertView.findViewById(R.id.like_collect);
                holder.shareCollect = (LinearLayout) convertView.findViewById(R.id.share_collect);
                holder.weixinCollect = (LinearLayout) convertView.findViewById(R.id.weixin_collect);
                convertView.setTag(holder);
                if (dynSearch.getQueryType() != Const.QUERY_TYPE_MY) {
                    holder.delete = (TextView) convertView.findViewById(R.id.activity_delete);
                    holder.delete.setVisibility(View.GONE);
                } else {
                    holder.delete = (TextView) convertView.findViewById(R.id.activity_delete);

                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.delete.setTag(bean);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dyn bean = (Dyn) v.getTag();
                    new AlertDialog.Builder(getActivity()).setTitle("确认删除？")
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String activityId = bean.getActivityId();
                                    deleteDyn(activityId);
                                    dataList.remove(bean);
                                    listAdapter.notifyDataSetChanged();
                                }
                            })
                            .create().show();
                }
            });
            String userPortrait = bean.getPortraitUrl();
            ImageManager.displayPortrait(userPortrait, holder.portraitl);
            holder.name.setText(bean.getNickName());
            holder.pasttime.setText(DateTimeUtil.getTimeInterval(bean.getCreateTime()));
            String thumbnailUrl = bean.getThumbnailUrl();
            if (thumbnailUrl != null) {
                ImageManager.displayImageDefault(thumbnailUrl, holder.photo);
                int screenWidth = UIUtil.getScreenWidth(getActivity());
                holder.photo.setVisibility(View.VISIBLE);
                holder.photo.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenWidth));
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), OriginalPictureActivity.class);
                        intent.putExtra(Const.Intent.ORIGINAL_PICTURE_URL_KEY, bean.getFileUrl());
                        startActivity(intent);
                    }
                });
            } else {
                holder.photo.setVisibility(View.GONE);
            }
            holder.content.setText(bean.getContent());
            Integer likeNum = bean.getLikedNum();
            Integer shareNum = bean.getSharedNum();
            holder.likeNum.setText(likeNum.toString());
            holder.shareNum.setText(shareNum.toString());
            /*初始化时是否被赞过*/
            if (bean.getIsLiked() == 1) {
                holder.likeimg.setImageResource(R.drawable.ic_liked);
            } else {
                holder.likeimg.setImageResource(R.drawable.ic_like);
            }

            holder.likeCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = bean.getActivityId();
                    /*赞与取消赞的逻辑*/
                    if (bean.getIsLiked() == Const.INTERACT_ACTION_LIKED) {
                        holder.likeNum.setText((Integer.valueOf(holder.likeNum.getText().toString()) + 1) + "");
                        byte action = Const.INTERACT_ACTION_LIKED;
                        interact(id, action);
                        holder.likeimg.setImageResource(R.drawable.ic_liked);
                        bean.setIsLiked(Const.INTERACT_ACTION_CANCEL);
                        bean.setLikedNum((Integer.valueOf(holder.likeNum.getText().toString())));
                    } else {
                        holder.likeNum.setText((Integer.valueOf(holder.likeNum.getText().toString()) - 1) + "");
                        byte action = Const.INTERACT_ACTION_CANCEL;
                        holder.likeimg.setImageResource(R.drawable.ic_like);
                        interact(id, action);
                        bean.setIsLiked(Const.INTERACT_ACTION_LIKED);
                        bean.setLikedNum((Integer.valueOf(holder.likeNum.getText().toString())));
                    }
                }
            });
            holder.shareCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showShare(bean.getContent(), bean.getFileUrl());
                    String id = bean.getActivityId();
                    holder.shareNum.setText((Integer.valueOf(holder.shareNum.getText().toString()) + 1) + "");
                    byte action = Const.INTERACT_ACTION_SHARED;
                    interact(id, action);
                    bean.setSharedNum((Integer.valueOf(holder.shareNum.getText().toString())));
                }
            });
            holder.weixinCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getUserImId().equals(Cache.getInstance().getUser().getUserImId())) {
                        return;
                    }
                    Intent i = new Intent();
                    i.setClass(getActivity(), ChatActivity.class);
                    i.putExtra(Const.Intent.FRIEND_SOURCE_KEY, getTitle());
                    i.putExtra(Const.Intent.HX_USER_ID, bean.getUserImId());
                    i.putExtra(Const.Intent.HX_USER_NICK_NAME, bean.getNickName());
                    i.putExtra(Const.Intent.HX_USER_TO_CHAT_AVATAR, bean.getPortraitUrl());
                    startActivity(i);
                }
            });
            return convertView;

        }


        class ViewHolder {
            /**
             * 肖像url
             */
            private ImageView portraitl;
            /**
             * 网名
             */
            private TextView name;
            /**
             * 距离发布时间
             */
            private TextView pasttime;
            /**
             * 发布的文字
             */
            private TextView content;
            /**
             * 发布的图片
             */
            private ImageView photo;
            /**
             * 点赞次数
             */
            private ImageView likeimg;

            private TextView delete;
            private TextView likeNum;

            /**
             * 分享次数
             */
            private ImageView shareimg;
            private TextView shareNum;
            /**
             * 微信
             */
            private ImageView weixin;

            private LinearLayout likeCollect, shareCollect, weixinCollect;

        }
    }


}
