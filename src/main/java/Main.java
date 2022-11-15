import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        //TODO переделать Класс Abiturient
        //TODO почистить код от лишнего и оптимизировать методы
        //TODO доделать логику распределения
        //TODO переименовать нормально все переменные
        //TODO сделать прием абитуриентов в JSON формате

        /*
        Работа логики программы:
        1) Мы загружаем Xlsx файл
        2) Забираем оттуда абитуриентов, создаем для каждого свой объект
        3) Сортируем по убыванию весь список
        4) Проходим по списку сверху-вниз определяя для каждого абитуриента
        факультет на который он хочет попасть (faculty1).
        4.1) Далее проверяем проходит ли он на него по среднему баллу
        4.1+) проходит -> вносим (в соответствующую группу)
        *** При внесении человека в группу, вероятно лучше будет его удалять из рабочего списка
            4.1.-) если не проходит, проверяем наличие у него (faculty2)
                4.1.1.+) если есть то проверяем проходит ли по баллу
                    4.1.2.-) если не проходит то проверяем наличие (faculty3)
                        4.1.3.+) если есть снова проверяем по среднему баллу
                            4.1.4+) проходит -> вносим (в соответствующую группу)
                            4.1.4-) не проходит -> в отсев
                        4.1.3.-) нет faculty3 -> в отсев
                    4.1.2.+) проходит -> вносим
                4.1.1.-) проходит -> вносим
            4.1.+) нет faculty2 -> в отсев
         *** Отсев предполагает удаление из текущего списка, на всякий перенесем данные в другой?
         5) Если наш абитуриент проходит в ту или иную группу мы добавялем его
         в соответствующий список
         5.1) После добавления абитуриента в список мы заново сортируем его
            5.2) Если после добавления абитуриента список стал > 25
                5.2.1.+) Мы достаем 26 абитуриента и переносим его в рабочий список на следующее
                для проверки место (речь о верхушке списка), мы либо используем SortedSet\Map
                или каждый раз сортируем оставшийся рабочий список.
                5.2.1.-) Продолжаем работу с основным списком п.4
         6) Возможно чтоб не идти по всему списку можно паралельно собирать информацию по минимальным
         средним баллам во всех группах и когда первый абитуриент не пройдет во все группы (отдельная проверка
         без учета выбраной для поступления группы) прекратить цикл распределения. Но тут надо глянуть
         что дешевле по ресурсам каждого человека проверять на min балл (при этом собирая его с групп)
         или пройтись по всем людям до конца.
         7) Сохраняем результат в Коллекции (Set, Map ... ) для последующего ыввода в Xlsx файл
         Нам скорей подходит SortedSet потому как мапа тут нахрен не упала, ключи вообще не нужны
         мы не ищем определенный объект мы просто следуем по очередни по сортированному списку\множеству
         и переносим элементы в другой список, периодически возвращая элементы обратно или списывая их окончательно
         в отсев (еще один список)
         */

        //Создаем поток входных данных черех файл
        FileInputStream inputStream = new FileInputStream("src/testBase.xlsx");
        //Создаем объект хранящий данные из xls, передаем поток с данными
        Workbook book = new XSSFWorkbook(inputStream);
        //Создаем объект представляющий страницу из нашего book
        Sheet sheet = book.getSheetAt(1);

        List<Abiturient> sortedAbiturientList = MyExtraMethods.sortAbiturientsList(
                XlsxHandler.parseToAbiturient(XlsxHandler.readToList(sheet)));

        //Проверка парсинга по количеству правильных имен
        System.out.println(sortedAbiturientList.stream()
                .filter((a) -> a.getName() != "")
                .count());

        mainLoop(sortedAbiturientList);


//        Profession[] myEnums = Profession.values();
//        for( Profession numsE : myEnums) {
//            List<Abiturient> filtredAbiturientList =
//            MyExtraMethods.collectAbiturientsToProfMainWay(sortedAbiturientList, numsE);
//            System.out.println(numsE + " \n" + filtredAbiturientList.toString());
//        }

//        List<Abiturient> filtredAbiturientList =
//                MyExtraMethods.collectAbiturientsToProfMainWay(sortedAbiturientList, Profession.TECHMECH);
//        System.out.println(filtredAbiturientList.toString());
    }

    public static void mainLoop(List<Abiturient> sortedAbiturientList) {


        mainLoops:
        {
            while (true) {
                //TODO Основная логика распределения людей, возможно надо все переделать в PriorityQueue
                System.out.println("Выберите действие: " + "\n"
                        + "1 - Просмотреть текущие списки" + "\n"
                        + "2 - Провести распределение" + "\n"
                        + "3 - Выгрузить данные в файл" + "\n"
                        + "4 - Завершить работу" + "\n");

                switch (SCANNER.nextInt()) {
                    case 1: {
                    //TODO просмотр списков
                    }
                    break;

                    case 2: {
                        //System.out.println(MyExtraMethods.distributionOfAbiturients(workList));
                        MyExtraMethods.distributionOfAbiturients(sortedAbiturientList);
                    }
                    break;

                    case 3: {
                        XlsxHandler.uploadDataToXlsFile();
                    }
                    break;

                    case 4: {
                        SCANNER.close();
                        break mainLoops;
                    }
                    default:
                        System.out.println("SMTH gonna wrong! Reenter you choise");
                    break;
                }
            }
        }
    }
}
