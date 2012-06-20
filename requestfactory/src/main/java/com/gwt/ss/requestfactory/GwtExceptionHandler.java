/**
 * $Id$
 */
package com.gwt.ss.requestfactory;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.gwt.ss.GwtResponseUtil;

/**
 * Converts spring exceptions to their equivalent 
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public class GwtExceptionHandler implements ExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GwtExceptionHandler.class);

    /** {@inheritDoc} */
    @Override
    public ServerFailure createServerFailure(Throwable throwable) {
        if (throwable == null) return null;
        // Convert the exception if possible.
        Throwable t = throwable;
        if (t instanceof Exception) {
            t = GwtResponseUtil.createGwtException((Exception) t);
            if (t == null) {
                t = throwable;
            }
        }
        LOG.debug(t.getMessage(), t);
        // Get the stack trace as a string.
        StringWriter tw = new StringWriter();
        t.printStackTrace(new PrintWriter(tw));
        return new ServerFailure(t.getMessage(), t.getClass().getName(), tw.toString(), true);
    }

}
