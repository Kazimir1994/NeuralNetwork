package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.model;

public class DataConnect {
    private final String login;
    private final String password;
    private final String ip;
    private final int port;

    public DataConnect(String login, String password, String ip, int port) {
        this.login = login;
        this.password = password;
        this.ip = ip;
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
