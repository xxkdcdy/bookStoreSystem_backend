package com.cdy.mapper;

import com.cdy.entity.Book;
import com.cdy.entity.Cart;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository(value = "cartMapper")
public interface CartMapper {

    //插入一条记录
    @Insert("Insert into cart(uid,bid,title,price) values('${param1}', '${param2}', '${param3}', ${param4})")
    int insertUser(String uid,String bid, String title, int price);

    // 购物车查重
    @Select("Select * from cart where cart.uid=#{param1} and cart.bid=#{param2} and cart.check = 0")
    List<Cart> checkUnique(String uid, String bid);

    //根据用户账户找到未结账的购物车
    @Select("Select * from cart where cart.uid=#{param1} and cart.check = 0")
    List<Cart> findAllByUid(String uid);

    //根据用户账户找到已经结账的购物车
    @Select("Select * from cart where cart.uid=#{param1} and cart.check = 1")
    List<Cart> findAllBuyByUid(String uid);

    // 购物车数量修改
    @Update("update cart set cart.num=#{param1} where cart.uid=#{param2} and cart.bid=#{param3}")
    Boolean updateCartNum(int num, String uid, String bid);

    // 删除购物车记录
    @Delete("DELETE FROM cart WHERE cart.uid=#{param1} and cart.bid=#{param2}")
    Boolean deleteCart(String uid, String bid);

    // 购物车结账
    @Update("update cart set cart.check=1 where cart.uid=#{param1} and cart.bid=#{param2}")
    Boolean Bill(String uid, String bid);
}
