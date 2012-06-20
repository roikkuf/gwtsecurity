/**
 * $Id$
 */
package com.gwt.ss.requestfactory.rebind;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.gwt.ss.requestfactory.client.loginable.ExceptionCreator;

/**
 * Generates the ExceptionCreatorImpl class.
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public class ExceptionCreatorGenerator extends Generator {

    /**
     * Find the classes in a directory.
     * 
     * @param directory the directory to search.
     * @param packageName the package name.
     * @return a list of classes in the package
     * @throws ClassNotFoundException an error occurred.
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.isDirectory()) { return classes; }
        for (File file : directory.listFiles()) {
            String filename = file.getName();
            if (file.isDirectory()) {
                assert !filename.contains(".");
                classes.addAll(findClasses(file, packageName + "." + filename));
            } else if (filename.endsWith(".class")) {
                filename = filename.substring(0, filename.length() - 6);
                classes.add(Class.forName(packageName + '.' + filename));
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
    private static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /** {@inheritDoc} */
    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName)
            throws UnableToCompleteException {
        final String genPackageName = "com.gwt.ss.requestfactory.client.loginable";
        final String genClassName = "ExceptionCreatorImpl";
        ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(genPackageName, genClassName);
        composer.addImplementedInterface(ExceptionCreator.class.getCanonicalName());
        composer.addImport("com.gwt.ss.client.exceptions.*");
        PrintWriter printWriter = context.tryCreate(logger, genPackageName, genClassName);
        if (printWriter != null) {
            SourceWriter writer = composer.createSourceWriter(context, printWriter);
            writer.println("ExceptionCreatorImpl( ) {");
            writer.println("}");
            printFactoryMethod(writer);
            writer.commit(logger);
        }
        return composer.getCreatedClassName();
    }

    /**
     * Print the create method.
     * 
     * @param writer the writer to write to.
     */
    private void printFactoryMethod(SourceWriter writer) {
        writer.println();
        writer.println("public <T extends GwtSecurityException> T create( String type, String msg ) {");
        writer.indent();
        writer.println("if (type != null) {");
        writer.indent();
        try {
            for (Class<?> clazz : getClasses("com.gwt.ss.client.exceptions")) {
                if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) continue;
                writer.indent();
                writer.println("if (type.startsWith(\"" + clazz.getName() + "\")) {");
                writer.indentln("return (T) new " + clazz.getName() + "(msg);");
                writer.println("}");
                writer.outdent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.outdent();
        writer.println("}");
        writer.println("return (T) null;");
        writer.outdent();
        writer.println("}");
        writer.outdent();
        writer.println();
    }

}
