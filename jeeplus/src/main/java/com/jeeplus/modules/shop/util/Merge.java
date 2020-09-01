package com.jeeplus.modules.shop.util;

import java.lang.reflect.Field;

public class Merge {
    /**
     *
     * @param sourceBean 前端对象
     * @param targetBean 数据库对象
     * @return
     */
    @SuppressWarnings("unused")
    public static  Object combineSydwCore(Object sourceBean, Object targetBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = sourceBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);

            try {
                if (!(sourceField.get(sourceBean) == null)&&!sourceField.getName().contains("serialVersionUID")) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }
}
