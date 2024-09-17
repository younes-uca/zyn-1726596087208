package ma.zyn.app.service.facade.admin.money;

import java.util.List;
import ma.zyn.app.bean.core.money.PurchaseItem;
import ma.zyn.app.dao.criteria.core.money.PurchaseItemCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface PurchaseItemAdminService {



    List<PurchaseItem> findByProductId(Long id);
    int deleteByProductId(Long id);
    long countByProductCode(String code);
    List<PurchaseItem> findByPurchaseId(Long id);
    int deleteByPurchaseId(Long id);
    long countByPurchaseReference(String reference);




	PurchaseItem create(PurchaseItem t);

    PurchaseItem update(PurchaseItem t);

    List<PurchaseItem> update(List<PurchaseItem> ts,boolean createIfNotExist);

    PurchaseItem findById(Long id);

    PurchaseItem findOrSave(PurchaseItem t);

    PurchaseItem findByReferenceEntity(PurchaseItem t);

    PurchaseItem findWithAssociatedLists(Long id);

    List<PurchaseItem> findAllOptimized();

    List<PurchaseItem> findAll();

    List<PurchaseItem> findByCriteria(PurchaseItemCriteria criteria);

    List<PurchaseItem> findPaginatedByCriteria(PurchaseItemCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(PurchaseItemCriteria criteria);

    List<PurchaseItem> delete(List<PurchaseItem> ts);

    boolean deleteById(Long id);

    List<List<PurchaseItem>> getToBeSavedAndToBeDeleted(List<PurchaseItem> oldList, List<PurchaseItem> newList);

    public String uploadFile(String checksumOld, String tempUpladedFile,String destinationFilePath) throws Exception ;

}
