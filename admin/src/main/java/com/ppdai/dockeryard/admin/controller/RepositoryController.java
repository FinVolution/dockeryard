package com.ppdai.dockeryard.admin.controller;

import com.ppdai.dockeryard.admin.service.RepositoryService;
import com.ppdai.dockeryard.admin.util.RequestUtil;
import com.ppdai.dockeryard.admin.vo.PageVO;
import com.ppdai.dockeryard.core.dto.RepositoryDto;
import com.ppdai.dockeryard.core.po.RepositoryEntity;
import com.ppdai.dockeryard.core.po.UserEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RepositoryController {

    @Autowired
    private RepositoryService repositoryService;

    @ApiOperation(value = "获取所有Repository列表")
    @RequestMapping(value = "/repo/all", method = RequestMethod.GET)
    public ResponseEntity<List<RepositoryEntity>> getAllRepositories() {
        List<RepositoryEntity> repositories = repositoryService.getAll();
        return new ResponseEntity<>(repositories, HttpStatus.OK);
    }

    @ApiOperation(value = "根据参数获取Repository列表")
    @RequestMapping(value = "/repos", method = RequestMethod.GET)
    public ResponseEntity<PageVO<RepositoryEntity>> getRepositoriesByParam(@RequestParam(required = false) Long orgId,
                                                                           @RequestParam(required = false) String orgName,
                                                                           @RequestParam(required = false) String name,
                                                                           @RequestParam(required = false) Integer page,
                                                                           @RequestParam(required = false) Integer size) {
        RepositoryDto repositoryDto = new RepositoryDto();
        repositoryDto.setName(name);
        repositoryDto.setOrgId(orgId);
        repositoryDto.setOrgName(orgName);
        repositoryDto.setPageNo(page);
        repositoryDto.setPageSize(size);
        List<RepositoryEntity> repositories = repositoryService.getByParam(repositoryDto);
        int recordCount = repositoryService.getRecordCountByParam(repositoryDto);
        PageVO<RepositoryEntity> RepositoryPageVO = new PageVO<>();
        RepositoryPageVO.setContent(repositories);
        RepositoryPageVO.setTotalElements(recordCount);
        return new ResponseEntity<>(RepositoryPageVO, HttpStatus.OK);
    }

    @ApiOperation(value = "根据orgId或者orgName获取Repository列表")
    @RequestMapping(value = "/repos/condition", method = RequestMethod.GET)
    public ResponseEntity<List<RepositoryEntity>> getRepositoriesByOrdId(@RequestParam(required = false) Long orgId,
                                                                         @RequestParam(required = false) String orgName) {
        List<RepositoryEntity> repositories = null;
        if(orgId != null){
            repositories = repositoryService.getByOrg(orgId);

        }else if(orgName != null && orgId == null){
            repositories = repositoryService.getByOrgName(orgName);
        }
        return new ResponseEntity<>(repositories, HttpStatus.OK);

    }

    @ApiOperation(value = "根据id获取Repository")
    @RequestMapping(value = "/repo/{id}", method = RequestMethod.GET)
    public ResponseEntity<RepositoryEntity> getRepository(@PathVariable Long id) {
        RepositoryEntity repository = repositoryService.getById(id);
        return new ResponseEntity<>(repository, HttpStatus.OK);
    }

    @ApiOperation(value = "更新Repository")
    @RequestMapping(value = "/repo", method = RequestMethod.PUT)
    public ResponseEntity<String> updateRepository(@RequestBody RepositoryEntity repository) {
        repositoryService.update(repository);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @ApiOperation(value = "添加Repository")
    @RequestMapping(value = "/repo", method = RequestMethod.POST)
    public ResponseEntity<String> addRepository(@RequestBody RepositoryEntity repository) {
        repositoryService.insert(repository);
        return new ResponseEntity<>("add success", HttpStatus.OK);
    }

    @ApiOperation(value = "删除Repository")
    @RequestMapping(value = "/repo/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRepository(@PathVariable Long id) {
        String userName = null;
        UserEntity user = RequestUtil.parseJWTTokenFromHeader("jwt-token");
        if (user != null) {
            userName = user.getName();
        }
        repositoryService.delete(id, userName);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

}
