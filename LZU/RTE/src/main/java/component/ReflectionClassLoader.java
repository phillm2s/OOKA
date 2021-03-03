package component;

import annotations.*;
import component.Component;
import exceptions.MissingAnnotationException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class ReflectionClassLoader {

    public static Component loadComponentFromFilesystem(String jarDirectory, String componentName, String ComponentID) throws IOException, ClassNotFoundException, MissingAnnotationException {    //https://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
        /**
         *
         * @param jarDirectory path to the directory
         * @param componentName exclusive .jar
         * @return component.Component
         * @throws IOException
         * @throws ClassNotFoundException
         */

        boolean instantiate= false, start= false, stop= false, close=false, state= false; //required annotations
        String pathToJar = jarDirectory+"\\"+componentName+".jar";
        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        Component component = new Component(componentName, ComponentID);
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
                if (m.isAnnotationPresent(Instantiate.class) ) { //Identify annotations via Name
                    component.setInstantiateMethod(m);
                    instantiate=true;
                }
                else if (m.isAnnotationPresent(Start.class) ) { //Identify annotations via Name
                    component.setStartMethod(m);
                    start=true;
                }
                else if (m.isAnnotationPresent(Stop.class) ) {
                    component.setStopMethod(m);
                    stop=true;
                }
                else if (m.isAnnotationPresent(Close.class) ) {
                    component.setCloseMethod(m);
                    close=true;
                }
                else if (m.isAnnotationPresent(Subscribe.class) )
                    component.setSubscribeMethod(m);
                else if (m.isAnnotationPresent(State.class) ) { //annotation.annotationType().getSimpleName().equals("State")
                    component.setGetStateMethod(m);
                    state=true;
                }

            }
        }
        if(!instantiate || !start || !stop || !state)
            throw new MissingAnnotationException();
        return component;
    }

}
