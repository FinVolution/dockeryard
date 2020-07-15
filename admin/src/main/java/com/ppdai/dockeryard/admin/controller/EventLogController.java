package com.ppdai.dockeryard.admin.controller;

import com.ppdai.dockeryard.admin.service.EventLogService;
import com.ppdai.dockeryard.admin.vo.PageVO;
import com.ppdai.dockeryard.core.dto.EventLogDto;
import com.ppdai.dockeryard.core.po.EventLogEntity;
import com.ppdai.dockeryard.core.po.ImageEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EventLogController {

    private static final String ARRAY_SEPARATOR = ",";

    @Autowired
    private EventLogService eventLogService;

    @ApiOperation(value = "根据条件获取EventLog列表")
    @RequestMapping(value = "/logs", method = RequestMethod.GET)
    public ResponseEntity<PageVO<EventLogEntity>> getEventLogsByParam(@RequestParam(required = false) String eventName,
                                                                      @RequestParam(required = false) String eventAddition,
                                                                      @RequestParam(required = false) String optionType,
                                                                      @RequestParam(required = false) String operator,
                                                                      @RequestParam(required = false) String operateTime,
                                                                      @RequestParam(required = false) Integer page,
                                                                      @RequestParam(required = false) Integer size) {
        EventLogDto eventLogDto = new EventLogDto();
        eventLogDto.setEventName(eventName);
        eventLogDto.setEventAddition(eventAddition);
        eventLogDto.setOptionType(optionType);
        eventLogDto.setOperator(operator);
        eventLogDto.setPageNo(page);
        eventLogDto.setPageSize(size);
        if(StringUtils.hasLength(operateTime) && operateTime.contains(EventLogController.ARRAY_SEPARATOR)){
            String[] timeArray = operateTime.split(EventLogController.ARRAY_SEPARATOR);
            eventLogDto.setStartDateTime(timeArray[0]);
            eventLogDto.setEndDateTime(timeArray[1]);
        }
        List<EventLogEntity> logs = eventLogService.getByParam(eventLogDto);
        int recordCount = eventLogService.getRecordCountByParam(eventLogDto);
        PageVO<EventLogEntity> eventLogPageVO = new PageVO<>();
        eventLogPageVO.setContent(logs);
        eventLogPageVO.setTotalElements(recordCount);
        return new ResponseEntity<>(eventLogPageVO, HttpStatus.OK);
    }
}
