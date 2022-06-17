/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FiimorateApplicationTests {

    FilmController filmController;
    UserController userController;
    @BeforeEach
    public void createServiceObjects (){
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        FilmController filmController = new FilmController(new FilmService(filmStorage, userStorage));
        UserController userController = new UserController(new UserService(userStorage));
    }
    @Test
    void userOkTest() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(1980, 07, 15))
                .email("test@tast")
                .login("login")
                .name("name")
                .build();
        assertEquals(user, userController.addUser(user));
    }

    @Test
    void userEmailWithoutAt() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(1980, 07, 15))
                .email("test")
                .login("login")
                .name("name")
                .build();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void userEmptyEmail() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(1980, 07, 15))
                .email("")
                .login("login")
                .name("name")
                .build();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void userEmptyLogin() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(1980, 07, 15))
                .email("test@tast")
                .login("")
                .name("name")
                .build();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void userWithSpaceInLogin() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(1980, 07, 15))
                .email("test@tast")
                .login("lo gin")
                .name("name")
                .build();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void userWithEmptyName() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(1980, 07, 15))
                .email("test@tast")
                .login("login")
                .name("")
                .build();
        User user1 = User.builder()
                .id(1)
                .birthday(LocalDate.of(1980, 07, 15))
                .email("test@tast")
                .login("login")
                .name("login")
                .build();
        assertEquals(user1, userController.addUser(user));
    }

    @Test
    void userWithBirthdayInFuture() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(2200, 07, 15))
                .email("test@tast")
                .login("login")
                .name("name")
                .build();
        assertThrows(ValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void filmOkTest() throws ValidationException {
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1993, 9, 2))
                .duration(120)
                .build();
        assertEquals(film, filmController.addFilm(film));
    }

    @Test
    void filmWithEmptyName() throws ValidationException {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(1993, 9, 2))
                .duration(120)
                .build();
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void filmWithLongDescription() throws ValidationException {
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("Two strangers, freelance photographer Adam Stanheight and " +
                        "Dr. Lawrence Gordon, awaken in a dilapidated bathroom with no memory " +
                        "of how they ended up there. Both men find a tape recording each in their " +
                        "pockets, and not long after listening to them, realize they have been " +
                        "trapped in a game perpetrated by the infamous Jigsaw Killer. Escape seems " +
                        "unlikely at first, but Adam and Lawrence soon realize they may have a chance. " +
                        "But at what cost? And are they alone?")
                .releaseDate(LocalDate.of(1993, 9, 2))
                .duration(120)
                .build();
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void filmInSeventeenCentury() throws ValidationException {
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1693, 9, 2))
                .duration(120)
                .build();
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void filmWithoutDuration() throws ValidationException {
        Film film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1993, 9, 2))
                .duration(-7)
                .build();
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

}
*/