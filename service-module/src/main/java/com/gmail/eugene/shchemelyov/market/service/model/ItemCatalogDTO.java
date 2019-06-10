package com.gmail.eugene.shchemelyov.market.service.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "itemCatalog")
public class ItemCatalogDTO {
    private List<NewItemDTO> items;

    public List<NewItemDTO> getItems() {
        return items;
    }

    @XmlElement(name = "item")
    public void setItems(List<NewItemDTO> items) {
        this.items = items;
    }
}
