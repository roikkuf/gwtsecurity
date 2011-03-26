package com.gwt.ss.sharedservice.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwt.ss.client.GwtSecurityException;

public class ServiceAction {
    private String name;
    private RemoteAsync service;

    public ServiceAction(RemoteAsync service,String name) {
        this.service = service;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void doGreeting(String name,final ScheduledCommand<String,GwtSecurityException> command){
        service.greetServer(name, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                if(caught instanceof GwtSecurityException)
                    command.onException((GwtSecurityException)caught);
                else
                    GWT.log(caught.getMessage(), caught);
            }

            @Override
            public void onSuccess(String result) {
                command.execute(result);
            }
        });
    }
    public void doWhisper(String name,final ScheduledCommand<String,GwtSecurityException> command){
        service.whisperServer(name, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                if(caught instanceof GwtSecurityException)
                    command.onException((GwtSecurityException)caught);
                else
                    GWT.log(caught.getMessage(), caught);
            }

            @Override
            public void onSuccess(String result) {
                command.execute(result);
            }
        });
    }
}
