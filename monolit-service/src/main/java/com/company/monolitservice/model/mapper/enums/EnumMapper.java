package com.company.monolitservice.model.mapper.enums;

import com.company.monolitservice.model.dto.enumDto.DescriptiveEnumDto;
import com.company.monolitservice.model.enums.IDescriptiveEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumMapper {
    default DescriptiveEnumDto toDto(IDescriptiveEnum<?> enumValue) {
        return new DescriptiveEnumDto(enumValue.getCode(), enumValue.getDescription());
    }
}
