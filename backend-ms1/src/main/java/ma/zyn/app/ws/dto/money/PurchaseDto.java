package  ma.zyn.app.ws.dto.money;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Date;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;


import ma.zyn.app.ws.dto.catalog.ProductDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseDto  extends AuditBaseDto {

    private String reference  ;
    private String purchaseDate ;
    private String image  ;
    private BigDecimal total  ;
    private String description  ;
    private Boolean enabled  ;

    private PurchaseStateDto purchaseState ;

    private List<PurchaseItemDto> purchaseItems ;


    public PurchaseDto(){
        super();
    }




    public String getReference(){
        return this.reference;
    }
    public void setReference(String reference){
        this.reference = reference;
    }


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    public String getPurchaseDate(){
        return this.purchaseDate;
    }
    public void setPurchaseDate(String purchaseDate){
        this.purchaseDate = purchaseDate;
    }


    public String getImage(){
        return this.image;
    }
    public void setImage(String image){
        this.image = image;
    }


    public BigDecimal getTotal(){
        return this.total;
    }
    public void setTotal(BigDecimal total){
        this.total = total;
    }


    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }


    public Boolean getEnabled(){
        return this.enabled;
    }
    public void setEnabled(Boolean enabled){
        this.enabled = enabled;
    }


    public PurchaseStateDto getPurchaseState(){
        return this.purchaseState;
    }

    public void setPurchaseState(PurchaseStateDto purchaseState){
        this.purchaseState = purchaseState;
    }



    public List<PurchaseItemDto> getPurchaseItems(){
        return this.purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItemDto> purchaseItems){
        this.purchaseItems = purchaseItems;
    }



}
