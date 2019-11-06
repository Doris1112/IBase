package com.doris.ibase.utils;

import android.annotation.SuppressLint;
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
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Doris
 * @date 2018/8/20
 */
@SuppressWarnings("WeakerAccess")
public abstract class IBaseLogUtils {

    /**
     * 默认开启日志
     */
    protected boolean mLogSwitch = true;
    /**
     * 默认不加密
     */
    protected boolean mLogEncrypt = false;

    /**
     * 获取打印 TAG
     */
    protected abstract String getTag();

    /**
     * 获取日志保存目录
     */
    protected abstract String getLogSavePath();

    /**
     * 设置日志开关
     * @param logSwitch 是否保存日志
     */
    public void setLogSwitch(boolean logSwitch){
        mLogSwitch = logSwitch;
    }

    /**
     * 设置是否加密日志
     * @param logEncrypt 是否加密
     */
    public void setLogEncrypt(boolean logEncrypt){
        mLogEncrypt = logEncrypt;
    }

    /**
     * 解密
     */
    protected String decode(String string){
        return string;
    }

    /**
     * 加密
     */
    protected String encode(String string){
        return string;
    }

    /**
     * 读取日志
     */
    public String redLog(String filePath) {
        StringBuilder result = new StringBuilder();
        try {
            File file = new File(filePath);
            InputStream stream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            //分行读取
            while ((line = bufferedReader.readLine()) != null) {
                if (mLogEncrypt) {
                    result.append(decode(line))
                            .append("\n");
                } else {
                    result.append(line)
                            .append("\n");
                }
            }
            stream.close();
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
        if (mLogSwitch) {
            File file = checkLogFileIsExist();
            if (file == null) {
                return;
            }
            FileOutputStream fos = null;
            try {
                msg = DateFormat.getDateTimeInstance().format(new Date()) + "  " + msg;
                if (mLogEncrypt) {
                    msg = encode(msg) + "\r\n";
                } else {
                    msg += "\r\n";
                }
                fos = new FileOutputStream(file, true);
                fos.write(msg.getBytes(StandardCharsets.UTF_8));
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
        if (mLogSwitch) {
            File file = checkLogFileIsExist();
            if (file == null) {
                return;
            }
            String msg = getExceptionInfo(throwable);
            FileOutputStream fos = null;
            try {
                msg = DateFormat.getDateTimeInstance().format(new Date()) + "  " + msg;
                if (mLogEncrypt) {
                    msg = encode(msg) + "\r\n";
                } else {
                    msg += "\r\n";
                }
                fos = new FileOutputStream(file, true);
                fos.write(msg.getBytes(StandardCharsets.UTF_8));
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File checkLogFileIsExist() {
        File file = new File(getLogSavePath());
        if (!file.exists()) {
            file.mkdirs();
        }
        @SuppressLint("SimpleDateFormat")
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
                return false;
            }
            for (File value : files) {
                String name = value.getName().trim();
                if (name.equalsIgnoreCase(file.getName())) {
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
