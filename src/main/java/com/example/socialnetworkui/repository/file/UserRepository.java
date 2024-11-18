package com.example.socialnetworkui.repository.file;

import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.domain.validators.Validator;

public class UserRepository extends AbstractFileRepository<Long, User>{
    public UserRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     *
     * @param line
     *          line must not be null
     * @return User
     * transform String to User
     */
    @Override
    public User createEntity(String line) {
        String[] splited = line.split(",");
        User u = new User(splited[1], splited[2], splited[3], splited[4]);
        u.setId(Long.parseLong(splited[0]));
        return u;
    }
    /**
     *
     * @param entity
     *          entity must not be null
     * @return String
     * transform User to String
     */
    @Override
    public String saveEntity(User entity) {
        return entity.getId() + "," + entity.getFirstName() + "," + entity.getLastName() + ',' + entity.getEmail() + ',' + entity.getPassword();
    }
}
