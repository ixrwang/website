/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app;

import java.util.List;
import name.ixr.website.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 用户业务逻辑
 * @author IXR
 */
@Service
public class UserService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 用户登录
     * @param account 账号
     * @param password 密码
     * @return 
     */
    public User login(String account,String password) {
        String sql = "SELECT * FROM t_user WHERE account=? AND password=?";
        List<User> list = jdbcTemplate.query(sql,new Object[]{account,password}, new BeanPropertyRowMapper<>(User.class));
        return list.isEmpty() ? null : list.get(0);
    }
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 
     */
    public int register(User user) {
        String sql = "INSERT INTO t_user(id,account,password,nickname,email,qq,phone) VALUES (?,?,?,?,?,?,?)";
        Object[] param = new Object[]{user.getId(), user.getAccount(), user.getPassword(), user.getNickname(), user.getEmail(), user.getQq(), user.getPhone()};
        return jdbcTemplate.update(sql, param);
    }
    
    public int deleteByAccount(String account) {
        String sql = "DELETE FROM t_user WHERE account=?";
        Object[] param = new Object[]{account};
        return jdbcTemplate.update(sql, param);
    }
    
}
