package com.ppdai.dockeryard.admin.controller;

import com.ppdai.dockeryard.admin.service.EnvironmentService;
import com.ppdai.dockeryard.core.po.EnvironmentEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class EnvironmentController {

    @Autowired
    private EnvironmentService envService;

    @ApiOperation(value = "获取所有Environment列表")
    @RequestMapping(value = "/env/all", method = RequestMethod.GET)
    public ResponseEntity<List<EnvironmentEntity>> getAllEnvironment() {
        List<EnvironmentEntity> env = envService.getAll();
        return new ResponseEntity<>(env, HttpStatus.OK);
    }

    @ApiOperation(value = "获取所有Environment列表的名称")
    @RequestMapping(value = "/env/allNames", method = RequestMethod.GET)
    public ResponseEntity<Set<String>> getAllEnvironmentNames() {
        List<String> nameList = envService.getAllNames();
        //防止名称出现重复
        Set<String> nameSet = new HashSet<>(nameList);
        return new ResponseEntity<>(nameSet, HttpStatus.OK);
    }

    @ApiOperation(value = "根据id获取Environment")
    @RequestMapping(value = "/env/{id}", method = RequestMethod.GET)
    public ResponseEntity<EnvironmentEntity> getEnvironment(@PathVariable Long id) {
        EnvironmentEntity env = envService.getById(id);
        return new ResponseEntity<>(env, HttpStatus.OK);
    }

    /*@ApiOperation(value = "根据name获取Environment")
    @RequestMapping(value = "/env/{name}", method = RequestMethod.GET)
    public ResponseEntity<EnvironmentEntity> getEnvironmentByName(@PathVariable String name) {
        EnvironmentEntity env = envService.getByName(name);
        return new ResponseEntity<>(env, HttpStatus.OK);
    }*/

    @ApiOperation(value = "根据url获取Environment")
    @RequestMapping(value = "/env", method = RequestMethod.GET)
    public ResponseEntity<EnvironmentEntity> getEnvironmentByUrl(@RequestParam String url) {
        EnvironmentEntity env = envService.getByUrl(url);
        return new ResponseEntity<>(env, HttpStatus.OK);
    }

    @ApiOperation(value = "添加Environment")
    @RequestMapping(value = "/env", method = RequestMethod.POST)
    public ResponseEntity<String> addEnvironment(@RequestBody EnvironmentEntity env) {
        envService.insert(env);
        return new ResponseEntity<>("add success", HttpStatus.OK);
    }

    @ApiOperation(value = "更新Environment")
    @RequestMapping(value = "/env", method = RequestMethod.PUT)
    public ResponseEntity<String> updateEnvironment(@RequestBody EnvironmentEntity env) {
        envService.update(env);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @ApiOperation(value = "删除Environment")
    @RequestMapping(value = "/env/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteEnvironment(@PathVariable Long id) {
        envService.delete(id);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }
}
