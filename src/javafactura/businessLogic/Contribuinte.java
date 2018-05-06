package javafactura.businessLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Contribuinte implements User,
                                              Serializable {

    /**
     * The NIF of the Contribuinte
     */
    private final String nif;
    /**
     * The email of the Contribuinte
     */
    private String email;
    /**
     * The name of the Contribuinte
     */
    private final String name;
    /**
     * The address of the Contribuinte
     */
    private String address;
    /**
     * The password of the Contribuinte
     */
    private String password;

    /**
     * The receipts
     */
    protected final List<Factura> facturas;

    /**
     * The empty constructor
     */
    Contribuinte(){
        this.nif = "";
        this.email = "";
        this.name = "";
        this.address = "";
        this.password = "";
        this.facturas = new LinkedList<>();
    }

    /**
     * Parametrised constructor
     * @param nif      The NIF
     * @param email    The email
     * @param name     The Name
     * @param address  The Address
     * @param password The Password
     */
    Contribuinte(String nif, String email, String name, String address, String password){
        this.nif = nif;
        this.email = email;
        this.name = name;
        this.address = address;
        this.password = password;
        this.facturas = new LinkedList<>();
    }

    /**
     * Copy constructor
     * @param contribuinte The Contribuinte to copy
     */
    Contribuinte(Contribuinte contribuinte){
        this.nif = contribuinte.getNif();
        this.email = contribuinte.getEmail();
        this.name = contribuinte.getName();
        this.address = contribuinte.getAddress();
        this.password = contribuinte.getPassword();
        this.facturas = contribuinte.getFacturas();
    }

    /**
     * Returns the NIF
     * @return The NIF
     */
    public String getNif(){
        return nif;
    }

    /**
     * Returns the email
     * @return The email
     */
    private String getEmail(){
        return email;
    }

    /**
     * Sets the email
     * @param email the new email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Returns the name
     * @return The name
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the address
     * @return The address
     */
    private String getAddress(){
        return address;
    }

    /**
     * Sets the new address
     * @param address the new address
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * Returns the password
     * @return The password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Changes the password
     * @param password The new password
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Returns the list of Facturas associated
     * @return The list of Facturas associated
     */
    public List<Factura> getFacturas(){
        List<Factura> l = new ArrayList<>();
        this.facturas.forEach(f -> l.add(f.clone()));
        return l;
    }

    protected void associateFactura(Factura f){
        this.facturas.add(f);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;

        if(o == null || this.getClass() != o.getClass()) return false;

        Contribuinte that = (Contribuinte) o;
        return this.nif.equals(that.getNif())
               && this.email.equals(that.getEmail())
               && this.name.equals(that.getName())
               && this.address.equals(that.getAddress())
               && this.password.equals(that.getPassword())
               && this.facturas.equals(that.getFacturas());
    }

    @Override
    public String toString(){
        return "Contribuinte{" +
               "nif='" + nif + '\''
               + ", email='" + email + '\''
               + ", name='" + name + '\''
               + ", address='" + address + '\''
               + ", password='" + password + '\''
               + ", facturas=" + facturas
               + '}';
    }
}