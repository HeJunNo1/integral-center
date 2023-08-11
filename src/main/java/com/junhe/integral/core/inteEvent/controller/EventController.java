package com.junhe.integral.core.inteEvent.controller;

import com.junhe.integral.common.PageData;
import com.junhe.integral.common.Result;
import com.junhe.integral.core.inteEvent.dto.IntegralEventDTO;
import com.junhe.integral.core.inteEvent.service.EventService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 事件管理控制器
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/7
 */
@RestController
@RequestMapping("/event")
public class EventController {

    @Resource
    private EventService eventService;

    @PostMapping
    public Result add(@RequestBody IntegralEventDTO dto) {
        eventService.add(dto);
        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody IntegralEventDTO dto) {
        eventService.update(dto);
        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody IntegralEventDTO dto) {
        eventService.delete(dto);
        return new Result();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable("id") Long id) {
        return new Result().ok(eventService.get(id));
    }

    @GetMapping("/page")
    public Result page(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                       @RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
                       IntegralEventDTO params){
        PageData<IntegralEventDTO> data = eventService.page(page, limit, params);
        return new Result().ok(data);
    }
}
