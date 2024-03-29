package javafactura.businessLogic;

import javafactura.businessLogic.econSectors.EconSector;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The abstract Contribuinte from which the other ones extend
 */
public abstract class Contribuinte implements User,
                                              Serializable {

    private static final long serialVersionUID = 2591014538307976339L;
    /**
     * The receipts
     */
    protected final LinkedList<Factura> facturas;
    /**
     * The NIF of the Contribuinte
     */
    private final String nif;
    /**
     * The name of the Contribuinte
     */
    private final String name;
    /**
     * The fiscal coefficient
     */
    private final float fiscalCoefficient;
    /**
     * The Economic Sectors this <tt>Contribuinte</tt> is eligible for
     */
    private final Set<EconSector> econActivities;
    /**
     * The email of the Contribuinte
     */
    private String email;
    /**
     * The address of the Contribuinte
     */
    private String address;
    /**
     * The password of the Contribuinte
     */
    private String password;

    /**
     * Parametrised constructor
     * @param nif               The NIF
     * @param email             The email
     * @param name              The Name
     * @param address           The Address
     * @param password          The Password
     * @param fiscalCoefficient The fiscal coefficient
     * @param econActivities    The economic sectors
     */
    protected Contribuinte(String nif, String email, String name, String address, String password,
                           float fiscalCoefficient, Collection<EconSector> econActivities){
        this.nif = nif;
        this.email = email;
        this.name = name;
        this.address = address;
        this.password = password;
        this.fiscalCoefficient = fiscalCoefficient;
        this.econActivities = new HashSet<>(econActivities);
        this.facturas = new LinkedList<>();
    }

    /**
     * Copy constructor
     * @param contribuinte The Contribuinte to copy
     */
    protected Contribuinte(Contribuinte contribuinte){
        this.nif = contribuinte.getNif();
        this.email = contribuinte.getEmail();
        this.name = contribuinte.getName();
        this.address = contribuinte.getAddress();
        this.password = contribuinte.getPassword();
        this.fiscalCoefficient = contribuinte.getFiscalCoefficient();
        this.econActivities = contribuinte.getEconActivities();
        this.facturas = contribuinte.getFacturas();
    }

    /** {@inheritDoc} */
    @Override
    public String getNif(){
        return nif;
    }

    /** {@inheritDoc} */
    @Override
    public String getName(){
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public String getPassword(){
        return password;
    }

    /** {@inheritDoc} */
    @Override
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Returns the email
     * @return The email
     */
    public String getEmail(){
        return email;
    }

    /**
     * Changes the email
     * @param email the new email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Returns the address
     * @return The address
     */
    public String getAddress(){
        return address;
    }

    /**
     * Changes the address
     * @param address the new address
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * Returns the fiscal coefficient
     * @return The fiscal coefficient
     */
    public float getFiscalCoefficient(){
        return this.fiscalCoefficient;
    }

    /**
     * Returns the {@link EconSector}s associated with this {@link Contribuinte}
     * <ul>
     * <li>
     * In the case of a {@link ContribuinteEmpresarial} they represent the sectors this company's
     * {@link Factura}s can have.
     * </li>
     * <li>
     * In the case of a {@link ContribuinteIndividual} they represent the sectors this individual
     * can deduct from.
     * </li>
     * </ul>
     * @return The {@link EconSector}s associated
     */
    public Set<EconSector> getEconActivities(){
        return new HashSet<>(this.econActivities);
    }

    /**
     * Returns the list of all {@link Factura}s associated
     * @return The list of all {@link Factura}s associated
     */
    public LinkedList<Factura> getFacturas(){
        return this.facturas
                .stream()
                .map(Factura::clone)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Returns a sorted list of all {@link Factura}s associated
     * @param c The comparator used for sorting
     * @return a sorted list of {@link Factura}
     */
    public List<Factura> getFacturas(Comparator<Factura> c){
        return this.facturas.stream().map(Factura::clone).sorted(c).collect(Collectors.toList());
    }

    /**
     * Returns a list of all {@link Factura}s between 2 dates associated
     * @param from The begin date
     * @param to   The end date
     * @return a list of {@link Factura}s
     */
    public List<Factura> getFacturas(LocalDateTime from, LocalDateTime to){
        return this.facturas.stream()
                            .filter(f -> f.getCreationDate().isAfter(from))
                            .filter(f -> f.getCreationDate().isBefore(to))
                            .map(Factura::clone)
                            .collect(Collectors.toList());
    }

    /**
     * Returns a sorted list of all {@link Factura}s between 2 dates associated
     * @param c    The comparator used for sorting
     * @param from The begin date
     * @param to   The end date
     * @return a list of {@link Factura}s
     */
    public List<Factura> getFacturas(Comparator<Factura> c, LocalDateTime from, LocalDateTime to){
        return this.facturas.stream()
                            .filter(f -> f.getCreationDate().isAfter(from))
                            .filter(f -> f.getCreationDate().isBefore(to))
                            .map(Factura::clone)
                            .sorted(c)
                            .collect(Collectors.toList());
    }

    /**
     * Associates a new {@link Factura}
     * @param f the factura
     */
    protected void associateFactura(Factura f){
        this.facturas.addFirst(f);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;

        if(obj == null || this.getClass() != obj.getClass()) return false;

        Contribuinte that = (Contribuinte) obj;
        return this.fiscalCoefficient == that.getFiscalCoefficient()
               && this.nif.equals(that.getNif())
               && this.email.equals(that.getEmail())
               && this.name.equals(that.getName())
               && this.address.equals(that.getAddress())
               && this.password.equals(that.getPassword())
               && this.facturas.equals(that.getFacturas())
               && this.econActivities.equals(that.getEconActivities());
    }

    /**
     * Returns a deep copy of the instance
     * @return A deep copy of the instance
     */
    @Override
    public abstract Contribuinte clone();

    /** {@inheritDoc} */
    @Override
    public String toString(){
        return "Contribuinte{" +
               "nif='" + nif + '\''
               + ", email='" + email + '\''
               + ", name='" + name + '\''
               + ", address='" + address + '\''
               + ", password='" + password + '\''
               + ", fiscalCoefficient=" + fiscalCoefficient + '\''
               + ", econActivities=" + econActivities + '\''
               + ", facturas=" + facturas + '\''
               + '}';
    }
}
