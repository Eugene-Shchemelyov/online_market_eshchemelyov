package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.CommentService;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final PaginationService paginationService;

    @Autowired
    public CommentController(
            CommentService commentService,
            PaginationService paginationService
    ) {
        this.commentService = commentService;
        this.paginationService = paginationService;
    }

    @GetMapping(value = {"/private/comments", "/private/comments/{page}"})
    public String showComments(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable(required = false) Integer page
    ) {
        Pagination pagination = paginationService.getCommentPagination(page);
        List<CommentDTO> comments = commentService.getComments(pagination);
        model.addAttribute("comments", comments);
        model.addAttribute("pagination", pagination);
        String message = httpServletRequest.getParameter("message");
        if (message != null) {
            model.addAttribute("message", Boolean.parseBoolean(message));
        }
        return "comment/allForAdministrator";
    }

    @PostMapping("/private/comments/display")
    public String changeViewComments(
            @RequestParam(value = "commentsId", required = false) List<Long> commentsId
    ) {
        if (commentsId != null) {
            commentService.changeCommentsDisplay(commentsId);
        }
        return "redirect:/private/comments";
    }

    @GetMapping("/private/comments/{id}/delete")
    public String deleteComment(
            @PathVariable Long id
    ) {
        commentService.delete(id);
        return "redirect:/private/comments?message=true";
    }
}
