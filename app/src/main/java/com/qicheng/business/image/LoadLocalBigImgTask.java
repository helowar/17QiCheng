/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */
package com.qicheng.business.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.easemob.util.ImageUtils;
import com.qicheng.R;
import com.qicheng.business.ui.chat.utils.ImageCache;
import com.qicheng.business.ui.component.PhotoView;

public class LoadLocalBigImgTask extends AsyncTask<Void, Void, Bitmap> {

	private ProgressBar pb;
	private PhotoView photoView;
	private String path;
	private int width;
	private int height;
	private Context context;

	public LoadLocalBigImgTask(Context context,String path, PhotoView photoView,
			ProgressBar pb, int width, int height) {
		this.context = context;
		this.path = path;
		this.photoView = photoView;
		this.pb = pb;
		this.width = width;
		this.height = height;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		int degree = ImageUtils.readPictureDegree(path);
		if (degree != 0) {
			pb.setVisibility(View.VISIBLE);
			photoView.setVisibility(View.INVISIBLE);
		} else {
			pb.setVisibility(View.INVISIBLE);
			photoView.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		Bitmap bitmap = ImageUtils.decodeScaleImage(path, width, height);
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		pb.setVisibility(View.INVISIBLE);
		photoView.setVisibility(View.VISIBLE);
		if (result != null)
			ImageCache.getInstance().put(path, result);
		else
			result = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.signin_local_gallry);
		photoView.setImageBitmap(result);
	}
}
