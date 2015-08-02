package com.monash.survivalguide.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by xiaoduo on 7/19/15.
 */

@ParseClassName("Reply")
public class Reply extends ParseObject implements Serializable {

    public Reply getReplyTo() {
        return (Reply) getParseObject("replyTo");
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setReplyTo(Reply replyTo) {
        put("replyTo", replyTo);
    }

    public void setAuthor(ParseUser author) {
        put("author", author);
    }

    public void setBelongTo(Post belongTo) {
        put("belongTo", belongTo);
    }

    public void setReplyContent(String replyContent) {
        put("replyContent", replyContent);
    }

    public Post getBelongTo() {
        return (Post) getParseObject("belongTo");
    }

    public String getReplyContent() {
        return getString("replyContent");
    }
}
