package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model;

import org.bson.types.ObjectId;

import java.util.Set;

public class SmartBoard {
    private ObjectId id;
    private String ip;
    private int port;
    private String login;
    private String password;
    private String description;
    private Set<ElementVision> tolls;
    private Set<ElementVision> borders;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ElementVision> getTools() {
        return tolls;
    }

    public void setTools(Set<ElementVision> elementVisions) {
        this.tolls = elementVisions;
    }

    public Set<ElementVision> getBorders() {
        return borders;
    }

    public void setBorders(Set<ElementVision> borders) {
        this.borders = borders;
    }

    @Override
    public String toString() {
        return "SmartBoard{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", tools=" + tolls +
                ", borders=" + borders +
                '}';
    }
}
