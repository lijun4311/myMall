package com.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 检查用户字段是否存在
     * @param params key 字段名 value 字段值
     * @return  返回字段值和总计条数 集合
     */
    List<Map<String, String>> checkUserValid(@Param("params") Map<String, String> params);
}
