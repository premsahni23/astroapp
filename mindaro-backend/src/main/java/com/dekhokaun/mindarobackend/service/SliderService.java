package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.Slider;
import com.dekhokaun.mindarobackend.payload.request.SliderRequest;
import com.dekhokaun.mindarobackend.payload.response.SliderResponse;
import com.dekhokaun.mindarobackend.repository.SliderRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SliderService {

    private final SliderRepository sliderRepository;

    public SliderService(SliderRepository sliderRepository) {
        this.sliderRepository = sliderRepository;
    }

    public SliderResponse addSlider(SliderRequest request) {
        Slider slider = new Slider();
        
        // Set fields from request
        slider.setImagename(request.getImageUrl());
        
        // Set default values for other fields
        slider.setFbid(""); // Default empty string
        slider.setCode(0); // Default code
        slider.setAction(""); // Default action
        slider.setLink(""); // Default link
        slider.setOrderx(0); // Default order
        slider.setStatus(1); // Default active status
        
        sliderRepository.save(slider);
        return mapToResponse(slider);
    }

    public List<SliderResponse> getAllSliders() {
        return sliderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SliderResponse getSliderById(String id) {
        Slider slider = sliderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new InvalidRequestException("Slider not found"));
        return mapToResponse(slider);
    }

    public void deleteSlider(String id) {
        sliderRepository.deleteById(UUID.fromString(id));
    }

    public SliderResponse updateSlider(String id, SliderRequest request) {
        Slider slider = sliderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new InvalidRequestException("Slider not found"));

        slider.setImagename(request.getImageUrl());
        slider.setAction(request.getTitle());
        slider.setLink(request.getDescription());

        Slider updated = sliderRepository.save(slider);
        return mapToResponse(updated);
    }

    private SliderResponse mapToResponse(Slider slider) {
        SliderResponse response = new SliderResponse();
        response.setId(slider.getId() != null ? slider.getId().toString() : null);
        response.setImageUrl(slider.getImagename());
        response.setTitle(slider.getAction());
        response.setDescription(slider.getLink());
        return response;
    }
}
