/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.app;

import java.util.List;
import name.ixr.website.app.model.OAuth;
import name.ixr.website.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 跨站认证
 * @author IXR
 */
@Service
public class OAuthService {
    
    /** QQ登录类型 */
    public static final String QQ_LOGIN_TYPE= "QQ_LOGIN_TYPE";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 跨站用户登录
     * @param oid 跨站认证
     * @param type 跨站类型
     * @return 
     */
    public User login(String oid,String type) {
        String sql = "SELECT * FROM t_user u LEFT JOIN t_oauth o ON o.rid=u.id WHERE o.oid=? AND o.type=?";
        List<User> list = jdbcTemplate.query(sql, new Object[]{oid, type}, new BeanPropertyRowMapper<>(User.class));
        return list.isEmpty() ? null : list.get(0);
    }
    
    /**
     * 保存认证
     * @param oauth
     * @return 
     */
    public int save(OAuth oauth) {
        String sql = "INSERT INTO t_oauth(rid,oid,type) VALUES (?,?,?)";
        Object[] param = new Object[]{oauth.getRid(), oauth.getOid(), oauth.getType()};
        return jdbcTemplate.update(sql, param);
    }
}
