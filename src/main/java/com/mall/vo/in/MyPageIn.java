package com.mall.vo.in;

import com.mall.common.consts.propertiesconsts.SystemConsts;
import lombok.Data;
/**
 * @author lijun
 * @date 2020-06-30 15:57
 * @description  自定义分页接收对象
 * @since version-1.0
 * @error
 */
@Data
public class MyPageIn {
    int current = SystemConsts.PAGE_CURRENT;
    int size = SystemConsts.PAGE_SIZE;
    int isAsc=SystemConsts.ASCEND;
    String orderBy;
    String fuzzySearchKey;
    String fuzzySearchValue;
    String accurateSearchKey;
    String accurateSearchValue;
    Integer searchId;

}
