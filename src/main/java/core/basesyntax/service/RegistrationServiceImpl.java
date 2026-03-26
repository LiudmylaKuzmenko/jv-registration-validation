package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.exceptions.InvalidDataException;
import core.basesyntax.exceptions.UserAlreadyExistsException;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private final StorageDao storageDao = new StorageDaoImpl();
    private final int minCharacters = 6;
    private final int minAge = 18;

    @Override
    public User register(User user) {
        if (user.getLogin() == null) {
            throw new InvalidDataException("Login can't be null");
        }
        if (user.getPassword() == null) {
            throw new InvalidDataException("Password can't be null");
        }
        if (user.getAge() == null) {
            throw new InvalidDataException("Age can't be null");
        }

        if (user.getLogin().length() < minCharacters) {
            throw new InvalidDataException("Login should be 6 or more characters");
        }
        if (user.getPassword().length() < minCharacters) {
            throw new InvalidDataException("Password should be 6 or more characters");
        }
        if (user.getAge() < minAge) {
            throw new InvalidDataException("Age must be over 18 years old");
        }

        User existingUser = storageDao.get(user.getLogin());
        if (existingUser != null) {
            throw new UserAlreadyExistsException("User exists, can't register.");
        }

        return storageDao.add(user);
    }
}
