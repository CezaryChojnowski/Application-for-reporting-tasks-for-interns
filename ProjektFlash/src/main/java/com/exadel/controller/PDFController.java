package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.InternService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PDFController {

    @Autowired
    InternService internService;

    @Autowired
    ServletContext servletContext;

    @GetMapping("/createPdf")
    public void createPDf(HttpServletRequest request,
                          HttpServletResponse response
    ) throws FileNotFoundException, DocumentException, ParseException {
        String email = "abcd@gmail.com";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = format.parse("2019-07-03");
        Date finishDate = format.parse("2019-07-14");
        Intern intern = internService.findInternByEmail(email);
        List<Task> taskResult = internService.findTasksBeetwenTwoDates(email,startDate,finishDate);
        boolean isFlag = internService.createPdf(intern, taskResult, servletContext, request, response);
        if(isFlag){
            String fullPath = request.getServletContext().getRealPath("/resources/reports/"+"interns"+".pdf");
            filedownload(fullPath, response, "interns.pdf");
        }
    }

    public void filedownload(String fullPath, HttpServletResponse response, String fileName){
        File file = new File(fullPath);
        final int BUFFER_SIZE = 4096;
        if(file.exists()){
            try{
                FileInputStream inputStream = new FileInputStream(file);
                String mimeType = servletContext.getMimeType(fullPath);
                response.setContentType(mimeType);
                response.setHeader("context-disposition", "attachment; filename=" + fileName);
                OutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while((bytesRead = inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,bytesRead);
                }
                inputStream.close();
                outputStream.close();
                file.delete();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
