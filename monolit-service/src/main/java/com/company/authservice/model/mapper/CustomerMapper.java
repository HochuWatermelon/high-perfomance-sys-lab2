package com.company.authservice.model.mapper;

import com.company.authservice.model.dto.customer.CustomerPostDto;
import com.company.authservice.model.dto.customer.CustomerPutDto;
import com.company.authservice.model.entity.CustomerEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "firstName", source = "first_name")
    @Mapping(target = "secondName", source = "second_name")
    @Mapping(target = "middleName", source = "middle_name")
    CustomerEntity customerPostDtoToEntity(CustomerPostDto customerPostDto);

    @Mapping(target = "firstName", source = "first_name")
    @Mapping(target = "secondName", source = "second_name")
    @Mapping(target = "middleName", source = "middle_name")
    void updateEntityFromCustomerPutDto(@MappingTarget CustomerEntity entity, CustomerPutDto dto);
}
