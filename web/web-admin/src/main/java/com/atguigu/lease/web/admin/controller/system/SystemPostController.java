package com.atguigu.lease.web.admin.controller.system;

import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.SystemPost;
import com.atguigu.lease.model.enums.BaseStatus;
import com.atguigu.lease.web.admin.service.SystemPostService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "后台用户岗位管理")
@RequestMapping("/admin/system/post")
public class SystemPostController {

    @Autowired
    private SystemPostService service;

    @Operation(summary = "分页获取岗位信息")
    @GetMapping("page")
    private Result<IPage<SystemPost>> page(@RequestParam long current, @RequestParam long size) {
        Page<SystemPost> systemPostPage = new Page<>(current, size);
        Page<SystemPost> result = service.page(systemPostPage);
        return Result.ok(result);
    }

    @Operation(summary = "保存或更新岗位信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemPost systemPost) {
        service.saveOrUpdate(systemPost);
        return Result.ok();
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据id删除岗位")
    public Result removeById(@RequestParam Long id) {
        service.removeById(id);
        return Result.ok();
    }

    @GetMapping("getById")
    @Operation(summary = "根据id获取岗位信息")
    public Result<SystemPost> getById(@RequestParam Long id) {
        SystemPost sy = service.getById(id);
        return Result.ok(sy);
    }

    @Operation(summary = "获取全部岗位列表")
    @GetMapping("list")
    public Result<List<SystemPost>> list() {
        List<SystemPost> list = service.list();
        return Result.ok(list);
    }

    @Operation(summary = "根据岗位id修改状态")
    @PostMapping("updateStatusByPostId")
    public Result updateStatusByPostId(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<SystemPost> systemPostLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        systemPostLambdaUpdateWrapper.eq(SystemPost::getId, id).set(SystemPost::getStatus, status);
        service.update(systemPostLambdaUpdateWrapper);
        return Result.ok();
    }
}
