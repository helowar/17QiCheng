package com.qicheng.framework.logic;

import android.os.Handler;

import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventCenter;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

public class BaseLogic {
	
	public interface Factory {
		BaseLogic create();
	}
	
	protected Handler mHandler = new Handler();

	protected void fireStatusEvent(EventId eventId) {
		fireEvent(eventId, new StatusEventArgs(OperErrorCode.Success));
	}

	protected void fireStatusEvent(EventId eventId, OperErrorCode errCode) {
		fireEvent(eventId, new StatusEventArgs(errCode));
	}
	
	protected void fireEvent(final EventId eventId, final EventArgs args) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				EventCenter.self().fireEvent(eventId, args);
			}
		});
	}
	
	protected void fireStatusEvent(EventListener listener) {
		fireEvent(listener, new StatusEventArgs(OperErrorCode.Success));
	}

	protected void fireStatusEvent(EventListener listener, OperErrorCode errCode) {
		fireEvent(listener, new StatusEventArgs(errCode));
	}
	
	protected void fireEvent(final EventListener listener, final EventArgs args) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				listener.onEvent(EventId.eNone, args);
			}
		});
	}
}
