package com.company.authservice.model.mapper.enums;

import com.company.authservice.model.dto.enumDto.DescriptiveEnumDto;
import com.company.authservice.model.enums.IDescriptiveEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumMapper {
    default DescriptiveEnumDto toDto(IDescriptiveEnum<?> enumValue) {
        return new DescriptiveEnumDto(enumValue.getCode(), enumValue.getDescription());
    }
}
