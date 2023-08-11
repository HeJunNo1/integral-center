package com.junhe.integral.core.category.controller;

import com.junhe.integral.common.Result;
import com.junhe.integral.core.category.dto.CategoryDTO;
import com.junhe.integral.core.category.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 积分分类控制器
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/21
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询积分分类列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        return new Result();
    }

    @PostMapping()
    public Result add(@RequestBody CategoryDTO dto){
        categoryService.add(dto);
        return new Result();
    }

    @PutMapping()
    public Result update(@RequestBody CategoryDTO dto){
        categoryService.update(dto);
        return new Result();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        categoryService.delete(id);
        return new Result();
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable("id") Long id){
        CategoryDTO dto = categoryService.getById(id);
        return new Result().ok(dto);
    }
}
