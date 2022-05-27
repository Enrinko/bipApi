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
                    total = exam + consultations + coursework + semesterFirst + semesterSecond + diplom_hours;
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
                    total = exam + consultations + coursework + semesterFirst + semesterSecond + diplom_hours;
                }

                workload.add(new FinalLoadEntity(teacher, subject, group, exam, diplom_hours, consultations, coursework, semesterFirst, semesterSecond, semesterFirst + semesterSecond, total));
            }
            fileIN.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setValues(String filePath, String sheetName) {
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
            List<Trio> trioList = new ArrayList<>();
            for (int j = 0; j < workload.size(); j++) {
                trioList.add(new Trio(workload.get(j).getTeacherInLoad(), workload.get(j).getGroupInLoad(),
                        workload.get(j).getSubjectInLoad()));
            }
            int cellNum = 1; // номер ячейки с начала таблицы (без учёта шапки)
            for (int i = 0; i < workload.size(); i++) {
                Row rowTable = sheet.createRow(cellNum);
//                myCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
//                myCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                CellStyle myCellStyle = wb.createCellStyle();
                BorderStyle bs_medium = BorderStyle.THIN;
                myCellStyle.setBorderTop(bs_medium);
                List<String> subjectsList = trioList.get(0).allTeacherSubjects(workload.get(i).getTeacherInLoad(), trioList);
                Cell rowNumber = rowTable.createCell(0);
                rowNumber.setCellValue(i + 1);
                rowNumber.setCellStyle(myCellStyle);

                Cell initials = rowTable.createCell(1);
                initials.setCellValue(workload.get(i).getTeacherInLoad());
                initials.setCellStyle(myCellStyle);

                Cell subjects = rowTable.createCell(2);
                subjects.setCellValue(workload.get(i).getSubjectInLoad());
                subjects.setCellStyle(myCellStyle);

                Cell diploma = rowTable.createCell(3);
                diploma.setCellValue(workload.get(i).getDiplomaHours());
                diploma.setCellStyle(myCellStyle);

                Cell consultations = rowTable.createCell(4);
                consultations.setCellValue(workload.get(i).getConsultationHours());
                consultations.setCellStyle(myCellStyle);

                Cell exam = rowTable.createCell(5);
                exam.setCellValue(workload.get(i).getExamHours());
                exam.setCellStyle(myCellStyle);

                Cell coursework = rowTable.createCell(6);
                coursework.setCellValue(workload.get(i).getCourseworkHours());
                coursework.setCellStyle(myCellStyle);

                Cell semesterFirst = rowTable.createCell(7);
                semesterFirst.setCellValue(workload.get(i).getSemesterFirstHours());
                semesterFirst.setCellStyle(myCellStyle);

                Cell semesterSecond = rowTable.createCell(8);
                semesterSecond.setCellValue(workload.get(i).getSemesterSecondHours());
                semesterSecond.setCellStyle(myCellStyle);

                Cell total = rowTable.createCell(9);
                total.setCellValue(workload.get(i).getTotal());
                total.setCellStyle(myCellStyle);
                if (subjectsList.size() <= 1) {
                    cellNum++;
                    continue;
                }
                for (int d = 1; d < subjectsList.size(); d++) {
                    Row rowDist = sheet.createRow(cellNum + 1);
                    rowDist.createCell(2).setCellValue(subjectsList.get(d));
                    rowDist.createCell(4).setCellValue(workload.get(i).getFirst_term().get(d));
                    rowDist.createCell(5).setCellValue(workload.get(i).getSecond_term().get(d));
                    rowDist.createCell(6).setCellValue(workload.get(i).getRes_terms().get(d));
                    cellNum++;
                }
                trioList.remove(0);
                teachers.remove(0);
                subjectsList.clear();
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
        public List<String> allTeacherSubjects(String teacher, List<Trio> trio) {
            List<String> subject = new ArrayList<>();
            for (int i = 0; i < trio.size(); i++) {
                if (trio.get(i).teacher.equals(teacher)) {
                    subject.add(trio.get(i).subject);
                }
            }
            return subject;
        }
        public List<String> allTeachers(List<Trio> trio) {
            HashSet<String> subTeacher = new HashSet<>();
            for (int i = 0; i < trio.size(); i++) {
                subTeacher.add(trio.get(i).teacher);
            }
            return new ArrayList<>(subTeacher);
        }
    }
}
