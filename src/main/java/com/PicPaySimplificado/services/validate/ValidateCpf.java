package com.PicPaySimplificado.services.validate;

public class ValidateCpf {

    public static boolean valid(String cpf) {

        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se o cpf tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) {
            firstDigit = 0;
        }

        // Calcula o segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) {
            secondDigit = 0;
        }

        // Verifica se os dígitos calculados são iguais aos dígitos do cpf
        return (firstDigit == (cpf.charAt(9) - '0')) && (secondDigit == (cpf.charAt(10) - '0'));
    }


}
