package com.exadel.training.service.impl;

import com.exadel.training.controller.model.StatisticsModel;
import com.exadel.training.dao.domain.*;
import com.exadel.training.service.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 20,
            Font.BOLD);

    private static Font INFO_TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLDITALIC);

    private static Font MAIN_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.NORMAL);

    private static Font CELL_FONT = new Font(Font.FontFamily.COURIER, 9, Font.NORMAL);

    private static Font CELL_BOLD_FONT = new Font(Font.FontFamily.COURIER, 9, Font.BOLD);

    private static Integer MAX_COLUMN_NUBMER = 30;

    private static Integer OPTIMAL_COLUMN_NUBMER = 6;

    private static Integer VERTICAL_ROTATION = 90;

    private static Integer HORISONTAL_ROTATION = 0;

    private static Format format = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private UserService userService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private AttendanceService attendanceService;

    private void drawMainInfo(Document document, String title, String text)  throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk(title + ": ", INFO_TITLE_FONT));
        paragraph.add(new Chunk(text, MAIN_FONT));
        document.add(paragraph);
    }

    private void drawTitle(Document document, String title) throws DocumentException {
        Paragraph paragraph = new Paragraph(title, TITLE_FONT);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
    }

    private void drawTrainingShortInfo(Document document, Training training) throws DocumentException {
        drawMainInfo(document, "Title", training.getTitle());
        drawMainInfo(document, "Excerpt", training.getExcerpt());
        drawMainInfo(document, "Start date", format.format(lessonService.getStartDateByTraining(training.getId())));
        drawMainInfo(document, "End date", format.format(lessonService.getEndDateByTraining(training.getId())));
        switch(training.getState()) {
            case CREATE:
                drawMainInfo(document, "State", "waiting for creation approve");
                break;
            case REMOVE:
                drawMainInfo(document, "State", "removed");
                break;
        }
        document.add(Chunk.NEWLINE);
    }

    private void addHeaderCell(PdfPTable table, String text, Integer rotation) {
        PdfPCell cell = new PdfPCell(new Phrase(text, CELL_BOLD_FONT));
        cell.setRotation(rotation);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    private List<Attendance> drawUserTable(Document document, List<Attendance> attendanceList) throws DocumentException {
        Integer columnNumber = attendanceList.size() + 1;
        PdfPTable table = new PdfPTable(columnNumber);
        table.setWidthPercentage(100);
        List<Attendance> attendancesWithComments = new ArrayList<Attendance>();
        Integer rotation;
        if (columnNumber <= OPTIMAL_COLUMN_NUBMER) {
            rotation = HORISONTAL_ROTATION;
        } else {
            rotation = VERTICAL_ROTATION;
        }
        addHeaderCell(table, "", rotation);
        for (Attendance attendance : attendanceList) {
            addHeaderCell(table, format.format(attendance.getLesson().getDate()), rotation);
        }
        addHeaderCell(table, attendanceList.get(0).getLesson().getTraining().getTitle(), rotation);
        for (Attendance attendance : attendanceList) {
            if(StringUtils.isNotBlank(attendance.getComment())) {
                attendancesWithComments.add(attendance);
            }
            if (attendance.isPresence()) {
                PdfPCell cell = new PdfPCell(new Paragraph("", CELL_FONT));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            } else {
                PdfPCell cell = new PdfPCell(new Paragraph("-", CELL_FONT));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
        }
        table.setHeaderRows(1);
        document.add(table);
        document.add(Chunk.NEWLINE);
        return attendancesWithComments;
    }

    private void drawPresenceComments(Document document,List<Attendance> attendancesWithComments,
                                      Boolean isUserStatistics) throws DocumentException {
        for (Attendance attendance : attendancesWithComments) {
            String title;
            if(isUserStatistics){
                title = attendance.getLesson().getTraining().getTitle();
            } else {
                title = attendance.getUser().getFirstName() + " " + attendance.getUser().getLastName();
            }
            drawMainInfo(document, title
                    + ", " + format.format(attendance.getLesson().getDate()), attendance.getComment());
        }
    }

    private void drawUserTrainings(Document document, List<Training> trainingList,
                                   String title, Boolean drawTable, Long id, Long startDate, Long endDate)
            throws DocumentException{
        if (trainingList.size() != 0) {
            drawTitle(document, title);
            for (Training training : trainingList) {
                drawTrainingShortInfo(document, training);
                if (drawTable) {
                    List<Attendance> attendancesWithComments = new ArrayList<Attendance>();
                    List<Attendance> attendanceList = attendanceService
                            .getAllAttendanceByUserIDBetweenDates(id,
                                    training.getId(), new Date(startDate), new Date(endDate));
                    do {
                        attendancesWithComments.addAll(
                                drawUserTable(document, attendanceList.subList(0,
                                        (attendanceList.size() > MAX_COLUMN_NUBMER)
                                                ? MAX_COLUMN_NUBMER : attendanceList.size())));
                        attendanceList = attendanceList.subList((attendanceList.size() > MAX_COLUMN_NUBMER)
                                ? MAX_COLUMN_NUBMER : attendanceList.size(), attendanceList.size());
                    } while (attendanceList.size() > MAX_COLUMN_NUBMER);
                    if (attendancesWithComments.size() != 0){
                        drawTitle(document, "Comments to presence");
                        drawPresenceComments(document, attendancesWithComments, true);
                    }
                }
            }
        }
    }

    private void drawFeedbacks(Document document, List<Feedback> feedbackList, Boolean isUserStatistics)
            throws DocumentException {
        for (Feedback feedback : feedbackList) {
            String title;
            if (isUserStatistics) {
                title = feedback.getTraining().getTitle();
            } else {
                title = feedback.getUser().getFirstName() + " " + feedback.getUser().getLastName();
            }
            String feedbackData = "\n";
            //todo questions
            drawMainInfo(document, title
                    + ", " + format.format(feedback.getDate()), feedbackData);
        }
    }

    private void drawComments(Document document, List<Comment> commentList, Boolean isUserStatistics)
            throws DocumentException {
        for (Comment comment : commentList) {
            String title;
            if (isUserStatistics) {
                title = comment.getTraining().getTitle();
            } else {
                title = comment.getUser().getFirstName() + " " + comment.getUser().getLastName();
            }
            StringBuffer commentData = new StringBuffer("\n");
            commentData.append("Was it interesting? ");
            if (comment.getInteresting()) {
                commentData.append("Yes\n");
            } else {
                commentData.append("No\n");
            }
            commentData.append("Was it clear? ");
            if (comment.getClear()) {
                commentData.append("Yes\n");
            } else {
                commentData.append("No\n");
            }
            commentData.append("Did you learn something new? ");
            if (comment.getNewMaterial()) {
                commentData.append("Yes\n");
            } else {
                commentData.append("No\n");
            }
            commentData.append("Was the coach creative? ");
            if (comment.getCreativity()) {
                commentData.append("Yes\n");
            } else {
                commentData.append("No\n");
            }
            commentData.append("Would you recommend this course? ");
            if (comment.getRecommendation()) {
                commentData.append("Yes\n");
            } else {
                commentData.append("No\n");
            }
            commentData.append("The effectiveness rating: ");
            commentData.append(comment.getEffective() + "\n");
            if (StringUtils.isNotBlank(comment.getOther())) {
                commentData.append("Something else: ");
                commentData.append(comment.getOther());
            }
            drawMainInfo(document, title
                    + ", " + format.format(comment.getDate()), commentData.toString());
        }
    }
//    private void drawTrainingTable(Document document, List<Attendance> attendanceList) throws DocumentException {
//        Integer columnNumber = attendanceList.size() + 1;
//        Integer rowNumber = 2;
//        PdfPTable table = new PdfPTable(columnNumber);
//        table.setWidthPercentage(100);
//        Integer rotation;
//        if (columnNumber <= OPTIMAL_COLUMN_NUBMER) {
//            rotation = HORISONTAL_ROTATION;
//        } else {
//            rotation = VERTICAL_ROTATION;
//        }
//        Format format = new SimpleDateFormat("dd-MM-yyyy");
//        addHeaderCell(table, "", rotation);
//        for (Attendance attendance : attendanceList) {
//            addHeaderCell(table, format.format(attendance.getLesson().getDate()), rotation);
//        }
//        for (Integer j = 0; j < rowNumber; j++) {
//            addHeaderCell(table, j.toString() + "left", rotation);
//            for (Integer i = 0; i < columnNumber - 1; i++) {
//                table.addCell(new PdfPCell(new Paragraph(i.toString() + " cell", CELL_FONT)));
//            }
//        }
//        table.setHeaderRows(1);
//        document.add(table);
//        document.add(Chunk.NEWLINE);
//    }

    @Transactional
    private String getUserStatistics(Long id, Long startDate, Long endDate) throws DocumentException {
        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        drawTitle(document, "User Statistics");
        User user = userService.getUserById(id);
        drawMainInfo(document, "First name", user.getFirstName());
        drawMainInfo(document, "Last name", user.getLastName());
        drawMainInfo(document, "Email", user.getEmail());
        drawMainInfo(document, "Phone number", user.getPhone());
        List<Training> coachTrainingList = userService.getCoachTrainingListOfUser(id);
        if (coachTrainingList.size() != 0) {
            drawTitle(document, "Courses created by this user");
            for (Training training : coachTrainingList) {
                drawTrainingShortInfo(document, training);
            }
        }
        drawUserTrainings(document, userService.getUserTrainingsByState(id, Listener.State.ACCEPTED),
                "User's accepted courses", true, id, startDate, endDate);
        drawUserTrainings(document, userService.getUserTrainingsByState(id, Listener.State.WAITING),
                "User's courses he/she is waiting for", false, id, startDate, endDate);
        drawUserTrainings(document, userService.getUserTrainingsByState(id, Listener.State.LEAVE),
                "User's courses he/she left", true, id, startDate, endDate);
        List<Feedback> feedbackList = user.getFeedbackList();
        if (feedbackList.size() != 0) {
            drawTitle(document, "Feedbacks on this user");
            drawFeedbacks(document, feedbackList, true);
        }
        List<Comment> commentList = user.getCommentList();
        if(commentList.size() != 0) {
            drawTitle(document, "Comments from this user");
            drawComments(document, commentList, true);
        }
        document.close();
        String encodedBytes = encoder.encode(outputStream.toByteArray());

        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodedBytes = decoder.decodeBuffer(encodedBytes);
            FileOutputStream fos = new FileOutputStream("test.pdf");
            fos.write(decodedBytes);
            fos.close();
        } catch (IOException e) {
            System.out.println(":(");
        }
        return encodedBytes;
    }

    @Transactional
    private String getTrainingStatistics(Long id, Long startDate, Long endDate) {
        return null;
    }

    @Override
    public String getStatistics(StatisticsModel statisticsModel) {
        try {
            if (statisticsModel.getStatisticsType() == StatisticsModel.StatisticsType.USER) {
                return getUserStatistics(statisticsModel.getId(),
                        statisticsModel.getStartDate(), statisticsModel.getEndDate());
            } else {
                return getTrainingStatistics(statisticsModel.getId(),
                        statisticsModel.getStartDate(), statisticsModel.getEndDate());
            }
        } catch (DocumentException documentException) {
            return "";
        }
    }
}
