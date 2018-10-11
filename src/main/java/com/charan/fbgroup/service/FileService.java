package com.charan.fbgroup.service;

import com.charan.fbgroup.response.FeedMessage;
import com.charan.fbgroup.response.PageFeedDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {

    public String appendFiles(String folderPath) throws IOException {
        final File folder = new File(folderPath);
        String finalFile = "finalFile_" + new Date().getTime() + ".txt";
        PageFeedDetails pageFeedDetails = new PageFeedDetails();
        for (final File fileEntry : folder.listFiles()) {
            String filePath = fileEntry.getAbsolutePath();
            String fName = fileEntry.getName();
            if (fName.indexOf("temp") != 0) {
                continue;
            }
            System.out.println(filePath);
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            PageFeedDetails tempPageFeedDetails = new ObjectMapper().readValue(content, PageFeedDetails.class);
            if (tempPageFeedDetails == null || tempPageFeedDetails.getData() == null || tempPageFeedDetails.getData().isEmpty()) {
                continue;
            }
            appendData(pageFeedDetails, tempPageFeedDetails);
        }
        writeToFile(pageFeedDetails, finalFile);
        System.out.println(finalFile);
        return finalFile;
    }

    private void writeToFile(PageFeedDetails pageFeedDetails, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String jsonInString = mapper.writeValueAsString(pageFeedDetails);
            File file = new File(fileName);
            boolean isSuccess = file.createNewFile();
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

}
