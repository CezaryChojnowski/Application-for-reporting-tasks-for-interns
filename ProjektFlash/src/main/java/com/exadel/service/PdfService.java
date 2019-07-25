package com.exadel.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.repository.InternRepository;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Service
public class PdfService {

    @Autowired
    InternRepository internRepository;

    @Autowired
    private TemplateEngine templateEngine;

    public boolean createPdf(Intern intern, List<Task> taskResult) throws IOException, DocumentException {
        try{Context tempContext = new Context();
            int totalHourseInTheRange = 0;
            for(int i=0; i< taskResult.size(); i++){
                totalHourseInTheRange = totalHourseInTheRange + taskResult.get(i).getHours();
            }
            tempContext.setVariable("totalHourseInTheRange",totalHourseInTheRange );
        tempContext.setVariable("taskResult", taskResult);
        String processHtml = templateEngine.process("UniversityTemplate/" + "test", tempContext);

        OutputStream outputStream = new FileOutputStream("interns.pdf");
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processHtml);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        return true;
        }catch (IOException e){
            return false;
        }
        catch (DocumentException d){
            return false;
        }
    }
}
