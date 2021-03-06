package com.lyon.Controller.UserController;

import com.alibaba.fastjson.JSON;
import com.lyon.Entity.Image;
import com.lyon.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.lyon.Security.logTime.now;


@RestController
@RequestMapping("/api/image")
public class ImageUserController {

    @Autowired
    private ImageRepository imageRepository;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public HashMap<String, String> addUser(
            @RequestBody HashMap<String, String> data
    ) {
        HashMap<String, String> response = new HashMap<>();
        System.out.println("image/new " + JSON.toJSONString(data) + now());
        Image image = new Image();
        long id = Long.parseLong(data.get("id"));
        image.setIid(id);
        image.setImg(data.get("img"));
        imageRepository.save(image);
        image = imageRepository.findById(id);
        if (image.getImg().equals(""))
            response.put("flag", "false");
        else
            response.put("flag", "true");
        return response;
    }

//    @RequestMapping(value = "/new", method = RequestMethod.POST)
//    public HashMap<String, String> addUser(
//            @RequestBody String data
//    ) {
//        HashMap<String, String> response = new HashMap<>();
////        data.replaceAll("")
//        System.out.println("image/new " + data + now());
//        System.out.println("image/new " + JSON.toJSONString(data) + now());
//        Image image = new Image();
//        long id = image.getIid();
//        System.out.println("image/new id now: " + id);
//        image.setImg(data);
//        imageRepository.save(image);
//        id = image.getIid();
//        System.out.println("image/new id now: " + id);
//        image = imageRepository.findById(id);
//        if (image.getImg().equals(""))
//            response.put("flag", "false");
//        else
//            response.put("flag", "true");
//        System.out.println("image/new image " + JSON.toJSONString(image) + now());
//        id = image.getIid();
//        System.out.println("image/new id now: " + id);
//        return response;
//    }

}
