package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.MenuRequest;
import com.dekhokaun.mindarobackend.payload.response.MenuResponse;
import com.dekhokaun.mindarobackend.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu Controller", description = "APIs for managing menu items")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Operation(summary = "Create menu item", description = "Creates a new menu entry (Admin only)")
    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@Valid @RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.addMenuItem(request));
    }

    @Operation(summary = "Get all menu items", description = "Retrieves a list of all menu items")
    @GetMapping("/list")
    public ResponseEntity<List<MenuResponse>> getMenus() {
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    @Operation(summary = "Get menu item", description = "Retrieve menu by text id")
    @GetMapping("/{text}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable String text) {
        return ResponseEntity.ok(menuService.getMenuItem(text));
    }

    @Operation(summary = "Update menu item", description = "Update menu details (Admin only)")
    @PutMapping("/{text}")
    public ResponseEntity<Void> updateMenu(@PathVariable String text,
                                           @Valid @RequestBody MenuRequest request) {
        menuService.updateMenu(text, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a menu item", description = "Deletes a menu item by its text identifier")
    @DeleteMapping("/delete/{text}")
    public ResponseEntity<Void> deleteMenu(
            @Parameter(description = "Text identifier of the menu item to delete", required = true) @PathVariable String text) {
        menuService.deleteMenu(text);
        return ResponseEntity.noContent().build();
    }
}
