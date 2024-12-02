package com.company.authservice.model.dto.feedback;

import lombok.Data;

@Data
public class FeedbackDto {
    private String customerFirstName;
    private String customerSecondName;
    private String customerMiddleName;
    private String feedback;
    private Double score;

    @Override
    public String toString() {
        return "Заказчик: " + customerFirstName + customerSecondName + customerMiddleName + "\nОтзыв: " + feedback + "\nОценка: " + score;
    }
}
