package com.company.authservice.model.mapper;

import com.company.authservice.model.dto.order.OrderDto;
import com.company.authservice.model.dto.order.OrderPostDto;
import com.company.authservice.model.dto.order.OrderPutDto;
import com.company.authservice.model.entity.OrderEntity;
import com.company.authservice.model.mapper.enums.EnumMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EnumMapper.class})
public interface OrderMapper {

    @Mapping(target = "worker", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "profession", ignore = true)
    @Mapping(target = "work", ignore = true)
    OrderEntity orderPostDtoToEntity(OrderPostDto orderPostDto);


    @Mapping(target = "customerFirstName", source = "customer.firstName")
    @Mapping(target = "customerSecondName", source = "customer.secondName")
    @Mapping(target = "orderStatusType", source = "orderStatus.orderStatusType")
    OrderDto orderEntityToOrderDto(OrderEntity orderEntity);

    void updateEntityFromOrderPutDto(@MappingTarget OrderEntity entity, OrderPutDto dto);
}
