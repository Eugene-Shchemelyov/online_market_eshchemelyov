package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.ItemService;
import com.gmail.eugene.shchemelyov.market.service.model.NewOrderDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/private/items")
    public String getItemsPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        Pagination pagination = itemService.getLimitItems(page);
        model.addAttribute("pagination", pagination);
        model.addAttribute("order", new NewOrderDTO());
        return "item/all";
    }

    @GetMapping("/private/items/{id}")
    public String getItemPage(
            Model model,
            @PathVariable("id") Long id
    ) {
        ViewItemDTO viewItemDTO = itemService.getById(id);
        model.addAttribute("item", viewItemDTO);
        return "item/current";
    }

    @GetMapping("/private/items/{id}/copy")
    public String copyItem(
            @PathVariable("id") Long id
    ) {
        itemService.copyById(id);
        return "redirect:/private/items";
    }

    @GetMapping("/private/items/{id}/delete")
    public String deleteItem(
            @PathVariable("id") Long id
    ) {
        itemService.deleteById(id);
        return "redirect:/private/items";
    }
}
