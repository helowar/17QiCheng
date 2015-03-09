package com.qicheng.business.ui;


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
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.DynLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.DynEventAargs;
import com.qicheng.business.logic.event.StationEventAargs;
import com.qicheng.business.module.City;
import com.qicheng.business.module.Dyn;
import com.qicheng.business.module.Location;
import com.qicheng.business.module.Train;
import com.qicheng.business.module.TrainStation;
import com.qicheng.business.ui.component.DynListView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.util.Const;

import java.io.Serializable;
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
    private List<Dyn> dynSearchList = new ArrayList<Dyn>();
    /*城市列表*/
    private List<City> cityList;
    private String[] cities;
    private List<Train> trainList;
    private String[] trains;
    private List<TrainStation> stationList;
    private String[] stations;
    private String[] cityCodes;
    /*搜索条件*/
    private DynSearch dynSearch = new DynSearch();
    private List<Dyn> newData;
    /*动态的类型*/
    private String title;
    private String cityCode;

    private static final int ADD_SUCCESS = 0;

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

        searchList = (GridView) view.findViewById(R.id.activity_search_grid_view);
        searchList.setAdapter(new DynSearchGridViewAdapter(getActivity()));
        /*为每个item绑定点击事件监听器*/
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                isVisible = View.GONE;
                switch (position) {
                    /*当position的位置为0时是按城市搜索最新动态*/
                    case 0:
                        cityList = Cache.getInstance().getTripRelatedCityCache();
                        searchByCity();
                        dynSearchList.clear();
                        break;
                    /*当position的位置为1时是按车次搜索最新动态*/
                    case 1:
                        trainList = Cache.getInstance().getTripRelatedTrainCache();
                        searchByTrain();
                        dynSearchList.clear();
                        cityCode = null;
                        break;
                    /*当position的位置为2时是按最新搜索最新动态*/
                    case 2:
                        dynSearchList = new ArrayList<Dyn>();
                        dynSearch = new DynSearch();
                        title = "最新";
                        getActivity().invalidateOptionsMenu();
                        getDynList(dynSearch);
                        searchLinearLayout.setVisibility(View.GONE);
                        cityCode = null;
                        break;
                    /*当position的位置为3时是按附近搜索最新动态*/
                    case 3:
                        title = "最近";
                        /*配置当前搜索条件*/
                        dynSearch = new DynSearch();
                        dynSearch.setQueryType(Const.QUERY_TYPE_NEAR);
                        Location location = Cache.getInstance().getUser().getLocation();
                        dynSearch.setQueryValue(location.getLongitude() + '|' + location.getLatitude());
                        getActivity().invalidateOptionsMenu();
                        getDynList(dynSearch);
                        searchLinearLayout.setVisibility(View.GONE);
                        cityCode = null;
                        break;
                    default:
                        searchLinearLayout.setVisibility(View.GONE);
                        break;
                }

            }
        });
        return view;
    }


    /*搜索GridView是否存在的标志，默认不存在*/
    private int isVisible = View.GONE;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return super.onOptionsItemSelected(item);
            case R.id.activity_add:
                Intent intent = new Intent(getActivity(), DynPublishActivity.class);
                if (dynSearch.queryType != null) {
                    Bundle bundle = new Bundle();
                    bundle.putByte("query_type", dynSearch.getQueryType());
                    bundle.putString("query_value", dynSearch.getQueryValue());
                    intent.putExtras(bundle);
                }
                startActivityForResult(intent, 0);
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
                break;
            case R.id.activity_title:
                if (cityCode != null) {
                    searchByTrainStation();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    /*添加动态完成后，捕获返回值后的操作*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("result", requestCode + " " + resultCode);
        switch (resultCode) {
            case ADD_SUCCESS:
                getDynList(dynSearch);
                break;
        }
        if (data != null) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 通过车次作为搜索条件搜索
     */
    public void searchByTrain() {
        if (trainList != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("请选择车次");
            //    指定下拉列表的显示数据
            //    设置一个下拉的列表选择项
            trains = new String[trainList.size()];
            for (int i = 0; i < trainList.size(); i++) {
                trains[i] = trainList.get(i).getTrainCode();
            }
            builder.setItems(trains, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    index = which;
                    Toast.makeText(getActivity(), "选择的车次为：" + trains[which], Toast.LENGTH_SHORT).show();
                    title = trains[which];
                    getActivity().invalidateOptionsMenu();
                    searchLinearLayout.setVisibility(View.GONE);
                    dynSearch = new DynSearch();
                    dynSearch.setQueryType(Const.QUERY_TYPE_TRAIN);
                    dynSearch.setQueryValue(trainList.get(which).getTrainCode());
                    getDynList(dynSearch);
                }
            });
            builder.show();
        } else {
            Alert.Toast("您当前没有与行程相关的车次");
        }
    }


    /**
     * 通过城市作为搜索条件搜索
     */
    public void searchByCity() {
        if (cityList != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("请选择城市");
            //    指定下拉列表的显示数据
            //    设置一个下拉的列表选择项
            cities = new String[cityList.size()];
            cityCodes = new String[cityList.size()];
            for (int i = 0; i < cityList.size(); i++) {
                cities[i] = cityList.get(i).getCityName();
                cityCodes[i] = cityList.get(i).getCityCode();
            }
            builder.setItems(cities, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    index = which;
                    Toast.makeText(getActivity(), "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
                    title = cities[which];
                    cityCode = cityCodes[which];
                    getActivity().invalidateOptionsMenu();
                    searchLinearLayout.setVisibility(View.GONE);
                    dynSearch = new DynSearch();
                    dynSearch.setQueryType(Const.QUERY_TYPE_CITY);
                    dynSearch.setQueryValue(cityList.get(which).getCityCode());
                    getDynList(dynSearch);
                    stationList = Cache.getInstance().getTripRelatedStationCache(cityCode);
                    if (stationList == null) {
                        getStationList(cityCode);
                    }
                }
            });
            builder.show();
        } else {
            Alert.Toast("您当前没有与行程相关的城市");
        }
    }


    /**
     * 通过车站作为搜索条件搜索
     */
    public void searchByTrainStation() {
        if (stationList != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("请选择车站");
            //    指定下拉列表的显示数据
            //    设置一个下拉的列表选择项
            stations = new String[stationList.size()];
            for (int i = 0; i < stationList.size(); i++) {
                stations[i] = stationList.get(i).getStationName();
            }
            builder.setItems(stations, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    index = which;
                    Toast.makeText(getActivity(), "选择的车站为：" + stations[which], Toast.LENGTH_SHORT).show();
                    title = stations[which];
                    cityCode = null;
                    getActivity().invalidateOptionsMenu();
                    searchLinearLayout.setVisibility(View.GONE);
                    dynSearch = new DynSearch();
                    dynSearch.setQueryType(Const.QUERY_TYPE_STATION);
                    dynSearch.setQueryValue(stationList.get(which).getStationCode());
                    getDynList(dynSearch);
                }
            });
            builder.show();
        } else {
            Alert.Toast("该城市没有车站");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.getItem(0);
        item.setTitle(title);
    }


    public void getStationList(final String cityCode) {
        DynLogic dynLogic = (DynLogic) LogicFactory.self().get(LogicFactory.Type.Dyn);
        dynLogic.getStationList(cityCode, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                StationEventAargs stationEventAargs = (StationEventAargs) args;
                OperErrorCode errCode = stationEventAargs.getErrCode();
                switch (errCode) {
                    case Success:
                        stationList = stationEventAargs.getStationList();
                        Log.d("StatinList", stationList.toString());
                        break;
                }
            }
        }));
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
                        newData = dynEventAargs.getDynList();
                        if (type == null) {
                            refreshSearchList(orderBy);
                        } else {
                            switch (type) {
                                case Const.QUERY_TYPE_CITY:
                                    refreshSearchList(orderBy);
                                    break;
                                case Const.QUERY_TYPE_TRAIN:
                                    refreshSearchList(orderBy);
                                    break;
                            }
                        }
                        break;
                    case NoDataFound:
                        Alert.handleErrCode(errCode);
                        Alert.Toast(getResources().getString(R.string.activity_noMoreData));
                        listAdapter.notifyDataSetChanged();
                        break;
                    default:
                        Alert.handleErrCode(errCode);
                        Alert.Toast(getResources().getString(R.string.activity_reject));
                        break;
                }
            }
        }));


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
                        Alert.handleErrCode(errCode);
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
                holder.photo.setVisibility(View.VISIBLE);
            }else {
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

            holder.likeimg.setOnClickListener(new View.OnClickListener() {
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
                    //分享后分享数字加一
                    String id = bean.getActivityId();
                    holder.shareNum.setText((Integer.valueOf(holder.shareNum.getText().toString()) + 1) + "");
                    byte action = Const.INTERACT_ACTION_SHARED;
                    interact(id, action);
                    bean.setSharedNum((Integer.valueOf(holder.shareNum.getText().toString())));
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
    public class DynSearch implements Serializable {

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
