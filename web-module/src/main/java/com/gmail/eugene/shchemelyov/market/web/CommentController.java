package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/private/comments/{id}/delete")
    public String deleteComment(
            @PathVariable("id") Long commentId,
            @RequestParam("articleId") Long articleId
    ) {
        commentService.deleteById(commentId);
        return "redirect:/private/articles/" + articleId;
    }
}
