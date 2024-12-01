package com.ra.busBooking;
import com.ra.busBooking.DTO.UserRegisteredDTO;
import com.ra.busBooking.model.Role;
import com.ra.busBooking.model.User;
import com.ra.busBooking.repository.RoleRepository;
import com.ra.busBooking.repository.UserRepository;
import com.ra.busBooking.service.DefaultUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceUnitTests {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @InjectMocks
    private DefaultUserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerAndLoginWithUserAccount() {
        UserRegisteredDTO userRegisteredDTO = new UserRegisteredDTO();
        userRegisteredDTO.setEmail_id("temp@gmail.com");
        userRegisteredDTO.setName("Temp");
        userRegisteredDTO.setPassword("12345");
        userRegisteredDTO.setRole("USER");


        Role mockRole = new Role();
        mockRole.setRole("USER");
        when(roleRepo.findByRole("USER")).thenReturn(mockRole);


        User mockUser = new User();
        mockUser.setEmail("temp@gmail.com");
        mockUser.setName("Temp");
        mockUser.setPassword("12345");
        when(userRepo.findByEmail("temp@gmail.com")).thenReturn(mockUser);
        when(userRepo.save(any(User.class))).thenReturn(mockUser);  // Сохраняем пользователя при вызове save()
        User savedUser = userService.save(userRegisteredDTO);
        verify(userRepo, times(1)).save(any(User.class));
        assertNotNull(savedUser, "User should not be null after saving");
        UserDetails loadedUser = userService.loadUserByUsername("temp@gmail.com");
        assertNotNull(loadedUser, "UserDetails should not be null");
        assertEquals("temp@gmail.com", loadedUser.getUsername(), "User email should match");
        verify(userRepo, times(1)).findByEmail("temp@gmail.com");
    }
}
