package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.ViewArticleService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import com.gmail.eugene.shchemelyov.market.service.model.NewArticleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewArticleDTO;
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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ApiArticleController extends ApiExceptionController {
    private final ArticleService articleService;
    private final ViewArticleService viewArticleService;

    @Autowired
    public ApiArticleController(
            ArticleService articleService,
            ViewArticleService viewArticleService
    ) {
        this.articleService = articleService;
        this.viewArticleService = viewArticleService;
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/articles")
    public List<ViewArticleDTO> showAllArticles() {
        return viewArticleService.getArticles();
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping(value = "/api/v1/articles/{id}")
    public ViewArticleDTO showArticleById(@PathVariable("id") Long id) {
        return viewArticleService.getById(id);
    }

    @Secured(value = {SECURE_REST_API})
    @SuppressWarnings(value = "unchecked")
    @PostMapping(value = "/api/v1/articles", consumes = APPLICATION_JSON_VALUE)
    public NewArticleDTO addArticle(@Valid @RequestBody NewArticleDTO newArticleDTO) {
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        return articleService.add(newArticleDTO, userId);
    }

    @Secured(value = {SECURE_REST_API})
    @DeleteMapping(value = "/api/v1/articles/{id}")
    public ResponseEntity<HttpStatus> deleteArticleById(@PathVariable("id") Long id) {
        articleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
