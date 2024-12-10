package com.company.orderservice.model.mapper;

import com.company.orderservice.model.dto.order.OrderDto;
import com.company.orderservice.model.dto.order.OrderPostDto;
import com.company.orderservice.model.dto.order.OrderPutDto;
import com.company.orderservice.model.entity.OrderEntity;
import com.company.orderservice.model.mapper.enums.EnumMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EnumMapper.class})
public interface OrderMapper {

    @Mapping(target = "workerId", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "professionName", ignore = true)
    @Mapping(target = "work", ignore = true)
    OrderEntity orderPostDtoToEntity(OrderPostDto orderPostDto);


    @Mapping(target = "customerFullName", source = "customerName")
    @Mapping(target = "orderStatusType", source = "orderStatus.orderStatusType")
    OrderDto orderEntityToOrderDto(OrderEntity orderEntity);

    void updateEntityFromOrderPutDto(@MappingTarget OrderEntity entity, OrderPutDto dto);
}
