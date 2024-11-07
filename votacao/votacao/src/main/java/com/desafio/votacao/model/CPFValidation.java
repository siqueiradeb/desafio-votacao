package com.desafio.votacao.model;

import lombok.Data;

import java.util.Random;

@Data
public class CPFValidation {
    private String status;

    public static CPFValidation validate(String cpf) {
        Random random = new Random();
        boolean isValid = random.nextBoolean();
        CPFValidation validation = new CPFValidation();
        validation.setStatus(isValid ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE");
        return validation;
    }
}
