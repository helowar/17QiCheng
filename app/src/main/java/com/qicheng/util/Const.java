package com.qicheng.util;

import android.os.Environment;

import com.qicheng.QichengApplication;

public class Const {

    /**
     * For 友盟事件统计
     */

    public static class MobclickAgent{
        //新增行程
        public static final String EVENT_ADD_TRIP = "add_trip";
        //新增动态
        public static final String EVENT_ADD_ACTIVITY = "add_activity";
        //进入聊天
        public static final String EVENT_BEGIN_CHAT = "begin_chat";
        //public static final String EVENT_IMAGE_UPLOAD = "upload_image";
        //摇一摇
        public static final String EVENT_SHAKE_BENEFIT = "shake_benefit";
        //转发福利
        public static final String EVENT_TRANS_BENEFIT = "trans_benefit";
        //求福利
        public static final String EVENT_BEG_BENEFIT = "beg_benefit";
        //分享动态
        public static final String EVENT_SHARE_ACTIVITY = "share_activity";
        //赞
        public static final String EVENT_LIKE_ACTIVITY = "like_activity";
        //取消赞
        public static final String EVENT_UNLIKE_ACTIVITY = "unlike_activity";
    }

    /**
     * For 环信
     */
    public static class Easemob {
        public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
        public static final String GROUP_USERNAME = "item_groups";
        public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
        public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
        public static final String ACCOUNT_REMOVED = "account_removed";

        public static final String TO_USER_NICK = "to_nick_name";
        public static final String TO_USER_ID = "to_im_id";
        public static final String TO_USER_AVATAR = "to_avatar";
        public static final String FROM_USER_NICK = "from_nick_name";
        public static final String FROM_USER_AVATAR = "from_avatar";
        public static final String FROM_USER_ID = "from_im_id";

        public static final String BENEFIT_ICON_URL = "benefit_icon_url";
        public static final String BENEFIT_TITLE_TXT = "benefit_title_txt";
        public static final String BENEFIT_VALUE = "benefit_value";
    }

    /**
     * startyActivityForResult中的request code
     */
    public static class RequestCode {

        // 不关注，仅仅占位
        public static final int AnyOne = 0;

        // 从相册获取照片
        public static final int PhotoFromAlbum = 1000;

        // 拍照过去照片
        public static final int PhotoFromCamera = 1001;

        // 剪裁照片
        public static final int PhotoCrop = 1002;

        // 地图位置选择
        public static final int Location = 1003;

        // 发布友约
        public static final int PublishAppoint = 1004;

        // 友约详情
        public static final int AppointDetail = 1005;

        // 聊天
        public static final int Chat = 1006;
    }

    /**
     * intent中的extra的key
     */
    public static class Intent {
        /**
         * ouser的uid
         */
        public static final String UID = "uid";

        /**
         * ouser的昵称
         */
        public static final String NickName = "nickname";

        /**
         * 聊天对象的id
         */
        public static final String ChatId = "chatid";

        /**
         * 友约对象
         */
        public static final String Appoint = "appoint";

        /**
         * 友约发布者uid
         */
        public static final String PublisherUid = "publisheruid";

        /**
         * 友约内容
         */
        public static final String AppointContent = "aname";

        /**
         * 针对不同页有不同的含义
         */
        public static final String Type = "type";

        /**
         * 转到top时需要选择的页面
         */
        public static final String SwitchPage = "page";

        /**
         * 迁移到Traveller页面的查询类型
         */
        public static final String TRAVELLER_QUERY_TYPE = "traveller_query_type";

        /**
         * 迁移到Traveller页面的查询值
         */
        public static final String TRAVELLER_QUERY_VALUE = "traveller_query_value";

        /**
         * 迁移到Traveller页面的查询名称
         */
        public static final String TRAVELLER_QUERY_NAME = "traveller_query_name";

        /**
         * 用户头像URL
         */
        public static final String PORTRAIT_URL = "user_portrait_url";

        /**
         * 迁移到TripToDyn页面的查询类型
         */
        public static final String DYN_QUERY_TYPE = "dyn_query_type";

        /**
         * 迁移到TripToDyn页面的查询值
         */
        public static final String DYN_QUERY_VALUE = "dyn_query_value";

        /**
         * 迁移到TripToDyn页面的查询名称
         */
        public static final String DYN_QUERY_NAME = "dyn_query_name";

        public static final String HX_USER_ID = "userId";

        public static final String HX_USER_NICK_NAME = "nickName";

        public static final String HX_USER_TO_CHAT_AVATAR = "toChatAvatar";

        public static final String HX_NTF_TO_MAIN = "goMessage";

        /**
         * 迁移到用户详细信息页面的用户详细信息Key
         */
        public static final String USER_DETAIL_KEY = "com.qicheng.business.module.UserDetail";

        /**
         * 迁移到OriginalPicture页面的图片URLKey
         */
        public static final String ORIGINAL_PICTURE_URL_KEY = "original.picture.url";

        public static final String UPDATE_USER_INFORMATION_RESULT = "result_value";
        public static final String UPDATE_USER_INFORMATION_TYPE = "update_type";
        public static final String UPDATE_USER_INFORMATION_VALUE = "update_value";
        public static final String UPDATE_USER_INFORMATION_TITLE = "update_title";

        /**
         * 好友来源值Key
         */
        public static final String FRIEND_SOURCE_KEY = "friend.source";

        /**
         * 是否来源聊天Activity Key
         */
        public static final String IS_FROM_CHAT_ACTIVITY_KEY = "is.from.chat.activity";
        /**
         * 福利物品类型Key
         */
        public static final String TYPE_THING = "type_thing";
        /**
         * 福利类型id Key
         */
        public static final String BENEFIT_TYPE_ID = "benefit_type_id";

        /**
         * 福利详情Key
         */
        public static final String BENEFIT_DETAIL = "benefit_detail";

        /**
         * 福利实例详情Key
         */
        public static final String BENEFIT_ENTITY_FOR_DETAIL = "benefit_entity_for_detail";

        public static final String USER_ENTITY_FROM_CONTACT = "user_entity_from_contact";

        /**
         * 相册下标Key
         */
        public static final String ALBUM_ITEM_INDEX_KEY = "index";

        /**
         * 相册列表Key
         */
        public static final String ALBUM_LIST_KEY = "photos";

        /**
         * 更新密码来源key
         */
        public static final String UPDATE_PASSWORD_RESOURCE = "update_password_resource";
    }

    public static class DefaultValue {

        public static final int Age = -1;
        public static final int Time = -1;
        public static final int Distance = -1;
    }

    public static class SharedPreferenceKey {
        public static final String DefaultName = "qicheng";

        public static final String UserPreference = "user";

        public static final String TrainPreference = "trains";

        public static final String TripRelatedPreference = "tripsr";

        public static final String FirstStartup = "v1_0_first";
    }

    /**
     * 升级服务器地址
     */
//	public static final String UpgradeServer = "http://ouser.zhengre.com/upgrade";

    public static QichengApplication Application = null;

    // 调试
    public static final boolean FakeProtocol = false;

    public static final String WorkDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/QiCheng/";

    //服务端接口地址
    public static final String BASE_URL = "http://www.17qicheng.com:8080/";

    // 地图缩放
    public static final int MapZoom = 16;
    public static final int PoiResultCount = 25;
    public static final int PoiSearchRaduis = 5 * 1000;

    /**
     * time
     */
    public static final int MessageCountInterval = 5;
    public static final int MessageChatInterval = 10;

    /**
     * 消息会话间隔
     */
    public static final int SessionInterval = 15 * 60 * 1000;

    /**
     * 语音聊天的最小间隔
     */
    public static final int ChatVoiceMinDuring = 1000;

    /**
     * 两次点击back退出应用的最小应用
     */
    public static final int ExitAppClickBackMinInterval = 2000;

    /**
     * limit
     */
    /**
     * 获取藕丝每次获取的条数
     */
    public static final int OuserFetchCount = 20;

    /**
     * 所有友约每次获取的条数
     */
    public static final int AppointsFetchCount = 20;

    /**
     * 动态每次获取条数
     */
    public static final int TimelineFetchCount = 20;

    /**
     * 消息每次获取条数
     */
    public static final int TripListFetchCount = 10;

    /**
     * cache
     */
    /**
     * 照片下载最大失败次数
     */
    public static final int PhotoMaxTryTime = 3;

    /**
     * Activity ResultCode
     */
    public static class ActivityResultCode {
        /**
         * 响应结果码 1:成功
         */
        public static final int RESULT_SUCCESS = 1;

        /**
         * 响应结果码 -1:失败
         */
        public static final int RESULT_FAIL = -1;
    }

    /**
     * 服务端响应结果代码常量类
     */
    public static class ResponseResultCode {

        /**
         * 响应结果码 9999:内部异常
         */
        public static final int RESULT_EXCEPTION = 9999;

        /**
         * 响应结果码 0:成功
         */
        public static final int RESULT_SUCCESS = 0;

        /**
         * 响应结果码 1:失败
         */
        public static final int RESULT_FAIL = 1;

        /**
         * 响应结果码 2:非法调用
         */
        public static final int RESULT_ILLEGAL_CALL = 2;

        /**
         * 响应结果码 3:未登录
         */
        public static final int RESULT_NOT_LOGINED = 3;

        /**
         * 响应结果码 4:登录超时，请重登录
         */
        public static final int RESULT_LOGIN_TIMEOUT = 4;

        /**
         * 响应结果码 5:用户名为空
         */
        public static final int RESULT_USER_NAME_EMPTY = 5;

        /**
         * 响应结果码 6:用户密码为空
         */
        public static final int RESULT_PWD_EMPTY = 6;

        /**
         * 响应结果码 7:手机号码已存在
         */
        public static final int RESULT_CELL_NUM_EXIST = 7;

        /**
         * 响应结果码 8:手机号码不存在
         */
        public static final int RESULT_CELL_NUM_NOT_EXIST = 8;

        /**
         * 响应结果码 9:验证码错误
         */
        public static final int RESULT_VERIFY_CODE_ERROR = 9;

        /**
         * 响应结果码 10:验证码过期
         */
        public static final int RESULT_VERIFY_CODE_INVALID = 10;

        /**
         * 响应结果码 11:用户密码错误
         */
        public static final int RESULT_PWD_ERROR = 11;

        /**
         * 响应结果码 12:用户名不存在
         */
        public static final int RESULT_USER_NAME_NOT_EXIST = 12;

        /**
         * 响应结果码 13:该用户已无效
         */
        public static final int RESULT_USER_NAME_INVALID = 13;

        /**
         * 响应结果码 14:昵称已存在
         */
        public static final int RESULT_NICKNAME_EXIST = 14;

        /**
         * 响应结果码 15:没有响应数据，即响应体(body)不存在
         */
        public static final int RESULT_BODY_IS_NOT_EXIST = 15;

        /**
         * 响应结果码 16:没有权限
         */
        public static final int RESULT_NOT_PERMIT = 16;

        /**
         * 响应结果码 17:注册本系统账户成功，但注册IM账户失败
         */
        public static final int RESULT_REGISTER_IM_ACCOUNT_FAIL = 17;

        /**
         * 响应结果码 18:没有争抢到派发福利机会，请重试
         */
        public static final int RESULT_NO_GRAB = 18;

        /**
         * 响应结果码 19:福利已派发完了
         */
        public static final int RESULT_DISTRIBUTE_FINISHED = 19;

        /**
         * 响应结果码 20:该行程没有相关福利
         */
        public static final int RESULT_NO_BENEFIT = 20;
    }

    /**
     * 查询类型 -1：全站
     */
    public static final byte QUERY_TYPE_ALL = -1;

    /**
     * 查询类型 0：车站
     */
    public static final byte QUERY_TYPE_STATION = 0;

    /**
     * 查询类型 1：出发车站
     */
    public static final byte QUERY_TYPE_BEGIN = 1;

    /**
     * 查询类型 2：到达车站
     */
    public static final byte QUERY_TYPE_END = 2;

    /**
     * 查询类型 3：车次
     */
    public static final byte QUERY_TYPE_TRAIN = 3;

    /**
     * 查询类型 4：未上车
     */
    public static final byte QUERY_TYPE_NOT_ON_CAR = 4;

    /**
     * 查询类型 5：上车
     */
    public static final byte QUERY_TYPE_ON_CAR = 5;

    /**
     * 查询类型 6：下车
     */
    public static final byte QUERY_TYPE_OFF_CAR = 6;

    /**
     * 查询类型 7：附近
     */
    public static final byte QUERY_TYPE_NEAR = 7;

    /**
     * 查询类型 8：城市
     */
    public static final byte QUERY_TYPE_CITY = 8;

    /**
     * 查询类型 9：我的
     */
    public static final byte QUERY_TYPE_MY = 9;

    /**
     * 查询类型 10：来到城市
     */
    public static final byte QUERY_TYPE_COME_CITY = 10;

    /**
     * 查询类型 11：离开城市
     */
    public static final byte QUERY_TYPE_LEAVE_CITY = 11;

    /**
     * 查询类型 12：用户
     */
    public static final byte QUERY_TYPE_USER = 12;

    /**
     * 查询方向 0：往最新方向查询
     */
    public static final byte ORDER_BY_NEWEST = 0;

    /**
     * 查询方向 1：往最早方向查询
     */
    public static final byte ORDER_BY_EARLIEST = 1;

    /**
     * 图片加载器的滚动状态标识
     */
    public static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";

    /**
     * 图片加载器的滑动状态标识
     */
    public static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

    public static final int INDEX_TRIP = 1;
    public static final int INDEX_SOCIAL = 2;
    public static final int INDEX_ACTIVITY = 3;
    public static final int INDEX_MESSAGE = 4;
    public static final int INDEX_VOUCHER = 5;

    /**
     * 互动操作 0：赞
     */
    public static final byte INTERACT_ACTION_LIKED = 0;

    /**
     * 互动操作 1：取消赞
     */
    public static final byte INTERACT_ACTION_CANCEL = 1;

    /**
     * 互动操作 2：分享
     */
    public static final byte INTERACT_ACTION_SHARED = 2;

    /**
     * 互动操作 3：举报
     */
    public static final byte INTERACT_ACTION_REPORTED = 3;

    /**
     * 性别 -1:全部
     */
    public static final byte SEX_ALL = -1;

    /**
     * 性别 1:男
     */
    public static final byte SEX_MAN = 1;

    /**
     * 性别 0:女
     */
    public static final byte SEX_FEMALE = 0;

    /**
     * 更新用户个人信息枚举值
     */
    public static class UserUpdateCode {
        /**
         * 更新昵称
         */
        public static final int UPDATE_NICKNAME = 8;
        /**
         * 更新生日
         */
        public static final int UPDATE_BIRTHDAY = 1;
        /**
         * 更新头像
         */
        public static final int UPDATE_PORTRAIT_URL = 2;
        /**
         * 更新家乡
         */
        public static final int UPDATE_HOMETOWN = 4;
        /**
         * 更新学历
         */
        public static final int UPDATE_EDUCATION = 5;
        /**
         * 更新行业
         */
        public static final int UPDATE_INDUSTRY = 6;
        /**
         * 更新居住地
         */
        public static final int UPDATE_RESIDENCE = 7;

    }

    /**
     * ID类型 0：用户ID
     */
    public static final byte ID_TYPE_USER_ID = 0;

    /**
     * ID类型 1：用户IM账户ID
     */
    public static final byte ID_TYPE_USER_IM_ID = 1;

    /**
     * 修改密码
     */
    public static final byte UPDATE_PWD_FROM_MODIFY = 0;
    /**
     * 忘记密码
     */
    public static final byte UPDATE_PWD_FROM_FORGET = 1;

    /**
     * 操作类型 0:注册
     */
    public static final byte ACTION_TYPE_REGISTER = 0;

    /**
     * 操作类型 1:找回密码
     */
    public static final byte ACTION_TYPE_FIND = 1;

    /**
     * 操作类型 2:修改手机号码
     */
    public static final byte ACTION_TYPE_MODIFY = 2;


}
