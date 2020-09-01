package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.ChatRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.tools.Result;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IChatRecordService extends IService<ChatRecord> {

    List<Map<String, Object>> getMessage(String userId);

    void deleteMessage(String chat_record_id,String userId);

    Integer selectMessageNumber(String userId);

    List<Map<String, Object>> selectMessageContent(String toId,String status, String userId, Integer pageNum, Integer pageSize);

    void setGroupChatIsRead(String userId, String groupChatId);

    void setPeopleIsRead(String userId, String groupChatId);

    List<Map<String, Object>> getEndMessage(String userId);

    BigDecimal robRedMoney(String userId, String chatRecordId);

    Result sendRedMoney(String chatRecordId,String userId, Integer redNumber, BigDecimal redMoney);
}
