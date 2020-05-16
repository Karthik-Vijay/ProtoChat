package com.sk.protochat.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.sk.protochat.model.Contact;
import com.sk.protochat.model.Message;

@Repository
public class MessageDao {

	private NamedParameterJdbcTemplate nPjdbcTemplate;
	
	private DataSource datasource;

	@Autowired
	public MessageDao(DataSource datasource) {
		
		nPjdbcTemplate = new NamedParameterJdbcTemplate(datasource);
		
	}
	
	public List<Message> selectAll(String fromUser,String toUser){
		
		String sql = "select message_id,fromusername as fromUser,fromusername as toUser,content,timestamp "
				+ "from Messages where fromusername in (:fromusername,:tousername) "
				+ "and tousername in (:fromusername,:tousername) "
				+ "order by timestamp";
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("fromusername", fromUser)
				.addValue("tousername", toUser);
		
		return nPjdbcTemplate.query(sql,inparams,new BeanPropertyRowMapper(Message.class));
		
	}
	
	public int  insert(Message message) {
		
		String sql = "insert into Messages (fromusername,tousername,content,timestamp) "
				+ "values(:fromusername,:tousername,:content,:timestamp)";
		
		SqlParameterSource inparams = new MapSqlParameterSource()
				.addValue("fromusername", message.getFromUser())
				.addValue("tousername", message.getToUser())
				.addValue("content", message.getContent())
				.addValue("timestamp", message.getTimestamp());
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
	
		
		nPjdbcTemplate.update(sql,inparams,keyHolder);
		
		return keyHolder.getKey().intValue();
		
	}
}
