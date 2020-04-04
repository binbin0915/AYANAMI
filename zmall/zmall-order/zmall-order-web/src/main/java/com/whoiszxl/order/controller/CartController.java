package com.whoiszxl.order.controller;

import com.whoiszxl.common.entity.Result;
import com.whoiszxl.order.config.TokenDecode;
import com.whoiszxl.order.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: whoiszxl
 * @create: 2020-03-26
 **/
@Api(value = "ZMALL-购物车管理控制器", tags = "ZMALL-购物车管理控制器")
@RestController
@CrossOrigin
@RequestMapping("/order/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    @ApiOperation("添加商品到购物车")
    @GetMapping("/add")
    public Result add(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num) {
        String username = tokenDecode.getUsername();
        cartService.addCart(skuId, num, username);
        return Result.success();
    }

    @ApiOperation("获取购物车列表")
    @GetMapping("/list")
    public Result list() {
        String username = tokenDecode.getUsername();
        return Result.success(cartService.list(username));
    }

}


