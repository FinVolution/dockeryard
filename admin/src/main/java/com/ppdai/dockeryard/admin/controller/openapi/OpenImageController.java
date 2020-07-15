package com.ppdai.dockeryard.admin.controller.openapi;

import com.ppdai.dockeryard.admin.controller.ImageController;
import com.ppdai.dockeryard.admin.service.ImageService;
import com.ppdai.dockeryard.core.dto.ImageDto;
import com.ppdai.dockeryard.core.po.ImageEntity;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/openapi")
public class OpenImageController {

    private static Logger logger = LoggerFactory.getLogger(ImageController.class);

    public static final String DEFAULT_IMAGE_TAG = "latest";
    @Autowired
    private ImageService imageService;

    @ApiOperation(value = "给存在的镜像打新tag，实际上会生成一条新的镜像记录：镜像相同，名字不同而已。",notes = "repoName+existingTag 唯一确定一个已经存在的镜像；重新打成repoName+expectedTag")
    @PutMapping(value = "/image/retag")
    public ResponseEntity<ImageEntity> tagImage(@RequestParam String repoName,
                                                @RequestParam String existingTag,
                                                @RequestParam String expectedTag) {
        if (!StringUtils.hasLength(repoName)) {
            return new ResponseEntity("repoName cannot be null", HttpStatus.BAD_REQUEST);
        }
        if ((!StringUtils.hasText(existingTag)) ||
                (!StringUtils.hasText(existingTag))) {
            logger.warn(String.format("[TAGIMAGE]Param error.RepoName:%s ,ExistingTag:%s "));
            return new ResponseEntity("Tag info cannot be null.", HttpStatus.BAD_REQUEST);
        }
        //搜索现存镜像
        ImageDto imageDto = new ImageDto();
        imageDto.setRepoName(repoName.trim());
        imageDto.setTag(existingTag.trim());

        ImageEntity expectedImage ;
        try{
            expectedImage = imageService.tagExistingImage(imageDto,expectedTag);
        }catch (Throwable throwable){
            logger.error(throwable.getMessage());
            return new ResponseEntity(throwable.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<ImageEntity>(expectedImage, HttpStatus.OK);
    }
}
