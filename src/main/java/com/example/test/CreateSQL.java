package com.example.test;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author chengsp
 * @date 2020/6/9 16:23
 */
public class CreateSQL {

    public static void main(String[] args) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\data\\wechat-recieve\\WeChat " +
                "Files\\adg1280306513\\FileStorage\\File\\2020-06\\客户域数据库设计-国银客户化.xlsx");
        int numberOfSheets = workbook.getNumberOfSheets();
        BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Administrator\\AppData\\Roaming\\JetBrains" +
                "\\IntelliJIdea2020.1\\scratches\\new.sql"));

        for (int i = 2; i < numberOfSheets; i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            int lastRowNum = sheet.getLastRowNum();
            StringBuilder createSql =
                    new StringBuilder("create table ").append(sheet.getSheetName().trim()).append("\n");
            createSql.append("(\n");
            for (int j = 2; j <= lastRowNum; j++) {
                XSSFRow row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                String code = getCellValue(row, 0);
                String comment = getCellValue(row, 1);
                String type = getCellValue(row, 2);
                String defaultValue = getCellValue(row, 4);
                String primary = getCellValue(row, 5);
                if (code == null || code.length() == 0) {
                    continue;
                }
                Colum colum = new Colum(code, comment, type, defaultValue, primary);
                String sql = colum.toString();
                createSql.append(sql).append(",");


            }
            createSql.delete(createSql.length() - 2, createSql.length() - 1);
            createSql.append(") ");
            createSql.append("comment '").append(sheet.getRow(0).getCell(0).getStringCellValue()).append("';\n");
            String str = createSql.toString();
            str = str.replace(",)", ")");
            str = str.replace("）", ")");
            str = str.replace("（", "(");
            str = str.replace("varchar2", "varchar");
            str = str.replace(".0", "");
            bw.write(str);
        }
        bw.flush();
        bw.close();
    }

    private static String getCellValue(XSSFRow row, int index) {
        String s = null;
        XSSFCell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            s = cell.getNumericCellValue() + "";
        } else if (cell.getCellType().equals(CellType.BOOLEAN)) {
            s = cell.getBooleanCellValue() + "";

        }
        if (s == null) {
            s = cell.getStringCellValue();
        }
        return s;
    }

}
