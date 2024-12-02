package com.company.authservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "feedback")
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private WorkerEntity worker;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;


    @Column(nullable = false, length = 128)
    private String feedback;

    @Min(0)
    @Max(5)
    @Column(nullable = false)
    private Double score;

    public FeedbackEntity(String feedback, Double score) {
        this.feedback = feedback;
        this.score = score;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0); // Используйте только id для hashCode
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackEntity that = (FeedbackEntity) o;
        return id != null && id.equals(that.id);
    }
}
