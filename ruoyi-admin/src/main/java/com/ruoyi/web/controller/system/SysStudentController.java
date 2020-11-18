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
    // 默认排序的值
    private static long sortNum;

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
        Long asInt = list.stream().mapToLong(student -> student.getSort()).max().getAsLong();
        sortNum = asInt;
        System.out.println("asInt = " + asInt);
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
        sortNum ++;
        return toAjax(sysStudentService.toTop(Long.parseLong(id),sortNum));
    }

    @GetMapping("/getUrl/{url}")
    public String getUrl(@PathVariable(name = "url") String url) {
        return "http://localhost:9090/"+url;
    }

    /**
     * 修改学生头像
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadHeadPic")
    public AjaxResult updateStuHeadPic(@RequestParam("headPicFile") MultipartFile file, @RequestParam("stuId") Long stuId) throws IOException {
        if (file.isEmpty()) return AjaxResult.error("上传图片异常，请联系管理员");

        String picUrl = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file);
        boolean updateStuHeadPic = sysStudentService.updateStuHeadPic(stuId, picUrl);
        if (!updateStuHeadPic) return AjaxResult.error("上传图片异常，请联系管理员");
        System.out.println("picUrl = " + picUrl);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("imgUrl", picUrl);
        return ajax;

    }

    /**
     *  更新学生头像
     */
    @PostMapping("/updateStuPic/{id}")
    public AjaxResult updateSingerPic(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) {
        if (file.isEmpty()) {
            return AjaxResult.error("上传头像失败");
        }
        //修改文件名
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        //文件上传路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"img"+
                System.getProperty("file.separator")+"stuHeadPic";
        File file1 = new File(filePath);
        //如果文件路径不存在，则创建
        if (!file1.exists()) {
            file1.mkdir();
        }
        //图片文件存储位置
        File destFilePath = new File(filePath + System.getProperty("file.separator") + fileName);
        //存储到数据库的相对地址
        String storeAvatorPath = "/img/stuHeadPic/"+fileName;

        try {
            file.transferTo(destFilePath);
            //更新学生的头像地址
            SysStudent student = new SysStudent();
            student.setId(id);
            student.setPic(storeAvatorPath);
            int result = sysStudentService.updateSysStudent(student);
            if (result > 0) {
                return AjaxResult.success("头像上传成功");
            }
            return AjaxResult.error("上传头像失败");
        } catch (IOException e) {
            return AjaxResult.error(e.getMessage());
        }

    }

}
