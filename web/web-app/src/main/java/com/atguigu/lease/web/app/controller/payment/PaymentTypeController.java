package com.atguigu.lease.web.app.controller.payment;


import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.PaymentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "支付方式接口")
@RestController
@RequestMapping("/app/payment")
public class PaymentTypeController {

    @Operation(summary = "根据房间id获取可选支付方式列表")
    @GetMapping("listByRoomId")
    public Result<List<PaymentType>> list(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "获取全部支付方式列表")
    @GetMapping("list")
    public Result<List<PaymentType>> list() {
        return Result.ok();
    }
}
