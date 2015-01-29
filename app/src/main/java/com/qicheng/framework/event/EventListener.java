package com.qicheng.framework.event;

public interface EventListener {
	void onEvent(EventId id, EventArgs args);
}
