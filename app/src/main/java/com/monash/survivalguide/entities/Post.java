package com.monash.survivalguide.entities;

import com.parse.CountCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.Serializable;

/**
 * Created by xiaoduo on 7/19/15.
 */

@ParseClassName("Post")
public class Post extends ParseObject implements Serializable {

    public String getTitle() {
        return getString("title");
    }

    public String getContent() {
        return getString("content");
    }


    public void setTitle(String title) {
        put("title", title);
    }

    public void setContent(String content) {
        put("content", content);
    }


    public void setUnit(Unit unit) {
        put("unit", unit);
    }

    public void setAuthor(ParseUser author) {
        put("author", author);
    }

    public Unit getUnit() {
        return (Unit) getParseObject("unit");
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setLiked(int liked) {
        put("liked", liked);
    }

    public void setSeen(int seen) {
        put("seen", seen);
    }

    public int getLiked() {
        return getInt("liked");

    }

    public int getSeen() {
        return getInt("seen");
    }

    public void likeToggleWithCallBack(final Post p, final SaveCallback addCallBack, final SaveCallback minusCallBack) {
        final ParseUser currentUser = ParseUser.getCurrentUser();
        final ParseRelation<ParseObject> relation = currentUser.getRelation("likedPost");
        ParseQuery<ParseObject> query = relation.getQuery();
        query.whereEqualTo("objectId", p.getObjectId());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null) {
                    if (i > 0) {
                        relation.remove(p);
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                p.setLiked(p.getLiked() - 1);
                                p.saveInBackground(minusCallBack);
                            }
                        });
                    } else {
                        relation.add(p);
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                p.setLiked(p.getLiked() + 1);
                                p.saveInBackground(addCallBack);
                            }
                        });
                    }
                }
            }
        });
    }
}
