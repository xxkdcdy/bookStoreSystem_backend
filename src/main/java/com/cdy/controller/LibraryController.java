package com.cdy.controller;

import com.cdy.entity.Book;
import com.cdy.entity.Cart;
import com.cdy.entity.myResult;
import com.cdy.service.BookService;
import com.cdy.service.CartService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author nanfang
 * @create 2020/02/07/10:52
 */
@RestController
public class LibraryController {
    @Resource(name = "bookService")
    private BookService bookService;

    @Resource(name = "cartService")
    private CartService cartService;

    //返回所有的图书
    @CrossOrigin
    @GetMapping("api/books")
    public List<Book> findAll() throws Exception {
        return bookService.findAll();
    }

    //返回新书
    @CrossOrigin
    @GetMapping("api/newBook")
    public List<Book> findNew() throws Exception {
        return bookService.findAllNew();
    }

    //返回推荐
    @CrossOrigin
    @PostMapping("api/hotBook")
    public List<Book> findHot(@RequestBody Map<String, Object> requestMap) throws Exception {
        List<Cart> Purchased = cartService.findAllBuy(requestMap);
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<Book> res = new ArrayList<Book>();

        // 统计用户每个类型的书购买的数量
        for(int i = 0; i < Purchased.size(); i++){
            String cid = bookService.findCid(Purchased.get(i).getBid());
            if(map.containsKey(cid)){
                map.put(cid, map.get(cid) + Purchased.get(i).getNum());
            }
            else{
                map.put(cid, Purchased.get(i).getNum());
            }
        }

        // 如果有购买记录，就推荐购买最多的种类
        if(map.size() > 0) {
            // 将map.entrySet()转换成list
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
            // 通过比较器来实现排序
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    // 降序排序
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            for (Map.Entry<String, Integer> mapping : list) {
                System.out.println("key:" + mapping.getKey() + "  value:" + mapping.getValue());
            }
            res = bookService.findAllByCategory(list.get(0).getKey());
            // 不足8本的，用新书补够
            if(res.size() < 8){
                res.addAll(bookService.findAllNew());
            }
            return res;
        }

        // 否则就推荐新书
        return bookService.findAllNew();
    }

    //根据用户账号返回所有的书
    @CrossOrigin
    @PostMapping("api/booksByUid")
    public List<Book> findAllByUid(@RequestBody Book book) {
        return bookService.findAllByUid(book.getUid());
    }

    //根据种类查询图书
    @CrossOrigin
    @GetMapping("api/categories/{cid}/books")
    public List<Book> listByCategory(@PathVariable("cid") String cid) throws Exception {
        if (!cid.equals("0")) {
            return bookService.findAllByCategory(cid);
        } else {
            return findAll();
        }
    }

    //根据种类和用户名查询用户添加的图书
    @CrossOrigin
    @PostMapping("api/categories/uid/UserBooks")
    public List<Book> listByCategoryAndUid(@RequestBody Book book) {
        System.out.println(book);
        if (!book.getCid().equals("0")) {
            return bookService.findAllByCategoryAndUid(book.getCid(), book.getUid());
        } else {
            return bookService.findAllByUid(book.getUid());
        }
    }


    //根据id删除图书
    @CrossOrigin
    @PostMapping("api/delete")
    public myResult deleteBookById(@RequestBody Book book) {
        int num = bookService.deleteBookById(book.getId());
        if (num == 0) {
            return new myResult(400);
        } else {
            return new myResult(200);
        }
    }

    //根据id和用户编号删除图书，并删除本地服务器存储的图片
    @PostMapping("api/deleteById")
    @CrossOrigin
    public myResult deleteBookByIdAndUid(@RequestBody Book book) {
        int num = bookService.deleteBookByIdAndUid(book);
        if (num == 0) {
            return new myResult(400);
        } else {
            if (!book.getCover().equals("")) {
                String cover = book.getCover().split("/api/file/")[1];
                deleteImg(cover);
            }
            if (!book.getImg_1().equals("")) {
                String img_1 = book.getImg_1().split("/api/file/")[1];
                deleteImg(img_1);
            }
            if (!book.getImg_2().equals("")) {
                String img_2 = book.getImg_2().split("/api/file/")[1];
                deleteImg(img_2);
            }
            if (!book.getImg_3().equals("")) {
                String img_3 = book.getImg_3().split("/api/file/")[1];
                deleteImg(img_3);
            }
            if (!book.getImg_4().equals("")) {
                String img_4 = book.getImg_4().split("/api/file/")[1];
                deleteImg(img_4);
            }
            if (!book.getImg_5().equals("")) {
                String img_5 = book.getImg_5().split("/api/file/")[1];
                deleteImg(img_5);
            }
            return new myResult(200);
        }
    }

    @CrossOrigin
    public void deleteImg(String imgName) {
        String imgUrl = "/home/Youth-imgs/" + imgName;
        File img = new File(imgUrl);
        img.delete();
    }

    //添加一本图书
    @CrossOrigin
    @PostMapping("api/insert")
    public myResult insertBook(@RequestBody Book book) {
        System.out.println(book);
        if (bookService.insertBook(book) == 0) {
            return new myResult(400);
        } else {
            return new myResult(200);
        }
    }

    //根据书名查找图书
    //使用form表单传递数据
    @CrossOrigin
    @PostMapping("api/search")
    public List<Book> searchBooksByTitle(@RequestParam Map<String, Object> keywords, HttpSession session) {
        return bookService.finAllByTitle((String) keywords.get("keywords"));
    }

    //上传图片
    @CrossOrigin
    @PostMapping("api/uploadImg")
    public String coversUpload(MultipartFile file) throws Exception {
        System.out.println(file);
        //查看图片类型
        String type = file.getContentType().split("/")[1];
        if (type == null || file.getSize() > 1048576) {
            return null;
        }

        String folder = "D:\\j2ee\\bookStoreSystem\\img";
        File imageFolder = new File(folder);
        File f = new File(imageFolder, getRandomString(10) + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            //如果不存在文件则创建文件夹
            f.getParentFile().mkdirs();
        try {
            //存入图片
            file.transferTo(f);
            Thumbnails.of(f)
                    .outputFormat("jpg")
                    .scale(0.5f)//图片比例压缩
                    .outputQuality(0.5f)//图片清晰度压缩
                    .toFile(f.getPath().split("\\.")[0] + ".jpg");
            if (!type.equals("jpeg")) {
                //删除图片
                f.delete();
            }
            String imgURL = "http://localhost:8443/api/file/" + f.getName().split("\\.")[0] + ".jpg";
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    //删除图片
    @CrossOrigin
    @PostMapping("api/deleteImg")
    public void deleteImg(@RequestParam Map<String, Object> requestMap) {
        String imgName = requestMap.get("imgUrl").toString().split("/api/file/")[1];
        deleteImg(imgName);
    }

    //生成length个随机数组成的字符串
    public String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
