package com.company.orderservice.model.mapper.enums;

import com.company.orderservice.model.dto.enumDto.DescriptiveEnumDto;
import com.company.orderservice.model.enums.IDescriptiveEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumMapper {
    default DescriptiveEnumDto toDto(IDescriptiveEnum<?> enumValue) {
        return new DescriptiveEnumDto(enumValue.getCode(), enumValue.getDescription());
    }
}
