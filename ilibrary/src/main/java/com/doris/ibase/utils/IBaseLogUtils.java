package com.doris.ibase.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Doris
 * @date 2018/8/20
 */
public abstract class IBaseLogUtils {

    /**
     * 获取打印 TAG
     */
    public abstract String getTag();

    /**
     * 获取日志保存目录
     */
    public abstract String getLogSavePath();

    /**
     * 日志开关
     */
    protected abstract boolean getLogSwitch();

    /**
     * 是否加密日志
     */
    protected abstract boolean getLogEncrypt();

    /**
     * 解密
     */
    protected abstract String decode(String string);

    /**
     * 加密
     */
    protected abstract String encode(String string);

    /**
     * 读取日志
     */
    public String redLog(String filePath) {
        StringBuffer result = new StringBuffer();
        try {
            File file = new File(filePath);
            InputStream stream = new FileInputStream(file);
            if (stream != null) {
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                //分行读取
                while ((line = bufferedReader.readLine()) != null) {
                    if (getLogEncrypt()) {
                        result.append(decode(line) + "\n");
                    } else {
                        result.append(line + "\n");
                    }
                }
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 插入日志
     */
    public void writeLog(String msg) {
        if (msg == null) {
            return;
        }
        Log.i(getTag(), msg);
        if (getLogSwitch()) {
            File file = checkLogFileIsExist();
            if (file == null) {
                return;
            }
            FileOutputStream fos = null;
            try {
                msg = DateFormat.getDateTimeInstance().format(new Date()) + "  " + msg;
                if (getLogEncrypt()) {
                    msg = encode(msg) + "\r\n";
                } else {
                    msg += "\r\n";
                }
                fos = new FileOutputStream(file, true);
                fos.write(msg.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
                file = null;
            }
        }
    }

    /**
     * 插入日志
     */
    public void writeLog(String msg, Throwable throwable) {
        writeLog(msg);
        writeLog(throwable);
    }

    /**
     * 插入日志
     */
    public void writeLog(Throwable throwable) {
        throwable.printStackTrace();
        if (getLogSwitch()) {
            File file = checkLogFileIsExist();
            if (file == null) {
                return;
            }
            String msg = getExceptionInfo(throwable);
            FileOutputStream fos = null;
            try {
                msg = DateFormat.getDateTimeInstance().format(new Date()) + "  " + msg;
                if (getLogEncrypt()) {
                    msg = encode(msg) + "\r\n";
                } else {
                    msg += "\r\n";
                }
                fos = new FileOutputStream(file, true);
                fos.write(msg.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
                file = null;
            }
        }
    }

    /**
     * 获取异常信息
     */
    private String getExceptionInfo(Throwable ex) {
        String result = null;
        try {
            Writer info = new StringWriter();
            PrintWriter printWrite = new PrintWriter(info);
            ex.printStackTrace(printWrite);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWrite);
                cause = cause.getCause();
            }
            result = info.toString();
            printWrite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 检查日志文件是否存在
     */
    private File checkLogFileIsExist() {
        File file = new File(getLogSavePath());
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        file = new File(getLogSavePath() + dateStr + ".txt");
        if (!isLogExist(file)) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sdf = null;
        return file;
    }

    /**
     * 检查当天日志文件是否存在
     */
    private boolean isLogExist(File file) {
        boolean ret = false;
        try {
            File tempFile = new File(getLogSavePath());
            File[] files = tempFile.listFiles();
            if (files == null) {
                return ret;
            }
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName().trim();
                if (name != null && name.equalsIgnoreCase(file.getName())) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
