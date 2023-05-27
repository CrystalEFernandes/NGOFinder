package com.example.ngofinder.Model;

public class ReviewModel {
    public String getReviewbody() {
        return Reviewbody;
    }

    public void setReviewbody(String reviewbody) {
        Reviewbody = reviewbody;
    }

    public String getStars() {
        return Stars;
    }

    public void setStars(String stars) {
        Stars = stars;
    }

    private String Reviewbody;
    private String Stars;

    public String getReviewedBy() {
        return ReviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        ReviewedBy = reviewedBy;
    }

    private String ReviewedBy;

    public Long getReviewedAt() {
        return ReviewedAt;
    }

    public void setReviewedAt(Long reviewedAt) {
        ReviewedAt = reviewedAt;
    }

    private Long ReviewedAt;

    public ReviewModel() {
    }
}
