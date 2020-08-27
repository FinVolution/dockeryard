package com.ppdai.dockeryard.admin.controller;

import com.ppdai.dockeryard.admin.service.UserService;
import com.ppdai.dockeryard.core.po.UserEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/all", method = RequestMethod.GET)
    public ResponseEntity<List<UserEntity>> getAllIUsers() {
        List<UserEntity> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);

    }


    @ApiOperation(value = "根据id获取Use")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        UserEntity user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "添加Use")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<String> addUser(@RequestBody UserEntity userEntity) {
        userService.insert(userEntity);
        return new ResponseEntity<>("add success", HttpStatus.OK);
    }

    @ApiOperation(value = "更新Use")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity<String> updateUser(@RequestBody UserEntity userEntity) {
        userService.update(userEntity);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @ApiOperation(value = "删除Use")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

}
