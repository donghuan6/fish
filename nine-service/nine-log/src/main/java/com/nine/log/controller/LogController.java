package com.nine.log.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nine.common.domain.PageR;
import com.nine.common.domain.R;
import com.nine.common.utils.excel.ExcelUtil;
import com.nine.log.dao.Log;
import com.nine.log.dto.LogAddDto;
import com.nine.log.dto.LogPageDto;
import com.nine.log.service.LogService;
import com.nine.log.vo.LogVo;
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
public class LogController {

    private final LogService logService;


    @Operation(summary = "分页查询日志")
    @PostMapping("/page")
    public PageR<LogVo> page(@RequestBody @Valid LogPageDto dto) {
        Page<Log> page = logService.page(dto.getPageNum(), dto.getPageSize(), new Log().convert(dto));
        List<LogVo> list = page.getRecords().stream().map(Log::convert).collect(Collectors.toList());
        return new PageR<>(list, page.getTotal());
    }

    @Operation(summary = "保存日志")
    @PostMapping
    public R<Boolean> add(@RequestBody @Valid LogAddDto log) {
        logService.add(log);
        return R.ok();
    }

    @Operation(summary = "清除所有日志")
    @PostMapping("/clean")
    public R<Boolean> clean() {
        logService.remove(null);
        return R.ok();
    }

    @Operation(summary = "删除日志")
    @PostMapping("/remove")
    public R<Boolean> remove(@RequestBody @NotEmpty Set<Long> logIds) {
        logService.removeByIds(logIds);
        return R.ok();
    }

    @Operation(summary = "导出日志")
    @PostMapping("/export")
    public void export(@RequestBody @Valid LogPageDto dto, HttpServletResponse rsp) throws IOException {
        List<Log> list = logService.list(new Log().convert(dto));
        ExcelUtil.exportExcel("log", list, rsp.getOutputStream());
    }


}
