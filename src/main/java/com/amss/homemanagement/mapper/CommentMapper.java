package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.CommentDto;
import com.amss.homemanagement.dto.FamilyDto;
import com.amss.homemanagement.model.Comment;
import com.amss.homemanagement.model.Family;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "task.id", target = "taskId")
    CommentDto mapToDto(Comment comment);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "taskId", target = "task.id")
    Comment mapToEntity(CommentDto commentDto);
}
