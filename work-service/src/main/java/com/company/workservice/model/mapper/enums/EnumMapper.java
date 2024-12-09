package com.company.workservice.model.mapper.enums;

import com.company.workservice.model.dto.enumDto.DescriptiveEnumDto;
import com.company.workservice.model.enums.IDescriptiveEnum;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumMapper {
    default DescriptiveEnumDto toDto(IDescriptiveEnum<?> enumValue) {
        return new DescriptiveEnumDto(enumValue.getCode(), enumValue.getDescription());
    }
}
