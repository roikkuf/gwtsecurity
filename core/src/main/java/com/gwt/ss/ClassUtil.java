/**
 * $Id$
 */
package com.gwt.ss;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to find classes in a package.
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public final class ClassUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);

    private static final String SUFFIX = ".class";

    /**
     * Find the classes in a directory.
     * 
     * @param directory the directory to search.
     * @param packageName the package name.
     * @return a list of classes in the package
     * @throws ClassNotFoundException an error occurred.
     */
    public static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.isDirectory()) { return classes; }
        for (File file : directory.listFiles()) {
            String filename = file.getName();
            if (file.isDirectory()) {
                assert !filename.contains(".");
                classes.addAll(findClasses(file, packageName + "." + filename));
            } else if (filename.endsWith(SUFFIX)) {
                filename = filename.substring(0, filename.length() - SUFFIX.length());
                try {
                    classes.add(Class.forName(packageName + '.' + filename));
                } catch (Exception e) {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
        return classes;
    }

    /**
     * Get the classes in a package.
     * 
     * @param packageName the package to find.
     * @return an array of classes in the package.
     * @throws ClassNotFoundException an error occurred.
     * @throws IOException an io error occurred.
     */
    public static Class<?>[] getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        List<File> dirs = new ArrayList<File>();
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
        } catch (Exception e) {
            LOG.debug(e.getMessage(), e);
        }
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Private constructor.
     */
    private ClassUtil() {
        super();
    }

}
