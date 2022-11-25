public enum Profession {

    ELECTRICITY("Монтаж, наладка и эксплуатация электрооборудования"
            , "08.02.09"
            , 25, 0),

    OPERATOR("Оператор станков с программным управлением"
            , "15.01.32"
            , 25, 1),

    ADJUSTER("Наладчик станков и оборудования в механообработке"
            , "15.01.23"
            , 50, 2),

    ADDTECH("Аддитивные технологии"
            , "15.02.09"
            , 25, 3),

    ROBOTECH("Мехатроника и мобильная робототехника"
            , "15.02.10"
            , 25,4),

    PROMROBOTECH("Техническая эксплуатация и обслуживание роботизированного производства"
            , "15.02.11"
            , 25,5),

    AUTOMATICS("Оснащение средствами автоматизации технологических процессов и производств",
            "15.02.14"
            , 25, 6),

    TECHMECH("Технология машиностроения"
            , "15.02.16"
            , 25, 7),

    CARMASTER("Мастер по ремонту и обслуживанию автомобилей"
            , "23.01.17"
            , 25, 8),

    PRODUCTINSPECTOR("Управление качеством продукции"
            , "27.02.07"
            , 25, 9),

    SPORTMASTER("Физическая культура"
            , "49.02.01"
            , 25, 10);

    private final String fullName;
    private final String professionCode;
    private final int placeLimit;
    private final int enumIndex;

    public String getFullName() {
        return fullName;
    }
    public String getProfessionCode() {
        return professionCode;
    }
    public int getPlaceLimit() { return placeLimit; }
    public int getEnumIndex() { return enumIndex;}

    Profession(String fullName, String professionCode, int placeLimit, int enumIndex) {
        this.fullName = fullName;
        this.professionCode = professionCode;
        this.placeLimit = placeLimit;
        this.enumIndex = enumIndex;
    }
}
