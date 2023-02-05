package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.TaskDto;
import com.amss.homemanagement.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "creator.id", target = "creatorId")
    TaskDto mapToDto(Task task);

    @Mapping(source = "familyId", target = "family.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "creatorId", target = "creator.id")
    Task mapToEntity(TaskDto taskDto);

}
