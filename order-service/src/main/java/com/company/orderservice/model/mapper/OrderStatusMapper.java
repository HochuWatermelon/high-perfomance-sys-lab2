package com.company.orderservice.model.mapper;


import com.company.orderservice.model.dto.orderstatus.OrderStatusPostDto;
import com.company.orderservice.model.entity.OrderStatusEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderStatusMapper {

    @Mapping(target = "orderStatusType", source = "order_status_type")
    OrderStatusEntity orderStatusPostDtoToOrderStatusEntity(OrderStatusPostDto orderStatusPostDto);
}
