package com.exadel.service;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import com.exadel.repository.InternRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service
public class InternService {

    @Autowired
    InternRepository internRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncode;

    public List<Intern> getAllIntern(){
        return internRepository.findAll();
    }

    public Intern findByid(ObjectId id){
        return internRepository.findByid(id);
    }

    public Intern findTasksByEmail(String email){
        return internRepository.findTasksByEmail(email);
    }

    public Intern findInternByEmail(String email){
        return internRepository.findInternByEmail(email);
    }

    public List<Task> findTasksByid(ObjectId id){
        return internRepository.findTasksByid(id);
    }

    public Intern createIntern(String firstName, String surname, String school, String email, String pass, int hoursPerWeek, Date[] internshipTime){
        Intern intern = new Intern();
        intern.setFirstName(firstName);
        intern.setSurname(surname);
        intern.setSchool(school);
        intern.setEmail(email);
        intern.setRole("intern");
        intern.setInternshipTime(internshipTime);
        intern.setHoursPerWeek(hoursPerWeek);
        List<Task> emptyTasksList = new ArrayList<Task>();
        intern.setTasks(emptyTasksList);
        intern.setPassword(bCryptPasswordEncode.encode(pass));
        return internRepository.save(intern);
    }

    public void delete(String email){
        Intern intern = internRepository.findInternByEmail(email);
        internRepository.delete(intern);
    }

    public Intern update(ObjectId _id, String firstName, String surname, String school, String email, int hoursPerWeek, Date[] internshipTime){
        Intern intern = internRepository.findByid(_id);
        intern.set_id(_id);
        intern.setFirstName(firstName);
        intern.setSurname(surname);
        intern.setSchool(school);
        intern.setEmail(email);
        intern.setRole("intern");
        intern.setInternshipTime(internshipTime);
        intern.setHoursPerWeek(hoursPerWeek);
        return internRepository.save(intern);
    }

    public Intern addTask(String email, Task task){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        tasks.add(task);
        intern.setTasks(tasks);
        return internRepository.save(intern);
    }

    public Intern deleteTask(String email, ObjectId _idTask){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        for (Task t: tasks) {
            if(t.get_idTask().equals(_idTask)){
                tasks.remove(t);
                intern.setTasks(tasks);
                break;
            }
        }
        return internRepository.save(intern);
    }

    public Intern updateTask(String email, ObjectId _idTask, Date date, int hours, String task, String EK){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        for (Task t: tasks) {
            if(t.get_idTask().equals(_idTask)){
                t.setDate(date);
                t.setHours(hours);
                t.setTask(task);
                t.setEK(EK);
                intern.setTasks(tasks);
                break;
            }
        }
        return internRepository.save(intern);
    }

    public Task findTask(String email, ObjectId _idTask){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        for (Task t: tasks) {
            if(t.get_idTask().equals(_idTask)){
                return t;
            }
        }
        return null;
    }

    public List<Task> findTasksBeetwenTwoDates(String email, Date startdate, Date finishdate){
        Intern intern = findTasksByEmail(email);
        List<Task> tasks = intern.getTasks();
        List<Task> taskRestult = new ArrayList<Task>();
        Date correctStartDate = subtractDays(startdate, 1);
        Date correctFinishDate = addDays(finishdate, 1);
        for (Task t: tasks) {
            if(t.getDate().after(correctStartDate ) && t.getDate().before(correctFinishDate)){
                taskRestult.add(t);
            }
        }
        return taskRestult;
    }
    public boolean checkCorrectRange(Date startDate, Date finishDate){
        if(startDate.after(finishDate)){
            return false;
        }
        return true;
    }

    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }

    public boolean checkIfTheTaskIsInTheRange(Date dateTask, Date[] internshipTime){
        internshipTime[0] = subtractDays(internshipTime[0], 1);
        internshipTime[1] = addDays(internshipTime[1], 1);
        if(dateTask.after(internshipTime[0]) && dateTask.before(internshipTime[1])){
           return true;
        }
        return false;
    }

    public boolean checkIfTheTimeIsInTheLimit(int workTime){
        if(workTime <=8 && workTime >=1){
            return true;
        }
        return false;
    }

    public boolean checkIfTheEmailIsUnique(String email){
        if(internRepository.findInternByEmail(email)==null){
            return true;
        }
        return false;
    }

    public boolean createPdf(Intern intern, List<Task> taskResult, ServletContext context, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.A4, 15,15,45,30);
        try {
            String filePath = context.getRealPath("/resources/reports");
            File file = new File(filePath);
            boolean exist = new File(filePath).exists();
            if (!exist) {
                new File(filePath).mkdirs();
            }

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file + "/" + "interns.pdf"));
            document.open();

            Font mainFont = FontFactory.getFont("Arial", 10, BaseColor.BLACK);

            Paragraph paragraph = new Paragraph(intern.getFirstName() + " " + intern.getSurname(), mainFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.setIndentationLeft(50);
            paragraph.setIndentationRight(50);
            paragraph.setSpacingAfter(10);
            document.add(paragraph);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10);

            Font tableHeader = FontFactory.getFont("Arial", 10, BaseColor.BLACK);
            Font tableBody = FontFactory.getFont("Arial", 9, BaseColor.BLACK);

            float[] columnWidths = {1f, 1f, 5f, 1f};
            table.setWidths(columnWidths);

            PdfPCell date = new PdfPCell(new Paragraph("Date", tableHeader));
            date.setBorderColor(BaseColor.BLACK);
            date.setPadding(10);
            date.setHorizontalAlignment(Element.ALIGN_CENTER);
            date.setVerticalAlignment(Element.ALIGN_CENTER);
            date.setBackgroundColor(BaseColor.GRAY);
            date.setExtraParagraphSpace(5f);
            table.addCell(date);

            PdfPCell hours = new PdfPCell(new Paragraph("Hours", tableHeader));
            hours.setBorderColor(BaseColor.BLACK);
            hours.setPadding(10);
            hours.setHorizontalAlignment(Element.ALIGN_CENTER);
            hours.setVerticalAlignment(Element.ALIGN_CENTER);
            hours.setBackgroundColor(BaseColor.GRAY);
            hours.setExtraParagraphSpace(5f);
            table.addCell(hours);

            PdfPCell task = new PdfPCell(new Paragraph("Task", tableHeader));
            task.setBorderColor(BaseColor.BLACK);
            task.setPadding(10);
            task.setHorizontalAlignment(Element.ALIGN_CENTER);
            task.setVerticalAlignment(Element.ALIGN_CENTER);
            task.setBackgroundColor(BaseColor.GRAY);
            task.setExtraParagraphSpace(5f);
            table.addCell(task);

            PdfPCell ek = new PdfPCell(new Paragraph("EK", tableHeader));
            ek.setBorderColor(BaseColor.BLACK);
            ek.setPadding(10);
            ek.setHorizontalAlignment(Element.ALIGN_CENTER);
            ek.setVerticalAlignment(Element.ALIGN_CENTER);
            ek.setBackgroundColor(BaseColor.GRAY);
            ek.setExtraParagraphSpace(5f);
            table.addCell(ek);

            int totalHourseInTheRange = 0;

            for (Task printTask : taskResult) {
                Date dateTemp = printTask.getDate();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
                String strDate = formatter.format(dateTemp);
                PdfPCell dateValue = new PdfPCell(new Paragraph(strDate, tableBody));
                dateValue.setBorderColor(BaseColor.BLACK);
                dateValue.setPadding(10);
                dateValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                dateValue.setVerticalAlignment(Element.ALIGN_CENTER);
                dateValue.setBackgroundColor(BaseColor.WHITE);
                dateValue.setExtraParagraphSpace(5f);
                table.addCell(dateValue);

                String hourseTemp = Integer.toString(printTask.getHours());
                PdfPCell hourseValue = new PdfPCell(new Paragraph(hourseTemp, tableBody));
                hourseValue.setBorderColor(BaseColor.BLACK);
                hourseValue.setPadding(10);
                hourseValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                hourseValue.setVerticalAlignment(Element.ALIGN_CENTER);
                hourseValue.setBackgroundColor(BaseColor.WHITE);
                hourseValue.setExtraParagraphSpace(5f);
                table.addCell(hourseValue);

                PdfPCell taskValue = new PdfPCell(new Paragraph(printTask.getTask(), tableBody));
                taskValue.setBorderColor(BaseColor.BLACK);
                taskValue.setPadding(10);
                taskValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                taskValue.setVerticalAlignment(Element.ALIGN_CENTER);
                taskValue.setBackgroundColor(BaseColor.WHITE);
                taskValue.setExtraParagraphSpace(5f);
                table.addCell(taskValue);

                PdfPCell ekValue = new PdfPCell(new Paragraph(printTask.getEK(), tableBody));
                ekValue.setBorderColor(BaseColor.BLACK);
                ekValue.setPadding(10);
                ekValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                ekValue.setVerticalAlignment(Element.ALIGN_CENTER);
                ekValue.setBackgroundColor(BaseColor.WHITE);
                ekValue.setExtraParagraphSpace(5f);
                table.addCell(ekValue);

                totalHourseInTheRange = totalHourseInTheRange + printTask.getHours();

            }

            PdfPCell total = new PdfPCell(new Paragraph("Total: ", tableBody));
            total.setBorderColor(BaseColor.BLACK);
            total.setPadding(10);
            total.setHorizontalAlignment(Element.ALIGN_CENTER);
            total.setVerticalAlignment(Element.ALIGN_CENTER);
            total.setBackgroundColor(BaseColor.GRAY);
            total.setExtraParagraphSpace(5f);
            table.addCell(total);

            PdfPCell totalValue = new PdfPCell(new Paragraph(String.valueOf(totalHourseInTheRange), tableBody));
            totalValue.setBorderColor(BaseColor.BLACK);
            totalValue.setPadding(10);
            totalValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalValue.setVerticalAlignment(Element.ALIGN_CENTER);
            totalValue.setBackgroundColor(BaseColor.GRAY);
            totalValue.setExtraParagraphSpace(5f);
            table.addCell(totalValue);

            PdfPCell temp = new PdfPCell(new Paragraph(" ", tableBody));
            temp.setColspan(2);
            temp.setBorderColor(BaseColor.BLACK);
            temp.setPadding(10);
            temp.setHorizontalAlignment(Element.ALIGN_CENTER);
            temp.setVerticalAlignment(Element.ALIGN_CENTER);
            temp.setBackgroundColor(BaseColor.WHITE);
            temp.setExtraParagraphSpace(5f);
            temp.setBorder(Rectangle.NO_BORDER);
            table.addCell(temp);


            document.add(table);
            document.close();
            writer.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
