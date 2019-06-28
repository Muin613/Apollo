package com.munin.library.utils;

import java.util.List;

/**
 * @author M
 */
public class ListUtils {
    public static boolean isEmpty(List dataList) {
        return dataList == null || dataList.size() == 0;
    }
}
