package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
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

@RestController
public class ApiController {
    private final UserService userService;
    private final ArticleService articleService;

    @Autowired
    public ApiController(
            UserService userService,
            ArticleService articleService
    ) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @Secured(value = {SECURE_REST_API})
    @PostMapping(value = "/api/v1/users", consumes = "application/json")
    public ResponseEntity<HttpStatus> addUserDTO(@Valid @RequestBody UserDTO userDTO) {
        userService.add(userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/articles")
    public List<ArticleDTO> showAllArticles() {
        return articleService.getArticles();
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping(value = "/api/v1/articles/{id}", consumes = "application/json")
    public ArticleDTO showArticleById(@PathVariable("id") Long id) {
        return articleService.getById(id);
    }

    @Secured(value = {SECURE_REST_API})
    @PostMapping(value = "/api/v1/articles", consumes = "application/json")
    public ResponseEntity<HttpStatus> addArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        articleService.add(articleDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured(value = {SECURE_REST_API})
    @DeleteMapping(value = "/api/v1/articles/{id}", consumes = "application/json")
    public ResponseEntity<HttpStatus> deleteArticleById(@PathVariable("id") Long id) {
        articleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
