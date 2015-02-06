package com.coderdream.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.coderdream.domain.User;

@Repository // （1）通过Spring注解定义一个DAO
public class UserDao {

    @Autowired // （2）自动注入JdbcTemplate的Bean
    private JdbcTemplate jdbcTemplate;

    public int getMatchCount(String userName, String password) {
        String sqlStr = " SELECT count(*) FROM t_user "
                + " WHERE user_name =? and password=? ";
        return jdbcTemplate.queryForObject(sqlStr, new Object[] { userName,
                password }, Integer.class);
    }

    public User findUserByUserName(final String userName) {
        String sqlStr = " SELECT user_id,user_name,credits "
                + " FROM t_user WHERE user_name =? ";
        final User user = new User();
        jdbcTemplate.query(sqlStr, new Object[] { userName },
                new RowCallbackHandler() {
                    public void processRow(ResultSet rs) throws SQLException {
                        user.setUserId(rs.getInt("user_id"));
                        user.setUserName(userName);
                        user.setCredits(rs.getInt("credits"));
                    }
                });
        return user;
    }

    public void updateLoginInfo(User user) {
        String sqlStr = " UPDATE t_user SET last_visit=?,last_ip=?,credits=? "
                + " WHERE user_id =?";
        jdbcTemplate.update(
                sqlStr,
                new Object[] { user.getLastVisit(), user.getLastIp(),
                        user.getCredits(), user.getUserId() });
    }
}