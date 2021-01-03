package rte;

import component.Component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionClassLoader {


    public Component loadComponentFromFilesystem(String jarDirectory, String componentName) throws IOException, ClassNotFoundException {    //https://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
        /**
         *
         * @param jarDirectory path to the directory
         * @param componentName exclusive .jar
         * @return component.Component
         * @throws IOException
         * @throws ClassNotFoundException
         */

        String pathToJar = jarDirectory+"\\"+componentName+".jar";
        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        Component component = new Component(componentName);
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            Class c = cl.loadClass(className); //throws ClassNotFoundException
            component.addClass(c);

            //get methods via annotations
            for (Method m : c.getMethods()) {
                for (Annotation annotation : m.getAnnotations()) {
                    if (annotation.annotationType().getSimpleName().equals("Start")) //Identify annotations via Name
                        component.setStartMethod(m);
                    else if (annotation.annotationType().getSimpleName().equals("Stop"))
                        component.setStopMethod(m);
                    else if (annotation.annotationType().getSimpleName().equals("Subscribe"))
                        component.setSubscribeMethod(m);
                }
            }
        }
        return component;
    }
}
