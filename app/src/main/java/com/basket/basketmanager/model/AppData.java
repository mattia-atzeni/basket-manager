package com.basket.basketmanager.model;

public class AppData {
    public static final String[] PLACES = {
            "Cagliari",
            "Quartu S. Elena",
            "Selargius",
            "Sinnai",
            "Settimo S. Pietro",
            "Mogoro",
            "Arbatax",
            "Arbus",
            "Assemini",
            "Bari Sardo",
            "Barrali",
            "Barumini",
            "Bosa",
            "Buggerru",
            "Burcei",
            "Capoterra",
            "Carbonia",
            "Cardedu",
            "Castiadas",
            "Decimo",
            "Decimomannu",
            "Dolianova",
            "Elmas",
            "Fonni",
            "Gergei",
            "Gesico",
            "Iglesias",
            "Isili",
            "Jerzu",
            "Lanusei",
            "Mandas",
            "Maracalagonis",
            "Monserrato",
            "Muravera",
            "Narcao",
            "Nuoro",
            "Nuraminis",
            "Olia Speciosa",
            "Oristano",
            "Perdasdefogu",
            "Pula",
            "Quartucciu",
            "Sassari",
            "Sadali",
            "San Sperate",
            "Santadi",
            "Serdiana",
            "Serramanna",
            "Serrenti",
            "Sestu",
            "Siliqua",
            "Suelli",
            "Tertenia",
            "Teulada",
            "Tuili",
            "Ulassai",
            "Ussana",
            "Uta",
            "Villacidro",
            "Villaputzu",
            "Villasimius",
            "Villasor"
    };

    public static final Address[] ADDRESSES = {
            new Address("Via Pessagno", 1),
            new Address("Via Pessina", 29),
            new Address("Viale Vienna", 1),
            new Address("Via Perra", 11),
            new Address("Via Lussu", 10),
            new Address ("Via G. Dessì", 5),
    };

    public static final String TEAMS[] = {
            "Esperia",
            "Ferrini",
            "San Salvatore",
            "Sinnai Basket",
            "Settimo Basket",
            "Vitalis"
    };

    public static final String CATEGORIES[] = {
            "Serie C",
            "Serie D",
            "U18/F",
            "U14/M",
            "Esordienti",
            "U13/M",
            "PM"
    };

    public static final Referee FIRST_REFEREE =
            new Referee("45678", "Pierluigi", "Fenu")
            .setEmail("pierluigi.fenu@gmail.com")
            .setPlace("Villasor")
            .setAddress("Via Roma, 5")
            .setBirthDate("10/07/1980")
            .setPhone("333 1453610")
            .setCategory("Serie C")
        ;

    public static final Referee REFEREES[] = {
            FIRST_REFEREE,
            new Referee("47553", "Riccardo", "Mulas"),
            new Referee("48963", "Stefano", "Piras"),
            new Referee("41953", "Andrea", "Sanna"),
            new Referee("42583", "Simona", "Lai"),
            new Referee("42897", "Matteo", "Putzulu")
    };

}
