package com.ppdai.dockeryard.admin.controller;

import com.ppdai.dockeryard.admin.service.ImageService;
import com.ppdai.dockeryard.admin.util.RequestUtil;
import com.ppdai.dockeryard.admin.vo.PageVO;
import com.ppdai.dockeryard.core.dto.ImageDto;
import com.ppdai.dockeryard.core.po.ImageEntity;
import com.ppdai.dockeryard.core.po.UserEntity;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageController {

    private static Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;


    @ApiOperation(value = "获取所有image列表")
    @RequestMapping(value = "/image/all", method = RequestMethod.GET)
    public ResponseEntity<List<ImageEntity>> getAllImages() {
        List<ImageEntity> images = imageService.getAll();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @ApiOperation(value = "根据获取image列表")
    @RequestMapping(value = "/images", method = RequestMethod.GET)
    public ResponseEntity<PageVO<ImageEntity>> getImagesByParam(@RequestParam(required = false) Long repoId,
                                                                @RequestParam(required = false) String repoName,
                                                                @RequestParam(required = false) Long orgId,
                                                                @RequestParam(required = false) String orgName,
                                                                @RequestParam(required = false) String appName,
                                                                @RequestParam(required = false) String tag,
                                                                @RequestParam(required = false) Integer page,
                                                                @RequestParam(required = false) Integer size) {
        ImageDto imageDto = new ImageDto();
        imageDto.setRepoId(repoId);
        imageDto.setRepoName(repoName);
        imageDto.setOrgId(orgId);
        imageDto.setOrgName(orgName);
        imageDto.setAppName(appName);
        imageDto.setTag(tag);
        imageDto.setPageNo(page);
        imageDto.setPageSize(size);
        List<ImageEntity> images = null;
        int recordCount = 0;
        images = imageService.getByParam(imageDto);
        recordCount = imageService.getRecordCountByParam(imageDto);
        PageVO<ImageEntity> imagesPageVO = new PageVO<>();
        imagesPageVO.setContent(images);
        imagesPageVO.setTotalElements(recordCount);
        return new ResponseEntity<>(imagesPageVO, HttpStatus.OK);
    }

    @ApiOperation(value = "根据id获取image")
    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public ResponseEntity<ImageEntity> getImage(@PathVariable Long id) {
        ImageEntity image = imageService.getById(id);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @ApiOperation(value = "根据appName获取image列表")
    @RequestMapping(value = "/image/app", method = RequestMethod.GET)
    public ResponseEntity<List<ImageEntity>> getImageByAppName(@RequestParam String appName) {
        List<ImageEntity> images = imageService.getByAppName(appName);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @ApiOperation(value = "更新image")
    @RequestMapping(value = "/image", method = RequestMethod.PUT)
    public ResponseEntity<String> updateImage(@RequestBody ImageEntity image) {
        String userName = null;
        UserEntity user = RequestUtil.parseJWTTokenFromHeader("jwt-token");
        if(user != null) {
            userName = user.getName();
        }
        image.setUpdateBy(userName);
        imageService.update(image);
        return new ResponseEntity<>("add success", HttpStatus.OK);
    }

    @ApiOperation(value = "保存image")
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public ResponseEntity<String> addImage(@RequestBody ImageEntity image) {
        String userName = null;
        UserEntity user = RequestUtil.parseJWTTokenFromHeader("jwt-token");
        if(user != null) {
            userName = user.getName();
        }
        image.setInsertBy(userName);
        image.setUpdateBy(userName);
        imageService.insert(image);
        return new ResponseEntity<>("update success", HttpStatus.OK);
    }

    @ApiOperation(value = "删除image")
    @RequestMapping(value = "/image/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        String userName = "stargate";
        UserEntity user = RequestUtil.parseJWTTokenFromHeader("jwt-token");
        if (user != null) {
            userName = user.getName();
        }
        imageService.delete(id, userName);
        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

}
