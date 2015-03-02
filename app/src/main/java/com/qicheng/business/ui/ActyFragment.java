package com.qicheng.business.ui;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.DynLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.DynEventAargs;
import com.qicheng.business.module.Dyn;
import com.qicheng.business.module.DynBean;
import com.qicheng.business.ui.component.DynListView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.util.Const;

import java.util.ArrayList;
import java.util.List;

public class ActyFragment extends BaseFragment {


    private View view = null;
    /*动态的ListView视图*/
    private DynListView dynListView = null;
    /*动态的ListView的Adapter*/
    private DynListViewAdapter listAdapter;
    /*搜索选线的GridView视图*/
    private GridView searchList = null;
    private LinearLayout searchLinearLayout;
    private View searchView;
    private int index = -1;
    private List<DynBean> dynList = DynModel.getDynList();
    private List<Dyn> dynSearchList = new ArrayList<Dyn>();
    private String[] cities = {"广州", "上海", "北京", "香港", "澳门"};
    /*搜索条件*/
    private DynSearch dynSearch = new DynSearch();
    ;
    private ArrayList<Dyn> dynArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_acty, container, false);
        searchView = inflater.inflate(R.layout.dyn_search_edit_layout, container, false);
        dynListView = (DynListView) view.findViewById(R.id.dynlist);
        listAdapter = new DynListViewAdapter(getActivity().getApplicationContext());
        getDynList(dynSearch);
        dynListView.setAdapter(listAdapter);

        dynListView.setonRefreshListener(new DynListView.OnRefreshListener() {

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
                        //list.add("刷新后添加的内容");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        listAdapter.notifyDataSetChanged();
                        dynListView.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }

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

        searchList = (GridView) view.findViewById(R.id.activity_search_grid_view);
        searchList.setAdapter(new DynSearchGridViewAdapter(getActivity()));
        /*为每个item绑定点击事件监听器*/
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    /*当position的位置为0时是按城市搜索最新动态*/
                    case 0:
                        searchByCity();
                        dynSearch = new DynSearch();
                        dynSearch.setQueryType(Const.QUERY_TYPE_CITY);
                        dynSearch.setOrderBy(Const.ORDER_BY_NEWEST);
                        dynSearch.setQueryValue("城市代码");
                        break;
                    /*当position的位置为1时是按车次搜索最新动态*/
                    case 1:
                        searchLinearLayout.setVisibility(View.GONE);
                        break;
                    /*当position的位置为2时是按最新搜索最新动态*/
                    case 2:
                        dynSearchList = new ArrayList<Dyn>();
                        dynSearch = new DynSearch();
                        getDynList(dynSearch);
                        searchLinearLayout.setVisibility(View.GONE);
                        break;
                    /*当position的位置为3时是按附近搜索最新动态*/
                    case 3:
                        searchLinearLayout.setVisibility(View.GONE);
                        break;
                    default:
                        searchLinearLayout.setVisibility(View.GONE);
                        break;
                }

            }
        });
        return view;
    }

    /*获取更多动态*/

    private void loadMoreData(int flag) {
        if (flag == 0) {
            dynList.addAll(0, DynModel.getDynList());
        } else if (flag == 1) {
            dynList.addAll(DynModel.getDynList());
        }
        listAdapter.notifyDataSetChanged();
    }

    /*搜索GridView是否存在的标志，默认不存在*/
    private int isVisible = View.GONE;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return super.onOptionsItemSelected(item);
            case R.id.activity_add:
                startActivity(new Intent(getActivity(), DynPublishActivity.class));
                break;
            case R.id.activity_search:
                searchLinearLayout = (LinearLayout) view.findViewById(R.id.activity_search_list);
                switch (isVisible) {
                    case View.GONE:
                        searchLinearLayout.setVisibility(View.VISIBLE);
                        isVisible = View.VISIBLE;
                        break;
                    case View.VISIBLE:
                        searchLinearLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        break;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }


    /**
     * 通过城市作为搜索条件搜索
     */
    public void searchByCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请选择城市");
        //    指定下拉列表的显示数据
        //    设置一个下拉的列表选择项
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                index = which;
                ActionBar actionBar = getActivity().getActionBar();
                Toast.makeText(getActivity(), "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
                getActivity().invalidateOptionsMenu();
                searchLinearLayout.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.getItem(0);
        switch (index) {
            case 0:
                item.setTitle(cities[0]);
                break;
            case 1:
                item.setTitle(cities[1]);
                break;
            case 2:
                item.setTitle(cities[2]);
                break;
            case 3:
                item.setTitle(cities[3]);
                break;
            case 4:
                item.setTitle(cities[4]);
                break;

        }
    }

    /*获取动态列表*/
    public void getDynList(final DynSearch dynSearchCondition) {
        DynLogic dynLogic = (DynLogic) LogicFactory.self().get(LogicFactory.Type.Dyn);
        dynLogic.getDynList(dynSearchCondition, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                Log.d("..", dynSearchCondition.toString());
                DynEventAargs dynEventAargs = (DynEventAargs) args;
                OperErrorCode errCode = dynEventAargs.getErrCode();
                int orderBy = dynSearchCondition.getOrderBy();
                Byte type = dynSearchCondition.getQueryType();
                switch (errCode) {
                    case Success:
                        List<Dyn> newData = dynEventAargs.getDynList();

                        if (dynSearchList.size() == 0 && type == null && orderBy == Const.ORDER_BY_NEWEST) {
                            /**
                             * 场景刚刚进入动态列表，当动态的列表为空，并且查询类型为空，并且查询方向为最新
                             */
                            dynSearchList = newData;
                            listAdapter.setDataList(dynSearchList);
                            listAdapter.notifyDataSetChanged();
                        } else if (dynSearchList.size() > 0 && type == null && orderBy == Const.ORDER_BY_NEWEST) {
                            /*
                            场景最新动态列表，上拉刷新
                             */
                            dynSearchList.addAll(0, newData);
                            listAdapter.notifyDataSetChanged();
                        } else if (dynSearchList.size() > 0 && type == null && orderBy == Const.ORDER_BY_EARLIEST) {
                            /*
                            场景：下拉刷新动态列表
                             */
                            dynSearchList.addAll(newData);
                            listAdapter.notifyDataSetChanged();
                        }

                        break;
                    case NoDataFound:
                        Alert.handleErrCode(errCode);
                        Alert.Toast(getResources().getString(R.string.activity_noMoreData));
                        break;
                    default:
                        Alert.handleErrCode(errCode);
                        Alert.Toast(getResources().getString(R.string.activity_reject));
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
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
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Dyn bean = dataList.get(position);
            String userPortrait = bean.getPortraitUrl();
            ImageManager.displayPortrait(userPortrait, holder.portraitl);
            holder.name.setText(bean.getNickName());
            holder.pasttime.setText(DateTimeUtil.getTimeInterval(bean.getCreateTime()));
            String thumbnailUrl = bean.getThumbnailUrl();
            if (thumbnailUrl != null) {
//            holder.photo.setImageURI();
                ImageManager.displayPortrait(thumbnailUrl, holder.photo);
            }
            holder.content.setText(bean.getContent());

            Integer likeNum = bean.getLikedNum();
            Integer shareNum = bean.getSharedNum();
            holder.likeNum.setText(likeNum.toString());
            holder.shareNum.setText(shareNum.toString());
            holder.likeimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.likeNum.setText((Integer.valueOf(holder.likeNum.getText().toString()) + 1) + "");

                }
            });

            holder.shareimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    if (bean.getThumbnailUrl() != null) {
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bean.getThumbnailUrl());
                        shareIntent.setType("image/*");
                        shareIntent.putExtra("sms_body", bean.getContent());
                    } else {
                        shareIntent.setType("text/plain");
                    }
                    shareIntent.putExtra(Intent.EXTRA_TEXT, bean.getContent());
                    mContext.startActivity(Intent.createChooser(shareIntent, "").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
            holder.weixin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
        }
    }

    /**
     * 动态搜索类
     */
    public class DynSearch {
        private int orderBy;
        private int orderNum;
        private int size = 10;
        private Byte queryType;
        private String queryValue;

        public int getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(int orderBy) {
            this.orderBy = orderBy;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public Byte getQueryType() {
            return queryType;
        }

        public void setQueryType(Byte queryType) {
            this.queryType = queryType;
        }

        public String getQueryValue() {
            return queryValue;
        }

        public void setQueryValue(String queryValue) {
            this.queryValue = queryValue;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("DynSearch{");
            sb.append("orderBy=").append(orderBy);
            sb.append(", orderNum=").append(orderNum);
            sb.append(", size=").append(size);
            sb.append(", queryType=").append(queryType);
            sb.append(", queryValue='").append(queryValue).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /*动态搜索的GridView的Adapter*/
    private class DynSearchGridViewAdapter extends BaseAdapter {

        private Context context;
        private Integer[] imgs = {
                R.drawable.ic_city, R.drawable.ic_channel,
                R.drawable.ic_place, R.drawable.ic_meet
        };

        DynSearchGridViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DisplayMetrics dm = new DisplayMetrics();
            //取得窗口属性
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            //窗口的宽度
            int screenWidth = dm.widthPixels;
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / 4, screenWidth / 4));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(4, 4, 4, 4);//设置间距
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(imgs[position]);//为ImageView设置图片资源
            return imageView;
        }
    }


}
