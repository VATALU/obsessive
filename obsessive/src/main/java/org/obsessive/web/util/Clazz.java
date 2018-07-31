package org.obsessive.web.util;

import org.obsessive.web.log.Record;
import org.obsessive.web.util.function.Fn;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class Clazz {

    private static final Record LOGGER = Record.get(Clazz.class);

    /**
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Objects.requireNonNull(packageName);
        Set<Class<?>> classSet = new HashSet<>();
        Enumeration<URL> urls = Fn.getJvm(LOGGER,
                () -> getClassLoader().getResources(packageName.replace(".", "/")),
                packageName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if(url!=null) {
                String protocol = url.getProtocol();
                if (Objects.equals(protocol, "file")) {
                    String packagePath = url.getPath().replace("%20", "");
                    classSet.addAll(addClass(packagePath, packageName));
                } else if (Objects.equals(protocol, "jar")) {
                    //扫描 Jar 包
                    classSet.addAll(addJar(url));
                }
            }
        }
        return classSet;
    }

    private static Set<Class<?>> addJar(URL url) {
        return Fn.safeGet(() -> {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            Set<Class<?>> clsSet = new HashSet<>();
            if (jarURLConnection != null) {
                JarFile jarFile = jarURLConnection.getJarFile();
                if (jarFile != null) {
                    Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                    while (jarEntryEnumeration.hasMoreElements()) {
                        JarEntry jarEntry = jarEntryEnumeration.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class")) {
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                            Class<?> clazz = Reflections.clazz(className);
                            clsSet.add(clazz);
                        }
                    }
                }
            }
            return clsSet;
        }, LOGGER);
    }

    private static Set<Class<?>> addClass(final String packagePath,
                                          final String packageName) {
        File[] files = new File(packagePath).
                listFiles(file ->
                        (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        Set<Class<?>> classSet = new HashSet<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile()) {
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    if (Strings.isNotEmpty(packageName)) {
                        className = packageName + "." + className;
                    }
                    classSet.add(Reflections.clazz(className));
                } else {
                    String subPackagePath = fileName;
                    if (Strings.isNotEmpty(packagePath)) {
                        subPackagePath = packagePath + "/" + subPackagePath;
                    }
                    String subPackageName = fileName;
                    if (Strings.isNotEmpty(packageName)) {
                        subPackageName = packageName + "." + subPackageName;
                    }
                    classSet.addAll(addClass(subPackagePath, subPackageName));
                }
            }
        }
        return classSet;
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
