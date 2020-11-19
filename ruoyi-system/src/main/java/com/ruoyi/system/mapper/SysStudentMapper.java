package com.ruoyi.system.mapper;


import com.ruoyi.common.core.domain.entity.SysStudent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【学生信息的增删改查】Mapper接口
 *
 * @author ruoyi
 * @date 2020-11-17
 *
 */
public interface SysStudentMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    SysStudent selectSysStudentById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sysStudent 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<SysStudent> selectSysStudentList(SysStudent sysStudent);

    /**
     * 新增【请填写功能名称】
     *
     * @param sysStudent 【请填写功能名称】
     * @return 结果
     */
    int insertSysStudent(SysStudent sysStudent);

    /**
     * 修改【请填写功能名称】
     *
     * @param sysStudent 【请填写功能名称】
     * @return 结果
     */
    int updateSysStudent(SysStudent sysStudent);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    int deleteSysStudentById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysStudentByIds(Long[] ids);

    /**
     * 置顶数据
     * @param id
     * @param sort
     * @return
     */
    int toTop(@Param(value = "id") Long id, @Param(value = "sort")Long sort);

    /**
     * 得到排序的最大值
     * @return
     */
    long getTopSortNum();
}
