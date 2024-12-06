package com.company.monolitservice.model.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "worker")
public class WorkerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", length = 32, nullable = false)
    private String firstName;

    @Column(name = "second_name", length = 32, nullable = false)
    private String secondName;

    @Column(name = "middle_name", length = 32, nullable = false)
    private String middleName;

    @Column(name = "registration_time", nullable = false)
    private Instant registrationTime = Instant.now();

    @Column(name = "number_of_completed_orders", nullable = false)
    private Integer numberOfCompletedOrders = 0;


    @Column(nullable = false)
    @Min(0)
    @Max(5)
    private Double rating = 0.0;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedbackEntity> feedbacks = new HashSet<>();

    public WorkerEntity(String firstName, String secondName, String middleName) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.middleName = middleName;
    }

    public void addFeedback(FeedbackEntity feedback){
        feedbacks.add(feedback);
        feedback.setWorker(this);
    }

    public void deleteFeedback(FeedbackEntity feedback) {
        feedbacks.remove(feedback);
        feedback.setWorker(this);
    }

    public void updateFeedback(UUID feedbackId, FeedbackEntity updatedFeedback) {
        for (FeedbackEntity feedback : feedbacks) {
            if (feedback.getId().equals(feedbackId)) {
                feedback.setFeedback(updatedFeedback.getFeedback());
                feedback.setScore(updatedFeedback.getScore());
                feedback.setWorker(this);
                return;
            }
        }
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0); // Используйте только id для hashCode
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerEntity that = (WorkerEntity) o;
        return id != null && id.equals(that.id);
    }
}
