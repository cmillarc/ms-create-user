package com.crud.user.service;

import com.crud.user.dto.UserRequestDTO;
import com.crud.user.dto.UserResponseDTO;
import com.crud.user.exception.UserAlreadyExistsException;
import com.crud.user.model.Phone;
import com.crud.user.model.User;
import com.crud.user.repository.PhoneRepository;
import com.crud.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;


    public UserServiceImpl(UserRepository userRepository, PhoneRepository phoneRepository,
                           ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder
            , JwtService jwtService) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) throws UserAlreadyExistsException {
        // Transform the DTO into an Entity
        User user = modelMapper.map(userRequestDTO, User.class);
        // Check if the User email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(String.format("El correo %s ya está registrado", user.getEmail()));
        }
        // Save the User
        saveUser(user);
        // Transform and save the Phones
        savePhones(userRequestDTO, user);
        log.info("User saved: {}", user);
        return modelMapper.map(user, UserResponseDTO.class);
    }

    private void saveUser(User user) {
        user.setActive(true);
        //generate token with JWT
        user.setToken(jwtService.buildToken(user));
        //save encrypted password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private void savePhones(UserRequestDTO userRequestDTO, User user) {
        phoneRepository.saveAll(userRequestDTO.getPhones().stream()
                .map(phoneDTO -> modelMapper.map(phoneDTO, Phone.class))
                .peek(phone -> phone.setUser(user))
                .toList());
    }


}
