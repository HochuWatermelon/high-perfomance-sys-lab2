package com.company.authservice.model.mapper;


import com.company.authservice.model.dto.orderstatus.OrderStatusPostDto;
import com.company.authservice.model.entity.OrderStatusEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderStatusMapper {

    @Mapping(target = "orderStatusType", source = "order_status_type")
    OrderStatusEntity orderStatusPostDtoToOrderStatusEntity(OrderStatusPostDto orderStatusPostDto);
}
