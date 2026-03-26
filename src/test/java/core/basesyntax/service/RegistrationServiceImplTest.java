package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.exceptions.InvalidDataException;
import core.basesyntax.exceptions.UserAlreadyExistsException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private RegistrationService registrationService;
    private StorageDao storageDao;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl();
        storageDao = new StorageDaoImpl();
        Storage.people.clear();
    }

    @Test
    void register_userIsNull_notOk() {
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(null));
    }

    @Test
    void register_validUser_isOk() {
        User testUserFirst = new User();
        testUserFirst.setLogin("testUserFirst");
        testUserFirst.setPassword("12345678");
        testUserFirst.setAge(23);
        User result = registrationService.register(testUserFirst);
        assertEquals(testUserFirst, result);
        User fromStorage = storageDao.get(testUserFirst.getLogin());
        assertEquals(testUserFirst, fromStorage);
    }

    @Test
    void register_edgeCaseValidUser_isOk() {
        User testUserSecond = new User();
        testUserSecond.setLogin("testUs");
        testUserSecond.setPassword("123456");
        testUserSecond.setAge(18);
        User result = registrationService.register(testUserSecond);
        assertEquals(testUserSecond, result);
        User fromStorage = storageDao.get(testUserSecond.getLogin());
        assertEquals(testUserSecond, fromStorage);
    }

    @Test
    void register_existingLogin_notOk() {
        User testUserFirst = new User();
        testUserFirst.setLogin("testUserFirst");
        testUserFirst.setPassword("12345678");
        testUserFirst.setAge(23);
        Storage.people.add(testUserFirst);
        assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.register(testUserFirst));

        User testUserSecondTry = new User();
        testUserSecondTry.setLogin("testUserFirst");
        testUserSecondTry.setPassword("123456789");
        testUserSecondTry.setAge(20);
        assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.register(testUserSecondTry));
    }

    @Test
    void register_loginLessThan6Chars_notOk() {
        User incorrectUserFirst = new User();
        incorrectUserFirst.setLogin("testU");
        incorrectUserFirst.setPassword("123456789");
        incorrectUserFirst.setAge(25);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserFirst));

        User incorrectUserSecond = new User();
        incorrectUserSecond.setLogin("teU");
        incorrectUserSecond.setPassword("123456789");
        incorrectUserSecond.setAge(37);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserSecond));

        User incorrectUserThird = new User();
        incorrectUserThird.setLogin("");
        incorrectUserThird.setPassword("123456789");
        incorrectUserThird.setAge(37);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserThird));
    }

    @Test
    void register_passwordLessThan6Chars_notOk() {
        User incorrectUserFirst = new User();
        incorrectUserFirst.setLogin("testUser");
        incorrectUserFirst.setPassword("12345");
        incorrectUserFirst.setAge(28);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserFirst));

        User incorrectUserSecond = new User();
        incorrectUserSecond.setLogin("testUser");
        incorrectUserSecond.setPassword("123");
        incorrectUserSecond.setAge(45);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserSecond));

        User incorrectUserThird = new User();
        incorrectUserThird.setLogin("testUser");
        incorrectUserThird.setPassword("");
        incorrectUserThird.setAge(45);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserThird));
    }

    @Test
    void register_ageLessThan18_notOk() {
        User incorrectUserFirst = new User();
        incorrectUserFirst.setLogin("testUser");
        incorrectUserFirst.setPassword("1234567");
        incorrectUserFirst.setAge(5);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserFirst));

        User incorrectUserSecond = new User();
        incorrectUserSecond.setLogin("testUser");
        incorrectUserSecond.setPassword("1234567");
        incorrectUserSecond.setAge(17);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserSecond));

        User incorrectUserThird = new User();
        incorrectUserThird.setLogin("testUser");
        incorrectUserThird.setPassword("1234567");
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserThird));

        User incorrectUserFourth = new User();
        incorrectUserFourth.setLogin("testUser");
        incorrectUserFourth.setPassword("1234567");
        incorrectUserFourth.setAge(-17);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(incorrectUserFourth));
    }

    @Test
    void register_nullLogin_notOk() {
        User nullUserLogin = new User();
        nullUserLogin.setLogin(null);
        nullUserLogin.setPassword("hfjfkdkjdh");
        nullUserLogin.setAge(55);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(nullUserLogin));
    }

    @Test
    void register_nullPassword_notOk() {
        User nullUserPassword = new User();
        nullUserPassword.setLogin("TestUserNull");
        nullUserPassword.setPassword(null);
        nullUserPassword.setAge(85);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(nullUserPassword));
    }

    @Test
    void register_nullAge_notOk() {
        User nullUserAge = new User();
        nullUserAge.setLogin("TestUserNull");
        nullUserAge.setPassword("hfjfkdkjdh");
        nullUserAge.setAge(null);
        assertThrows(InvalidDataException.class,
                () -> registrationService.register(nullUserAge));
    }
}
