package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/private/articles")
    public String getArticlesPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "sort", required = false, defaultValue = "DATE_DESC") SortEnum sort
    ) {
        Pagination pagination = articleService.getLimitArticles(page, sort);
        model.addAttribute("pagination", pagination);
        return "article/all";
    }

    @GetMapping("/private/articles/{id}")
    public String getArticlePage(
            Model model,
            @PathVariable("id") Long id
    ) {
        articleService.updateCountViews(id);
        ArticleDTO articleDTO = articleService.getById(id);
        model.addAttribute("article", articleDTO);
        return "article/display";
    }
}
