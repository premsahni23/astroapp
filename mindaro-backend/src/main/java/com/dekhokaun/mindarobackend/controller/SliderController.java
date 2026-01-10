package com.dekhokaun.mindarobackend.controller;

import com.dekhokaun.mindarobackend.payload.request.SliderRequest;
import com.dekhokaun.mindarobackend.payload.response.SliderResponse;
import com.dekhokaun.mindarobackend.service.SliderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slider")
@Tag(name = "Slider Controller", description = "APIs for managing sliders")
public class SliderController {

    private final SliderService sliderService;

    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @Operation(summary = "Create slider", description = "Create a new slider (Admin only)")
    @PostMapping
    public ResponseEntity<SliderResponse> addSlider(@Valid @RequestBody SliderRequest request) {
        return ResponseEntity.ok(sliderService.addSlider(request));
    }

    @Operation(summary = "Update slider", description = "Update an existing slider (Admin only)")
    @PutMapping("/{id}")
    public ResponseEntity<SliderResponse> updateSlider(@PathVariable String id, @RequestBody SliderRequest request) {
        return ResponseEntity.ok(sliderService.updateSlider(id, request));
    }

    @Operation(summary = "Get all sliders", description = "Retrieve a list of all sliders")
    @GetMapping("/list")
    public ResponseEntity<List<SliderResponse>> getSliders() {
        return ResponseEntity.ok(sliderService.getAllSliders());
    }

    @Operation(summary = "Get slider", description = "Retrieve slider details by id")
    @GetMapping("/{id}")
    public ResponseEntity<SliderResponse> getSlider(@PathVariable String id) {
        return ResponseEntity.ok(sliderService.getSliderById(id));
    }

    @Operation(summary = "Delete slider", description = "Remove a slider by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlider(@PathVariable String id) {
        sliderService.deleteSlider(id);
        return ResponseEntity.noContent().build();
    }
}
