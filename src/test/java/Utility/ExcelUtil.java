package Utility;

import base.TestBase;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import  base.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import static base.TestBase.*;

public class ExcelUtil {


    public String sheetName="SearchDomain";
    private static final String EXCEL_FILE_PATH = System.getProperty("user.dir")+"\\src\\main\\resources\\Excel\\DomainSearchDataSheet.xlsx";


    public static Object[][] readData() {
        Object[][] data = null;
        try {
            file = new FileInputStream(new File(EXCEL_FILE_PATH));
            workbook = (XSSFWorkbook) WorkbookFactory.create(file);
            sheet = workbook.getSheet("SearchDomain");

            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
            int columnCount = sheet.getRow(0).getLastCellNum();
            data = new Object[rowCount][columnCount];
            for (int i = 0; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < columnCount; j++) {
                    Cell cell = row.getCell(j);
                    switch (cell.getCellType()) {
                        case STRING:
                            data[i][j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            data[i][j] = cell.getNumericCellValue();
                            break;
                        case BOOLEAN:
                            data[i][j] = cell.getBooleanCellValue();
                            break;
                        default:
                            data[i][j] = "";
                    }
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
