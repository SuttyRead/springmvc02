package com.ua.sutty.dao;

import com.ua.sutty.models.Car;
import com.ua.sutty.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 04.04.2018
 * UsersDaoJdbcTemplateImpl
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */

@Component
public class UsersDaoJdbcTemplateImpl implements UsersDao {

    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Map<Long, User> usersMap = new HashMap<>();

    //language=SQL
    private final String SQL_SELECT_USER_WITH_CARS =
            "SELECT user.*, car.id as car_id, car.model FROM user LEFT JOIN car ON user.id = car.owner_id WHERE user.id = ?";

    private final String SQL_SELECT_USERS_WITH_CARS =
            "SELECT user.*, car.id as car_id, car.model FROM user LEFT JOIN car ON user.id = car.owner_id";

    private final String SQL_SELECT_ALL_BY_FIRST_NAME =
            "SELECT * FROM user WHERE first_name = ?";

    private final String SQL_SELECT_BY_ID =
            "SELECT * FROM user WHERE id = :id";

    //language=SQL
    private final String SQL_INSERT_USER = "" +
            "INSERT INTO user(first_name, last_name) VALUES (:firstName, :lastName)";

    @Autowired
    public UsersDaoJdbcTemplateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private RowMapper<User> userRowMapperWithoutCars = (resultSet, i) -> User.builder()
            .id(resultSet.getLong("id"))
            .firstName(resultSet.getString("first_name"))
            .lastName(resultSet.getString("last_name"))
            .build();

    private RowMapper<User> userRowMapper
            = (ResultSet resultSet, int i) -> {
       Long id = resultSet.getLong("id");

       if (!usersMap.containsKey(id)) {
           String firstName = resultSet.getString("first_name");
           String lastName = resultSet.getString("last_name");
           User user = new User(id, firstName, lastName, new ArrayList<>());
           usersMap.put(id, user);
       }

       Car car = new Car(resultSet.getLong("car_id"),
               resultSet.getString("model"), usersMap.get(id));

       usersMap.get(id).getCars().add(car);

       return usersMap.get(id);
    };

    @Override
    public List<User> findAllByFirstName(String firstName) {
        return template.query(SQL_SELECT_ALL_BY_FIRST_NAME, userRowMapperWithoutCars, firstName);
    }

    @Override
    public Optional<User> find(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<User> result = namedParameterJdbcTemplate.query(SQL_SELECT_BY_ID, params, userRowMapperWithoutCars);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public void save(User model) {
        Map<String, Object> params = new HashMap<>();
        params.put("firstName", model.getFirstName());
        params.put("lastName", model.getLastName());
        namedParameterJdbcTemplate.update(SQL_INSERT_USER, params);
    }

    @Override
    public void update(User model) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<User> findAll() {
        List<User> result =  template.query(SQL_SELECT_USERS_WITH_CARS, userRowMapper);
        usersMap.clear();
        return result;
    }
}
