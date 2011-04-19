package com.gwt.ss.sharedservice.client;

import com.google.gwt.core.client.Scheduler;

public interface ScheduledCommand<T,E> extends Scheduler.ScheduledCommand{
    void execute(T result);
    void onException(E e);
}
