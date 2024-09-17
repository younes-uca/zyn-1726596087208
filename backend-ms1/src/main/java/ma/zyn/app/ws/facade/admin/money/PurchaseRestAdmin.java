package  ma.zyn.app.ws.facade.admin.money;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.ArrayList;

import ma.zyn.app.bean.core.money.Purchase;
import ma.zyn.app.dao.criteria.core.money.PurchaseCriteria;
import ma.zyn.app.service.facade.admin.money.PurchaseAdminService;
import ma.zyn.app.ws.converter.money.PurchaseConverter;
import ma.zyn.app.ws.dto.money.PurchaseDto;
import ma.zyn.app.zynerator.controller.AbstractController;
import ma.zyn.app.zynerator.dto.AuditEntityDto;
import ma.zyn.app.zynerator.util.PaginatedList;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ma.zyn.app.zynerator.process.Result;


import org.springframework.web.multipart.MultipartFile;
import ma.zyn.app.zynerator.dto.FileTempDto;

@RestController
@RequestMapping("/api/admin/purchase/")
public class PurchaseRestAdmin {




    @Operation(summary = "Finds a list of all purchases")
    @GetMapping("")
    public ResponseEntity<List<PurchaseDto>> findAll() throws Exception {
        ResponseEntity<List<PurchaseDto>> res = null;
        List<Purchase> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
            converter.initObject(true);
        List<PurchaseDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds an optimized list of all purchases")
    @GetMapping("optimized")
    public ResponseEntity<List<PurchaseDto>> findAllOptimized() throws Exception {
        ResponseEntity<List<PurchaseDto>> res = null;
        List<Purchase> list = service.findAllOptimized();
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<PurchaseDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds a purchase by id")
    @GetMapping("id/{id}")
    public ResponseEntity<PurchaseDto> findById(@PathVariable Long id) {
        Purchase t = service.findById(id);
        if (t != null) {
            converter.init(true);
            PurchaseDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Finds a purchase by reference")
    @GetMapping("reference/{reference}")
    public ResponseEntity<PurchaseDto> findByReference(@PathVariable String reference) {
	    Purchase t = service.findByReferenceEntity(new Purchase(reference));
        if (t != null) {
            converter.init(true);
            PurchaseDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Saves the specified  purchase")
    @PostMapping("")
    public ResponseEntity<PurchaseDto> save(@RequestBody PurchaseDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            Purchase myT = converter.toItem(dto);
            Purchase t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                PurchaseDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  purchase")
    @PutMapping("")
    public ResponseEntity<PurchaseDto> update(@RequestBody PurchaseDto dto) throws Exception {
        ResponseEntity<PurchaseDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            Purchase t = service.findById(dto.getId());
            converter.copy(dto,t);
            Purchase updated = service.update(t);
            PurchaseDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of purchase")
    @PostMapping("multiple")
    public ResponseEntity<List<PurchaseDto>> delete(@RequestBody List<PurchaseDto> dtos) throws Exception {
        ResponseEntity<List<PurchaseDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<Purchase> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified purchase")
    @DeleteMapping("id/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) throws Exception {
        ResponseEntity<Long> res;
        HttpStatus status = HttpStatus.PRECONDITION_FAILED;
        if (id != null) {
            boolean resultDelete = service.deleteById(id);
            if (resultDelete) {
                status = HttpStatus.OK;
            }
        }
        res = new ResponseEntity<>(id, status);
        return res;
    }

    @Operation(summary = "find by purchaseState code")
    @GetMapping("purchaseState/code/{code}")
    public List<PurchaseDto> findByPurchaseStateCode(@PathVariable String code){
        return findDtos(service.findByPurchaseStateCode(code));
    }
    @Operation(summary = "delete by purchaseState code")
    @DeleteMapping("purchaseState/code/{code}")
    public int deleteByPurchaseStateCode(@PathVariable String code){
        return service.deleteByPurchaseStateCode(code);
    }

    @Operation(summary = "Finds a purchase and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<PurchaseDto> findWithAssociatedLists(@PathVariable Long id) {
        Purchase loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        PurchaseDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds purchases by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<PurchaseDto>> findByCriteria(@RequestBody PurchaseCriteria criteria) throws Exception {
        ResponseEntity<List<PurchaseDto>> res = null;
        List<Purchase> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initList(false);
        converter.initObject(true);
        List<PurchaseDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated purchases by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody PurchaseCriteria criteria) throws Exception {
        List<Purchase> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initList(false);
        converter.initObject(true);
        List<PurchaseDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets purchase data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody PurchaseCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<PurchaseDto> findDtos(List<Purchase> list){
        converter.initList(false);
        converter.initObject(true);
        List<PurchaseDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<PurchaseDto> getDtoResponseEntity(PurchaseDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }




   public PurchaseRestAdmin(PurchaseAdminService service, PurchaseConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final PurchaseAdminService service;
    private final PurchaseConverter converter;





}
