package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.CommentDto;
import com.amss.homemanagement.dto.LikeDto;
import com.amss.homemanagement.model.Comment;
import com.amss.homemanagement.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "comment.id", target = "commentId")
    LikeDto mapToDto(Like like);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "commentId", target = "comment.id")
    Like mapToEntity(LikeDto likeDto);
}
