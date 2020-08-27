package com.ppdai.dockeryard.admin.controller;

import com.ppdai.atlas.api.OrgControllerApiClient;
import com.ppdai.atlas.model.OrgDto;
import com.ppdai.atlas.model.ResponseListOrgDto;
import com.ppdai.dockeryard.admin.service.OrganizationService;
import com.ppdai.dockeryard.core.po.OrganizationEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrgControllerApiClient orgControllerApiClient;


    @ApiOperation(value = "获取所有Organization列表")
    @RequestMapping(value = "/org/all", method = RequestMethod.GET)
    public ResponseEntity<List<OrganizationEntity>> getAllOrganizations() {
        List<OrganizationEntity> organizationEntities = organizationService.getAll();
        return new ResponseEntity<>(organizationEntities, HttpStatus.OK);
    }

    @ApiOperation(value = "根据id获取Organization")
    @RequestMapping(value = "/org/{id}", method = RequestMethod.GET)
    public ResponseEntity<OrganizationEntity> getOrganization(@PathVariable Long id) {
        OrganizationEntity organizationEntity = organizationService.getById(id);
        return new ResponseEntity<>(organizationEntity, HttpStatus.OK);
    }

    @ApiOperation(value = "添加Organization")
    @RequestMapping(value = "/org", method = RequestMethod.POST)
    public ResponseEntity<String> addOrganization(@RequestBody OrganizationEntity organization) {
        organizationService.insert(organization);
        return new ResponseEntity<>("add success", HttpStatus.OK);
    }

    @ApiOperation(value = "更新Organization")
    @RequestMapping(value = "/org", method = RequestMethod.PUT)
    public ResponseEntity<String> updateOrganization(@RequestBody OrganizationEntity organization) {
        organizationService.update(organization);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @ApiOperation(value = "删除Organization")
    @RequestMapping(value = "/org/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
        organizationService.delete(id);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

    @ApiOperation(value = "从atlas同步Organization")
    @RequestMapping(value = "/orgs/sync", method = RequestMethod.GET)
    public ResponseEntity<String> syncOrganizations() {
        ResponseEntity<ResponseListOrgDto> remoteResponse = orgControllerApiClient.getAllOrgsUsingGET();
        ResponseListOrgDto remoteList = remoteResponse.getBody();
        List<OrgDto> remoteOrgList = remoteList.getDetail();
        organizationService.syncOrganizations(remoteOrgList);
        return new ResponseEntity<>("sync organizations success", HttpStatus.OK);
    }

}
