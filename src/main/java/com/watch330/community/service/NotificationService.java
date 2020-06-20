package com.watch330.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.watch330.community.dto.NotificationDTO;
import com.watch330.community.enums.NotificationStatus;
import com.watch330.community.enums.NotificationType;
import com.watch330.community.exception.CustomizeException;
import com.watch330.community.exception.ErrorCode;
import com.watch330.community.mapper.NotificationMapper;
import com.watch330.community.model.Notification;
import com.watch330.community.model.NotificationExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;


    public PageInfo getListByUserId(Integer pageNum, Integer pageSize, Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        notificationExample.setOrderByClause("gmt_create desc");

        PageHelper.startPage(pageNum, pageSize);
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);

        PageInfo tempPageInfo = new PageInfo<>(notifications, 5);
        List<NotificationDTO> notificationDTOList = new ArrayList<>();


        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationType.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

        PageInfo pageInfo = new PageInfo<>(notificationDTOList);
        BeanUtils.copyProperties(tempPageInfo, pageInfo, "list");

        return pageInfo;
    }

    public long getUnRead(Long id) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id)
                .andStatusEqualTo(NotificationStatus.UNREAD.getStatus());
        return notificationMapper.countByExample(example);

    }

    public NotificationDTO readNotification(Long id, Long userId) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
            throw new CustomizeException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(), userId)){
            throw new CustomizeException(ErrorCode.READ_NOTIFICATION_FAILED);
        }

        notification.setStatus(NotificationStatus.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationType.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
