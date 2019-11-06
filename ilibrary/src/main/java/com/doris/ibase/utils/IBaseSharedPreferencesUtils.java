package com.doris.ibase.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Map;
import java.util.Set;

/**
 * @author Doris
 * @date 2018/8/20
 */
@SuppressWarnings("WeakerAccess")
public abstract class IBaseSharedPreferencesUtils {

    /**
     * 获取保存在手机里面的文件名
     */
    protected abstract String getFileName();

    /**
     * 获取上下文对象
     */
    protected abstract Context getContext();

    /**
     * 获取 SharedPreferences
     */
    public SharedPreferences getSharedPreferences(){
        return getContext().getSharedPreferences(getFileName(), Context.MODE_PRIVATE);
    }

    /**
     * 获取 SharedPreferences.Editor
     */
    public SharedPreferences.Editor getEditor(){
        return getSharedPreferences().edit();
    }

    /**
     * 使用SharedPreferences保存数据
     */
    public void putValue(String key, String value){
        getEditor().putString(key, value).apply();
    }

    /**
     * 使用SharedPreferences获取数据
     */
    public String getValue(String key, String value){
        return getSharedPreferences().getString(key, value);
    }

    /**
     * 使用SharedPreferences保存数据
     */
    public void putValue(String key, int value){
        getEditor().putInt(key, value).apply();
    }

    /**
     * 使用SharedPreferences获取数据
     */
    public int getValue(String key, int value){
        return getSharedPreferences().getInt(key, value);
    }

    /**
     * 使用SharedPreferences保存数据
     */
    public void putValue(String key, boolean value){
        getEditor().putBoolean(key, value).apply();
    }

    /**
     * 使用SharedPreferences获取数据
     */
    public boolean getValue(String key, boolean value){
        return getSharedPreferences().getBoolean(key, value);
    }

    /**
     * 使用SharedPreferences保存数据
     */
    public void putValue(String key, float value){
        getEditor().putFloat(key, value).apply();
    }

    /**
     * 使用SharedPreferences获取数据
     */
    public float getValue(String key, float value){
        return getSharedPreferences().getFloat(key, value);
    }

    /**
     * 使用SharedPreferences获取数据
     */
    public long getValue(String key, long value){
        return getSharedPreferences().getLong(key, value);
    }

    /**
     * 使用SharedPreferences保存数据
     */
    public void putValue(String key, long value){
        SharedPreferences.Editor editor = getEditor();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 使用SharedPreferences保存数据
     */
    public void putValue(String key, Set<String> value){
        SharedPreferences.Editor editor = getEditor();
        editor.putStringSet(key, value);
        editor.apply();
    }

    /**
     * 使用SharedPreferences获取数据
     */
    public Set<String> getValue(String key, Set<String> value){
        return getSharedPreferences().getStringSet(key, value);
    }

    /**
     * 移除key值对应的值
     */
    public void remove(String key) {
        getEditor().remove(key).apply();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        getEditor().clear().apply();
    }

    /**
     * 查询key是否已经存在
     */
    public boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return getSharedPreferences().getAll();
    }

}
