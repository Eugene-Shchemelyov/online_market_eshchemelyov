package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.service.converter.PreviewItemConverter;
import com.gmail.eugene.shchemelyov.market.service.model.PreviewItemDTO;
import org.springframework.stereotype.Component;

@Component
public class PreviewItemConverterImpl implements PreviewItemConverter {
    @Override
    public PreviewItemDTO toDTO(Item item) {
        PreviewItemDTO previewItemDTO = new PreviewItemDTO();
        previewItemDTO.setId(item.getId());
        previewItemDTO.setName(item.getName());
        previewItemDTO.setUniqueNumber(item.getUniqueNumber());
        previewItemDTO.setPrice(item.getPrice());
        return previewItemDTO;
    }

    @Override
    public Item toEntity(PreviewItemDTO previewItemDTO) {
        Item item = new Item();
        item.setId(previewItemDTO.getId());
        item.setName(previewItemDTO.getName());
        item.setUniqueNumber(previewItemDTO.getUniqueNumber());
        item.setPrice(previewItemDTO.getPrice());
        return item;
    }
}
