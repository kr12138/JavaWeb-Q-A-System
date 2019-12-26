package com.lyon.Controller;

import com.alibaba.fastjson.JSON;
import com.lyon.Entity.User;
import com.lyon.Repository.UserRepository;
import static com.lyon.Security.logTime.now;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public Map<String, String> VerifyLogin(
//            @RequestParam("id") long id,
//            @RequestParam("password") String password,
//            @RequestParam("identity") short identity
//    ) {
//        List<User> users = userRepository.findByIdAndPasswordAndIdentity(id, password, identity);
//        Map<String, String> response = new HashMap<>();
//        if (users.isEmpty())
//            response.put("flag", "false");
//        else {
//            response.put("flag", "true");
//            response.put("name", users.get(0).getName());
//        }
//        return response;
//    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> VerifyLogin(
            @RequestBody User data
    ) {
        HashMap<String, String> response = new HashMap<>();
        System.out.println("user/login " + JSON.toJSONString(data) + now());
        User user = userRepository.findByIdAndPasswordAndIdentity(data.getId(), data.getPassword(), data.getIdentity());
        if (user == null)
            response.put("flag", "false");
        else {
            System.out.println(user.getName() + " login " + now());
            response.put("flag", "true");
            response.put("name", user.getName());
            response.put("identity", Short.toString(user.getIdentity()));
            if (user.getIdentity() == 0) {
                response.put("token", "admin");
            } else if (user.getIdentity() == 1) {
                response.put("token", "stude");
            } else if (user.getIdentity() == 2) {
                response.put("token", "teach");
            }
        }
        return response;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> addUser(
            @RequestBody User data
    ) {
        HashMap<String, String> response = new HashMap<>();
        System.out.println("user/register " + JSON.toJSONString(data) + now());
        User pre = userRepository.findByIdAndIdentity(data.getId(), data.getIdentity());
        if (pre != null) {
            response.put("flag", "false");
        }
        else {
            System.out.println(data.getId() + " registering " + now());
            response.put("flag", "true");
            userRepository.save(data);
        }
        return response;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> changeUser(
            @RequestBody HashMap<String, String> data
    ) {
        HashMap<String, String> response = new HashMap<>();
        System.out.println("user/changePassword " + JSON.toJSONString(data) + now());
        long id = Long.parseLong(data.get("id"));
        String oldPassword = data.get("oldPassword");
        String newPassword = data.get("newPassword");

        User newData = userRepository.findById(id);
        if (newData == null || !newData.getPassword().equals(oldPassword) ) {
            response.put("flag", "false");
        } else {
            System.out.println(id + " changing password " + now());
            newData.setPassword(newPassword);
//            userRepository.deleteById(id);//似乎会在主键冲突时自动将save改成update
            userRepository.save(newData);
            response.put("flag", "true");
        }
        return response;
    }

//    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
//    public HashMap<String, String> getAll() {
//        HashMap<String, String> response = new HashMap<>();
//        final List<User> list = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
//        System.out.println("user/getAll " + JSON.toJSONString(list) + now());
//        response.put("flag", "true");
//        response.put("users", JSON.toJSONString(list));
//        return response;
//    }
    @RequestMapping(value = "/getAll/{page}", method = RequestMethod.GET)
    public HashMap<String, String> getAll(
            @PathVariable int page
    ) {
        HashMap<String, String> response = new HashMap<>();
        final Page<User> users = userRepository.findAll(
                PageRequest.of(page,5,
                        Sort.by(Sort.Direction.ASC, "id")
                )
        );
        System.out.println("user/getAll("+ page +") " + JSON.toJSONString(users) + now());
        response.put("flag", "true");
        response.put("totalPages", Integer.toString(users.getTotalPages()));
        response.put("users", JSON.toJSONString(users.getContent()));
        return response;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public HashMap<String, String> add(
            @RequestBody User data
    ) {
        HashMap<String, String> response = new HashMap<>();
        System.out.println("User/add " + JSON.toJSONString(data) + now());
        if (userRepository.existsById(data.getId())) {
            response.put("flag", "false");
            return response;
        }
        userRepository.save(data);
        response.put("flag", "true");
        return response;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public HashMap<String, String> update(
            @RequestBody User data
    ) {
        HashMap<String, String> response = new HashMap<>();
        System.out.println("User/update " + JSON.toJSONString(data) + now());
        if (!userRepository.existsById(data.getId())) {
            response.put("flag", "false");
            return response;
        }
//        userRepository.deleteById(data.getId());
        userRepository.save(data);
        response.put("flag", "true");
        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public HashMap<String, String> delete(
            @RequestBody User data
    ) {
        HashMap<String, String> response = new HashMap<>();
        System.out.println("User/delete " + JSON.toJSONString(data) + now());
        if (!userRepository.existsById(data.getId())) {
            response.put("flag", "false");
            return response;
        }
        userRepository.deleteById(data.getId());
        response.put("flag", "true");
        return response;
    }
}