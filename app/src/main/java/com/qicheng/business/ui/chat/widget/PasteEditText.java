/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */
package com.qicheng.business.ui.chat.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView.BufferType;

import com.qicheng.R;
import com.qicheng.business.ui.ChatActivity;
import com.qicheng.business.ui.chat.activity.AlertDialog;


/**
 * 自定义的textview，用来处理复制粘贴的消息
 *
 */
public class PasteEditText extends EditText{
    private Context context;
    
    
    public PasteEditText(Context context) {
        super(context);
        this.context = context;
    }

    public PasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public PasteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
    
    @SuppressLint("NewApi")
	@Override
    public boolean onTextContextMenuItem(int id) {
        if(id == android.R.id.paste){
            ClipboardManager clip = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clip == null || clip.getText() == null) {
                return false;
            }
            String text = clip.getText().toString();
            if(text.startsWith(ChatActivity.COPY_IMAGE)){
//                intent.setDataAndType(Uri.fromFile(new File("/sdcard/mn1.jpg")), "image/*");     
                text = text.replace(ChatActivity.COPY_IMAGE, "");
                Intent intent = new Intent(context,AlertDialog.class);
                String str = context.getResources().getString(R.string.Send_the_following_pictures);
                intent.putExtra("title", str);
                intent.putExtra("forwardImage", text);
                intent.putExtra("cancel", true);
                ((Activity)context).startActivityForResult(intent,ChatActivity.REQUEST_CODE_COPY_AND_PASTE);
//                clip.setText("");
            }
        }
        return super.onTextContextMenuItem(id);
    }
    
    
    
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if(!TextUtils.isEmpty(text) && text.toString().startsWith(ChatActivity.COPY_IMAGE)){
            setText("");
        }
//        else if(!TextUtils.isEmpty(text)){
//        	setText(SmileUtils.getSmiledText(getContext(), text),BufferType.SPANNABLE);
//        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
    

}
