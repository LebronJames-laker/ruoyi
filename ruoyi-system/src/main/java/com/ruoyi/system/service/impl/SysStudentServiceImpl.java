package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysStudentMapper;
import com.ruoyi.system.service.ISysStudentService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2020-11-17
 */
@Service
public class SysStudentServiceImpl implements ISysStudentService
{
    @Autowired
    private SysStudentMapper sysStudentMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SysStudent selectSysStudentById(Long id)
    {
        return sysStudentMapper.selectSysStudentById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sysStudent 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysStudent> selectSysStudentList(SysStudent sysStudent)
    {
        return sysStudentMapper.selectSysStudentList(sysStudent);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param sysStudent 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSysStudent(SysStudent sysStudent)
    {
        return sysStudentMapper.insertSysStudent(sysStudent);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param sysStudent 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSysStudent(SysStudent sysStudent)
    {
        return sysStudentMapper.updateSysStudent(sysStudent);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSysStudentByIds(Long[] ids)
    {
        return sysStudentMapper.deleteSysStudentByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSysStudentById(Long id)
    {
        return sysStudentMapper.deleteSysStudentById(id);
    }

    /**
     * 置顶某一条学生数据
     * @param id
     * @param sort
     * @return
     */
    @Override
    public int toTop(Long id, Long sort) {
        return sysStudentMapper.toTop(id,sort);
    }

    /**
     *  修改学生头像
     * @param stuId
     * @param picUrl
     * @return
     */
    @Override
    public boolean updateStuHeadPic(Long stuId, String picUrl) {
        SysStudent student = new SysStudent();
        student.setId(stuId);
        student.setPic(picUrl);
        return sysStudentMapper.updateSysStudent(student) > 0;
    }

    /**
     * 得到排序最大值
     * @return
     */
    @Override
    public long getTopSortNum() {
        return sysStudentMapper.getTopSortNum();
    }
}
