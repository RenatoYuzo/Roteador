package roteador;

public abstract class MascaraDeRede {

    /*
         * Este metodo recebe como parametro um numero inteiro, e retorna o seu
         * binario correspondente com 8 bits.
     */
    public static String IntToBinary(int inteiro) {
        String binarioCom8bits = Integer.toBinaryString(inteiro);

        for (int i = 8 - binarioCom8bits.length(); i > 0; i--) {
            binarioCom8bits = "0" + binarioCom8bits;
        }

        return binarioCom8bits;
    }

    /*
	 * Este metodo recebe como parametro uma mascara (ex: 255.255.0.0 ou 16), e retorna false se a mascara
	 * estiver no formato CIDR, e retorna true, caso contrario.
     */
    public static boolean verificaMask(String mask) {
        String splitado[] = mask.split("[.]");

        if (splitado.length > 1) {
            return true;
        } else {
            return false;
        }
    }

    /*
	 * Este metodo recebe como parametro um numero inteiro referente ao CIDR, e tem
	 * como retorno o CIDR transformado na mascara de 32 bits ou em forma decimal
	 * (dependendo do parametro "formatoBinario").
	 * Exemplo de retorno: 255.255.255.0 ou 11111111.11111111.11111111.00000000
     */
    public static String CIDRToMask(int CIDR, boolean formatoBinario) {
        String primeiroOcteto = "";
        String segundoOcteto = "";
        String terceiroOcteto = "";
        String quartoOcteto = "";

        for (int i = 0; i < CIDR; i++) {
            if (i >= 0 && i <= 7) {
                primeiroOcteto = primeiroOcteto + "1";
            } else if (i >= 8 && i <= 15) {
                segundoOcteto = segundoOcteto + "1";
            } else if (i >= 16 && i <= 23) {
                terceiroOcteto = terceiroOcteto + "1";
            } else if (i >= 24) {
                quartoOcteto = quartoOcteto + "1";
            }
        }

        if (primeiroOcteto.length() < 8) {
            for (int i = 8 - primeiroOcteto.length(); i > 0; i--) {
                primeiroOcteto = primeiroOcteto + "0";
            }
        }
        if (segundoOcteto.length() < 8) {
            for (int i = 8 - segundoOcteto.length(); i > 0; i--) {
                segundoOcteto = segundoOcteto + "0";
            }
        }
        if (terceiroOcteto.length() < 8) {
            for (int i = 8 - terceiroOcteto.length(); i > 0; i--) {
                terceiroOcteto = terceiroOcteto + "0";
            }
        }
        if (quartoOcteto.length() < 8) {
            for (int i = 8 - quartoOcteto.length(); i > 0; i--) {
                quartoOcteto = quartoOcteto + "0";
            }
        }

        String maskInBinary = primeiroOcteto + "." + segundoOcteto + "." + terceiroOcteto + "." + quartoOcteto;

        if (formatoBinario == true) {
            return maskInBinary;
        } else {

            int primeiroOctetoDecimal = Integer.parseInt(primeiroOcteto, 2);
            int segundoOctetoDecimal = Integer.parseInt(segundoOcteto, 2);
            int terceiroOctetoDecimal = Integer.parseInt(terceiroOcteto, 2);
            int quartoOctetoDecimal = Integer.parseInt(quartoOcteto, 2);

            String maskInDecimal = primeiroOctetoDecimal + "." + segundoOctetoDecimal + "." + terceiroOctetoDecimal
                    + "." + quartoOctetoDecimal;

            return maskInDecimal;
        }
    }

    /*
	 * Este metodo recebe como parametro uma mascara em formato decimal (ex: 255.255.255.0)
	 * e retorna valor CIDR correspondente a essa mascara.
     */
    public static int MaskToCIDR(String mask) {
        int primeiroOcteto;
        int segundoOcteto;
        int terceiroOcteto;
        int quartoOcteto;

        String splitado[] = mask.split("[.]");
        primeiroOcteto = Integer.parseInt(splitado[0]);
        segundoOcteto = Integer.parseInt(splitado[1]);
        terceiroOcteto = Integer.parseInt(splitado[2]);
        quartoOcteto = Integer.parseInt(splitado[3]);

        String maskInBinary = IntToBinary(primeiroOcteto) + IntToBinary(segundoOcteto) + IntToBinary(terceiroOcteto)
                + IntToBinary(quartoOcteto);

        for (int i = 0; i < maskInBinary.length(); i++) {
            if (maskInBinary.charAt(i) != '1') {
                return i;
            }
        }

        return 32;
    }

    /*
	 * Este metodo recebe com parametro um endereco IP destino e uma mascara, e
	 * retorna a operacao & logica entre eles.
     */
    public static String IpHostToIpRede(String ipDestino, String mask) {
        String splitado[] = ipDestino.split("[.]");
        int primeiroDecimalIp = Integer.parseInt(splitado[0]);
        int segundoDecimalIp = Integer.parseInt(splitado[1]);
        int terceiroDecimalIp = Integer.parseInt(splitado[2]);
        int quartoDecimalIp = Integer.parseInt(splitado[3]);

        splitado = mask.split("[.]");
        int primeiroDecimalMask = Integer.parseInt(splitado[0]);
        int segundoDecimalMask = Integer.parseInt(splitado[1]);
        int terceiroDecimalMask = Integer.parseInt(splitado[2]);
        int quartoDecimalMask = Integer.parseInt(splitado[3]);

        int num1 = primeiroDecimalIp & primeiroDecimalMask;
        int num2 = segundoDecimalIp & segundoDecimalMask;
        int num3 = terceiroDecimalIp & terceiroDecimalMask;
        int num4 = quartoDecimalIp & quartoDecimalMask;
        return num1 + "." + num2 + "." + num3 + "." + num4;
    }

}
