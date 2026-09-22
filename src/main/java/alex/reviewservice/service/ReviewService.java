package alex.reviewservice.service;

import alex.reviewservice.model.Review;
import alex.reviewservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recensionen finns inte."
                ));
    }

    public Review create(Review review) {
        review.setId(null);
        return reviewRepository.save(review);
    }

    public Review update(Long id, Review input) {
        Review review = findById(id);

        review.setCustomerId(input.getCustomerId());
        review.setRating(input.getRating());
        review.setComment(input.getComment());

        return reviewRepository.save(review);
    }

    public void delete(Long id) {
        Review review = findById(id);
        reviewRepository.delete(review);
    }
}