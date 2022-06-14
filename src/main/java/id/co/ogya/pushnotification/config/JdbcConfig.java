package id.co.ogya.pushnotification.config;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan(basePackages = "id.co.ogya.pushnotification.*")
public class JdbcConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSourcePG() {
		PGSimpleDataSource pg = new PGSimpleDataSource();
		pg.setServerName(env.getProperty("postgres.server"));
		pg.setDatabaseName(env.getProperty("postgres.databaseName"));
		int portNumber = Integer.parseInt(env.getProperty("postgres.port"));
		pg.setPortNumber(portNumber);
		pg.setUser(env.getProperty("postgres.user"));
		pg.setPassword(env.getProperty("postgres.password"));
		return pg;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		return jdbcTemplate;
	}
	
	/*@Bean
	public PlatformTransactionManager transactionManager() throws SQLException {
	      DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
	      transactionManager.setDataSource(dataSourcePG());
	      return transactionManager;
	  }*/

}
