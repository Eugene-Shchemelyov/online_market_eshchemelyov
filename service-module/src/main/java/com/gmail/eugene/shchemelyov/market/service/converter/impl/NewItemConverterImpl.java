package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.service.converter.NewItemConverter;
import com.gmail.eugene.shchemelyov.market.service.model.NewItemDTO;
import org.springframework.stereotype.Component;

@Component
public class NewItemConverterImpl implements NewItemConverter {
    @Override
    public Item toEntity(NewItemDTO newItemDTO) {
        Item item = new Item();
        item.setName(newItemDTO.getName());
        item.setPrice(newItemDTO.getPrice());
        item.setDescription(newItemDTO.getDescription());
        item.setUniqueNumber(newItemDTO.getUniqueNumber());
        return item;
    }
}
