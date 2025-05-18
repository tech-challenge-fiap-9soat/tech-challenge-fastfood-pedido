package com.fastfood.pedido.utils;

public class CpfUtils {

    public static String removerPontuacoes(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("\\D", "");
    }
}
