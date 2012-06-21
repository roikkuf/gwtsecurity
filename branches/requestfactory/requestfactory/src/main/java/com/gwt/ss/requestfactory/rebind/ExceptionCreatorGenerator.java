/**
 * $Id$
 */
package com.gwt.ss.requestfactory.rebind;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.gwt.ss.ClassUtil;
import com.gwt.ss.requestfactory.client.loginable.ExceptionCreator;

/**
 * Generates the ExceptionCreatorImpl class.
 * 
 * @version $Rev$
 * @author Steven Jardine
 */
public class ExceptionCreatorGenerator extends Generator {

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
     * @throws UnableToCompleteException unable to complete generation.
     */
    private void printFactoryMethod(SourceWriter writer) {
        writer.println();
        writer.println("public <T extends GwtSecurityException> T create( String type, String msg ) {");
        writer.indent();
        writer.println("if (type != null) {");
        writer.indent();
        for (Class<?> clazz : ClassUtil.getClasses("com.gwt.ss.client.exceptions")) {
            if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
                continue;
            }
            writer.indent();
            writer.println("if (type.startsWith(\"" + clazz.getName() + "\")) {");
            writer.indentln("return (T) new " + clazz.getName() + "(msg);");
            writer.println("}");
            writer.outdent();
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
