package org.obsessive.web.engine;

import org.obsessive.web.entity.constant.AnnotationConstant;
import org.obsessive.web.log.Record;
import org.obsessive.web.util.Annotations;
import org.obsessive.web.util.Clazz;
import org.obsessive.web.util.Reflections;
import org.obsessive.web.util.Strings;
import org.obsessive.web.util.function.Fn;

import java.io.File;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class Scanner {

    private final static Record LOGGER = Record.get(Scanner.class);


    //scan class under this package
    public static ConcurrentMap<String, Class<?>> scan(final Class<?> clazz) {
        //get all class
        Set<Class<?>> classSet = getClassSet(clazz.getPackage().getName());
        //filter annotationed class
        Set<Class<?>> annotatedClsSet = Annotations.getAnnotatedCls(classSet, AnnotationConstant.MODULE);
        return annotatedClsSet.stream().collect(Collectors.toConcurrentMap(Class::getName, annotatedCls -> annotatedCls));
    }


    public static ConcurrentMap<String, Object> getSingleton(final ConcurrentMap<String, Class<?>> annotatedClsMap) {

        final ConcurrentMap<String, Object> singletonBeans = new ConcurrentHashMap<>();

        annotatedClsMap.forEach((className, clazz) -> {
            Object bean = Reflections.instance(clazz);
            singletonBeans.put(className, bean);
        });

        //set fields
        singletonBeans.forEach((className, bean) -> {
            final Field[] fields = bean.getClass().getFields();

            AnnotationConstant.FIELD_SETTER_MAP.forEach((clazz, fieldSetter) ->
                    Arrays.stream(fields).filter(field -> field.isAnnotationPresent(clazz))
                            .forEach(field -> fieldSetter.inject(singletonBeans, bean, field))
                );
//            // @Inject
//            fieldStream.filter(field -> field.isAnnotationPresent(Inject.class)).forEach(field -> {
//                String fieldName = field.getName();
//                Object instance = singletonBeans.get(fieldName);
//                Reflections.setField(bean, field, instance);
//            });
//            // @Value
//            fieldStream.filter(field -> field.isAnnotationPresent(Value.class)).forEach(field ->
//                    Reflections.setField(bean, field, field.getAnnotation(Value.class).value()));
        });
        return singletonBeans;
    }

    /**
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(final String packageName) {
        Objects.requireNonNull(packageName);
        return Fn.safeGet(() -> {
            Enumeration<URL> urls = Clazz.getClassLoader().getResources(packageName.replace(".", "/"));
            Set<Class<?>> classSet = new HashSet<>();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                classSet.addAll(Fn.get(new HashSet<>(), () -> {
                    String protocol = url.getProtocol();
                    if (Objects.equals(protocol, "file")) {
                        String packagePath = url.getPath().replace("%20", "");
                        return addClass(packagePath, packageName);
                    } else if (Objects.equals(protocol, "jar")) {
                        //scan jar package
                        return addJar(url);
                    } else {
                        return null;
                    }
                }, url));
            }
            return classSet;
        }, LOGGER);
    }

    private static Set<Class<?>> addJar(URL url) {
        return Fn.safeGet(() -> {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            return Fn.getJvm(LOGGER, () -> {
                JarFile jarFile = jarURLConnection.getJarFile();
                return Fn.get(() -> {
                    Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                    Set<Class<?>> clsSet = new HashSet<>();
                    while (jarEntryEnumeration.hasMoreElements()) {
                        JarEntry jarEntry = jarEntryEnumeration.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class")) {
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                            Class<?> clazz = Reflections.clazz(className);
                            clsSet.add(clazz);
                        }
                    }
                    return clsSet;
                }, jarFile);
            }, jarURLConnection);
        }, LOGGER);
    }

    private static Set<Class<?>> addClass(final String packagePath,
                                          final String packageName) {
        File[] files = new File(packagePath).
                listFiles(file ->
                        (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        return Fn.get(() -> {
            Set<Class<?>> classSet = new HashSet<>();
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
            return classSet;
        }, files);
    }
}