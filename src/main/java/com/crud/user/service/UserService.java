package com.crud.user.service;

import com.crud.user.dto.UserRequestDTO;
import com.crud.user.dto.UserResponseDTO;
import com.crud.user.exception.UserAlreadyExistsException;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO) throws UserAlreadyExistsException;

}
