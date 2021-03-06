package com.exadel.controller;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.service.InternService;
import com.exadel.service.PdfService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class PDFController {

    @Autowired
    InternService internService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    PdfService pdfService;

    @GetMapping("/createPdf/{email}/{startDate}/{finishDate}")
    @PreAuthorize("#email == authentication.principal.username or hasAuthority('admin')")
    public void createPDf(HttpServletRequest request,
                          HttpServletResponse response,
                          @PathVariable("email") String email,
                          @PathVariable(value="startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                          @PathVariable(value="finishDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date finishDate
    ) throws IOException, DocumentException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Intern intern = internService.findInternByEmail(email);
        List<Task> taskResult = internService.findTasksBeetwenTwoDates(email,startDate,finishDate);
        boolean isFlag = pdfService.createPdf(intern, taskResult);
        if(isFlag){
            filedownload("interns.pdf", response, "interns.pdf");
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
