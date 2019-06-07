package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.model.NewItemDTO;
import com.gmail.eugene.shchemelyov.market.service.model.PreviewItemDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewItemDTO;

import java.util.List;

public interface ItemService {
    Pagination getLimitItems(Integer page);

    ViewItemDTO getById(Long id);

    void copyById(Long id);

    void deleteById(Long id);

    List<ViewItemDTO> getItems();

    NewItemDTO add(NewItemDTO newItemDTO);
}
