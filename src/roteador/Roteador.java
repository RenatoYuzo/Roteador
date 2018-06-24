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
        this.tabela = TabelaDeRoteamento.criaTabelaDeRoteamento(parametrosRoteador);
    }

    @Override
    public void run() {
        TabelaDeRoteamento.populaTabela(tabela); // Metodo so para preencher a tabela da interface grafica

        while (true) {
            Pacote p = Pacote.recebePacote(myIP, interFace);
            if (p.verificaTTL()) { // Verifica a contagem do TTL
                
                // Faz a busca na tabela de roteamento
                TabelaDeRoteamento t = TabelaDeRoteamento.verificaTabelaRoteamento(p, tabela);
                if (t != null && !t.getProxRoteador().equals("0.0.0.0")) { // Se (IP AND Mask) = rede, e nao eh rota direta, envia para proximo roteador
                    Pacote.enviaPacote(p, t);
                } else if (t != null && t.getProxRoteador().equals("0.0.0.0")) { // Se (IP AND Mask) = rede, e eh rota direta, entrega pacote
                    String printout = "[ Pacote entregue ao destinatário! De " + p.getIpOrigem() 
                            + " para " + p.getIpDestino() + " : " + p.getMensagem() + " ]"; 
                    System.out.println(printout);
                    MainView.log.add(printout);
                } else { // IP & Mask resultou em uma rede que nao foi encontrada na tabela de roteamento
                    String printout = "[ Destino " + p.getIpDestino() + " não encontrado, pacote descartado! ]";
                    System.out.println(printout);
                    MainView.log.add(printout);
                }

            } else {
                String printout = "[ TTL Excedido, pacote descartado! ]";
                System.out.println(printout);
                MainView.log.add(printout);
            }
        }
    }

}
