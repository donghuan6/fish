package com.nine.log.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nine.common.domain.PageR;
import com.nine.common.domain.R;
import com.nine.common.utils.excel.ExcelUtil;
import com.nine.log.dao.SysLog;
import com.nine.log.dto.SysLogPageDto;
import com.nine.log.service.SysLogService;
import com.nine.log.vo.SysLogVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "操作日志")
@RestController
@RequestMapping("/log")
@AllArgsConstructor
public class SysLogController {

    private final SysLogService sysLogService;

    @Operation(summary = "分页查询日志")
    @PostMapping("/page")
    public PageR<SysLogVo> page(@RequestBody @Valid SysLogPageDto dto) {
        Page<SysLog> page = sysLogService.page(dto.getPageNum(), dto.getPageSize(), new SysLog().convert(dto));
        List<SysLogVo> list = page.getRecords().stream().map(SysLog::convert).collect(Collectors.toList());
        return new PageR<>(list, page.getTotal());
    }

    @Operation(summary = "保存日志")
    @PostMapping
    public R<Boolean> add(@RequestBody @Valid SysLog sysLog) {
        sysLogService.add(sysLog);
        return R.ok();
    }

    @Operation(summary = "清除所有日志")
    @PostMapping("/clean")
    public R<Boolean> clean() {
        sysLogService.remove(null);
        return R.ok();
    }

    @Operation(summary = "删除日志")
    @PostMapping("/remove")
    public R<Boolean> remove(@RequestBody @NotEmpty Set<Long> logIds) {
        sysLogService.removeByIds(logIds);
        return R.ok();
    }

    @Operation(summary = "导出日志")
    @PostMapping("/export")
    public void export(@RequestBody @Valid SysLogPageDto dto, HttpServletResponse rsp) throws IOException {
        List<SysLog> list = sysLogService.list(new SysLog().convert(dto));
        ExcelUtil.exportExcel("log", list, rsp.getOutputStream());
    }


}
