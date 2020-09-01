package com.jeeplus.modules.shop.api.chat.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.shop.util.Utils;


public class Chat extends DataEntity<Chat> {

    private static final long serialVersionUID = 1L;
    public Chat(){
        this.chatid= Utils.getUUID();
    }

    private String chatid;

    private String form;

    private String gotos;

    private String fromstatus;

    private String tostatus;

    private String content;

    private String addtime;

    private String isRead;

    private String type;


    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getGotos() {
        return gotos;
    }

    public void setGotos(String gotos) {
        this.gotos = gotos;
    }

    public String getFromstatus() {
        return fromstatus;
    }

    public void setFromstatus(String fromstatus) {
        this.fromstatus = fromstatus;
    }

    public String getTostatus() {
        return tostatus;
    }

    public void setTostatus(String tostatus) {
        this.tostatus = tostatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
