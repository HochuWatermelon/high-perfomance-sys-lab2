package com.company.authservice.model.mapper;

import com.company.authservice.model.dto.feedback.FeedbackDto;
import com.company.authservice.model.dto.feedback.FeedbackPostDto;
import com.company.authservice.model.dto.feedback.FeedbackPutDto;
import com.company.authservice.model.entity.FeedbackEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FeedbackMapper {

    @Mapping(target = "worker", ignore = true)
    @Mapping(target = "order", ignore = true)
    FeedbackEntity feedbackPostDtoToEntity(FeedbackPostDto feedbackPostDto);

    @Mapping(target = "customerFirstName", source = "order.customer.firstName")
    @Mapping(target = "customerSecondName", source = "order.customer.secondName")
    @Mapping(target = "customerMiddleName", source = "order.customer.middleName")
    FeedbackDto feedbackEntityToFeedbackDto(FeedbackEntity feedbackEntity);

    void updateEntityFromFeedbackPutDto(@MappingTarget FeedbackEntity entity, FeedbackPutDto dto);
}
