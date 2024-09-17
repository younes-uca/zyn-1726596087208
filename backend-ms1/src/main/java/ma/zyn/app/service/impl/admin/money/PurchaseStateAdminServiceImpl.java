package ma.zyn.app.service.impl.admin.money;


import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.money.PurchaseState;
import ma.zyn.app.dao.criteria.core.money.PurchaseStateCriteria;
import ma.zyn.app.dao.facade.core.money.PurchaseStateDao;
import ma.zyn.app.dao.specification.core.money.PurchaseStateSpecification;
import ma.zyn.app.service.facade.admin.money.PurchaseStateAdminService;
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


import java.util.List;
@Service
public class PurchaseStateAdminServiceImpl implements PurchaseStateAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public PurchaseState update(PurchaseState t) {
        PurchaseState loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{PurchaseState.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public PurchaseState findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public PurchaseState findOrSave(PurchaseState t) {
        if (t != null) {
            PurchaseState result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<PurchaseState> findAll() {
        return dao.findAll();
    }

    public List<PurchaseState> findByCriteria(PurchaseStateCriteria criteria) {
        List<PurchaseState> content = null;
        if (criteria != null) {
            PurchaseStateSpecification mySpecification = constructSpecification(criteria);
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


    private PurchaseStateSpecification constructSpecification(PurchaseStateCriteria criteria) {
        PurchaseStateSpecification mySpecification =  (PurchaseStateSpecification) RefelexivityUtil.constructObjectUsingOneParam(PurchaseStateSpecification.class, criteria);
        return mySpecification;
    }

    public List<PurchaseState> findPaginatedByCriteria(PurchaseStateCriteria criteria, int page, int pageSize, String order, String sortField) {
        PurchaseStateSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(PurchaseStateCriteria criteria) {
        PurchaseStateSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
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
    public List<PurchaseState> delete(List<PurchaseState> list) {
		List<PurchaseState> result = new ArrayList();
        if (list != null) {
            for (PurchaseState t : list) {
                if(dao.findById(t.getId()).isEmpty()){
					result.add(t);
				}
            }
        }
		return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public PurchaseState create(PurchaseState t) {
        PurchaseState loaded = findByReferenceEntity(t);
        PurchaseState saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public PurchaseState findWithAssociatedLists(Long id){
        PurchaseState result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<PurchaseState> update(List<PurchaseState> ts, boolean createIfNotExist) {
        List<PurchaseState> result = new ArrayList<>();
        if (ts != null) {
            for (PurchaseState t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    PurchaseState loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, PurchaseState t, PurchaseState loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public PurchaseState findByReferenceEntity(PurchaseState t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<PurchaseState> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<PurchaseState>> getToBeSavedAndToBeDeleted(List<PurchaseState> oldList, List<PurchaseState> newList) {
        List<List<PurchaseState>> result = new ArrayList<>();
        List<PurchaseState> resultDelete = new ArrayList<>();
        List<PurchaseState> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<PurchaseState> oldList, List<PurchaseState> newList, List<PurchaseState> resultUpdateOrSave, List<PurchaseState> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                PurchaseState myOld = oldList.get(i);
                PurchaseState t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                PurchaseState myNew = newList.get(i);
                PurchaseState t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}

    @Override
    public String uploadFile(String checksumOld, String tempUpladedFile, String destinationFilePath) throws Exception {
        return null;
    }








    public PurchaseStateAdminServiceImpl(PurchaseStateDao dao) {
        this.dao = dao;
    }

    private PurchaseStateDao dao;
}
