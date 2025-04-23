package org.nmgns.bps.system.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.nmgns.bps.system.utils.base.TreeEntity;

import java.util.ArrayList;
import java.util.List;

public class TreeEntityUtils {


    public static List<? extends TreeEntity> listToTree(List<? extends TreeEntity> paraTreeEntityList) {
        List<TreeEntity> treeEntity = new ArrayList<>();
        if (null == paraTreeEntityList) return new ArrayList<>();
        else {
            for (TreeEntity te:paraTreeEntityList){
                treeEntity.add(te);
            }
        }
        List<TreeEntity> roots = findRoots(treeEntity);
        List<TreeEntity> notRoots = (List<TreeEntity>) CollUtil.subtract(treeEntity, roots);
        for (TreeEntity root : roots) {
            root.setChildren(findChildren(root, notRoots));
        }
        return roots;
    }

    private static List<TreeEntity> findRoots(List<? extends TreeEntity> treeEntityList) {
        List<TreeEntity> results = new ArrayList<>();
        for (TreeEntity treeEntity : treeEntityList) {
            boolean isRoot = true;
            for (TreeEntity comparedOne : treeEntityList) {
                if (treeEntity.getParentId().equals(comparedOne.getId())) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                results.add(treeEntity);
            }
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    private static List<TreeEntity> findChildren(TreeEntity root, List<TreeEntity> treeEntityList) {
        List<TreeEntity> children = new ArrayList<>();

        for (TreeEntity comparedOne : treeEntityList) {
            if (comparedOne.getParentId().equals(root.getId())) {
                children.add(comparedOne);
            }
        }

        List<TreeEntity> notChildren = (List<TreeEntity>) CollectionUtil.subtract(treeEntityList, children);
        for (TreeEntity child : children) {
            //递归找孩子
            List<TreeEntity> tmpChildren = findChildren(child, notChildren);
            child.setChildren(tmpChildren);
        }
        if (children.isEmpty()) return null;
        return children;
    }

}
