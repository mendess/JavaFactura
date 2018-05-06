package javafactura.businessLogic;

import javafactura.businessLogic.econSectors.EconSector;
import javafactura.businessLogic.exceptions.InvalidEconSectorException;
import javafactura.businessLogic.exceptions.InvalidNumberOfDependantsException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContribuinteIndividual extends Contribuinte {


    /**
     * The NIFs of the people dependant of this Contribuinte
     */
    private List<String> familyAggregate;
    /**
     * Number of dependants
     */
    private int numDependants;
    /**
     * The fiscal coefficient of this Contribuinte
     */
    private double fiscalCoefficient;

    private Set<EconSector> econActivities;

    private ContribuinteIndividual(){
        this.familyAggregate = new ArrayList<>();
        this.numDependants = 0;
        this.fiscalCoefficient = 0.0;
        this.econActivities = new HashSet<>();
    }

    /**
     * Fully parametrised constructor for <tt>Contribuinte Individual</tt>
     * @param nif               The NIF
     * @param email             The email
     * @param nome              The name
     * @param address           The address
     * @param password          The password
     * @param numDependants     Number of dependents
     * @param familyAggregate   The list of NIF of the family aggregate
     * @param fiscalCoefficient The fiscal coefficient of the Contribuinte
     * @param econActivities    The economic activities eligible
     * @throws InvalidNumberOfDependantsException if the number of dependants is higher then the size
     *                                            of the family aggregate
     */
    public ContribuinteIndividual(String nif, String email, String nome,
                                  String address, String password, int numDependants,
                                  List<String> familyAggregate, double fiscalCoefficient,
                                  Set<EconSector> econActivities) throws
                                                                  InvalidNumberOfDependantsException{
        super(nif, email, nome, address, password);
        this.familyAggregate = familyAggregate;
        this.setNumDependants(numDependants);
        this.fiscalCoefficient = fiscalCoefficient;
        this.econActivities = new HashSet<>(econActivities);

    }

    public ContribuinteIndividual(ContribuinteIndividual contribuinteIndividual){
        super(contribuinteIndividual);
        this.familyAggregate = contribuinteIndividual.getFamilyAggregate();
        this.numDependants = contribuinteIndividual.getNumDependants();
        this.fiscalCoefficient = contribuinteIndividual.getFiscalCoefficient();
        this.econActivities = contribuinteIndividual.getEconActivities();
    }

    public List<String> getFamilyAggregate(){
        return familyAggregate;
    }

    public int getNumDependants(){
        return numDependants;
    }

    public double getFiscalCoefficient(){
        return fiscalCoefficient;
    }

    public Set<EconSector> getEconActivities(){
        return econActivities;
    }

    public void setFamilyAggregate(List<String> familyAggregate){
        this.familyAggregate = familyAggregate;
    }

    public void setNumDependants(int numDependants) throws InvalidNumberOfDependantsException{
        if(numDependants <= this.familyAggregate.size()){
            this.numDependants = numDependants;
        }else{
            throw new InvalidNumberOfDependantsException(numDependants);
        }
    }

    public void setFiscalCoefficient(double fiscalCoefficient){
        this.fiscalCoefficient = fiscalCoefficient;
    }

    public void setEconActivities(Set<EconSector> econActivities){
        this.econActivities = econActivities;
    }

    public Factura changeFatura(Factura f, EconSector e) throws InvalidEconSectorException{
        Factura changedF = this.facturas.get(this.facturas.indexOf(f));
        changedF.setEconSector(e);
        f.setEconSector(e);
        return f;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;

        if(o == null || this.getClass() != o.getClass()) return false;

        ContribuinteIndividual that = (ContribuinteIndividual) o;
        return super.equals(o)
               && this.numDependants == that.getNumDependants()
               && this.fiscalCoefficient == that.getFiscalCoefficient()
               && this.familyAggregate.equals(that.getFamilyAggregate())
               && this.econActivities.equals(that.getEconActivities());
    }

    @Override
    public String toString(){
        return "ContribuinteIndividual{" +
               "familyAggregate=" + familyAggregate
               + ", numDependants=" + numDependants
               + ", fiscalCoefficient=" + fiscalCoefficient
               + ", econActivities=" + econActivities
               + "} " + super.toString();
    }

    public ContribuinteIndividual clone(){
        return new ContribuinteIndividual(this);
    }
}