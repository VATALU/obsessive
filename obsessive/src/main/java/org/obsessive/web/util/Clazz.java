package org.obsessive.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class Clazz {

    private static final Logger LOGGER = LoggerFactory.getLogger(Clazz.class);

    /**
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
        while (urls.hasMoreElements()) {
            Optional.of(urls.nextElement()).ifPresent(url -> {
                try {
                    String protocol = url.getProtocol();
                    if (Objects.equals(protocol, "file")) {
                        String packagePath = url.getPath().replace("%20", "");
                        addClass(classSet, packagePath, packageName);
                    } else if (Objects.equals(protocol, "jar")) {
                        //扫描 Jar 包
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
//                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
//                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                                while (jarEntryEnumeration.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntryEnumeration.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
//                            }
//                        }
                    }
                } catch (IOException e) {
                    LOGGER.error("class set getJvm fail", e);
                    throw new RuntimeException(e);
                }
            });

        }
        return classSet;
    }


    private static void addClass(final Set<Class<?>> classSet, final String packagePath, final String packageName) {
        File[] files = new File(packagePath).listFiles(
                file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory()
        );
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (Strings.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (Strings.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (Strings.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(final Set<Class<?>> classSet, final String className) {
        Class<?> clazz = forName(className, false);
        classSet.add(clazz);
    }


    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> forName(String className, boolean isInitialized) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure", e);
            throw new RuntimeException(e);
        }
        return clazz;
    }
}
