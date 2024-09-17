package  ma.zyn.app.dao.criteria.core.catalog;



import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class ProductCriteria extends  BaseCriteria  {

    private String code;
    private String codeLike;
    private String reference;
    private String referenceLike;



    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCodeLike(){
        return this.codeLike;
    }
    public void setCodeLike(String codeLike){
        this.codeLike = codeLike;
    }

    public String getReference(){
        return this.reference;
    }
    public void setReference(String reference){
        this.reference = reference;
    }
    public String getReferenceLike(){
        return this.referenceLike;
    }
    public void setReferenceLike(String referenceLike){
        this.referenceLike = referenceLike;
    }


}
