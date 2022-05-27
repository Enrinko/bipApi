package client.bip.cleintdontdeletefuck.controller;

import client.bip.cleintdontdeletefuck.entity.FinalLoadEntity;
import client.bip.cleintdontdeletefuck.entity.LoadEntity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Iterator;
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
            iterate(rowIterator);
            fileIN.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void iterate(List<Row> rowIterator) {
        for (int i = 5; i < rowIterator.size(); i++) {
           Row row = rowIterator.get(i);
           System.out.println(row.getRowNum());
//              Инициалы учителя
           String surname = row.getCell(0).getStringCellValue();
//              Предмет
           String discipline = row.getCell(1).getStringCellValue();
//              Группа
           String group = row.getCell(2).getStringCellValue();
//         Для сочитания первого и второго семестра
            Trio toCheck = new Trio(surname, group, discipline);
           int term = row.getCell(4).getNumericCellValue() == 1 ? 1 : 2;
//              Экзамен
           int exam = 0;
           if (row.getCell(5).getStringCellValue().equalsIgnoreCase("э")) {
               exam = (int) row.getCell(6).getNumericCellValue();
           }
//              Диплом
           int diplom_hours = (int) row.getCell(7).getNumericCellValue();
           // Часы на 1-ый или 2-ой семестр
           int term_hours;
           if (term == 1) term_hours = (int) row.getCell(10).getNumericCellValue();
           else term_hours = (int) row.getCell(11).getNumericCellValue();
           workload.add(new Disciplines(surname, discipline, diplom_hours, term, term_hours, exam));
        }
    }
    @Data
    @AllArgsConstructor
    static class Trio {
        private String teacher;
        private String group;
        private String subject;
    }

    public static void setValues(String[] args) {
        try {
            FileInputStream fileIN = new FileInputStream(args[0]);
            XSSFWorkbook wb = new XSSFWorkbook(fileIN);
            fileIN.close();
            OutputStream fileOUT = new FileOutputStream(args[0]);
            Sheet sheet = wb.getSheet(args[1]);
            sheet.autoSizeColumn(0, true);
            sheet.setColumnWidth(1, 8 * 1000);
            sheet.setColumnWidth(2, 15 * 1000);
            sheet.autoSizeColumn(3, true);
            sheet.autoSizeColumn(4, true);
//             "№ п/п", "ФИО", "Читаемые дисциплины", "Диплом", "Консультация", "Экзамен", "Курсовая", "1 сем", "2 сем", "Итого по дисциплинам", "ВСЕГО"
            String[] header = {"№ п/п", "ФИО", "Читаемые дисциплины", "Диплом",
                    "Консультация", "Экзамен", "Курсовая",
                    "1 сем", "2 сем", "Итого по дисциплинам", "ВСЕГО"};
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
                initials.setCellValue(workload.get(i).getSurname());
                initials.setCellStyle(myCellStyle);

                Cell cell2 = rowTable.createCell(2);
                cell2.setCellValue(workload.get(i).getDisciplines().get(0));
                cell2.setCellStyle(myCellStyle);

                Cell cell3 = rowTable.createCell(3);
                cell3.setCellValue(workload.get(i).getDiplom_hours());
                cell3.setCellStyle(myCellStyle);

                Cell cell4 = rowTable.createCell(4);
                cell4.setCellValue(workload.get(i).getFirst_term().get(0));
                cell4.setCellStyle(myCellStyle);

                Cell cell5 = rowTable.createCell(5);
                cell5.setCellValue(workload.get(i).getSecond_term().get(0));
                cell5.setCellStyle(myCellStyle);

                Cell cell6 = rowTable.createCell(6);
                cell6.setCellValue(workload.get(i).getRes_terms().get(0));
                cell6.setCellStyle(myCellStyle);

                Cell cell7 = rowTable.createCell(7);
                cell7.setCellValue(workload.get(i).getTotal());
                cell7.setCellStyle(myCellStyle);

                if (workload.get(i).getDisciplines().size() <= 1) {
                    cellNum++;
                    continue;
                }
                for (int d = 1; d < workload.get(i).getDisciplines().size(); d++) {
                    Row rowDist = sheet.createRow(cellNum + 1);
                    rowDist.createCell(2).setCellValue(workload.get(i).getDisciplines().get(d));
                    rowDist.createCell(4).setCellValue(workload.get(i).getFirst_term().get(d));
                    rowDist.createCell(5).setCellValue(workload.get(i).getSecond_term().get(d));
                    rowDist.createCell(6).setCellValue(workload.get(i).getRes_terms().get(d));
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
}
