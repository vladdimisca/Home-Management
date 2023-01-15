package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.TaskDto;
import com.amss.homemanagement.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto mapToDto(Task task);

    Task mapToEntity(TaskDto taskDto);

}
