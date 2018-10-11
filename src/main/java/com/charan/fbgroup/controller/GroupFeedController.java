package com.charan.fbgroup.controller;

import com.charan.fbgroup.response.PageFeedDetails;
import com.charan.fbgroup.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupFeedController {
    @Autowired
    GroupService groupService;

    @GetMapping(value="pagefeed/{group_id}")
    public PageFeedDetails pageFeedDetails(@PathVariable(value="group_id") String groupId) {
        return groupService.getFeed(groupId);
    }
}
