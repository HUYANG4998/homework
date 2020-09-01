package com.jeeplus.modules.shop.util;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Iterator;

public class ExtendsArrayList<E> extends ArrayList<E> {
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',');
        }
    }
}
