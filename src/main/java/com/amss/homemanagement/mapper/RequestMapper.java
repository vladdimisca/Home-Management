package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.RequestDto;
import com.amss.homemanagement.model.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(source = "family.id", target = "familyId")
    RequestDto mapToDto(Request request);

    Request mapToEntity(RequestDto requestDto);
}
