package org.fortech.navigation.security.templates;

public class EmailTemplate {
    public static final String ITP_SUBJECT = "ALERTA EXPIRARE ITP";
    public static final String ITP_MESSAGE = "ITP-ul pentru autovehiculul {brand} {model} {plate_number} expirat {prep} {days} zile";
    public static final String ITP_MESSAGE_2 = "ITP-ul pentru autovehiculul {brand} {model} {plate_number} a expirat azi!";

    public static final String SERVICE_SUBJECT = "ALERTA EXPIRARE REVIZIE";
    public static final String SERVICE_MESSAGE = "Revizia pentru autovehiculul {brand} {model} {plate_number} expirat {prep} {days} zile";
    public static final String SERVICE_MESSAGE_2 = "Revizia pentru autovehiculul {brand} {model} {plate_number} a expirat azi!";

    public static final String OILCHANGE_SUBJECT = "ALERTA SCHIMB ULEI";
    public static final String OILCHANGE_MESSAGE = "Schimbul de ulei pentru autovehiculul {brand} {model} {plate_number} expirat {prep} {days} zile";
    public static final String OILCHANGE_MESSAGE_2 = "Schimbul de ulei pentru autovehiculul {brand} {model} {plate_number} a expirat azi!";

    public static final String VIGNETTE_SUBJECT = "ALERTA EXPIRARE VIGNETA";
    public static final String VIGNETTE_MESSAGE = "Vigneta pentru autovehiculul {brand} {model} {plate_number} expirat {prep} {days} zile";
    public static final String VIGNETTE_MESSAGE_2 = "Vigneta pentru autovehiculul {brand} {model} {plate_number} a expirat azi!";
}