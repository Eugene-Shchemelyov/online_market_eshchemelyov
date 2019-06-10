package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.ViewArticleService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import com.gmail.eugene.shchemelyov.market.service.model.NewArticleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateArticleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewArticleDTO;
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
    private final ViewArticleService viewArticleService;

    @Autowired
    public ArticleController(
            ArticleService articleService,
            ViewArticleService viewArticleService
    ) {
        this.articleService = articleService;
        this.viewArticleService = viewArticleService;
    }

    @GetMapping("/private/articles")
    public String getArticlesPage(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "sort", required = false, defaultValue = "DATE_DESC") SortEnum sort
    ) {
        Pagination pagination = viewArticleService.getLimitArticles(page, sort);
        model.addAttribute("pagination", pagination);
        return "article/all";
    }

    @GetMapping("/private/articles/{id}")
    public String getArticlePage(
            Model model,
            @PathVariable("id") Long id
    ) {
        articleService.incrementCountViews(id);
        ViewArticleDTO viewArticleDTO = viewArticleService.getById(id);
        model.addAttribute("article", viewArticleDTO);
        return "article/current";
    }

    @GetMapping("/private/articles/{id}/update")
    public String getUpdateArticlePage(
            Model model,
            @PathVariable("id") Long id
    ) {
        UpdateArticleDTO updateArticleDTO = articleService.getById(id);
        model.addAttribute("article", updateArticleDTO);
        return "article/update";
    }

    @PostMapping("/private/articles/{id}/update")
    public String updateArticle(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("article") UpdateArticleDTO updateArticleDTO,
            BindingResult bindingResult
    ) {
        updateArticleDTO.setId(id);
        if (bindingResult.hasErrors()) {
            return "article/update";
        }
        articleService.update(updateArticleDTO);
        return "redirect:/private/articles";
    }

    @GetMapping("/private/articles/article/new")
    public String getNewArticlePage(Model model) {
        model.addAttribute("article", new NewArticleDTO());
        return "article/new";
    }

    @PostMapping("/private/articles/article/new")
    public String newArticle(
            @Valid @ModelAttribute("article") NewArticleDTO newArticleDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "article/new";
        }
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        articleService.add(newArticleDTO, userId);
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
