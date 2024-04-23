package com.crud.user.service;


import com.crud.user.dto.PhoneDTO;
import com.crud.user.dto.UserRequestDTO;
import com.crud.user.dto.UserResponseDTO;
import com.crud.user.exception.UserAlreadyExistsException;
import com.crud.user.model.Phone;
import com.crud.user.model.User;
import com.crud.user.repository.PhoneRepository;
import com.crud.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtService jwtService;

    private UserServiceImpl userService;

    private UserRequestDTO userRequestDTO;
    private User user;
    private UserResponseDTO userResponseDTO;
    private PhoneDTO phoneDTO;
    private Phone phone;


    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, phoneRepository, modelMapper, bCryptPasswordEncoder, jwtService);

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setPassword("password");
        List<PhoneDTO> phoneList = new ArrayList<>();
        phoneDTO = PhoneDTO.builder().number("1234567890").build();
        phoneList.add(phoneDTO);
        userRequestDTO.setPhones(phoneList);

        user = new User();
        user.setEmail("test@example.com");
        user.setToken("Token");
        phone = new Phone();
        phone.setNumber("1234567890");
        userResponseDTO = new UserResponseDTO();
    }

    @Test
    public void testCreateUserSuccess() throws UserAlreadyExistsException {
        // Mock behavior
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);
        when(jwtService.buildToken(user)).thenReturn(user.getToken());
        when(modelMapper.map(phoneDTO, Phone.class)).thenReturn(phone);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");

        // Call the method
        UserResponseDTO createdUser = userService.createUser(userRequestDTO);

        // Verify interactions and return value
        verify(userRepository).existsByEmail(userRequestDTO.getEmail());
        verify(userRepository).save(user);
        verify(phoneRepository).saveAll(anyList());
        verify(modelMapper, times(3)).map(any(), any());
        assertEquals(userResponseDTO, createdUser);
    }

    @Test
    public void testCreateUserAlreadyExists() throws UserAlreadyExistsException {
        // Mock behavior
        when(userRepository.existsByEmail(userRequestDTO.getEmail())).thenReturn(true);
        when(modelMapper.map(userRequestDTO, User.class)).thenReturn(user);
        // Call the method (expecting exception)
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequestDTO));
    }
}
