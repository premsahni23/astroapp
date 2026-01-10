package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.model.Favorite;
import com.dekhokaun.mindarobackend.payload.request.FavoriteRequest;
import com.dekhokaun.mindarobackend.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorites", description = "Favorite mentors APIs")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(summary = "Add favorite")
    @PostMapping
    public ResponseEntity<Favorite> add(@Valid @RequestBody FavoriteRequest request) {
        return ResponseEntity.ok(favoriteService.add(request));
    }

    @Operation(summary = "Get favorites")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Favorite>> list(@PathVariable UUID userId) {
        return ResponseEntity.ok(favoriteService.list(userId));
    }

    @Operation(summary = "Get favorites (alias)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>> listAlias(@PathVariable UUID userId) {
        return ResponseEntity.ok(favoriteService.list(userId));
    }

    @Operation(summary = "Remove favorite")
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> delete(@PathVariable UUID favoriteId) {
        favoriteService.delete(favoriteId);
        return ResponseEntity.noContent().build();
    }
}
