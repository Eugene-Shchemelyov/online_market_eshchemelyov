package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.ItemService;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.converter.NewItemConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.PreviewItemConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.ViewItemConverter;
import com.gmail.eugene.shchemelyov.market.service.model.NewItemDTO;
import com.gmail.eugene.shchemelyov.market.service.model.PreviewItemDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final PaginationService paginationService;
    private final PreviewItemConverter previewItemConverter;
    private final ItemRepository itemRepository;
    private final ViewItemConverter viewItemConverter;
    private final NewItemConverter newItemConverter;

    @Autowired
    public ItemServiceImpl(
            PaginationService paginationService,
            PreviewItemConverter previewItemConverter,
            ItemRepository itemRepository,
            ViewItemConverter viewItemConverter,
            NewItemConverter newItemConverter
    ) {
        this.paginationService = paginationService;
        this.previewItemConverter = previewItemConverter;
        this.itemRepository = itemRepository;
        this.viewItemConverter = viewItemConverter;
        this.newItemConverter = newItemConverter;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Pagination getLimitItems(Integer page) {
        Pagination pagination = paginationService.getItemPagination(page);
        List<Item> items = itemRepository.getLimitItems(pagination, false);
        List<PreviewItemDTO> previewItemDTOS = items.stream()
                .map(previewItemConverter::toDTO)
                .collect(Collectors.toList());
        pagination.setEntities(previewItemDTOS);
        return pagination;
    }

    @Override
    @Transactional
    public ViewItemDTO getById(Long id) {
        Item item = itemRepository.getById(id);
        return viewItemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public void copyById(Long id) {
        Item item = itemRepository.getById(id);
        Item clonedItem = clone(item);
        itemRepository.create(clonedItem);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<ViewItemDTO> getItems() {
        List<Item> items = itemRepository.getAllEntities(false);
        return items.stream()
                .map(viewItemConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NewItemDTO add(NewItemDTO newItemDTO) {
        if (newItemDTO.getUniqueNumber() == null) {
            newItemDTO.setUniqueNumber(UUID.randomUUID().toString());
        }
        if (itemRepository.getCountByUniqueNumber(newItemDTO.getUniqueNumber()) > 0) {
            throw new ExpectedException(String.format(
                    "Item exists with unique number: %s.", newItemDTO.getUniqueNumber()));
        }
        Item item = newItemConverter.toEntity(newItemDTO);
        itemRepository.create(item);
        return newItemDTO;
    }

    private Item clone(Item item) {
        Item clonedItem = new Item();
        clonedItem.setName(item.getName());
        clonedItem.setDescription(item.getDescription());
        clonedItem.setUniqueNumber(UUID.randomUUID().toString());
        clonedItem.setPrice(item.getPrice());
        return clonedItem;
    }
}
