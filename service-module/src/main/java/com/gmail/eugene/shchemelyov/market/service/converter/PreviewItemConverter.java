package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.service.model.PreviewItemDTO;

public interface PreviewItemConverter {
    PreviewItemDTO toDTO(Item item);

    Item toEntity(PreviewItemDTO previewItemDTO);
}
