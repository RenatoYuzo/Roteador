package roteador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import view.MainView;

public class TabelaDeRoteamento {

    private String redeDestino;
    private String mask;
    private String proxRoteador;
    private Integer interFace;
    private Integer maskInCIDR;
    private String maskInDecimal;

    public TabelaDeRoteamento(String redeDestino, String mascara, String proxRoteador, Integer interFace) {
        this.redeDestino = redeDestino;
        this.mask = mascara;
        this.proxRoteador = proxRoteador;
        this.interFace = interFace;

        if (MascaraDeRede.verificaMask(mascara)) { // Se verdadeiro, mascara esta no formato decimal
            this.maskInCIDR = MascaraDeRede.MaskToCIDR(mascara);
            this.maskInDecimal = mascara;
        } else { // Se falso, mascara esta no formato CIDR
            this.maskInDecimal = MascaraDeRede.CIDRToMask(Integer.parseInt(mascara), false);
            this.maskInCIDR = Integer.parseInt(mascara);
        }
    }

    public String getRedeDestino() { return this.redeDestino; }
    public String getMask() { return this.mask; }
    public String getProxRoteador() { return this.proxRoteador; }
    public Integer getInterFace() { return this.interFace; }
    public Integer getMaskInCIDR() { return maskInCIDR; }
    public String getMaskInDecimal() { return maskInDecimal; }
    
    @Override
    public String toString() {
        return "TabelaDeRoteamento [redeDestino=" + redeDestino + ", mask=" + mask + ", proxRoteador=" + proxRoteador
                + ", interFace=" + interFace + "]";
    }
    
    public static void populaTabela(List<TabelaDeRoteamento> tabela) {
        String[] colunas = new String[]{"Rede Destino","Next Hop","Interface"};
        String[][] dados = new String[tabela.size()][3];
        TabelaDeRoteamento t;
        for (int i=0; i<tabela.size(); i++) {
            t = tabela.get(i);
            System.out.println(t.toString());
            dados[i][0] = t.getRedeDestino()+"/"+t.getMaskInCIDR();
            dados[i][1] = t.getProxRoteador();
            dados[i][2] = Integer.toString(t.getInterFace());
        }
        
        DefaultTableModel dtm = new DefaultTableModel(dados, colunas);
        MainView.RouterTable.setModel(dtm);
    }
    
    public static List<TabelaDeRoteamento> criaTabelaDeRoteamento(String parametrosRoteador[]) {    
        List<TabelaDeRoteamento> tabela = new ArrayList<TabelaDeRoteamento>();
        for (String s : parametrosRoteador) {
            String splitador[] = s.split("/");

            TabelaDeRoteamento tr = new TabelaDeRoteamento(splitador[0], splitador[1], splitador[2],
                    Integer.parseInt(splitador[3]));
            tabela.add(tr);
        }
        return tabela;
    }

    public static TabelaDeRoteamento verificaTabelaRoteamento(Pacote p, List<TabelaDeRoteamento> tabela) {
        int posicao = -1;
        int longestMatch = -1;

        for (int i = 0; i < tabela.size(); i++) {
            TabelaDeRoteamento t = tabela.get(i);

            String ipRede = MascaraDeRede.IpHostToIpRede(p.getIpDestino(), t.getMaskInDecimal());
            if (t.getRedeDestino().equals(ipRede)) {
                if (t.maskInCIDR >= longestMatch) {
                    longestMatch = t.maskInCIDR;
                    posicao = i;
                }
            }

        }

        if (posicao != -1) {
            return tabela.get(posicao);
        } else {
            return null;
        }
    }

}
