package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.ItemService;
import com.gmail.eugene.shchemelyov.market.service.model.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;
import static com.gmail.eugene.shchemelyov.market.web.constant.ApiConstant.APPLICATION_JSON;

@RestController
public class ApiItemController {
    private final ItemService itemService;

    @Autowired
    public ApiItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/items")
    public List<ItemDTO> showAllItems() {
        return itemService.getItems();
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/items/{id}")
    public ItemDTO showItemById(@PathVariable("id") Long id) {
        return itemService.getById(id);
    }

    @Secured(value = {SECURE_REST_API})
    @PostMapping(value = "/api/v1/items", consumes = APPLICATION_JSON)
    public ItemDTO addItem(@Valid @RequestBody ItemDTO itemDTO) {
        return itemService.add(itemDTO);
    }

    @Secured(value = {SECURE_REST_API})
    @DeleteMapping("/api/v1/items/{id}")
    public ResponseEntity<HttpStatus> deleteItemById(@PathVariable("id") Long id) {
        itemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
