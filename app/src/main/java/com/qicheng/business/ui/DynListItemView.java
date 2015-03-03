package com.qicheng.business.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class DynListItemView extends RelativeLayout {
	
	public DynListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DynListItemView(Context context) {
		super(context);
	}

//	public void setValue(MainMenu menu) {
//		TextView icon = (TextView) findViewById(R.id.left_icon);
//		icon.setBackgroundResource(menu.getIcon());
//		TextView desc = (TextView) findViewById(R.id.desc);
//		int descDrawableLeft = menu.getDescDrawableLeft();
//		if (descDrawableLeft != 0) {
//			desc.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(descDrawableLeft), null, null, null);
//		}else{
//			desc.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//		}
//		int descDrawablePadding = menu.getDescDrawablePadding();
//		if (descDrawablePadding != 0) {
//			desc.setCompoundDrawablePadding((int)getContext().getResources().getDimension(descDrawablePadding));
//		}else{
//			desc.setCompoundDrawablePadding(0);
//		}
//		desc.setText(menu.getDesc());
//		View line = findViewById(R.id.line);
//		if (menu.isShowLine()) {
//			line.setVisibility(VISIBLE);
//		} else {
//			line.setVisibility(GONE);
//		}
//		TextView valueView = (TextView) findViewById(R.id.value);
//		if (menu.getValueDrawableBg() != 0) {
//			valueView.setText(menu.getValue());
//			valueView.setBackgroundResource(menu.getValueDrawableBg());
//			valueView.setVisibility(VISIBLE);
//		}else {
//			valueView.setVisibility(GONE);
//		}
//	}
}
