/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui.chat.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.qicheng.R;
import com.qicheng.business.logic.ContactLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.ContactEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.business.ui.ChatActivity;
import com.qicheng.business.ui.chat.widget.ContactAdapter;
import com.qicheng.business.ui.chat.widget.Sidebar;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.UIEventListener;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.util.Const;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ContactActivity extends BaseActivity {
    private ContactAdapter adapter;
    private List<User> contactList;
    private ListView listView;
    private boolean hidden;
    private Sidebar sidebar;
    private InputMethodManager inputMethodManager;
    private List<String> blackList;
    ImageButton clearSearch;
    EditText query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = (ListView) findViewById(R.id.list);
        sidebar = (Sidebar) findViewById(R.id.sidebar);
        sidebar.setListView(listView);
        //黑名单列表
//        blackList = EMContactManager.getInstance().getBlackListUsernames();
        contactList = new ArrayList<User>();
        // 获取设置contactlist
        getContactList();

        //搜索框
        query = (EditText) findViewById(R.id.query);
        String strSearch = getResources().getString(R.string.search);
        query.setHint(strSearch);
        clearSearch = (ImageButton)findViewById(R.id.search_clear);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        // 设置adapter
        adapter = new ContactAdapter(getActivity(), R.layout.row_contact, contactList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String username = adapter.getItem(position).getUsername();
//                if (Constant.NEW_FRIENDS_USERNAME.equals(username)) {
//                    // 进入申请与通知页面
//                    User user = DemoApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
//                    user.setUnreadMsgCount(0);
//                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
//                } else if (Constant.GROUP_USERNAME.equals(username)) {
//                    // 进入群聊列表页面
//                    startActivity(new Intent(getActivity(), GroupsActivity.class));
//                } else {
                    // 直接进入聊天页面
                User u = adapter.getItem(position);
                if(getIntent().getBooleanExtra(Const.Intent.IS_FROM_CHAT_ACTIVITY_KEY,true)) {
                    Intent i = new Intent();
                    i.setClass(getActivity(), ChatActivity.class);
                    i.putExtra(Const.Intent.HX_USER_ID, u.getUserImId());
                    i.putExtra(Const.Intent.HX_USER_NICK_NAME, u.getNickName());
                    i.putExtra(Const.Intent.HX_USER_TO_CHAT_AVATAR, u.getPortraitURL());
                    startActivity(i);
                    finish();
                }else {
                    setResult(RESULT_OK,getIntent().putExtra(Const.Intent.USER_ENTITY_FROM_CONTACT,u));
                    finish();
                }
//                }
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getActivity().getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 长按前两个不弹menu
        if (((AdapterView.AdapterContextMenuInfo) menuInfo).position > 2) {
            getActivity().getMenuInflater().inflate(R.menu.context_contact_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //TODO 待实现
//        if (item.getItemId() == R.id.delete_contact) {
//            User tobeDeleteUser = adapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
//            // 删除此联系人
//            deleteContact(tobeDeleteUser);
////            // 删除相关的邀请消息
////            InviteMessgeDao dao = new InviteMessgeDao(getActivity());
////            dao.deleteMessage(tobeDeleteUser.getUsername());
//            return true;
//        }else if(item.getItemId() == R.id.add_to_blacklist){
//            User user = adapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
//            moveToBlacklist(user);
//            return true;
//        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        ActionBar bar = getActivity().getActionBar();
        if(bar!=null){
            bar.setDisplayHomeAsUpEnabled(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

    /**
     * 删除联系人
     *
     * @param tobeDeleteUser
     */
    public void deleteContact(final User tobeDeleteUser) {
        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUserImId());
                    //TODO 调用删除用户logic
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            adapter.remove(tobeDeleteUser);
                            adapter.notifyDataSetChanged();

                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Alert.Toast(st2 + e.getMessage());
                        }
                    });

                }

            }
        }).start();

    }

    /**
     * 把user移入到黑名单
     */
    private void moveToBlacklist(final User user){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st1 = getResources().getString(R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        //TODO 加黑名单
        new Thread(new Runnable() {
            public void run() {
                try {
                    //加入到黑名单
                    EMContactManager.getInstance().addUserToBlackList(user.getUserImId(),true);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Alert.Toast(st2);
                            refresh();
                        }
                    });
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Alert.Toast(st3);
                        }
                    });
                }
            }
        }).start();

    }

    // 刷新ui
    public void refresh() {
        try {
            // 可能会在子线程中调到这方法
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getContactList();
                    adapter.notifyDataSetChanged();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     */
    private void getContactList() {

        //获取通讯录
        ContactLogic logic = (ContactLogic)LogicFactory.self().get(LogicFactory.Type.Contact);
        logic.getContactList(createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                ContactEventArgs contactEventArgs = (ContactEventArgs)args;
                OperErrorCode errorCode = contactEventArgs.getErrCode();
                if(errorCode==OperErrorCode.Success){
                    contactList.clear();
                    contactList.addAll(contactEventArgs.getContactList());
                    adapter.notifyDataSetChanged();
                    Collections.sort(contactList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return lhs.getNickName().compareTo(rhs.getNickName());
                        }
                    });
                }
            }
        }));
        startLoading();
        // 排序
//        // 加入"申请与通知"和"群聊"
//        contactList.add(0, users.get(Constant.GROUP_USERNAME));
//        // 把"申请与通知"添加到首位
//        contactList.add(0, users.get(Constant.NEW_FRIENDS_USERNAME));
    }

    void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
