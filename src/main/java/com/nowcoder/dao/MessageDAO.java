package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    /**
     * 添加消息
     * @param message 消息实体
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values(#{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);

    /**
     * 获取对话消息序列
     * @param conversationId 对话id
     * @param offset    分页使用，偏移量
     * @param limit     分页使用，数量
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit")  int limit);

    /**
     * 获取当前用户的所有对话列表
     * @param userId 当前用户Id
     * @param offset 分页使用，偏移量
     * @param limit  分页使用，数量
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where id in (select max(id) from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} group by conversation_id)",
            "order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    /**
     * 当前用户在当前回话中未读信息数量
     * @param userId  用户id
     * @param conversationId 会话id
     * @return
     */
    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    /**
     * 当前用户在当前回话中信息总量
     * @param userId 用户id
     * @param conversationId 会话id
     * @return
     */
    @Select({"select count(id) from ", TABLE_NAME, " where has_read = 0 and conversation_id=#{conversationId}"})
    int getConversationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

}
