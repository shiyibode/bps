package org.nmgns.bps.cktj.service;

import org.nmgns.bps.cktj.dao.DepositCategoryDao;
import org.nmgns.bps.cktj.entity.DepositCategory;
import org.nmgns.bps.system.utils.TreeEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositCategoryService {

    @Autowired
    private DepositCategoryDao depositCategoryDao;
    @Autowired
    private BranchDictionaryService branchDictionaryService;

    public List<DepositCategory> getDepositCategoryTree() throws RuntimeException{

        List<DepositCategory> depositCategoryList = depositCategoryDao.getAll();

        for (int i=0;i<depositCategoryList.size();i++) {
            depositCategoryList.get(i).setDepositTypeStr(branchDictionaryService.getDictionaryNameByCode(depositCategoryList.get(i).getDepositType()));
            depositCategoryList.get(i).setCustomerTypeStr(branchDictionaryService.getDictionaryNameByCode(depositCategoryList.get(i).getCustomerType()));
            depositCategoryList.get(i).setBelongToStr(branchDictionaryService.getDictionaryNameByCode(depositCategoryList.get(i).getBelongTo()));
        }

        List depositCategoryTree = TreeEntityUtils.listToTree(depositCategoryList);

        return depositCategoryTree;
    }


}
