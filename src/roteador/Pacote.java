package roteador;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import view.MainView;

public class Pacote {

    private String ipOrigem;
    private String ipDestino;
    private String mensagem;
    private Integer TTL;

    public Pacote(String ipOrigem, String ipDestino, String msg, Integer TTL) {
        this.ipOrigem = ipOrigem;
        this.ipDestino = ipDestino;
        this.mensagem = msg;
        this.TTL = TTL;
    }

    public String getIpOrigem() { return this.ipOrigem; }
    public String getIpDestino() { return this.ipDestino; }
    public String getMensagem() { return this.mensagem; }
    public Integer getTTL() { return this.TTL; }
    
    public boolean verificaTTL() {
        return this.TTL > 1;
    }

    @Override
    public String toString() { 
        return this.ipOrigem+","+ this.ipDestino+","+ this.mensagem+","+ this.TTL; 
    }

    public static Pacote criaPacote(String msg) {
        String splitador[] = msg.split(",");
        Integer TTL = Integer.parseInt(splitador[3]) - 1;

        return new Pacote(splitador[0], splitador[1], splitador[2], TTL);
    }

    public static Pacote recebePacote(String myIP, Integer interFace) {
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
            
            String printout = "[ Pacote recebido com TTL=" + p.getTTL() + " | Conteúdo do pacote: " + p.toString() + " ]"; 
            System.out.println(printout);
            MainView.log.add(printout);
            return p;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void enviaPacote(Pacote p, TabelaDeRoteamento t) {
        DatagramSocket sendSocket;
        DatagramPacket sendPacket;

        try {
            sendSocket = new DatagramSocket();
            byte[] sendData = p.toString().getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(t.getProxRoteador()),
                    t.getInterFace());
            sendSocket.send(sendPacket);
            
            String printout = "[ Repassando pacote destino a " + p.ipDestino + " para o próximo salto "
                        + t.getProxRoteador() + " pela interface " + t.getInterFace() + " ]";
            System.out.println(printout);
            MainView.log.add(printout);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
