/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */
package com.qicheng.business.ui.chat.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.qicheng.business.image.ImageManager;
import com.qicheng.R;
import com.qicheng.business.module.User;

/**
 * 简单的好友Adapter实现
 *
 */
public class ContactAdapter extends ArrayAdapter<User>  implements SectionIndexer{

	List<String> list;
	List<User> userList;
	List<User> copyUserList;
	private LayoutInflater layoutInflater;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
	private int res;
	public MyFilter myFilter;

	public ContactAdapter(Context context, int resource, List<User> objects) {
		super(context, resource, objects);
		this.res = resource;
		this.userList=objects;
		copyUserList = new ArrayList<User>();
		copyUserList.addAll(objects);
		layoutInflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = layoutInflater.inflate(res, null);
		}
		
		ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
		TextView unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
		TextView nameTextview = (TextView) convertView.findViewById(R.id.name);
		TextView tvHeader = (TextView) convertView.findViewById(R.id.header);
        TextView tvSource = (TextView) convertView.findViewById(R.id.source);
		User user = getItem(position);
		if(user == null)
			Log.d("ContactAdapter", position + "");
		//设置nick，demo里不涉及到完整user，用username代替nick显示
		String username = user.getNickName();
		String header = user.getHeader();
		if (position == 0 || header != null && !header.equals(getItem(position - 1).getHeader())) {
			if ("".equals(header)) {
				tvHeader.setVisibility(View.GONE);
			} else {
				tvHeader.setVisibility(View.VISIBLE);
				tvHeader.setText(header);
			}
		} else {
			tvHeader.setVisibility(View.GONE);
		}
//		//显示申请与通知item
//		if(username.equals(Constant.NEW_FRIENDS_USERNAME)){
//			nameTextview.setText(user.getNick());
//			avatar.setImageResource(R.drawable.new_friends_icon);
//			if(user.getUnreadMsgCount() > 0){
//				unreadMsgView.setVisibility(View.VISIBLE);
//				unreadMsgView.setText(user.getUnreadMsgCount()+"");
//			}else{
//				unreadMsgView.setVisibility(View.INVISIBLE);
//			}
//		}else if(username.equals(Constant.GROUP_USERNAME)){
//			//群聊item
//			nameTextview.setText(user.getNick());
//			avatar.setImageResource(R.drawable.groups_icon);
//		}else{
			nameTextview.setText(username);
			if(unreadMsgView != null)
				unreadMsgView.setVisibility(View.INVISIBLE);
			ImageManager.displayPortrait(user.getPortraitURL(),avatar);
//		}
        tvSource.setText(user.getSource());
		return convertView;
	}
	
	@Override
	public User getItem(int position) {
		return super.getItem(position);
	}
	
	@Override
	public int getCount() {
		return super.getCount();
	}

	public int getPositionForSection(int section) {
		return positionOfSection.get(section);
	}

	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}
	
	@Override
	public Object[] getSections() {
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		list = new ArrayList<String>();
		list.add(getContext().getString(R.string.search_new));
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 1; i < count; i++) {

			String letter = getItem(i).getHeader();
			System.err.println("contactadapter getsection getHeader:" + letter + " name:" + getItem(i).getUserImId());
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}
	
	@Override
	public Filter getFilter() {
		if(myFilter==null){
			myFilter = new MyFilter(userList);
		}
		return myFilter;
	}
	
	private class  MyFilter extends Filter{
		List<User> mList=null;
		
		public MyFilter(List<User> myList) {
			super();
			this.mList = myList;
		}

		@Override
		protected synchronized FilterResults  performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			if(mList==null){
				mList = new ArrayList<User>();
			}
			if(prefix==null || prefix.length()==0){
				results.values = mList;
				results.count = mList.size();
			}else{
				String prefixString = prefix.toString();
				final int count = mList.size();
				final ArrayList<User> newValues = new ArrayList<User>();
				for(int i=0;i<count;i++){
					final User user = mList.get(i);
					String username = user.getNickName();
					
//					EMConversation conversation = EMChatManager.getInstance().getConversation(username);
//					if(conversation != null){
//						username = conversation.getUserName();
//					}
					
					if(username.contains(prefixString)){
						newValues.add(user);
					}
					else{
						 final String[] words = username.split(" ");
	                     final int wordCount = words.length;
	
	                     // Start at index 0, in case valueText starts with space(s)
	                     for (int k = 0; k < wordCount; k++) {
	                         if (words[k].contains(prefixString)) {
	                             newValues.add(user);
	                             break;
	                         }
	                     }
					}
				}
				results.values=newValues;
				results.count=newValues.size();
			}
			return results;
		}

		@Override
		protected synchronized void publishResults(CharSequence constraint,
				FilterResults results) {
			userList.clear();
			userList.addAll((List<User>)results.values);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
	
	
	
	

}
