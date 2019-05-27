package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class ApiArticleController {
    private final ArticleService articleService;

    @Autowired
    public ApiArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/articles")
    public List<ArticleDTO> showAllArticles() {
        return articleService.getArticles();
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping(value = "/api/v1/articles/{id}")
    public ArticleDTO showArticleById(@PathVariable("id") Long id) {
        return articleService.getById(id);
    }

    @Secured(value = {SECURE_REST_API})
    @PostMapping(value = "/api/v1/articles", consumes = APPLICATION_JSON)
    public ArticleDTO addArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        return articleService.add(articleDTO, userId);
    }

    @Secured(value = {SECURE_REST_API})
    @DeleteMapping(value = "/api/v1/articles/{id}")
    public ResponseEntity<HttpStatus> deleteArticleById(@PathVariable("id") Long id) {
        articleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
