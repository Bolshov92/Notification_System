package com.example.user_service.mapper;

import com.example.user_service.dto.UserAfterCreationDto;
import com.example.user_service.dto.UserCreateDto;
import com.example.user_service.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-25T00:07:22+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Homebrew)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreateDto userCreateDto) {
        if ( userCreateDto == null ) {
            return null;
        }

        User user = new User();

        return user;
    }

    @Override
    public UserAfterCreationDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserAfterCreationDto userAfterCreationDto = new UserAfterCreationDto();

        return userAfterCreationDto;
    }
}
