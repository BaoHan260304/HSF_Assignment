package base.api.controller;

import base.api.base.BaseAPIController;
import base.api.dto.request.AuthRequest;
import base.api.dto.request.CreateCategoryDto;
import base.api.dto.response.AuthResponse;
import base.api.dto.response.TFUResponse;
import base.api.model.CategoryModel;
import base.api.model.ProductModel;
import base.api.model.user.UserModel;
import base.api.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController extends BaseAPIController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/create-category")
    public ResponseEntity<TFUResponse<CategoryModel>> createCategory(
            @RequestBody CreateCategoryDto dto) {
        try {
            Long userId = getCurrentUserId();
            dto.setUserId(userId);
            CategoryModel categoryModel = categoryService.create(dto);
            return success(categoryModel);
        } catch (Exception e) {
            return badRequest("Không tạo được category");
        }
    }

    @PostMapping("create-category-v2")
    public ResponseEntity<CategoryModel> createCategoryV2(@RequestBody CreateCategoryDto dto) {
        CategoryModel categoryModel = categoryService.create(dto);
        return ResponseEntity.ok(categoryModel);
    }


    @GetMapping("get-category-by-id")
    public ResponseEntity<TFUResponse<CategoryModel>> getById(
            @RequestParam Long id) {
        try {
            CategoryModel categoryModel = categoryService.getById(id);
            if (categoryModel == null) {
                return badRequest("Không tìm thấy category");
            }
            return success(categoryModel);
        } catch (BadCredentialsException e) {
            return badRequest("Không tìm thấy category");
        }
    }


}
