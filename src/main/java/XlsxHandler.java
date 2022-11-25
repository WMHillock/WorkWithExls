import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class XlsxHandler {

    public static List readToList(Sheet sheet) {
        List<ArrayList<String>> parsedFromXlsInfoList = new ArrayList<>();
        int i = 0;
        for (Row row : sheet) {
            parsedFromXlsInfoList.add(i, new ArrayList<>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        parsedFromXlsInfoList.get(i).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            parsedFromXlsInfoList.get(i).add(cell.getDateCellValue() + "");
                        } else {
                            parsedFromXlsInfoList.get(i).add(cell.getNumericCellValue() + "");
                        }
                        break;
                    default:
                        parsedFromXlsInfoList.get(i).add(" ");
                }
            }
            i++;
        }
        return parsedFromXlsInfoList.stream()
                .limit(elementsCounter(parsedFromXlsInfoList))
                .collect(Collectors.toList());
    }

    public static int elementsCounter(List<ArrayList<String>> parsedFromXlsInfoList) {
        int j = 0;
        for (ArrayList<String> strings : parsedFromXlsInfoList) {
            if (!strings.get(0).isBlank()) {
                j++;
            }
        }
        return j;
    }

    public static Profession adapterForProfession(String string) {
        Profession[] professions = Profession.values();
        for (Profession profession : professions) {
            if (string.contains(profession.getProfessionCode())) {
                return profession;
            }
        }
        return null;
    }

    //TODO Нам нужен ObjectMapper? Думаю нет
    public static List<Abiturient> parseToAbiturient(List<ArrayList<String>> abiturientsFromSheet) {
        List<Abiturient> abiturientList = new LinkedList<>();

        for (int i = 0; i < abiturientsFromSheet.size(); i++) {

            abiturientList.add(new Abiturient());
            abiturientList.get(i).setName(abiturientsFromSheet.get(i).get(0));
            abiturientList.get(i).setMyProfessions(0, adapterForProfession(abiturientsFromSheet.get(i).get(1)));

            if (abiturientsFromSheet.get(i).get(abiturientsFromSheet.get(i).size() - 1).equals("1.0")) {
                abiturientList.get(i).setGrades(Double.parseDouble(abiturientsFromSheet.get(i)
                        .get(abiturientsFromSheet.get(i).size() - 2)));
                abiturientList.get(i).setFactor13(abiturientsFromSheet.get(i)
                        .get(abiturientsFromSheet.get(i).size() - 1));

                if (abiturientsFromSheet.get(i).size() > 4) {
                    abiturientList.get(i).setMyProfessions(1, adapterForProfession(abiturientsFromSheet.get(i).get(2)));
                    if (abiturientsFromSheet.get(i).size() > 5) {
                        abiturientList.get(i).setMyProfessions(2, adapterForProfession(abiturientsFromSheet.get(i).get(3)));
                    }
                }
            } else {
                abiturientList.get(i).setGrades(Double.parseDouble(abiturientsFromSheet.get(i)
                        .get(abiturientsFromSheet.get(i).size() - 1)));

                if (abiturientsFromSheet.get(i).size() > 3) {
                    abiturientList.get(i).setMyProfessions(1, adapterForProfession(abiturientsFromSheet.get(i).get(2)));
                    if (abiturientsFromSheet.get(i).size() > 4) {
                        abiturientList.get(i).setMyProfessions(2, adapterForProfession(abiturientsFromSheet.get(i).get(3)));
                    }
                }
            }
        }
        return abiturientList;
    }

    public static void uploadDataToXlsFile(List<Deque<Abiturient>> finalDistributionList) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        int i = 0;
        int j = 0;

        for (Deque<Abiturient> abiturientDeque : finalDistributionList) {
            if (j < finalDistributionList.size() - 1) {
                workbook.createSheet(abiturientDeque.getFirst().getMyProfessions(0).getProfessionCode());
            } else {
                workbook.createSheet("Резерв и спор");
            }
            j++;

            int rowNum = 0;
            for (Abiturient abiturient : abiturientDeque) {
                Row row = workbook.getSheetAt(i).createRow(rowNum++);

                Cell cellName = row.createCell(0);
                cellName.setCellValue(abiturient.getName());

                Cell cellP1 = row.createCell(1);
                cellP1.setCellValue(abiturient.getMyProfessions(0).getProfessionCode());

                Cell cellP2 = row.createCell(2);
                Cell cellP3 = row.createCell(3);
                if (abiturient.getMyProfessions(1) != null) {
                    cellP2.setCellValue(abiturient.getMyProfessions(1).getProfessionCode());
                    if (abiturient.getMyProfessions(2) != null) {
                        cellP3.setCellValue(abiturient.getMyProfessions(2).getProfessionCode());
                    }
                }

                Cell cellGrade = row.createCell(4);
                cellGrade.setCellValue(abiturient.getGrades());

                Cell cellDocs = row.createCell(5);
                if (!(abiturient.getFactor13().isBlank())) {
                    cellDocs.setCellValue(abiturient.getFactor13());
                }
            }
            i++;
        }

        try {
            // Writing the workbook
            FileOutputStream out = new FileOutputStream(
                    "workResult.xlsx");
            workbook.write(out);
            out.close();

            System.out.println("""
                    \n************************************************
                     Результаты сортировки абитуриентов успешно
                     выгружены, найти их можно в файле:
                     workResult.xlsx в корневой папке программы.
                    ************************************************
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Deque<Abiturient> uploadAbiturientsFromFileXls(String fileName) throws IOException {
        //TODO переделать с exception FileNotFound
        FileInputStream inputStream = new FileInputStream(fileName + ".xlsx");
        Workbook book = new XSSFWorkbook(inputStream);
        Sheet sheet = book.getSheetAt(0);

        Deque<Abiturient> sortedAbiturientList = new LinkedList<>(MyExtraMethods.sortAbiturientsList(
                XlsxHandler.parseToAbiturient(XlsxHandler.readToList(sheet))));

        System.out.println("Загружено - " + sortedAbiturientList.stream()
                .filter((a) -> !a.getName().isBlank())
                .count() + " абитуриентов.\n");

        return sortedAbiturientList;
    }
}
