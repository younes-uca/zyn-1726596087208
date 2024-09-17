package ma.zyn.app.service.facade.admin.money;

import java.util.List;
import ma.zyn.app.bean.core.money.PurchaseState;
import ma.zyn.app.dao.criteria.core.money.PurchaseStateCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface PurchaseStateAdminService {







	PurchaseState create(PurchaseState t);

    PurchaseState update(PurchaseState t);

    List<PurchaseState> update(List<PurchaseState> ts,boolean createIfNotExist);

    PurchaseState findById(Long id);

    PurchaseState findOrSave(PurchaseState t);

    PurchaseState findByReferenceEntity(PurchaseState t);

    PurchaseState findWithAssociatedLists(Long id);

    List<PurchaseState> findAllOptimized();

    List<PurchaseState> findAll();

    List<PurchaseState> findByCriteria(PurchaseStateCriteria criteria);

    List<PurchaseState> findPaginatedByCriteria(PurchaseStateCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(PurchaseStateCriteria criteria);

    List<PurchaseState> delete(List<PurchaseState> ts);

    boolean deleteById(Long id);

    List<List<PurchaseState>> getToBeSavedAndToBeDeleted(List<PurchaseState> oldList, List<PurchaseState> newList);

    public String uploadFile(String checksumOld, String tempUpladedFile,String destinationFilePath) throws Exception ;

}
