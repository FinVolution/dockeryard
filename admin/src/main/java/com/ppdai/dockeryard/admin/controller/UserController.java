package com.ppdai.dockeryard.admin.controller;

import com.ppdai.atlas.api.UserControllerApiClient;
import com.ppdai.atlas.model.PageDtoUserDto;
import com.ppdai.atlas.model.ResponsePageDtoUserDto;
import com.ppdai.dockeryard.admin.service.UserService;
import com.ppdai.dockeryard.admin.vo.PageVO;
import com.ppdai.dockeryard.core.po.UserEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserControllerApiClient userControllerApiClient;

    @RequestMapping(value = "/user/all", method = RequestMethod.GET)
    public ResponseEntity<List<UserEntity>> getAllIUsers() {
        List<UserEntity> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);

    }

    @ApiOperation(value = "根据参数获取Use列表")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<PageVO<UserEntity>> getUsersByByParam(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) String realName,
                                                              @RequestParam(required = false) String orgCode,
                                                              @RequestParam(required = false) Integer page,
                                                              @RequestParam(required = false) Integer size) {

        PageVO<UserEntity> pageVO = new PageVO();
        List<UserEntity> users = null;
        ResponseEntity<ResponsePageDtoUserDto> response = null;
        Long totalElements = 0L;
        try {
            response = userControllerApiClient.getUserByConditionUsingGET(page, size, realName, name, orgCode);
        } catch (Exception e) {
            throw new RuntimeException("获取远端用户信息失败",e);
        }
        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            ResponsePageDtoUserDto atlasResponse = response.getBody();
            if (atlasResponse.getCode() >= 0) {
                PageDtoUserDto userPageDto = atlasResponse.getDetail();
                List<com.ppdai.atlas.model.UserDto> userList = userPageDto.getContent();
                if (!CollectionUtils.isEmpty(userList)) {
                    users = userList.stream().map( fromRemote -> {
                        UserEntity user = new UserEntity();
                        user.setId(fromRemote.getId());
                        user.setRealName(fromRemote.getRealName());
                        user.setOrgId(fromRemote.getOrgDto() == null ? null : fromRemote.getOrgDto().getId());
                        user.setOrgName(fromRemote.getOrgDto() == null ? null : fromRemote.getOrgDto().getName());
                        user.setWorkNumber(fromRemote.getWorkNumber());
                        user.setEmail(fromRemote.getEmail());
                        user.setName(fromRemote.getUserName());
                        String roleNames = null;
                        if(!CollectionUtils.isEmpty(fromRemote.getRoles())){
                            List<String> roleNameList = fromRemote.getRoles().stream().map(roleDto -> roleDto.getName()).collect(Collectors.toList());
                            roleNames = StringUtils.collectionToDelimitedString(roleNameList,",");
                        }
                        user.setRole(roleNames);
                        return user;
                    }).collect(Collectors.toList());
                    totalElements = atlasResponse.getDetail().getTotalElements();
                }
            }

        }
        pageVO.setContent(users);
        pageVO.setTotalElements(totalElements.intValue());
        return new ResponseEntity(pageVO, HttpStatus.OK);
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
