package id.co.ogya.pushnotification.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import id.co.ogya.pushnotification.model.UserToken;

@Repository
public class NotificationRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public boolean registerToken(UserToken userToken) {	
		try {
			System.out.println("Register new user..");
			String sql = "INSERT INTO USER_TOKEN (USERNAME, REGISTRATIONTOKEN) VALUES (?,?)";
			this.jdbcTemplate.queryForObject(sql, 
				new Object[] {userToken.getUserName(), userToken.getRegistrationToken()}, 
				new BeanPropertyRowMapper<>(UserToken.class));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getTokenByUserName(String userName) {
		try {
			String sql = "SELECT REGISTRATIONTOKEN FROM USER_TOKEN WHERE USERNAME = ?";
			String userToken =  (String) this.jdbcTemplate.queryForObject(sql, String.class, userName);
			return userToken;
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean isExist(String userName) {
		String sql = "SELECT COUNT(1) FROM USER_TOKEN WHERE USERNAME = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userName);
		return count != null && count > 0;
	}
	
	public UserToken updateToken(UserToken userToken) {
		try {
			String sql = "UPDATE USER_TOKEN SET REGISTRATIONTOKEN = ? WHERE USERNAME = ?";
			UserToken entity = (UserToken) this.jdbcTemplate.query(sql, 
					new Object[] {userToken.getRegistrationToken(), userToken.getUserName()}, 
					new BeanPropertyRowMapper(UserToken.class));
			System.out.println("Token has been updated.");
			return entity;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public List<UserToken> findAll() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM USER_TOKEN";
		List<UserToken> listEntity = jdbcTemplate.query(sql, new BeanPropertyRowMapper(UserToken.class));
		return listEntity;
	}
	

}
