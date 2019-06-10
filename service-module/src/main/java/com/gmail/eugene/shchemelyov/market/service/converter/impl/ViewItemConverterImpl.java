package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.service.converter.ViewItemConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ViewItemDTO;
import org.springframework.stereotype.Component;

@Component
public class ViewItemConverterImpl implements ViewItemConverter {
    @Override
    public ViewItemDTO toDTO(Item item) {
        ViewItemDTO viewItemDTO = new ViewItemDTO();
        viewItemDTO.setId(item.getId());
        viewItemDTO.setName(item.getName());
        viewItemDTO.setUniqueNumber(item.getUniqueNumber());
        viewItemDTO.setPrice(item.getPrice());
        viewItemDTO.setDescription(item.getDescription());
        return viewItemDTO;
    }
}
