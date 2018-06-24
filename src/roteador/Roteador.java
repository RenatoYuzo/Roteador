package roteador;

import java.util.ArrayList;
import java.util.List;
import view.MainView;

public class Roteador implements Runnable {

    private String myIP;
    private Integer interFace;
    private List<TabelaDeRoteamento> tabela = new ArrayList<TabelaDeRoteamento>();

    public Roteador(String myIP, Integer porta, String parametrosRoteador[]) {
        this.myIP = myIP;
        this.interFace = porta;
        TabelaDeRoteamento.criaTabelaDeRoteamento(parametrosRoteador, tabela);
    }

    @Override
    public void run() {

        for (TabelaDeRoteamento t : tabela) {
            System.out.println(t.toString());
            MainView.log.add(t.toString());
        }

        while (true) {
            Pacote p = Pacote.recebePacote(myIP, interFace);
            if (Pacote.verificaTTL(p)) {

                TabelaDeRoteamento t = TabelaDeRoteamento.verificaTabelaRoteamento(p, tabela);
                if (t != null && !t.getProxRoteador().equals("0.0.0.0")) {
                    Pacote.enviaPacote(p, t);
                } else if (t != null && t.getProxRoteador().equals("0.0.0.0")) {
                    System.out.println("Pacote entregue ao destinatário! IP Origem do Pacote: " + p.getIpOrigem());
                    MainView.log.add("Pacote entregue ao destinatário! IP Origem do Pacote: " + p.getIpOrigem());
                } else {
                    System.out.println("Pacote Descartado!");
                    MainView.log.add("Pacote Descartado!");
                }

            } else {
                System.out.println("TTL Excedido!");
                MainView.log.add("TTL Excedido!");
            }
        }
    }

}
