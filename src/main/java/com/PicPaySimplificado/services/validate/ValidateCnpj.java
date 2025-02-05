package com.PicPaySimplificado.services.validate;

public class ValidateCnpj {

    public static boolean valid(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^\\d]", "");

        // Verifica se o CNPJ tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Calcula os dígitos verificadores
        char dig13, dig14;
        int sm, i, r, num, peso;

        // Cálculo do primeiro dígito verificador
        sm = 0;
        peso = 2;
        for (i = 11; i >= 0; i--) {
            num = (int) (cnpj.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso + 1;
            if (peso == 10) {
                peso = 2;
            }
        }

        r = sm % 11;
        if ((r == 0) || (r == 1)) {
            dig13 = '0';
        } else {
            dig13 = (char) ((11 - r) + 48);
        }

        // Cálculo do segundo dígito verificador
        sm = 0;
        peso = 2;
        for (i = 12; i >= 0; i--) {
            num = (int) (cnpj.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso + 1;
            if (peso == 10) {
                peso = 2;
            }
        }

        r = sm % 11;
        if ((r == 0) || (r == 1)) {
            dig14 = '0';
        } else {
            dig14 = (char) ((11 - r) + 48);
        }

        // Verifica se os dígitos calculados são iguais aos dígitos informados
        return (dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13));
    
    }


}
