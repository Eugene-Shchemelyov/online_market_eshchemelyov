package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.ItemService;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.converter.ItemConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final PaginationService paginationService;
    private final ItemConverter itemConverter;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(
            PaginationService paginationService,
            ItemConverter itemConverter,
            ItemRepository itemRepository
    ) {
        this.paginationService = paginationService;
        this.itemConverter = itemConverter;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Pagination getLimitItems(Integer page) {
        Pagination pagination = paginationService.getItemPagination(page);
        List<Item> items = itemRepository.getLimitItems(pagination);
        List<ItemDTO> itemDTOS = items.stream()
                .map(itemConverter::toDTO)
                .collect(Collectors.toList());
        pagination.setEntities(itemDTOS);
        return pagination;
    }

    @Override
    @Transactional
    public ItemDTO getById(Long id) {
        Item item = itemRepository.getById(id);
        return itemConverter.toDTO(item);
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
    public List<ItemDTO> getItems() {
        List<Item> items = itemRepository.getAllEntities();
        return items.stream()
                .map(itemConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDTO add(ItemDTO itemDTO) {
        Item item = itemConverter.toEntity(itemDTO);
        if (item.getUniqueNumber() == null) {
            item.setUniqueNumber(UUID.randomUUID().toString());
        }
        itemRepository.create(item);
        return itemConverter.toDTO(item);
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
