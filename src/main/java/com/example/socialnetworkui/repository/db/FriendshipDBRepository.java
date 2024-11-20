package com.example.socialnetworkui.repository.db;

import com.example.socialnetworkui.domain.Friendship;
import com.example.socialnetworkui.domain.Tuple;
import com.example.socialnetworkui.repository.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class FriendshipDBRepository implements Repository<Tuple<Long, Long>, Friendship> {
    private static final String PATH_TO_URL = "Url";
    private static final String PATH_TO_USERNAME = "Username";
    private static final String PATH_TO_PASSWORD = "Password";

    public FriendshipDBRepository(){}

    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> fr) {
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("select * from friendships WHERE (user_id1=? AND user_id2=?) or (user_id1=? and user_id2=?)");
            statement.setLong(1, fr.getLeft());
            statement.setLong(2, fr.getRight());
            statement.setLong(4, fr.getLeft());
            statement.setLong(3, fr.getRight());
            ResultSet resultSet = statement.executeQuery();
            Friendship friendship = new Friendship();
            resultSet.next();
            friendship.setId(new Tuple<>(resultSet.getLong(1), resultSet.getLong(2)));
            return Optional.of(friendship);
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try {
             Properties properties = FriendshipDBRepository.getProperties();
             Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("user_id1");
                    Long id2 = resultSet.getLong("user_id2");
                    Timestamp ts = resultSet.getTimestamp("friendsFrom");
                    LocalDateTime localDt = null;
                    if (ts != null)
                        localDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneOffset.UTC);
                    Friendship f = new Friendship();
                    f.setId(new Tuple<>(id1, id2));
                    f.setDate(localDt);
                    friendships.add(f);
                }
                return friendships;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Iterable<Long> findById(Long id) {
        Set<Long> ids = new HashSet<>();
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships where user_id1=? or user_id2=?");
            statement.setLong(1, id);
            statement.setLong(2, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("user_id1");
                Long id2 = resultSet.getLong("user_id2");
                int pending = resultSet.getInt("pending");
                if(pending == 0) {
                    if(id1.equals(id)) ids.add(id2);
                    else ids.add(id1);
                }
            }
            return ids;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean inPending(Long id1, Long toId2) {
        Boolean pending = false;
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships where user_id1=? and user_id2=?");
            statement.setLong(1, id1);
            statement.setLong(2, toId2);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return pending;
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> fr) {
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("delete from friendships WHERE (user_id1=? AND user_id2=?) or (user_id1=? and user_id2=?)");
            statement.setLong(1, fr.getLeft());
            statement.setLong(2, fr.getRight());
            statement.setLong(4, fr.getLeft());
            statement.setLong(3, fr.getRight());
            statement.executeUpdate();
            return Optional.empty();
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String query = "insert into friendships(user_id1, user_id2, friendsfrom, pending) values(?, ?, ?, 0)";
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, entity.getId().getLeft());
            ps.setLong(2, entity.getId().getRight());
            Timestamp ts = null;
            if(entity.getDate() != null)
                ts = new Timestamp(entity.getDate().toInstant(ZoneOffset.UTC).toEpochMilli());
            ps.setTimestamp(3, ts);
            ps.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static List<Long> IdsofUsers() {
        List<Long> users = new ArrayList<>();
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("select user_id from users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getLong("user_id"));
            }
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static Iterable<Long> receivedFriendRequest(Long id) {
        Set<Long> ids = new HashSet<>();
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement("SELECT * from friendships where user_id2=? and pending=1");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("user_id1");
                ids.add(id1);
            }
            return ids;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void friendRequest(Long id1, Long toId2) {
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));

            PreparedStatement statement = connection.prepareStatement("select * from friendships WHERE (user_id1=? AND user_id2=?)");
            statement.setLong(1, toId2);
            statement.setLong(2, id1);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                statement = connection.prepareStatement("update friendships set pending=0, friendsfrom=NOW() WHERE (user_id1=? AND user_id2=?)");
                statement.setLong(1, toId2);
                statement.setLong(2, id1);
            }
            else {

                statement = connection.prepareStatement("insert into friendships(user_id1, user_id2, pending) values(?, ?, 1)");
                statement.setLong(1, id1);
                statement.setLong(2, toId2);
            }
            statement.executeUpdate();
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> namesOfUsers(List<Long> ids) {
        List<String> users = new ArrayList<>();
        String query = "SELECT firstname FROM users WHERE user_id = ?";
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));
            PreparedStatement statement = connection.prepareStatement(query);
            for (Long id : ids) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        users.add(resultSet.getString("firstname"));
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static Iterable<Long> noofFriendRequests(Long id) {
        Set<Long> user_ids = new HashSet<>();
        try {
            Properties properties = FriendshipDBRepository.getProperties();
            Connection connection = DriverManager.getConnection(properties.getProperty(PATH_TO_URL), properties.getProperty(PATH_TO_USERNAME), properties.getProperty(PATH_TO_PASSWORD));

            PreparedStatement statement = connection.prepareStatement("select user_id1 from friendships WHERE user_id2=? and pending=1");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next()) {
                Long id1 = resultSet.getLong("user_id1");
                user_ids.add(id1);
            }
            return user_ids;
        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Properties getProperties() throws IOException {
        InputStream input = new FileInputStream("D:\\Faculta\\MAP\\SocialNetworkUI1\\src\\main\\resources\\app.properties");
        Properties properties = new Properties();
        properties.load(input);
        return properties;
    }
}
