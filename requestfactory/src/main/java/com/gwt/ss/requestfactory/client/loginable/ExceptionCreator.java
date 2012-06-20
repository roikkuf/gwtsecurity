/**
 * $Id$
 */
package com.gwt.ss.requestfactory.client.loginable;

import com.gwt.ss.client.exceptions.GwtSecurityException;

/**
 * Interface for GwtSecurityException creator.
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public interface ExceptionCreator {

    /**
     * Create the exception.
     * 
     * @param clazz the class of the exception to create.
     * @param msg the message.
     * @return the created exception.
     */
    public <T extends GwtSecurityException> T create(String clazz, String msg);

}