package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.TaskDto;
import com.amss.homemanagement.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "family.id", target = "familyId")
    TaskDto mapToDto(Task task);

    Task mapToEntity(TaskDto taskDto);

}
