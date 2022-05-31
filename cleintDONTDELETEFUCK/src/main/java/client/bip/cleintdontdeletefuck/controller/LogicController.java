package client.bip.cleintdontdeletefuck.controller;

import client.bip.cleintdontdeletefuck.entity.FinalLoadEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LogicController {
    public static List<FinalLoadEntity> workload = new ArrayList<>();

    public static void getValues(String filePath) {
        try {
            FileInputStream fileIN = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileIN);
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<Row> rowIterator = new ArrayList<>();
            for (Row row : sheet) {
                rowIterator.add(row);
            }
            for (int i = 5; i < rowIterator.size(); i++) {
                Row thisRow = rowIterator.get(i);
                System.out.println(thisRow.getRowNum());
//              Инициалы учителя
                String teacher = thisRow.getCell(0).getStringCellValue();
//              Предмет
                String subject = thisRow.getCell(1).getStringCellValue();
//              Группа
                String group = thisRow.getCell(2).getStringCellValue();
//           Для проверки второго семестра
                Trio toCheck = new Trio(teacher, group, subject);
                Row nextRowIfExist = null;
//            Экзамен
                int exam = 0;
//            Диплом
                int diplom_hours = 0;
//            Консультации
                int consultations = 0;
//            Курсовые
                int coursework = 0;
//            Семестры
                int term = thisRow.getCell(4).getNumericCellValue() == 1 ? 1 : 2;
//            Первый
                int semesterFirst = 0;
//            Второй
                int semesterSecond = 0;
//            итого
                int total = 0;
//                     Для сочитания первого и второго семестра
                boolean haveNextSemester = toCheck.checkIfSecondSemester(new Trio(
                        rowIterator.get(i + 1).getCell(0).getStringCellValue(),
                        rowIterator.get(i + 1).getCell(1).getStringCellValue(),
                        rowIterator.get(i + 1).getCell(2).getStringCellValue()));
//            Если есть, берём некоторые значения из второй записи
                if (haveNextSemester) {
                    nextRowIfExist = rowIterator.get(i + 1);
                    i++;
                    if (nextRowIfExist.getCell(5).getStringCellValue()
                            .equalsIgnoreCase("э")) {
                        exam = (int) nextRowIfExist.getCell(6).getNumericCellValue();
                        consultations = (int) nextRowIfExist.getCell(8).getNumericCellValue();
                        coursework = (int) nextRowIfExist.getCell(9).getNumericCellValue();
                    }
                    semesterFirst = (int) thisRow.getCell(10).getNumericCellValue();
                    semesterSecond = (int) nextRowIfExist.getCell(11).getNumericCellValue();
                    diplom_hours = (int) thisRow.getCell(7).getNumericCellValue();
//            Если нет, берём значения из первой записи, если они есть
                } else {
                    if (thisRow.getCell(5).getStringCellValue()
                            .equalsIgnoreCase("э")) {
                        exam = (int) thisRow.getCell(6).getNumericCellValue();
                        consultations = (int) thisRow.getCell(8).getNumericCellValue();
                        coursework = (int) thisRow.getCell(9).getNumericCellValue();
                    }
                    if (term == 1) {
                        semesterFirst = (int) thisRow.getCell(10).getNumericCellValue();
                    } else {
                        semesterSecond = (int) thisRow.getCell(11).getNumericCellValue();
                    }
                    diplom_hours = (int) thisRow.getCell(7).getNumericCellValue();
                }
                for (FinalLoadEntity check : workload) {
                    if (teacher.equals(check.getTeacherInLoad())) {
                        check.addAllWhatINeed(subject, group, exam, diplom_hours, consultations, coursework,
                                semesterFirst, semesterSecond);
                        break;
                    }
                }
                workload.add(new FinalLoadEntity(teacher, subject, group, exam, diplom_hours, consultations, coursework,
                        semesterFirst, semesterSecond));
            }
            fileIN.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setValues(String filePath, String sheetName, String whatIsNeed) {
        try {
            FileInputStream fileIN = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(fileIN);
            fileIN.close();
            OutputStream fileOUT = new FileOutputStream(filePath);
            Sheet sheet = wb.getSheet(sheetName);
            sheet.autoSizeColumn(0, true);
            sheet.setColumnWidth(1, 8 * 1000);
            sheet.setColumnWidth(2, 15 * 1000);
            sheet.autoSizeColumn(3, true);
            sheet.autoSizeColumn(4, true);
            sheet.autoSizeColumn(5, true);
            sheet.autoSizeColumn(6, true);
            sheet.autoSizeColumn(7, true);
            sheet.autoSizeColumn(8, true);
            sheet.autoSizeColumn(9, true);
//             "№ п/п", "ФИО", "Читаемые дисциплины", "Диплом", "Консультация", "Экзамен", "Курсовая", "1 сем", "2 сем", "Итого по дисциплинам", "ВСЕГО"
            String[] header = {"№ п/п", "ФИО", "Читаемые дисциплины", "Диплом",
                    "Консультация", "Экзамен", "Курсовая",
                    "1 сем", "2 сем", "ВСЕГО"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(header[i]);
            }
            int cellNum = 1; // номер ячейки с начала таблицы (без учёта шапки)
            for (int i = 0; i < workload.size(); i++) {
                Row rowTable = sheet.createRow(cellNum);
//                myCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
//                myCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                CellStyle myCellStyle = wb.createCellStyle();
                BorderStyle bs_medium = BorderStyle.THIN;
                myCellStyle.setBorderTop(bs_medium);
                Cell rowNumber = rowTable.createCell(0);
                rowNumber.setCellValue(i + 1);
                rowNumber.setCellStyle(myCellStyle);

                Cell initials = rowTable.createCell(1);
                initials.setCellValue(workload.get(i).getTeacherInLoad());
                initials.setCellStyle(myCellStyle);

                Cell subjects = rowTable.createCell(2);
                subjects.setCellValue(workload.get(i).getSubjectInLoad().get(0));
                subjects.setCellStyle(myCellStyle);

                if ('1' == whatIsNeed.charAt(0)) {
                    Cell diploma = rowTable.createCell(3);
                    diploma.setCellValue(workload.get(i).getDiplomaHours());
                    diploma.setCellStyle(myCellStyle);
                }   else {
                    workload.get(i).setTotal(workload.get(i).getTotal() - workload.get(i).getDiplomaHours());
                }

                if ('1' == whatIsNeed.charAt(1)){
                    Cell consultations = rowTable.createCell(4);
                    consultations.setCellValue(workload.get(i).getConsultationHours());
                    consultations.setCellStyle(myCellStyle);
                }   else {
                    workload.get(i).setTotal(workload.get(i).getTotal() - workload.get(i).getCourseworkHours());
                }

                if ('1' == whatIsNeed.charAt(2)) {
                    Cell exam = rowTable.createCell(5);
                    exam.setCellValue(workload.get(i).getExamHours());
                    exam.setCellStyle(myCellStyle);
                } else {
                    workload.get(i).setTotal(workload.get(i).getTotal() - workload.get(i).getExamHours());
                }

                if ('1' == whatIsNeed.charAt(3)){
                    Cell coursework = rowTable.createCell(6);
                    coursework.setCellValue(workload.get(i).getCourseworkHours());
                    coursework.setCellStyle(myCellStyle);
                } else {
                    workload.get(i).setTotal(workload.get(i).getTotal() - workload.get(i).getCourseworkHours());
                }

                if ('1' == whatIsNeed.charAt(4)) {
                    Cell semesterFirst = rowTable.createCell(7);
                    semesterFirst.setCellValue(workload.get(i).getSemesterFirstHours().get(0));
                    semesterFirst.setCellStyle(myCellStyle);
                } else {
                    workload.get(i).setTotal(workload.get(i).getTotal() - workload.get(i).summOfSemesters(workload.get(i).getSemesterFirstHours()));
                }

                if ('1' == whatIsNeed.charAt(5)) {
                    Cell semesterSecond = rowTable.createCell(8);
                    semesterSecond.setCellValue(workload.get(i).getSemesterSecondHours().get(0));
                    semesterSecond.setCellStyle(myCellStyle);
                }   else {
                    workload.get(i).setTotal(workload.get(i).getTotal() - workload.get(i).summOfSemesters(workload.get(i).getSemesterSecondHours()));

                }
                Cell total = rowTable.createCell(9);
                total.setCellValue(workload.get(i).getTotal());
                total.setCellStyle(myCellStyle);
                if (workload.get(i).getSubjectInLoad().size() <= 1) {
                    cellNum++;
                    continue;
                }
                for (int d = 1; d < workload.get(i).getSubjectInLoad().size(); d++) {
                    Row rowDist = sheet.createRow(cellNum + 1);
                    rowDist.createCell(2).setCellValue(workload.get(i).getSubjectInLoad().get(d));
                    rowDist.createCell(7).setCellValue(workload.get(i).getSemesterFirstHours().get(d));
                    rowDist.createCell(8).setCellValue(workload.get(i).getSemesterSecondHours().get(d));
                    cellNum++;
                }
                cellNum++;
            }
            wb.write(fileOUT);
            fileOUT.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createSheet(String filePath, String sheetName) {
        try {
            InputStream fileIN = new FileInputStream(filePath);
            Workbook wb = new XSSFWorkbook(fileIN);
            fileIN.close();
            OutputStream fileOut = new FileOutputStream(filePath);
            if (wb.getSheet(sheetName) == null) {
                wb.createSheet(sheetName);
            }
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception err) {
            System.out.println("ERROR: " + err.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    static class Trio {
        private String teacher;
        private String group;
        private String subject;

        public boolean checkIfSecondSemester(Trio trio) {
            return this.equals(trio);
        }
    }
}
