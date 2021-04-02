package com.jinhai.springcloud.controller;


import com.jinhai.springcloud.pojo.CommonResult;
import com.jinhai.springcloud.pojo.Payment;
import com.jinhai.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/*
 * 提供restful服务  供其他服务调用
 *
 * */
@RestController
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Value("${server.port}")
    private int serverPort;


    @ResponseBody
    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment dept){
        log.info("save dept: {}", dept);
        int i = paymentService.create(dept);
        if(i>0){
            return new CommonResult(200,"插入数据库成功"+this.serverPort,i);
        }else{
            return new CommonResult(444,"插入数据库失败",null);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult queryById(@PathVariable("id") Long id){
        log.info("***************开始查询*********");
        Payment payment = paymentService.queryById(id);
        log.info("***************查询成功*********"+payment);
        if(payment!=null){
            return new CommonResult(200,"查询成功"+this.serverPort,payment);
        }else{
            return new CommonResult(444,"查询失败",null);
        }
    }

}