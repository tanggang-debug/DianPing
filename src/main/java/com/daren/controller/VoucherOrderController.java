package com.daren.controller;


import com.daren.dto.Result;
import com.daren.service.impl.VoucherOrderServiceImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {

    private final VoucherOrderServiceImpl voucherOrderServiceImpl;

    public VoucherOrderController(VoucherOrderServiceImpl voucherOrderServiceImpl) {
        this.voucherOrderServiceImpl = voucherOrderServiceImpl;
    }

    @PostMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
        return voucherOrderServiceImpl.seckillVoucher(voucherId);
//        return Result.fail("功能未完成");
    }
}
