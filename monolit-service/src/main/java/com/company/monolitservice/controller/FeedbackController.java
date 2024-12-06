package com.company.monolitservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.monolitservice.model.dto.customer.CustomerSelfDto;
import com.company.monolitservice.model.dto.feedback.FeedbackPostDto;
import com.company.monolitservice.model.dto.feedback.FeedbackPutDto;
import com.company.monolitservice.model.dto.pageable.PageableGetDto;
import com.company.monolitservice.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<String> createFeedback(@Valid @RequestBody FeedbackPostDto feedbackDto) {
        return feedbackService.createFeedback(feedbackDto);
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<String> updateFeedback(@PathVariable UUID feedbackId, @Valid @RequestBody FeedbackPutDto feedbackDto){
        return feedbackService.updateFeedback(feedbackId, feedbackDto);
    }

    @GetMapping("/get-feedbacks/{workerId}")
    public ResponseEntity<String> getFeedbacks(@PathVariable UUID workerId, @RequestBody(required = false) Optional<PageableGetDto> pageableDto) {
        PageableGetDto pageableGetDto = pageableDto.orElse(new PageableGetDto(0, 50));
        if (pageableGetDto.getSize() > 50)
            pageableGetDto.setSize(50);
        return feedbackService.getWorkerFeedbacks(workerId, pageableGetDto);
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<String> deleteFeedbackByOrder(@Valid @RequestBody CustomerSelfDto customerSelfDto, @PathVariable UUID feedbackId){
        return feedbackService.deleteFeedbackByOrder(customerSelfDto, feedbackId);
    }
}
