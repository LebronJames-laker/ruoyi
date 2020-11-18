package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysStudent;

import java.util.List;


/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2020-11-17
 */
public interface ISysStudentService
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
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    int deleteSysStudentByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    int deleteSysStudentById(Long id);

    /**
     * 置顶某一条学生数据
     * @param id
     * @param sort
     * @return
     */
    int toTop(Long id, Long sort);

    /**
     *
     * @param stuId
     * @param picUrl
     * @return
     */
    boolean updateStuHeadPic(Long stuId, String picUrl);

}
