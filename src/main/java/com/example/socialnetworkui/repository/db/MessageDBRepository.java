package com.example.socialnetworkui.repository.db;

import com.example.socialnetworkui.domain.*;
import com.example.socialnetworkui.repository.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class MessageDBRepository implements Repository<Long, Message> {

    private static final String PATH_TO_URL = "Url";
    private static final String PATH_TO_USERNAME = "Username";
    private static final String PATH_TO_PASSWORD = "Password";

    @Override
    public Optional<Message> findOne(Long aLong) {
        try {
            Properties properties = MessageDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("select * from message where id=?");
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");
            String text = resultSet.getString("text");
            Long ids = resultSet.getLong("sender");
            Timestamp ts = resultSet.getTimestamp("date");
            LocalDateTime localDt = null;
            if (ts != null)
                localDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC);
            Message m = new Message(text, findUser(ids), localDt, new ArrayList<>());
            m.setId(id);
            PreparedStatement statementReceiver = connection.prepareStatement("SELECT * from receiver where message_id=?");
            statementReceiver.setLong(1, id);
            ResultSet resultSetReceiver = statement.executeQuery();
            while (resultSetReceiver.next()) {
                Long idReceiver = resultSet.getLong("user_id");
                m.addToUser(findUser(idReceiver));
            }
            return Optional.of(m);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try {
            Properties properties = MessageDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("SELECT * from message");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String text = resultSet.getString("text");
                Long ids = resultSet.getLong("sender");
                Timestamp ts = resultSet.getTimestamp("date");
                LocalDateTime localDt = null;
                if (ts != null)
                    localDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC);
                Message m = new Message(text, findUser(ids), localDt, new ArrayList<>());
                m.setId(id);
                PreparedStatement statementReceiver = connection.prepareStatement("SELECT * from receiver where message_id=?");
                statementReceiver.setLong(1, id);
                ResultSet resultSetReceiver = statement.executeQuery();
                while (resultSetReceiver.next()) {
                    Long idReceiver = resultSet.getLong("user_id");
                    m.addToUser(findUser(idReceiver));
                }
                messages.add(m);
            }
            return messages;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User findUser(long id) {
        try {
            Properties properties = MessageDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("select * from users where user_id=?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");

            User user = new User(firstName, lastName, email, password);
            user.setId(id);
            return user;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Message> save(Message entity) {
        String query = "insert into message(text, sender, date) values(?, ?, ?)";
        try {
            Properties properties = MessageDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, entity.getText());
            ps.setLong(2, entity.getFrom().getId());
            Timestamp ts = null;
            if(entity.getDate() != null)
                ts = new Timestamp(entity.getDate().toInstant(ZoneOffset.UTC).toEpochMilli());
            ps.setTimestamp(3, ts);
            ResultSet resultSet = ps.executeQuery();

            Long message_id = resultSet.getLong("id");
            String query1 = "insert into receiver(message_id, user_id) values(?, ?)";
            ps = connection.prepareStatement(query1);
            ps.setLong(1, message_id);
            for(User u : entity.getTo()) {
                ps.setLong(2, u.getId());
                ps.executeUpdate();
            }
            return Optional.of(entity);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        try {
            Properties properties = MessageDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("delete from message WHERE id=?");
            statement.setLong(1, aLong);
            statement.executeUpdate();
            return Optional.empty();
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

    private static Properties getProperties() throws IOException {
        InputStream input = new FileInputStream("D:\\Faculta\\MAP\\SocialNetworkUI1\\src\\main\\resources\\app.properties");
        Properties properties = new Properties();
        properties.load(input);
        return properties;
    }
}
