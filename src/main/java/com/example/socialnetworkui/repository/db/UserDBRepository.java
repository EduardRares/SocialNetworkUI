package com.example.socialnetworkui.repository.db;

import com.example.socialnetworkui.domain.User;
import com.example.socialnetworkui.domain.validators.Validator;
import com.example.socialnetworkui.repository.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class UserDBRepository implements Repository<Long, User> {
    private Validator<User> validator;
    private static final String PATH_TO_URL = "Url";
    private static final String PATH_TO_USERNAME = "Username";
    private static final String PATH_TO_PASSWORD = "Password";

    public UserDBRepository(Validator<User> validator) {
        this.validator = validator;
    }

    public static User login(String email, String password) {
        try {
            Properties properties = UserDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String emailAddress = resultSet.getString(4);
            String password1 = resultSet.getString(5);
            String path = resultSet.getString(6);
            User user =  new User(firstName, lastName, emailAddress, password1, path);
            user.setId(id);
            return user;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Optional<User> findOne(Long aLong) {
        try {
            Properties properties = UserDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("select * from users where user_id=?");
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String path = resultSet.getString("ProfileImagePath");
            User user = new User(firstName, lastName, email, password, path);
            user.setId(aLong);
            return Optional.of(user);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        Map<Long, User> users = new HashMap<>();
        try {
            Properties properties = UserDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String path = resultSet.getString("ProfileImagePath");

                User user = new User(firstName, lastName, email, password, path);
                user.setId(id);
                if(!("admin".equals(user.getEmail()) && "admin".equals(user.getPassword()))) {
                    users.put(id, user);
                }
            }
            return users.values();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<User> save(User entity) {
        validator.validate(entity);
        String query = "insert into users(firstname, lastname, email, password) values(?, ?, ?, ?)";
        try {
            Properties properties = UserDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());

            ps.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException | IOException e) {
            e.printStackTrace();

        }
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(Long aLong) {
        Optional<User> user = findOne(aLong);
        try {
            Properties properties = UserDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("delete from users WHERE user_id=?");
            statement.setLong(1, aLong);
            statement.executeUpdate();
            return user;
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        try {
            Properties properties = UserDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("update users set firstname=?, lastname=?, email=?, password=? where user_id=?");
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            statement.setLong(5, entity.getId());
            statement.executeUpdate();
            return findOne(entity.getId());
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private static Properties getProperties() throws IOException {
        InputStream input = new FileInputStream("src/main/resources/app.properties");
        Properties properties = new Properties();
        properties.load(input);
        return properties;
    }
}
