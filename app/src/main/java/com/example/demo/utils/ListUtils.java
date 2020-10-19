package com.example.demo.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by angeliahuang on 2018/6/15.
 */
public class ListUtils
{
    /**
     * 新数据去除重复
     * @param oldData
     * @param insertData
     * @return 去除重复之后的List
     */
    public static List removeDuplicate(List oldData, List insertData)
    {
        HashSet oldDataSet = new HashSet(oldData);
        List returnData = new ArrayList();
        for (Object o : insertData)
        {
            if (!oldDataSet.contains(o))
            {
                returnData.add(o);
            }
        }
        return returnData;
    }

    // 删除ArrayList中重复元素，保持顺序
    public static void removeDuplicateWithOrder(List list) {
        if (list == null || list.size() == 0) {
            return;
        }
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator item = list.iterator(); item.hasNext(); ) {
            Object element = item.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }
}
