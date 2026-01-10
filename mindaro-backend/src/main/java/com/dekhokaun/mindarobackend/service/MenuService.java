package com.dekhokaun.mindarobackend.service;

import com.dekhokaun.mindarobackend.exception.InvalidRequestException;
import com.dekhokaun.mindarobackend.model.Menu;
import com.dekhokaun.mindarobackend.payload.request.MenuRequest;
import com.dekhokaun.mindarobackend.payload.response.MenuResponse;
import com.dekhokaun.mindarobackend.repository.MenuRepository;
import com.dekhokaun.mindarobackend.utils.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuResponse addMenuItem(MenuRequest request) {
        Menu menu = new Menu();
        
        // Set fields from request
        menu.setText(request.getText());
        menu.setIcon(request.getIcon() != null ? request.getIcon() : "");
        menu.setAction(request.getAction() != null ? request.getAction() : "");
        menu.setLink(request.getLink() != null ? request.getLink() : "");
        menu.setOrderx(request.getOrder());
        
        // Set required fields with default values
        menu.setCode(0); // Default code
        menu.setStatus(1); // Default active status
        
        menuRepository.save(menu);
        return ObjectMapperUtils.map(menu, MenuResponse.class);
    }

    public List<MenuResponse> getAllMenuItems() {
        return menuRepository.findAll().stream()
                .map(menu -> ObjectMapperUtils.map(menu, MenuResponse.class))
                .collect(Collectors.toList());
    }

    public MenuResponse getMenuItem(String text) {
        Menu menu = menuRepository.findByText(text)
                .orElseThrow(() -> new InvalidRequestException("Menu item not found"));
        return ObjectMapperUtils.map(menu, MenuResponse.class);
    }

    public void updateMenu(String text, MenuRequest request) {
        Menu menu = menuRepository.findByText(text)
                .orElseThrow(() -> new InvalidRequestException("Menu item not found"));

        menu.setIcon(request.getIcon());
        menu.setAction(request.getAction());
        menu.setLink(request.getLink());
        menuRepository.save(menu);
    }

    public void deleteMenu(String text) {
        menuRepository.deleteByText(text);
    }
}
