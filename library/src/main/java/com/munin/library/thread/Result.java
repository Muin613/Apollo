package com.munin.library.thread;

/**
 * @author M
 */
public class Result<T> {
    /**
     * 获取数据正常
     */
    public static final int STATE_OK = 0;
    /**
     * 获取数据异常
     */
    public static final int STATE_FAIL = 1;
    private T data;
    private int state = STATE_FAIL;
    public Object exception;

    public Result(T data) {
        this.data = data;
        state = STATE_OK;
    }

    public Result() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState() {
        this.state = STATE_OK;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }
}
