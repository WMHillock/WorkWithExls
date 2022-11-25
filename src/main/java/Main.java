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
         6) После прохода по всему листу проходим по отсеву выбирая тех студентов кто выбирал более 1 профессии
         и потенциально может пройти в какую-либо группу если сменит приоритет, но это уже для ручной работы

         7) Сохраняем результат в Коллекции (Deque) для последующего вывода в Xlsx файл
         */

import java.io.IOException;
import java.util.Deque;
import java.util.Scanner;

public class Main {
    public static final Scanner SCANNER = new Scanner(System.in);
    public static void main(String[] args) throws IOException {

        //TODO сделать прием абитуриентов в JSON формате
        //TODO сделать выгрузку абитуриентов в JSON формате
        //TODO подключить базу данных и настроить работу с ней
        
        mainLoop();
    }

    public static void mainLoop() throws IOException {
        Deque<Abiturient> sortedAbiturientList = null;
        mainLoop:
        {
            while (true) {
                System.out.println("""
                        
                        Выберите действие:\s
                         1 - Получить данные из Xlsx файла (файл должен быть в корневой папке программы)
                         2 - Получить данные из Json
                         3 - Просмотреть текущие списки
                         4 - Провести распределение и выгрузить в файл
                         5 - Провести распределение и отправить данные в базу
                         6 - Завершить работу
                        """);

                switch (SCANNER.nextInt()) {
                    case 1 -> {SCANNER.nextLine();
                        System.out.println("Введите имя файла, без расширения:");
                        String fileName = SCANNER.nextLine();
                                sortedAbiturientList = XlsxHandler.uploadAbiturientsFromFileXls(fileName);}
                    case 2 -> System.out.println("Функция работы с json в работе");
                    case 3 -> System.out.println(sortedAbiturientList != null ? sortedAbiturientList.toString()
                            : "Сперва необходимо загрузить данные");
                    case 4 -> {
                        if (sortedAbiturientList != null) {
                            XlsxHandler.uploadDataToXlsFile(MyExtraMethods.distributionOfAbiturients(sortedAbiturientList));
                        } else System.out.println("Загрузите данные с помощью операций 1 или 2");
                    }
                    case 5 -> System.out.println("Функционал взаимодействия с БД в работе");
                    case 6 -> {System.out.println("Программа отключена.");SCANNER.close(); break mainLoop;}
                    default -> System.out.println("Введите корректный номер операции!");
                }
            }
        }
    }
}
