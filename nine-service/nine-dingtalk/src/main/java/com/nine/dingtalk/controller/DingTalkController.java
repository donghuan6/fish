package com.nine.dingtalk.controller;

import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.nine.common.domain.R;
import com.nine.dingtalk.util.DingTalkUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/dingtalk")
@AllArgsConstructor
public class DingTalkController {

    @Operation(summary = "获取审批模板code")
    @GetMapping("/processCode")
    public R<String> processCode(@RequestParam("name") String name) {
        return R.ok(DingTalkUtil.getProcessCode(name));
    }

    @Operation(summary = "获取审批模板详情")
    @GetMapping("/schema")
    public R<String> schema(@RequestParam("processCode") String processCode) {
        return R.ok(DingTalkUtil.querySchema(processCode));
    }

    @Operation(summary = "获取审批实例")
    @GetMapping("/processInstance")
    public R<String> processInstance(@RequestParam("processInstanceId") String processInstanceId) {
        return R.ok(DingTalkUtil.getProcessInstance(processInstanceId));
    }

    @Operation(summary = "发起审批")
    @PostMapping("/create")
    public R<String> create(@RequestBody String json) {
        OapiProcessinstanceCreateRequest request = new OapiProcessinstanceCreateRequest();

        request.setProcessCode("");
        request.setOriginatorUserId("");
        request.setDeptId(-1L);
        request.setAgentId(null);

        List<OapiProcessinstanceCreateRequest.FormComponentValueVo> values = new ArrayList<>();
        // 表单具体内容


        request.setFormComponentValues(values);
        return R.ok(DingTalkUtil.startProcess(request));
    }


}
