package com.doris.ibase.ilibrary.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Doris.
 * @date 2018/8/20.
 */

public abstract class ILogUtils {

    /**
     * 获取打印 TAG
     *
     * @return
     */
    public abstract String getTag();

    /**
     * 获取日志保存目录
     *
     * @return
     */
    public abstract String getLogSavePath();

    /**
     * 日志开关
     *
     * @return
     */
    protected abstract boolean getLogSwitch();

    /**
     * 是否加密日志
     *
     * @return
     */
    protected abstract boolean getLogEncrypt();

    /**
     * 解密
     *
     * @param string
     * @return
     */
    protected abstract String decode(String string);

    /**
     * 加密
     *
     * @param string
     * @return
     */
    protected abstract String encode(String string);

    /**
     * 读取日志
     *
     * @param filePath
     * @return
     */
    public String redLog(String filePath) {
        String result = "";
        try {
            File f = new File(filePath);
            int length = (int) f.length();
            byte[] buff = new byte[length];
            FileInputStream fin = new FileInputStream(f);
            fin.read(buff);
            fin.close();
            result = new String(buff, "GBK");
            if (getLogEncrypt()) {
                result = decode(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 插入日志
     *
     * @param msg
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
                msg = DateFormat.getDateTimeInstance().format(new Date()) + "	" + msg + "\r\n";
                if (getLogEncrypt()) {
                    msg = encode(msg);
                }
                fos = new FileOutputStream(file, true);
                fos.write(msg.getBytes("GBK"));
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
     *
     * @param msg
     * @param throwable
     */
    public void writeLog(String msg, Throwable throwable) {
        if (msg == null) {
            return;
        }
        Log.i(getTag(), msg);
        throwable.printStackTrace();
        if (getLogSwitch()) {
            File file = checkLogFileIsExist();
            if (file == null) {
                return;
            }
            msg += "\r\n";
            msg += getExceptionInfo(throwable);
            FileOutputStream fos = null;
            try {
                msg = DateFormat.getDateTimeInstance().format(new Date()) + "	" + msg + "\r\n";
                if (getLogEncrypt()) {
                    msg = encode(msg);
                }
                fos = new FileOutputStream(file, true);
                fos.write(msg.getBytes("GBK"));
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
     *
     * @param throwable
     */
    public void writeLog(Throwable throwable) {
        throwable.printStackTrace();
        if (getLogSwitch()) {
            File file = checkLogFileIsExist();
            if (file == null) {
                return;
            }
            String msg = "\r\n" + getExceptionInfo(throwable);
            FileOutputStream fos = null;
            try {
                msg = DateFormat.getDateTimeInstance().format(new Date()) + "	" + msg + "\r\n";
                if (getLogEncrypt()) {
                    msg = encode(msg);
                }
                fos = new FileOutputStream(file, true);
                fos.write(msg.getBytes("GBK"));
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
     *
     * @param ex
     * @return
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
     *
     * @param file
     * @return
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
