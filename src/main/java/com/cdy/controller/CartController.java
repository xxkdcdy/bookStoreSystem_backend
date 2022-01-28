package com.cdy.controller;

import com.cdy.entity.Book;
import com.cdy.entity.Cart;
import com.cdy.service.CartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class CartController {

    @Resource(name = "cartService")
    private CartService cartService;

    // 插入一条购物车数据
    @PostMapping(value = "api/insertCart")
    @ResponseBody
    @CrossOrigin
    public int insertCart(@RequestBody Map<String, Object> requestMap) {
        System.out.println(requestMap);
        return cartService.insertUser(requestMap);
    }

    // 购物车查重
    @CrossOrigin
    @PostMapping("api/cartCheckUnique")
    public int cartCheckUnique(@RequestBody Map<String, Object> requestMap) {
        return cartService.checkCartUnique(requestMap);
    }

    // 根据用户账号返回所有购物车
    @CrossOrigin
    @PostMapping("api/cartByUid")
    public List<Cart> findAllByUid(@RequestBody Map<String, Object> requestMap) {
        return cartService.findAll(requestMap);
    }

    // 根据用户账号返回所有购物车
    @CrossOrigin
    @PostMapping("api/cartBuyByUid")
    public List<Cart> findAllBuyByUid(@RequestBody Map<String, Object> requestMap) {
        return cartService.findAllBuy(requestMap);
    }

    // 购物车数量修改
    @CrossOrigin
    @PostMapping("api/updateCart")
    public boolean updateCart(@RequestBody Map<String, Object> requestMap) {
        return cartService.updateCart(requestMap);
    }

    // 删除购物车记录
    @CrossOrigin
    @PostMapping("api/deleteCart")
    public boolean deleteCart(@RequestBody Map<String, Object> requestMap) {
        return cartService.delectCart(requestMap);
    }

    // 结账
    @CrossOrigin
    @PostMapping("api/Bill")
    public boolean Bill(@RequestBody Map<String, Object> requestMap) {
        return cartService.Bill(requestMap);
    }
}
