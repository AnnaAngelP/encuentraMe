package uno.prueba.sanchez.augusto.login;

/**
 * Created by AUGUSTO on 24/10/2018.
 */

public class Usuario {
    private String nick, password, marca, modelo, mac;
    Usuario (){
        nick = password = marca = modelo = mac = null;
    }
    public void setNick(String n){
        nick = n;
    }
    public void setPassword(String n){
        password=n;
    }
    public void setMarca(String n){
        marca=n;
    }
    public void setModelo(String n){
        modelo=n;
    }
    public void setMac(String n){
        mac=n;
    }
    public String getNick(){
        return nick;
    }
    public String getPassword(){
        return password;
    }
    public String getMarca(){
        return marca;
    }
    public String getModelo(){
        return modelo;
    }
    public String getMac(){
        return mac;
    }
}
