package com.cdy.service;

import com.cdy.entity.Book;
import com.cdy.entity.Cart;
import com.cdy.mapper.CartMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service(value = "cartService")
public class CartService {

    @Resource(name = "cartMapper")
    private CartMapper cartMapper;

    // 插入一条购物车数据
    public int insertUser(Map<String, Object> requestMap){
        String uid = requestMap.get("uid").toString();
        String bid = requestMap.get("bid").toString();
        String title = requestMap.get("title").toString();
        Integer price = Integer.parseInt(requestMap.get("price").toString());
        return cartMapper.insertUser(uid, bid, title, price);
    }

    // 购物车查重
    public int checkCartUnique(Map<String, Object> requestMap){
        String uid = requestMap.get("uid").toString();
        String bid = requestMap.get("bid").toString();
        return cartMapper.checkUnique(uid, bid).size();
    }

    //返回所有的购物车
    public List<Cart> findAll(Map<String, Object> requestMap) {
        String uid = requestMap.get("uid").toString();
        return cartMapper.findAllByUid(uid);
    }

    //返回所有的购物车
    public List<Cart> findAllBuy(Map<String, Object> requestMap) {
        String uid = requestMap.get("uid").toString();
        return cartMapper.findAllBuyByUid(uid);
    }

    // 购物车数量修改
    public boolean updateCart(Map<String, Object> requestMap) {
        Integer num = Integer.parseInt(requestMap.get("num").toString());
        String uid = requestMap.get("uid").toString();
        String bid = requestMap.get("bid").toString();
        return cartMapper.updateCartNum(num, uid, bid);
    }

    // 删除购物车记录
    public boolean delectCart(Map<String, Object> requestMap) {
        String uid = requestMap.get("uid").toString();
        String bid = requestMap.get("bid").toString();
        return cartMapper.deleteCart(uid, bid);
    }

    // 结账
    public boolean Bill(Map<String, Object> requestMap){
        String uid = requestMap.get("uid").toString();
        String bid = requestMap.get("bid").toString();
        return cartMapper.Bill(uid, bid);
    }
}
