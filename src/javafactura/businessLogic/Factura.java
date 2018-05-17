package javafactura.businessLogic;

import javafactura.businessLogic.econSectors.Deductible;
import javafactura.businessLogic.econSectors.EconSector;
import javafactura.businessLogic.econSectors.Pendente;
import javafactura.businessLogic.exceptions.InvalidEconSectorException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class Factura implements Comparable<Factura>,
                                Serializable {

    private static final long serialVersionUID = 7367373616311847150L;
    /** The format the creationDate will be printed with when toString is called on this */
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyy\tkk:mm:ss");

    /**
     * \brief The NIF of the entity that issued this Receipt
     */
    private final String issuerNif;
    /**
     * \brief The name of the entity that issued this Receipt
     */
    private final String issuerName;
    /**
     * \brief The date this Receipt was issued
     */
    private final LocalDateTime creationDate;
    /**
     * \brief The date this Factura was last edited
     */
    private LocalDateTime lastEditDate;
    /**
     * \brief The NIF of the client to whom this Receipt was issued
     */
    private final String clientNif;
    /**
     * \brief The description of the purchase
     */
    private final String description;
    /**
     * \brief The value of the purchase
     */
    private final float value;
    /**
     * \brief The economic sector
     */
    private EconSector econSector;
    /**
     * \brief The econ sectors of the company that issued this receipt
     */
    private final Set<EconSector> possibleEconSectors;
    /**
     * \brief History of states of this <tt>Factura</tt>
     */
    private final LinkedList<Factura> history;
    /**
     * \brief A boolean to store if the company the issued the Receipt is to benefited
     */
    private boolean isEmpresaInterior;
    /**
     * \brief The size of the aggregate at the time of emission
     */
    private int aggregateSize;
    /**
     * \brief The fiscal coefficient of the company at the time of emission
     */
    private double coefEmpresa;
    /**
     * \brief The fiscal coefficient of the individual at the time of emission
     */
    private double coefIndividual;

    /**
     * Empty constructor
     */
    private Factura(){
        this.issuerNif = "";
        this.issuerName = "";
        this.creationDate = LocalDateTime.now();
        this.lastEditDate = this.creationDate;
        this.clientNif = "";
        this.description = "";
        this.value = 0.0f;
        this.history = new LinkedList<>();
        this.econSector = Pendente.getInstance();
        this.possibleEconSectors = new HashSet<>();
        this.isEmpresaInterior = false;
        this.aggregateSize = 0;
        this.coefEmpresa = 0.0;
        this.coefIndividual = 0.0;
    }

    /**
     * \brief Fully parametrised constructor for <tt>Factura</tt>
     * @param company           The NIF of the entity that issued this Receipt
     * @param individual          The name of the entity that issued this Receipt
     * @param description         The description of the purchase
     * @param value               The value of the purchase
     */
    public Factura(ContribuinteEmpresarial company, ContribuinteIndividual individual,
                   String description, float value){
        this.issuerNif = company.getNif();
        this.issuerName = individual.getName();
        this.creationDate = LocalDateTime.now();
        this.lastEditDate = this.creationDate;
        this.clientNif = individual.getNif();
        this.description = description;
        this.value = value;
        this.history = new LinkedList<>();
        if(company.getEconActivities().size() > 1){
            this.econSector = Pendente.getInstance();
            this.possibleEconSectors = new HashSet<>(company.getEconActivities());
            this.possibleEconSectors.remove(Pendente.getInstance());
        }else{
            this.econSector = company.getEconActivities().iterator().next();
            this.possibleEconSectors = new HashSet<>();
        }
        this.isEmpresaInterior = false; //TODO empresa do interior
        this.aggregateSize = individual.getNumDependants();
        this.coefEmpresa = company.getFiscalCoefficient();
        this.coefIndividual = individual.getFiscalCoefficient();
    }

    /**
     * Copy constructor for <tt>Factura</tt>
     * @param factura the <tt>Factura</tt> to copy
     */
    private Factura(Factura factura){
        this.issuerNif = factura.getIssuerNif();
        this.issuerName = factura.getIssuerName();
        this.creationDate = factura.getCreationDate();
        this.lastEditDate = factura.getLastEditDate();
        this.clientNif = factura.getClientNif();
        this.description = factura.getDescription();
        this.value = factura.getValue();
        this.history = factura.getHistory();
        this.econSector = factura.getEconSector();
        this.possibleEconSectors = factura.getPossibleEconSectors();
        this.isEmpresaInterior = factura.isEmpresaInterior();
        this.aggregateSize = factura.getAggregateSize();
        this.coefEmpresa = factura.getCoefEmpresa();
        this.coefIndividual = factura.getCoefIndividual();
    }

    /**
     * \brief The NIF of the entity that issued this Receipt
     * @return The NIF of the entity that issued this Receipt
     */
    public String getIssuerNif(){
        return this.issuerNif;
    }

    /**
     * Returns the Name of the company that issued the receipt
     * @return The Name of the company that issued the receipt
     */
    public String getIssuerName(){
        return this.issuerName;
    }

    /**
     * Returns the creation date of the receipt
     * @return The creation date of the receipt
     */
    public LocalDateTime getCreationDate(){
        return this.creationDate;
    }

    /**
     * Returns the last date the receipt was edited
     * @return The last date the receipt was edited
     */
    public LocalDateTime getLastEditDate(){
        return this.lastEditDate;
    }

    /**
     * Returns the NIF of the client
     * @return The NIF of the client
     */
    public String getClientNif(){
        return this.clientNif;
    }

    /**
     * Returns the description of the purchase
     * @return The description of the purchase
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Returns the value of the purchase
     * @return The value of the purchase
     */
    public float getValue(){
        return this.value;
    }

    /**
     * Returns the history of state of this <tt>Factura</tt>
     * @return The history of state of this <tt>Factura</tt>
     */
    public LinkedList<Factura> getHistory(){
        return this.history
                .stream()
                .map(Factura::clone)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Returns the economic sector
     * @return The economic sector
     */
    private EconSector getEconSector(){
        return this.econSector;
    }

    /**
     * Changes the economic sector of the <tt>Factura</tt>
     * @param econSector the new economic sector
     */
    public void setEconSector(EconSector econSector) throws InvalidEconSectorException{
        if(!this.possibleEconSectors.contains(econSector)) throw new InvalidEconSectorException(econSector.toString());
        this.possibleEconSectors.remove(econSector);
        if(!(this.econSector instanceof Pendente)){
            this.possibleEconSectors.add(this.econSector);
        }
        this.history.addFirst(this.clone().cleanHistory());
        this.econSector = econSector;
        this.lastEditDate = LocalDateTime.now();
    }

    /**
     * Cleans the history.
     * @return Returns the instance.
     */
    private Factura cleanHistory(){
        this.history.clear();
        this.possibleEconSectors.clear();
        return this;
    }

    /**
     * Returns the type of the <tt>Factura</tt>
     * @return The type of the <tt>Factura</tt>
     */
    public EconSector getType(){
        return this.econSector;
    }

    /**
     * Returns the list of possible econ sectors
     * @return The list of possible econ sectors
     */
    public Set<EconSector> getPossibleEconSectors(){
        return new HashSet<>(this.possibleEconSectors);
    }

    public boolean isEmpresaInterior(){
        return this.isEmpresaInterior;
    }

    public int getAggregateSize(){
        return this.aggregateSize;
    }

    public double getCoefEmpresa(){
        return this.coefEmpresa;
    }

    public double getCoefIndividual(){
        return this.coefIndividual;
    }

    /**
     * Returns if the <tt>Factura</tt> is deductible
     * @return if the <tt>Factura</tt> is deductible
     */
    private boolean isDeductible(){
        return this.econSector instanceof Deductible;
    }

    /**
     * Returns the amount that can be deducted from this
     * @return The amount that can be deducted from this
     */
    public float deducao(){
        if(this.isDeductible()){
            return ((Deductible) this.econSector).deduction(this.value, this.isEmpresaInterior, this.aggregateSize,
                                                            this.coefEmpresa, this.coefIndividual);
        }
        return 0.0f;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;

        if(o == null || this.getClass() != o.getClass()) return false;

        Factura factura = (Factura) o;
        return this.value == factura.getValue()
               && this.issuerNif.equals(factura.getIssuerNif())
               && this.issuerName.equals(factura.getIssuerName())
               && this.creationDate.equals(factura.getCreationDate())
               && this.lastEditDate.equals(factura.getLastEditDate())
               && this.clientNif.equals(factura.getClientNif())
               && this.description.equals(factura.getDescription())
               && this.history.equals(factura.getHistory())
               && this.econSector.equals(factura.getEconSector())
               && this.possibleEconSectors.equals(factura.getPossibleEconSectors());
    }

    @Override
    public String toString(){
        return "Factura{"
               + "issuerNif='" + issuerNif + '\''
               + ", issuerName='" + issuerName + '\''
               + ", creationDate=" + creationDate
               + ", lastEditDate=" + lastEditDate
               + ", clientNif='" + clientNif + '\''
               + ", description='" + description + '\''
               + ", value=" + value + '\''
               + ", econSector=" + econSector + '\''
               + ", possibleEconSectors=" + possibleEconSectors + '\''
               + ", history=" + history + '\''
               + '}';
    }

    @Override
    public Factura clone(){
        return new Factura(this);
    }

    @Override
    public int compareTo(Factura o){
        return this.creationDate.compareTo(o.getCreationDate());
    }
}