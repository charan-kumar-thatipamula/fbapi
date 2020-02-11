package com.charan.fbgroup.service;

import com.charan.fbgroup.api.RequestRelated;
import com.charan.fbgroup.api.RestApiManager;
import com.charan.fbgroup.response.FeedMessage;
import com.charan.fbgroup.response.PageFeedDetails;
import com.charan.fbgroup.response.PreviousNextLink;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GroupService {

    @Value("${fb.graph.url}")
    String graphUrl;
    @Value("${fb.graph.feedendpoint}")
    String feedEndPoint;

    @Autowired
    RestApiManager restApiManager;
    @Autowired
    RequestRelated requestRelated;
    @Autowired
    PostService postService;

    public PageFeedDetails getFeed(String groupId) {
        String query = requestRelated.getQueryForFeed();
        return getFeed(groupId, query);
    }

    public PageFeedDetails getFeed(String groupId, String query) {
        PageFeedDetails pageFeedDetails = null;
        try {
            pageFeedDetails = extractPageFeedRecursively(groupId, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String finalFileName = "/combinedData/02072020finalData" + new Date().getTime() + ".txt";
//        writeToFile(pageFeedDetails, finalFileName);
//        System.out.println(finalFileName);
        postService.addPosts(pageFeedDetails);
        return pageFeedDetails;
    }

    private PageFeedDetails extractPageFeedRecursively(String groupId, String query) throws Exception {
        HttpHeaders httpHeaders = null;
        PageFeedDetails pageFeedDetails = new PageFeedDetails();
        String fullUrl = null;
        while (true) {
            try {
                PageFeedDetails tempPageFeedDetails = null;
                if (fullUrl == null) {
                    tempPageFeedDetails = restApiManager.get(graphUrl, "/" + groupId + feedEndPoint, query, httpHeaders, PageFeedDetails.class);
                } else {
                    break;
//                    tempPageFeedDetails = restApiManager.get(fullUrl, httpHeaders, PageFeedDetails.class);
                }

                if (tempPageFeedDetails != null && !tempPageFeedDetails.getData().isEmpty()) {
                    PreviousNextLink previousNextLink = tempPageFeedDetails.getPaging();
                    fullUrl = previousNextLink.getNext();
                    System.out.println("previous: " + fullUrl);
                    System.out.println("next: " + fullUrl);
                }

                if (tempPageFeedDetails == null || tempPageFeedDetails.getData().isEmpty() || fullUrl == null || fullUrl.length() == 0) {
                    break;
                }

                writeToFile(tempPageFeedDetails, "02072020tempData_" + new Date().getTime() + ".txt");
                appendData(pageFeedDetails, tempPageFeedDetails);
            } catch (Exception e) {
                System.out.println("exception fetching details: " + e.getMessage());
                break;
            }

        }
        return pageFeedDetails;
    }

    private void writeToFile(PageFeedDetails pageFeedDetails, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String jsonInString = mapper.writeValueAsString(pageFeedDetails);
            File file = new File(fileName);
            file.createNewFile();
            FileWriter fw=new FileWriter(file, true);
            fw.write(jsonInString);
            fw.close();
        }catch(Exception e){System.out.println(e);}
        System.out.println("Success...");
    }

    private void appendData(PageFeedDetails pageFeedDetails, PageFeedDetails tempPageFeedDetails) {
        if (tempPageFeedDetails == null || tempPageFeedDetails.getData().isEmpty()) {
            return;
        }
        if (pageFeedDetails.getData() == null || pageFeedDetails.getData().isEmpty()) {
            pageFeedDetails.setData(tempPageFeedDetails.getData());
            pageFeedDetails.setPaging(tempPageFeedDetails.getPaging());
            return;
        }

        List<FeedMessage> feedMessages = appendLists(pageFeedDetails.getData(), tempPageFeedDetails.getData());
        pageFeedDetails.setData(feedMessages);
    }

    private static<T> List<T> appendLists(List<T>... lists) {
        return Stream.of(lists)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
    }

    public PageFeedDetails getPageFeedDetailsFromFile(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);
            List<FeedMessage> feedMessages = new ArrayList<>();
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String feedMessageString = jsonObject.toString();
                FeedMessage feedMessage = new ObjectMapper().readValue(feedMessageString, FeedMessage.class);
//                feedMessage.setId(null);
                feedMessage.setUpdated_time(null);
                feedMessages.add(feedMessage);
            }
            PageFeedDetails pageFeedDetails = new PageFeedDetails();
            pageFeedDetails.setData(feedMessages);
            return pageFeedDetails;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PageFeedDetails getFeed(String groupId, String limit, String accessToken) {
        String query = requestRelated.getQueryForFeed(limit, accessToken);
        return getFeed(groupId, query);
    }
}
