package com.d_m.noted.users;

import com.d_m.noted.auth.models.UserPrincipal;
import com.d_m.noted.shared.dtos.auth.SignUpDto;
import com.d_m.noted.users.entities.UserData;
import com.d_m.noted.users.enums.UserRole;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "WEB_CLIENT_ORIGIN=localhost"
})
public class UsersServiceTests {
    private static UsersRepository usersRepository;
    private static PasswordEncoder passwordEncoder;
    private static UsersService service;

    @BeforeAll
    public static void setup() {
        usersRepository = mock(UsersRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        service = new UsersService(usersRepository, passwordEncoder);
    }

    @Nested
    public class CreateUserTests {
        private SignUpDto payload;
        @BeforeEach
        public void setup() {
            reset(usersRepository, passwordEncoder);
            this.payload = new SignUpDto(
                    "myEmail@test.com",
                    "someone123!",
                    "LsZ1GKfFCuWkoRjhs0b"
            );
        }

        @Test
        public void shouldThrowEntityExistsExceptionWhenUserWithEmailAlreadyExists() {
            final Class<? extends Exception> expectedType = EntityExistsException.class;
            final String expectedMessage = "User with email " + payload.email() + " already exists";
            when(usersRepository.existsByEmail(payload.email())).thenReturn(true);

            final Exception actual = assertThrows(
                    expectedType,
                    () -> service.createUser(payload)
            );

            assertEquals(expectedMessage, actual.getMessage());
            verify(usersRepository, atMostOnce()).existsByEmail(payload.email());
        }

        @Test
        public void shouldPassUserDataWithEncodedPasswordToSaveMethod() {
            final String passwordHash = "hashed_password";
            when(usersRepository.existsByEmail(payload.email())).thenReturn(false);
            when(passwordEncoder.encode(payload.password())).thenReturn(passwordHash);

            ArgumentCaptor<UserData> userDataCaptor = ArgumentCaptor.forClass(UserData.class);
            service.createUser(payload);

            verify(passwordEncoder, atMostOnce()).encode(payload.password());
            verify(usersRepository, atMostOnce()).save(userDataCaptor.capture());

            final UserData captured = userDataCaptor.getValue();

            assertEquals(payload.email(), captured.getEmail());
            assertEquals(payload.username(), captured.getUsername());
            assertEquals(passwordHash, captured.getPassword());
        }

        @Test
        public void shouldReturnDataFromRepository() {
            final UserData expected = UserData.builder()
                    .id(1L)
                    .email("db_email")
                    .username("db_usrname")
                    .password("db_pwd")
                    .role(UserRole.USER)
                    .build();

            when(usersRepository.save(any(UserData.class))).thenReturn(expected);
            when(usersRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("pwd_hash");

            final UserData result = service.createUser(payload);

            assertEquals(expected.getId(), result.getId());
            assertEquals(expected.getEmail(), result.getEmail());
            assertEquals(expected.getUsername(), result.getUsername());
            assertEquals(expected.getPassword(), result.getPassword());
            assertEquals(expected.getRole(), result.getRole());
        }
    }

    @Nested
    public class GetByIdTests {
        @BeforeEach
        public void setup() {
            reset(usersRepository, passwordEncoder);
        }

        @Test
        void shouldThrowIfUserIsNotAdminAndRequestsForeignData() {
            final Long requestedId = 123L;
            final UserPrincipal basicUser = UserPrincipal.builder()
                    .id(2L)
                    .email("test_email@test.com")
                    .username("test_usr")
                    .password("SomePassword123")
                    .role(UserRole.USER)
                    .build();
            final Class<? extends Exception> expectedType = AccessDeniedException.class;
            final String expectedMessage = "User " + basicUser.getId() + " doesn't have access to "+ requestedId;

            final Exception actual = assertThrows(
                    expectedType,
                    () -> service.getById(requestedId, basicUser)
            );

            assertEquals(expectedMessage, actual.getMessage());
        }

        @Test
        void shouldReturnIfPrincipalIdMatchesUserData(){
            final Long requestedId = 111L;
            final UserPrincipal userPrincipal = UserPrincipal.builder()
                    .id(111L)
                    .email("test_email@test.com")
                    .username("test_usr")
                    .password("SomePassword123")
                    .role(UserRole.USER)
                    .build();
            final UserData expected = UserData.builder()
                    .id(requestedId)
                    .email(userPrincipal.getEmail())
                    .username(userPrincipal.getUsername())
                    .role(userPrincipal.getRole())
                    .password(userPrincipal.getPassword())
                    .build();
            when(usersRepository.findById(requestedId)).thenReturn(Optional.of(expected));

            final UserData actual = service.getById(requestedId, userPrincipal);

            verify(usersRepository, atMostOnce()).findById(requestedId);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getEmail(), actual.getEmail());
            assertEquals(expected.getUsername(), actual.getUsername());
            assertEquals(expected.getPassword(), actual.getPassword());
            assertEquals(expected.getRole(), actual.getRole());
        }

        @Test
        void shouldReturnForeignDataIfUserIsAdmin() {
            final Long requestedId = 123L;
            final UserPrincipal admin = UserPrincipal.builder()
                    .id(2L)
                    .email("test_email@test.com")
                    .username("test_usr")
                    .password("SomePassword123")
                    .role(UserRole.ADMIN)
                    .build();
            final UserData expected = UserData.builder()
                    .id(requestedId)
                    .username("requested_usr")
                    .email("req@email.com")
                    .role(UserRole.USER)
                    .password("some_pwd")
                    .build();
            when(usersRepository.findById(requestedId)).thenReturn(Optional.of(expected));

            final UserData actual = service.getById(requestedId, admin);

            verify(usersRepository, atMostOnce()).findById(requestedId);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getEmail(), actual.getEmail());
            assertEquals(expected.getUsername(), actual.getUsername());
            assertEquals(expected.getPassword(), actual.getPassword());
            assertEquals(expected.getRole(), actual.getRole());
        }

        @Test
        void shouldThrowEntityNotFoundExceptionIfDataIsNotPresent() {
            final Long id = 1L;
            final UserPrincipal userPrincipal = UserPrincipal.builder()
                    .id(id)
                    .email("test_email@test.com")
                    .username("test_usr")
                    .password("SomePassword123")
                    .role(UserRole.USER)
                    .build();
            final Class<? extends Exception> expectedType = EntityNotFoundException.class;
            final String expectedMessage = "Failed to find user data with id " + id;
            when(usersRepository.findById(id)).thenReturn(Optional.empty());

            final Exception actual = assertThrows(
                    expectedType,
                    () -> service.getById(id, userPrincipal)
            );

            verify(usersRepository, atMostOnce()).findById(id);

            assertEquals(expectedMessage, actual.getMessage());
        }
    }

//    public class ChangePasswordByEmail {
//
//    }
}
