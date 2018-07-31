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

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
