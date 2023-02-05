package com.amss.homemanagement.mapper;

import com.amss.homemanagement.dto.NotificationDto;
import com.amss.homemanagement.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "task.id", target = "taskId")
    NotificationDto mapToDto(Notification notification);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "taskId", target = "task.id")
    Notification mapToEntity(NotificationDto notificationDto);
}
