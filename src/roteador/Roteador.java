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

    /*private Pacote criaPacote(String msg) {
        String splitador[] = msg.split(",");
        Integer TTL = Integer.parseInt(splitador[3]) - 1;

        return new Pacote(splitador[0], splitador[1], splitador[2], TTL);
    }*/

    /*private boolean verificaTTL(Pacote p) {
        return p.getTTL() > 0;
    }*/

    /*private void criaTabelaDeRoteamento(String parametrosRoteador[]) {
        for (String s : parametrosRoteador) {
            String splitador[] = s.split("/");

            TabelaDeRoteamento tr = new TabelaDeRoteamento(splitador[0], splitador[1], splitador[2],
                    Integer.parseInt(splitador[3]));
            this.tabela.add(tr);
        }
    }*/

    /*private TabelaDeRoteamento verificaTabelaRoteamento(Pacote p) {
        for (TabelaDeRoteamento t : this.tabela) {
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
    }*/

    /*private Pacote recebePacote() {
        DatagramPacket recvPacket = null;
        DatagramSocket recvSocket;

        try {
            recvSocket = new DatagramSocket(null);
            recvSocket.setReuseAddress(true);
            recvSocket.setBroadcast(true);
            recvSocket.bind(new InetSocketAddress(myIP, interFace));

            byte[] recvData = new byte[1024];
            recvPacket = new DatagramPacket(recvData, recvData.length);

            System.out.println("Esperando Pacote . . .");
            MainView.log.add("Esperando Pacote . . .");
            recvSocket.receive(recvPacket);
            
            String msg = new String(recvPacket.getData()).trim();
            recvSocket.close();
            
            Pacote p = Pacote.criaPacote(msg);
            //Pacote p = criaPacote(msg);
            System.out.println("Pacote recebido com TTL=" + p.getTTL() + " | Conteúdo do pacote: " + p.toString());
            MainView.log.add("Pacote recebido com TTL=" + p.getTTL() + " | Conteúdo do pacote: " + p.toString());            
            return p;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*private void enviaPacote(Pacote p, TabelaDeRoteamento t) {
        DatagramSocket sendSocket;
        DatagramPacket sendPacket;

        //System.out.println("Achei na tabela de roteamento: " + t.getRedeDestino());
            //MainView.log.add("Achei na tabela de roteamento: " + t.getRedeDestino());
        try {
            sendSocket = new DatagramSocket();
            byte[] sendData = p.toString().getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(t.getProxRoteador()),
                    t.getInterFace());
            sendSocket.send(sendPacket);
            System.out.println("Pacote enviado para o roteador (" + t.getProxRoteador() + ", " + t.getInterFace() + ")");
            MainView.log.add("Pacote enviado para o roteador (" + t.getProxRoteador() + ", " + t.getInterFace() + ")");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    @Override
    public String toString() {
        return "Roteador [interFace=" + interFace + ", tabela=" + tabela + "]";
    }

}
