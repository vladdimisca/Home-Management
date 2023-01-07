package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.UserDto;
import com.amss.homemanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", expression = "java(null)")
    UserDto mapToDto(User user);

    User mapToEntity(UserDto userDTO);
}
