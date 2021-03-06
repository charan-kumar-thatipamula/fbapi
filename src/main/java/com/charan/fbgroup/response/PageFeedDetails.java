package com.charan.fbgroup.response;

import com.charan.fbgroup.response.FeedMessage;
import com.charan.fbgroup.response.PreviousNextLink;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public class PageFeedDetails {

    List<FeedMessage> data;
    PreviousNextLink paging;

    public List<FeedMessage> getData() {
        return data;
    }

    public void setData(List<FeedMessage> data) {
        this.data = data;
    }

    public PreviousNextLink getPaging() {
        return paging;
    }

    public void setPaging(PreviousNextLink paging) {
        this.paging = paging;
    }
}

