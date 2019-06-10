package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.ReviewService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateReviewConstant.MAX_REVIEW_SIZE;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateReviewConstant.NOT_VALID_REVIEW_TEXT_MESSAGE;

@Controller
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/private/reviews")
    public String showReviews(
            Model model,
            @RequestParam(value = "message", required = false) Boolean message,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page
    ) {
        Pagination pagination = reviewService.getLimitReviews(page);
        model.addAttribute("pagination", pagination);
        model.addAttribute("message", message);
        return "review/all";
    }

    @PostMapping("/private/reviews/display")
    public String changeViewReviews(
            @RequestParam(value = "reviewsId", required = false) List<Long> reviewsId
    ) {
        if (reviewsId != null) {
            reviewService.changeReviewsDisplay(reviewsId);
        }
        return "redirect:/private/reviews";
    }

    @GetMapping("/private/reviews/{id}/delete")
    public String deleteReviews(
            @PathVariable(value = "id") Long id
    ) {
        reviewService.delete(id);
        return "redirect:/private/reviews?message=true";
    }

    @GetMapping("/private/reviews/review/new")
    public String showReviewPage() {
        return "review/new";
    }

    @PostMapping("/private/reviews/review/new")
    public String newReview(
            Model model,
            @RequestParam(value = "review", required = false) String reviewText
    ) {
        if (reviewText.length() > MAX_REVIEW_SIZE || reviewText.trim().isEmpty()) {
            model.addAttribute("error",NOT_VALID_REVIEW_TEXT_MESSAGE + MAX_REVIEW_SIZE);
            return "review/new";
        }
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        reviewService.add(reviewText, userId);
        return "redirect:/private/items";
    }
}
