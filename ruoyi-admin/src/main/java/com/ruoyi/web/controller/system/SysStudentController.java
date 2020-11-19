package com.ruoyi.web.controller.system;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.SysStudent;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.web.service.TokenService;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISysStudentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2020-11-17
 */
@RestController
@RequestMapping("/system/student")
public class SysStudentController extends BaseController
{
    @Autowired
    private ISysStudentService sysStudentService;

    @Autowired
    private TokenService tokenService;


    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysStudent sysStudent)
    {
        startPage();
        List<SysStudent> list = sysStudentService.selectSysStudentList(sysStudent);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:student:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysStudent sysStudent)
    {
        List<SysStudent> list = sysStudentService.selectSysStudentList(sysStudent);
        ExcelUtil<SysStudent> util = new ExcelUtil<SysStudent>(SysStudent.class);
        return util.exportExcel(list, "student");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:student:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysStudentService.selectSysStudentById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:student:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysStudent sysStudent)
    {
        return toAjax(sysStudentService.insertSysStudent(sysStudent));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:student:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysStudent sysStudent)
    {
        return toAjax(sysStudentService.updateSysStudent(sysStudent));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:student:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysStudentService.deleteSysStudentByIds(ids));
    }

    /**
     * 置顶学生数据
     */
    @GetMapping("/top/{id}/{sort}")
    public AjaxResult toTop(@PathVariable(name = "id") String id, @PathVariable(name = "sort") String sort) {

        /*List<SysStudent> list = sysStudentService.selectSysStudentList(null);
        Long sortNum = list.stream().mapToLong(student -> student.getSort()).max().getAsLong();*/
        // 得到最大的排序值
        long sortNum = sysStudentService.getTopSortNum();
        return toAjax(sysStudentService.toTop(Long.parseLong(id),++sortNum));
    }

    /**
     * 修改学生头像
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadHeadPic")
    public AjaxResult updateStuHeadPic(@RequestParam("headPicFile") MultipartFile file, @RequestParam("stuId") Long stuId) throws IOException {
        if (file.isEmpty()) {
            return AjaxResult.error("上传图片异常，请联系管理员");
        }
        String picUrl = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
        boolean updateStuHeadPic = sysStudentService.updateStuHeadPic(stuId, picUrl);
        if (!updateStuHeadPic) {
            return AjaxResult.error("上传图片异常，请联系管理员");
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("imgUrl", picUrl);
        return ajax;

    }
}
