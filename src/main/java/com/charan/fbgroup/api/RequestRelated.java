package com.charan.fbgroup.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestRelated {

    @Value("${fb.app.accesstoken}")
    String accessToken;
    @Value("${fb.graph.url}")
    String graphUrl;
    @Value("${fb.graph.feedendpoint}")
    String feedEndPoint;
    @Value("${fb.post.commentsendpoint}")
    String commentsendpoint;

    public String getQueryForFeed() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?limit=100");
        stringBuilder.append("&access_token=" + accessToken);
        String query = stringBuilder.toString();
        return query;
    }

    public String getQueryForComments() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?summary=1&filter=stream");
        stringBuilder.append("&access_token=" + accessToken);
        String query = stringBuilder.toString();
        return query;
    }
}
