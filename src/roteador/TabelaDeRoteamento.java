package roteador;

import java.util.List;

public class TabelaDeRoteamento {

    private String redeDestino;
    private String mask;
    private String proxRoteador;
    private Integer interFace;

    public TabelaDeRoteamento(String redeDestino, String mask, String proxRoteador, Integer interFace) {
        this.redeDestino = redeDestino;
        this.mask = mask;
        this.proxRoteador = proxRoteador;
        this.interFace = interFace;
    }

    public String getRedeDestino() {
        return this.redeDestino;
    }

    public String getMask() {
        return this.mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getProxRoteador() {
        return this.proxRoteador;
    }

    public Integer getInterFace() {
        return this.interFace;
    }

    @Override
    public String toString() {
        return "TabelaDeRoteamento [redeDestino=" + redeDestino + ", mask=" + mask + ", proxRoteador=" + proxRoteador
                + ", interFace=" + interFace + "]";
    }
    
    public static List<TabelaDeRoteamento> criaTabelaDeRoteamento(String parametrosRoteador[], List<TabelaDeRoteamento> tabela) {
        for (String s : parametrosRoteador) {
            String splitador[] = s.split("/");

            TabelaDeRoteamento tr = new TabelaDeRoteamento(splitador[0], splitador[1], splitador[2],
                    Integer.parseInt(splitador[3]));
            tabela.add(tr);
        }
        return tabela;
    }
    
    public static TabelaDeRoteamento verificaTabelaRoteamento(Pacote p, List<TabelaDeRoteamento> tabela) {
        for (TabelaDeRoteamento t : tabela) {
            if(!MascaraDeRede.verificaMask(t.getMask())) { //Verificando se a mascara eh numero ou ip
                t.setMask(MascaraDeRede.CIDRToMask(Integer.parseInt(t.getMask()), false));
                System.out.println(t.getMask());
            }
            
            String ipRede = MascaraDeRede.IpHostToIpRede(p.getIpDestino(), t.getMask());
            if (t.getRedeDestino().equals(ipRede)) {
                return t;
            }
        }
        return null;
    }

}
