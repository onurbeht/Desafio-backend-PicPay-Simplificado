package com.PicPaySimplificado.services.validate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidateCpfTest {

    @Test
    public void testValidCpf() {
        String cpf = "12345678909"; // CPF válido
        assertTrue(ValidateCpf.valid(cpf), "CPF valid, must return true.");
    }

    @Test
    public void testInvalidCpfWithIncorrectDigits() {
        String cpf = "12345678900"; // CPF inválido com dígitos verificadores incorretos
        assertFalse(ValidateCpf.valid(cpf), "CPF with wrong verify digits, must return false.");
    }

    @Test
    public void testCpfWithAllDigitsEqual() {
        String cpf = "11111111111"; // CPF com todos os dígitos iguais (inválido)
        assertFalse(ValidateCpf.valid(cpf), "CPF with all the same digits, must return false.");
    }

    @Test
    public void testCpfWithIncorrectLength() {
        String cpf = "12345678"; // CPF com comprimento incorreto (menos de 11 dígitos)
        assertFalse(ValidateCpf.valid(cpf), "CPF with less than 11 digits, must return false.");
    }

    @Test
    public void testCpfWithMoreThan11Digits() {
        String cpf = "123456789012"; // CPF com mais de 11 dígitos
        assertFalse(ValidateCpf.valid(cpf), "CPF with more than 11 digits, must return false.");
    }

    @Test
    public void testCpfWithNonNumericCharacters() {
        String cpf = "123.456.789-09"; // CPF com caracteres especiais (pontos e traço)
        assertTrue(ValidateCpf.valid(cpf), "CPF with special char, must return true.");
    }

    @Test
    public void testCpfWithLetters() {
        String cpf = "123ABC78909"; // CPF com letras (inválido)
        assertFalse(ValidateCpf.valid(cpf), "CPF with non numeric char, must return false.");
    }

    @Test
    public void testCpfWithZeros() {
        String cpf = "00000000000"; // CPF com todos os dígitos iguais a zero (inválido)
        assertFalse(ValidateCpf.valid(cpf), "CPF with only chars zero, must return false.");
    }

}
