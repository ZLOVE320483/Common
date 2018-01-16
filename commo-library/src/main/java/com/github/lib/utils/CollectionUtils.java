package com.github.lib.utils;

import java.util.Collection;

/**
 * Created by zlove on 2018/1/12.
 */

public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> list) {
        return list == null || list.isEmpty();
    }
}
