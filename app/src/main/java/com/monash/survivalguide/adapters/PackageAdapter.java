package com.monash.survivalguide.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monash.survivalguide.R;
import com.monash.survivalguide.configurations.LocalConfiguration;
import com.monash.survivalguide.entities.Post;
import com.monash.survivalguide.entities.Unit;
import com.monash.survivalguide.helpers.ImageLoader;
import com.monash.survivalguide.util.Util;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.regex.Pattern;

public class PackageAdapter extends ParseQueryAdapter<ParseObject> {

    private Context c;
    private ImageLoader mLoader;
    public PackageAdapter(Context context) {

        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Post");
                query.addDescendingOrder("createdAt");
                return query;
            }
        });
        c = context;
        mLoader = new ImageLoader(context);
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_article, null);
        }

        super.getItemView(object, v, parent);
        final ViewHolder holder = new ViewHolder();
        holder.ll_catalogntags = (TextView) v.findViewById(R.id.tags);
        holder.tv_title = (TextView) v.findViewById(R.id.title);
        holder.tv_time = (TextView) v.findViewById(R.id.time);
        holder.tv_author = (TextView) v.findViewById(R.id.author);
        holder.tv_abstract = (TextView) v.findViewById(R.id.abstracts);
        holder.iv_portrait = (ImageView) v.findViewById(R.id.potrait);

        final Post item = (Post) object;
        holder.tv_title.setText(item.getTitle());
        holder.tv_title.setAlpha((float) 0.6);
        holder.tv_time.setText(Util.dateFormTransfer(item.getCreatedAt(), c));

        item.getAuthor().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseUser author = (ParseUser) object;
                    holder.tv_author.setText(author.getUsername());
                    // Do stuff with currUser
                }
            }
        });


        item.getAuthor().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null && item.getAuthor().getParseFile("portrait").isDataAvailable())
                {
                    mLoader.DisplayImage(item.getAuthor().getParseFile("portrait").getUrl(), holder.iv_portrait);
                }
            }
        });




        String tempStr = item.getContent();
        Pattern p = Pattern.compile(LocalConfiguration.IMAGE_SYMBOL_START + "\\d+" + LocalConfiguration.IMAGE_SYMBOL_END);
        String[] str = p.split(tempStr);
        tempStr = "";
        for (String rmp : str) {
            tempStr += rmp;
        }
        tempStr = tempStr.replace(LocalConfiguration.IMAGE_TEXT_SYMBOL_START, " ").replace(LocalConfiguration.IMAGE_TEXT_SYMBOL_END, " ").replace("\n", " ");
        if (tempStr.length() > LocalConfiguration.CONTENT_TEXT_MAX_LENGTH) {
            holder.tv_abstract.setText(tempStr.substring(0, LocalConfiguration.CONTENT_TEXT_MAX_LENGTH_MINUS_ONE));
        } else {
            holder.tv_abstract.setText(tempStr);
        }


        item.getUnit().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Unit unit = (Unit) object;
                    holder.ll_catalogntags.setText(unit.getName());
                    // Do stuff with currUser
                }
            }
        });


        return v;
    }

    class ViewHolder {
        TextView tv_title;
        TextView tv_time;
        TextView tv_author;
        TextView tv_abstract;
        TextView ll_catalogntags;
        ImageView iv_portrait;
    }
}
