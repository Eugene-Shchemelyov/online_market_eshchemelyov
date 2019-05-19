package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/private/administrator/reviews")
    public String showReviews(
            Model model,
            @RequestParam(value = "message", required = false) Boolean message,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page
    ) {
        Pagination pagination = reviewService.getLimitReviews(page);
        model.addAttribute("pagination", pagination);
        model.addAttribute("message", message);
        return "review/allForAdministrator";
    }

    @PostMapping("/private/administrator/reviews/display")
    public String changeViewReviews(
            @RequestParam(value = "reviewsId", required = false) List<Long> reviewsId
    ) {
        if (reviewsId != null) {
            reviewService.changeReviewsDisplay(reviewsId);
        }
        return "redirect:/private/administrator/reviews";
    }

    @GetMapping("/private/administrator/reviews/delete")
    public String deleteReviews(
            @RequestParam(value = "id") Long id
    ) {
        reviewService.delete(id);
        return "redirect:/private/administrator/reviews?message=true";
    }
}
