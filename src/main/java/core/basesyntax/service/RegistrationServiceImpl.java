package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exceptions.InvalidDataException;
import core.basesyntax.exceptions.UserAlreadyExistsException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_CHARACTERS = 6;
    private static final int MIN_AGE = 18;
    private final StorageDao storageDao = new StorageDaoImpl();

    @Override
    public User register(User user) {
        if (user == null) {
            throw new InvalidDataException("User cannot be null");
        }
        if (user.getLogin() == null) {
            throw new InvalidDataException("Login can't be null");
        }
        if (user.getPassword() == null) {
            throw new InvalidDataException("Password can't be null");
        }
        if (user.getAge() == null) {
            throw new InvalidDataException("Age can't be null");
        }

        if (user.getLogin().length() < MIN_CHARACTERS) {
            throw new InvalidDataException("Login should be 6 or more characters");
        }
        if (user.getPassword().length() < MIN_CHARACTERS) {
            throw new InvalidDataException("Password should be 6 or more characters");
        }
        if (user.getAge() < MIN_AGE) {
            throw new InvalidDataException("Age must be 18 or older");
        }

        User existingUser = storageDao.get(user.getLogin());
        if (existingUser != null) {
            throw new UserAlreadyExistsException("User exists, can't register.");
        }

        return storageDao.add(user);
    }
}
