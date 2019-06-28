package com.munin.library.cache;

public interface ISpCache<T> {

    T getData();

    Class setDataClass();

    String getSpName();

    String getDataKey();

    void update(String data);

    boolean isInvalid();

}
