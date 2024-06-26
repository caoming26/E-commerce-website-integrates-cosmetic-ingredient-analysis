package com.example.ecommerce.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {
    @NotNull(message = "Tên sản phẩm trống")
    @NotEmpty(message = "Tên sản phẩm trống")
    @Size(min = 1, max = 300, message = "Độ dài tên sản phẩm từ 1 - 300 ký tự")
    private String name;

    @NotNull(message = "Mô tả trống")
    @NotEmpty(message = "Mô tả trống")
    private String description;

    private int totalSold;

    private int quantity;

    @NotNull(message = "Nhãn hiệu trống")
    @JsonProperty("brand_id")
    private Integer brandId;

    @NotNull(message = "Category trống")
    @JsonProperty("category_ids")
    private ArrayList<Integer> categoryIds;

    @JsonProperty("ingredient_ids")
    private ArrayList<Integer> ingredientIds;

    private String ingredientNotFound;

    @JsonProperty("is_available")
    private boolean isAvailable;

    @Min(1000)
    @Max(1000000000)
    private Integer price;

    @NotNull(message = "Danh sách ảnh trống")
    @JsonProperty("product_images")
        private ArrayList<String> productImages;
}
