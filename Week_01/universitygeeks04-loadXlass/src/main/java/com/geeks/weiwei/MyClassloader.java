/**
 * www.hipac.cn Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.geeks.weiwei;

import com.geeks.weiwei.util.ApplicationHome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Path;

/**
 * 自定义ClassLoader
 *
 * @author jingtianer
 * @version universitygeeks04, v 0.1 2021-01-15 09:23 jingtianer Exp $
 */
public class MyClassloader extends ClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(MyClassloader.class);

    public static void main(String[] args) {
        try {
            Class<?> clazz = new MyClassloader().findClass("Hello");
            Method declaredMethod = clazz.getDeclaredMethod("hello");
            declaredMethod.invoke(clazz.newInstance());
        } catch (Exception e) {
            logger.error("MyClassloader main perform fail", e);
        }
    }

    @Override
    protected Class<?> findClass(String name) {
        File file = new File(this.getConfigDir().toString() + "/Hello.xlass");
        byte[] bytes = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            bytes = getDecodeBytes(inputStream);
        } catch (Exception e) {
            return null;
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    private Path getConfigDir() {
        ApplicationHome applicationHome = new ApplicationHome(getClass());
        File fileLocation = new File(applicationHome.getDir(), "xlass");
        return fileLocation.toPath();
    }

    private byte[] getDecodeBytes(InputStream inputStream) throws IOException {
        byte[] srcBytes = new byte[inputStream.available()];
        inputStream.read(srcBytes);
        byte[] bytes = new byte[srcBytes.length];
        for (int i = 0; i < srcBytes.length; i++) {
            bytes[i] = (byte) (255 - srcBytes[i]);
        }
        return bytes;
    }

}
