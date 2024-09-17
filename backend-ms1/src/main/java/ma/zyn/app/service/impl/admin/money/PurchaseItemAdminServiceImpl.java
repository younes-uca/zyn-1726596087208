package ma.zyn.app.service.impl.admin.money;


import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.money.PurchaseItem;
import ma.zyn.app.dao.criteria.core.money.PurchaseItemCriteria;
import ma.zyn.app.dao.facade.core.money.PurchaseItemDao;
import ma.zyn.app.dao.specification.core.money.PurchaseItemSpecification;
import ma.zyn.app.service.facade.admin.money.PurchaseItemAdminService;
import ma.zyn.app.zynerator.service.AbstractServiceImpl;
import static ma.zyn.app.zynerator.util.ListUtil.*;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ma.zyn.app.zynerator.util.RefelexivityUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ma.zyn.app.service.facade.admin.money.PurchaseAdminService ;
import ma.zyn.app.bean.core.money.Purchase ;
import ma.zyn.app.service.facade.admin.catalog.ProductAdminService ;
import ma.zyn.app.bean.core.catalog.Product ;

import java.util.List;
@Service
public class PurchaseItemAdminServiceImpl implements PurchaseItemAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public PurchaseItem update(PurchaseItem t) {
        PurchaseItem loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{PurchaseItem.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public PurchaseItem findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public PurchaseItem findOrSave(PurchaseItem t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            PurchaseItem result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<PurchaseItem> findAll() {
        return dao.findAll();
    }

    public List<PurchaseItem> findByCriteria(PurchaseItemCriteria criteria) {
        List<PurchaseItem> content = null;
        if (criteria != null) {
            PurchaseItemSpecification mySpecification = constructSpecification(criteria);
            if (criteria.isPeagable()) {
                Pageable pageable = PageRequest.of(0, criteria.getMaxResults());
                content = dao.findAll(mySpecification, pageable).getContent();
            } else {
                content = dao.findAll(mySpecification);
            }
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private PurchaseItemSpecification constructSpecification(PurchaseItemCriteria criteria) {
        PurchaseItemSpecification mySpecification =  (PurchaseItemSpecification) RefelexivityUtil.constructObjectUsingOneParam(PurchaseItemSpecification.class, criteria);
        return mySpecification;
    }

    public List<PurchaseItem> findPaginatedByCriteria(PurchaseItemCriteria criteria, int page, int pageSize, String order, String sortField) {
        PurchaseItemSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(PurchaseItemCriteria criteria) {
        PurchaseItemSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<PurchaseItem> findByProductId(Long id){
        return dao.findByProductId(id);
    }
    public int deleteByProductId(Long id){
        return dao.deleteByProductId(id);
    }
    public long countByProductCode(String code){
        return dao.countByProductCode(code);
    }
    public List<PurchaseItem> findByPurchaseId(Long id){
        return dao.findByPurchaseId(id);
    }
    public int deleteByPurchaseId(Long id){
        return dao.deleteByPurchaseId(id);
    }
    public long countByPurchaseReference(String reference){
        return dao.countByPurchaseReference(reference);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            dao.deleteById(id);
        }
        return condition;
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<PurchaseItem> delete(List<PurchaseItem> list) {
		List<PurchaseItem> result = new ArrayList();
        if (list != null) {
            for (PurchaseItem t : list) {
                if(dao.findById(t.getId()).isEmpty()){
					result.add(t);
				}
            }
        }
		return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public PurchaseItem create(PurchaseItem t) {
        PurchaseItem loaded = findByReferenceEntity(t);
        PurchaseItem saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public PurchaseItem findWithAssociatedLists(Long id){
        PurchaseItem result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<PurchaseItem> update(List<PurchaseItem> ts, boolean createIfNotExist) {
        List<PurchaseItem> result = new ArrayList<>();
        if (ts != null) {
            for (PurchaseItem t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    PurchaseItem loadedItem = dao.findById(t.getId()).orElse(null);
                    if (isEligibleForCreateOrUpdate(createIfNotExist, t, loadedItem)) {
                        dao.save(t);
                    } else {
                        result.add(t);
                    }
                }
            }
        }
        return result;
    }


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, PurchaseItem t, PurchaseItem loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public PurchaseItem findByReferenceEntity(PurchaseItem t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(PurchaseItem t){
        if( t != null) {
            t.setProduct(productService.findOrSave(t.getProduct()));
            t.setPurchase(purchaseService.findOrSave(t.getPurchase()));
        }
    }



    public List<PurchaseItem> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<PurchaseItem>> getToBeSavedAndToBeDeleted(List<PurchaseItem> oldList, List<PurchaseItem> newList) {
        List<List<PurchaseItem>> result = new ArrayList<>();
        List<PurchaseItem> resultDelete = new ArrayList<>();
        List<PurchaseItem> resultUpdateOrSave = new ArrayList<>();
        if (isEmpty(oldList) && isNotEmpty(newList)) {
            resultUpdateOrSave.addAll(newList);
        } else if (isEmpty(newList) && isNotEmpty(oldList)) {
            resultDelete.addAll(oldList);
        } else if (isNotEmpty(newList) && isNotEmpty(oldList)) {
			extractToBeSaveOrDelete(oldList, newList, resultUpdateOrSave, resultDelete);
        }
        result.add(resultUpdateOrSave);
        result.add(resultDelete);
        return result;
    }

    private void extractToBeSaveOrDelete(List<PurchaseItem> oldList, List<PurchaseItem> newList, List<PurchaseItem> resultUpdateOrSave, List<PurchaseItem> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                PurchaseItem myOld = oldList.get(i);
                PurchaseItem t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                PurchaseItem myNew = newList.get(i);
                PurchaseItem t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}

    @Override
    public String uploadFile(String checksumOld, String tempUpladedFile, String destinationFilePath) throws Exception {
        return null;
    }







    @Autowired
    private PurchaseAdminService purchaseService ;
    @Autowired
    private ProductAdminService productService ;

    public PurchaseItemAdminServiceImpl(PurchaseItemDao dao) {
        this.dao = dao;
    }

    private PurchaseItemDao dao;
}
