package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.ItemService;
import com.gmail.eugene.shchemelyov.market.service.model.NewItemDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
public class ApiItemController extends ApiExceptionController {
    private final ItemService itemService;

    @Autowired
    public ApiItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/items")
    public List<ViewItemDTO> showAllItems() {
        return itemService.getItems();
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/items/{id}")
    public ViewItemDTO showItemById(@PathVariable("id") Long id) {
        return itemService.getById(id);
    }

    @Secured(value = {SECURE_REST_API})
    @PostMapping(value = "/api/v1/items", consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public NewItemDTO addItem(@Valid @RequestBody NewItemDTO newItemDTO) {
        return itemService.add(newItemDTO);
    }

    @Secured(value = {SECURE_REST_API})
    @DeleteMapping("/api/v1/items/{id}")
    public ResponseEntity<HttpStatus> deleteItemById(@PathVariable("id") Long id) {
        itemService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
