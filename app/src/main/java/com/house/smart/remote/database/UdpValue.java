package com.house.smart.remote.database;

/**
 * Created by y.shlapak on Dec 16, 2014.
 */
public class UdpValue {
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UdpValue(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    private int id;

    public UdpValue(int id, String ip, String port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public UdpValue() {
    }

    private String ip;
    private String port;
}
