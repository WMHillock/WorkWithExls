import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class XlsxHandler {

    public static List readToList(Sheet sheet) {
        List<ArrayList<String>> parsedFromXlsInfoList = new ArrayList<>();
        int i = 0;
        // for each цикл заполнения нашего хранилища parsedFromXlsInfoList
        //Проходим по очереди по рядам
        //TODO Оптимизировать, сделать проверку на пустой ряд и скипать его.
        for (Row row : sheet) {
            //Создаем многомерный ArrayList
            parsedFromXlsInfoList.add(i, new ArrayList<>());
            //Проходим по ячейкам
            for (Cell cell : row) {
                //Осуществляем отрезание пустых значений.
                //Забираем данные из ячеек, обязательно своим методом для каждого типа!
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
                    case BOOLEAN:
                        parsedFromXlsInfoList.get(i).add(cell.getBooleanCellValue() + "");
                        break;
                    case FORMULA:
                        parsedFromXlsInfoList.get(i).add(cell.getCellFormula() + "");
                        break;
                    default:
                        parsedFromXlsInfoList.get(i).add(" ");
                }
            }
            i++;
        }
        //Отрезаем пустой конец Xlsx листа и возвращаем список
        return parsedFromXlsInfoList.stream()
                .limit(elementsCounter(parsedFromXlsInfoList))
                .collect(Collectors.toList());
    }
    public static int elementsCounter(List<ArrayList<String>> parsedFromXlsInfoList) {
        int j = 0;
        for (int i = 0; i < parsedFromXlsInfoList.size(); i++) {
            if (!parsedFromXlsInfoList.get(i).get(0).isBlank()) {
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

    //TODO Нам нужен ObjectMapper?
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
            if(j < finalDistributionList.size() - 1 ) {
                workbook.createSheet(abiturientDeque.getFirst().getMyProfessions(0).getProfessionCode());
            } else {
                workbook.createSheet("На проверку");
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
                if(abiturient.getMyProfessions(1)  != null) {
                    cellP2.setCellValue(abiturient.getMyProfessions(1).getProfessionCode());
                    if(abiturient.getMyProfessions(2)  != null) {
                        cellP3.setCellValue(abiturient.getMyProfessions(2).getProfessionCode());
                    }
                }

                Cell cellGrade = row.createCell(4);
                cellGrade.setCellValue(abiturient.getGrades());

                Cell cellDocs = row.createCell(5);
                if(!(abiturient.getFactor13().isBlank())) {
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
                    ***********************************
                    Мы залили в файл - workResult.xlsx
                    результаты сортировки абитуриентов.
                    ***********************************
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
