package com.charan.fbgroup.service;

import com.charan.fbgroup.conllection.Post;
import com.charan.fbgroup.conllection.PostsAndCommentsOnly;
import com.charan.fbgroup.response.FeedMessage;
import com.charan.fbgroup.response.PageFeedDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {

    public String appendFiles(String folderPath, String filePrefix, String combinedFilePrefix) throws IOException {
        final File folder = new File(folderPath);
        String finalFile = combinedFilePrefix + new Date().getTime() + ".txt";
        PageFeedDetails pageFeedDetails = new PageFeedDetails();
        for (final File fileEntry : folder.listFiles()) {
            String filePath = fileEntry.getAbsolutePath();
            String fName = fileEntry.getName();
            if (fName.indexOf(filePrefix) != 0) {
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

    public <T> void writeToFile(T t, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String jsonInString = mapper.writeValueAsString(t);
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

    public static<T> List<T> appendLists(List<T>... lists) {
        return Stream.of(lists)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
    }

    public List<PageFeedDetails> readFiles(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);
            List<PageFeedDetails> pageFeedDetailsList = new ArrayList<>();
            for (Object obj : jsonArray) {
                PageFeedDetails pageFeedDetails = (PageFeedDetails) obj;
                pageFeedDetailsList.add(pageFeedDetails);
            }
            return pageFeedDetailsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public String createExcelFromPosts(List<Post> posts, String excelPath) {
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("Java Books");
//
//        Object[][] bookData = {
//                {"Head First Java", "Kathy Serria", 79},
//                {"Effective Java", "Joshua Bloch", 36},
//                {"Clean Code", "Robert martin", 42},
//                {"Thinking in Java", "Bruce Eckel", 35},
//        };
//
//        int rowCount = 0;
//
//        for (Object[] aBook : bookData) {
//            Row row = sheet.createRow(++rowCount);
//
//            int columnCount = 0;
//
//            for (Object field : aBook) {
//                Cell cell = row.createCell(++columnCount);
//                if (field instanceof String) {
//                    cell.setCellValue((String) field);
//                } else if (field instanceof Integer) {
//                    cell.setCellValue((Integer) field);
//                }
//            }
//
//        }
//
//
//        try (FileOutputStream outputStream = new FileOutputStream("JavaBooks.xlsx")) {
//            workbook.write(outputStream);
//        }
//        return excelPath;
//    }

    public File[] getFilesFromFolder(String folderPath) {
        File folder = new File(folderPath);
        return folder.listFiles();
    }

    public void createTxtFromPosts(List<Post> posts, String txtFilePath) throws IOException {
        List<String> allPosts = posts
                .stream()
                .map(post -> {
                    PostsAndCommentsOnly postsAndCommentsOnly = new PostsAndCommentsOnly();
                    postsAndCommentsOnly.setPost(post.getPostMessage());
                    postsAndCommentsOnly.setComments(post.getCommentList());
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        return objectMapper.writeValueAsString(postsAndCommentsOnly);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        Files.write(Paths.get(txtFilePath), Collections.singleton("[" + String.join(",", allPosts) + "]"));
    }

    public void createExcelFromPosts(List<Post> posts, String excelFilePath) {
    }
}
