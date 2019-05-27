package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

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
        return "article/current";
    }

    @GetMapping("/private/articles/{id}/update")
    public String getUpdateArticlePage(
            Model model,
            @PathVariable("id") Long id
    ) {
        ArticleDTO articleDTO = articleService.getById(id);
        model.addAttribute("article", articleDTO);
        return "article/update";
    }

    @PostMapping("/private/articles/{id}/update")
    public String updateArticle(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("article") ArticleDTO articleDTO,
            BindingResult bindingResult
    ) {
        articleDTO.setId(id);
        if (bindingResult.hasErrors()) {
            return "article/update";
        }
        articleService.update(articleDTO);
        return "redirect:/private/articles";
    }

    @GetMapping("/private/articles/article/new")
    public String getNewArticlePage(Model model) {
        model.addAttribute("article", new ArticleDTO());
        return "article/new";
    }

    @PostMapping("/private/articles/article/new")
    public String newArticle(
            @Valid @ModelAttribute("article") ArticleDTO articleDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "article/update";
        }
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        articleService.add(articleDTO, userId);
        return "redirect:/private/articles";
    }

    @GetMapping("/private/articles/{id}/delete")
    public String deleteArticle(
            @PathVariable("id") Long id
    ) {
        articleService.deleteById(id);
        return "redirect:/private/articles";
    }
}
