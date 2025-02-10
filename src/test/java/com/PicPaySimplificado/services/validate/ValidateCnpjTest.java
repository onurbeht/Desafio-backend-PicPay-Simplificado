package com.PicPaySimplificado.services.validate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidateCnpjTest {
    @Test
    public void testValidCnpjWithoutSpecialCharacters() {
        String cnpj = "12345678000195";
        assertTrue(ValidateCnpj.valid(cnpj), "CNPJ valid must return true.");
    }

    @Test
    public void testValidCnpjWithSpecialCharacters() {
        String cnpj = "12.345.678/0001-95";
        assertTrue(ValidateCnpj.valid(cnpj), "CNPJ with special chars, must return true.");
    }

    @Test
    public void testInvalidCnpj() {
        String cnpj = "12.345.678/0001-96"; // CNPJ inválido (dígitos verificadores errados)
        assertFalse(ValidateCnpj.valid(cnpj), "CNPJ invalid must return false.");
    }

    @Test
    public void testCnpjWithLessThan14Digits() {
        String cnpj = "1234567800019"; // Apenas 13 dígitos
        assertFalse(ValidateCnpj.valid(cnpj), "CNPJ with less than 14 digits must return false.");
    }

    @Test
    public void testCnpjWithMoreThan14Digits() {
        String cnpj = "123456780001957"; // 15 dígitos
        assertFalse(ValidateCnpj.valid(cnpj), "CNPJ with more than 14 digits must return false.");
    }

    @Test
    public void testCnpjWithInvalidLength() {
        String cnpj = "1234"; // CNPJ com comprimento incorreto
        assertFalse(ValidateCnpj.valid(cnpj), "CNPJ with wrong length must return false.");
    }

    @Test
    public void testCnpjWithZeros() {
        String cnpj = "00000000000000"; // CNPJ de zeros, inválido
        assertFalse(ValidateCnpj.valid(cnpj), "CNPJ with only chars zero, must return false.");
    }

    @Test
    public void testCnpjWithValidButIncorrectCheckDigits() {
        String cnpj = "12345678000196"; // CNPJ válido mas com dígitos verificadores incorretos
        assertFalse(ValidateCnpj.valid(cnpj), "CNPJ with wrong verify digits must return false.");
    }

}
