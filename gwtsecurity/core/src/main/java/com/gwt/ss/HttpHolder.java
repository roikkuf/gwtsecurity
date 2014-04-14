/**
 * $Id$
 * 
 * Copyright (c) 2014 Steven Jardine, All Rights Reserved.
 * Copyright (c) 2014 MJN Services, Inc., All Rights Reserved.
 */
package com.gwt.ss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;

/**
 * The Class HttpHolder.
 */
public class HttpHolder {

    /**
     * Gets the single instance of HttpHolder.
     * 
     * @param jp the jp
     * @return single instance of HttpHolder
     */
    public static HttpHolder getInstance(final JoinPoint jp) {
        if (jp == null) {
            return null;
        } else {
            HttpHolder holder = new HttpHolder();
            for (Object obj : jp.getArgs()) {
                if (obj != null) {
                    if (obj instanceof HttpServletRequest) {
                        holder.setRequest((HttpServletRequest) obj);
                    } else if (obj instanceof HttpServletResponse) {
                        holder.setResponse((HttpServletResponse) obj);
                    }
                    if (holder.getRequest() != null && holder.getResponse() != null) {
                        break;
                    }
                }
            }
            return holder;
        }
    }

    /** The request. */
    private HttpServletRequest request;

    /** The response. */
    private HttpServletResponse response;

    /**
     * Gets the request.
     * 
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Gets the response.
     * 
     * @return the response
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Checks if is gwt.
     * 
     * @return true, if is gwt
     */
    public boolean isGwt() {
        return GwtResponseUtil.isGwt(request);
    }

    /**
     * Sets the request.
     * 
     * @param request the new request
     */
    public void setRequest(final HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Sets the response.
     * 
     * @param response the new response
     */
    public void setResponse(final HttpServletResponse response) {
        this.response = response;
    }
}
