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

import ma.zyn.app.bean.core.money.PurchaseItem;
import ma.zyn.app.dao.criteria.core.money.PurchaseItemCriteria;
import ma.zyn.app.service.facade.admin.money.PurchaseItemAdminService;
import ma.zyn.app.ws.converter.money.PurchaseItemConverter;
import ma.zyn.app.ws.dto.money.PurchaseItemDto;
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
@RequestMapping("/api/admin/purchaseItem/")
public class PurchaseItemRestAdmin {




    @Operation(summary = "Finds a list of all purchaseItems")
    @GetMapping("")
    public ResponseEntity<List<PurchaseItemDto>> findAll() throws Exception {
        ResponseEntity<List<PurchaseItemDto>> res = null;
        List<PurchaseItem> list = service.findAll();
        HttpStatus status = HttpStatus.NO_CONTENT;
            converter.initObject(true);
        List<PurchaseItemDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;
        res = new ResponseEntity<>(dtos, status);
        return res;
    }


    @Operation(summary = "Finds a purchaseItem by id")
    @GetMapping("id/{id}")
    public ResponseEntity<PurchaseItemDto> findById(@PathVariable Long id) {
        PurchaseItem t = service.findById(id);
        if (t != null) {
            converter.init(true);
            PurchaseItemDto dto = converter.toDto(t);
            return getDtoResponseEntity(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Saves the specified  purchaseItem")
    @PostMapping("")
    public ResponseEntity<PurchaseItemDto> save(@RequestBody PurchaseItemDto dto) throws Exception {
        if(dto!=null){
            converter.init(true);
            PurchaseItem myT = converter.toItem(dto);
            PurchaseItem t = service.create(myT);
            if (t == null) {
                return new ResponseEntity<>(null, HttpStatus.IM_USED);
            }else{
                PurchaseItemDto myDto = converter.toDto(t);
                return new ResponseEntity<>(myDto, HttpStatus.CREATED);
            }
        }else {
            return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Updates the specified  purchaseItem")
    @PutMapping("")
    public ResponseEntity<PurchaseItemDto> update(@RequestBody PurchaseItemDto dto) throws Exception {
        ResponseEntity<PurchaseItemDto> res ;
        if (dto.getId() == null || service.findById(dto.getId()) == null)
            res = new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            PurchaseItem t = service.findById(dto.getId());
            converter.copy(dto,t);
            PurchaseItem updated = service.update(t);
            PurchaseItemDto myDto = converter.toDto(updated);
            res = new ResponseEntity<>(myDto, HttpStatus.OK);
        }
        return res;
    }

    @Operation(summary = "Delete list of purchaseItem")
    @PostMapping("multiple")
    public ResponseEntity<List<PurchaseItemDto>> delete(@RequestBody List<PurchaseItemDto> dtos) throws Exception {
        ResponseEntity<List<PurchaseItemDto>> res ;
        HttpStatus status = HttpStatus.CONFLICT;
        if (dtos != null && !dtos.isEmpty()) {
            converter.init(false);
            List<PurchaseItem> ts = converter.toItem(dtos);
            service.delete(ts);
            status = HttpStatus.OK;
        }
        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Delete the specified purchaseItem")
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


    @Operation(summary = "Finds a purchaseItem and associated list by id")
    @GetMapping("detail/id/{id}")
    public ResponseEntity<PurchaseItemDto> findWithAssociatedLists(@PathVariable Long id) {
        PurchaseItem loaded =  service.findWithAssociatedLists(id);
        converter.init(true);
        PurchaseItemDto dto = converter.toDto(loaded);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Finds purchaseItems by criteria")
    @PostMapping("find-by-criteria")
    public ResponseEntity<List<PurchaseItemDto>> findByCriteria(@RequestBody PurchaseItemCriteria criteria) throws Exception {
        ResponseEntity<List<PurchaseItemDto>> res = null;
        List<PurchaseItem> list = service.findByCriteria(criteria);
        HttpStatus status = HttpStatus.NO_CONTENT;
        converter.initObject(true);
        List<PurchaseItemDto> dtos  = converter.toDto(list);
        if (dtos != null && !dtos.isEmpty())
            status = HttpStatus.OK;

        res = new ResponseEntity<>(dtos, status);
        return res;
    }

    @Operation(summary = "Finds paginated purchaseItems by criteria")
    @PostMapping("find-paginated-by-criteria")
    public ResponseEntity<PaginatedList> findPaginatedByCriteria(@RequestBody PurchaseItemCriteria criteria) throws Exception {
        List<PurchaseItem> list = service.findPaginatedByCriteria(criteria, criteria.getPage(), criteria.getMaxResults(), criteria.getSortOrder(), criteria.getSortField());
        converter.initObject(true);
        List<PurchaseItemDto> dtos = converter.toDto(list);
        PaginatedList paginatedList = new PaginatedList();
        paginatedList.setList(dtos);
        if (dtos != null && !dtos.isEmpty()) {
            int dateSize = service.getDataSize(criteria);
            paginatedList.setDataSize(dateSize);
        }
        return new ResponseEntity<>(paginatedList, HttpStatus.OK);
    }

    @Operation(summary = "Gets purchaseItem data size by criteria")
    @PostMapping("data-size-by-criteria")
    public ResponseEntity<Integer> getDataSize(@RequestBody PurchaseItemCriteria criteria) throws Exception {
        int count = service.getDataSize(criteria);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
	
	public List<PurchaseItemDto> findDtos(List<PurchaseItem> list){
        converter.initObject(true);
        List<PurchaseItemDto> dtos = converter.toDto(list);
        return dtos;
    }

    private ResponseEntity<PurchaseItemDto> getDtoResponseEntity(PurchaseItemDto dto) {
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }




   public PurchaseItemRestAdmin(PurchaseItemAdminService service, PurchaseItemConverter converter){
        this.service = service;
        this.converter = converter;
    }

    private final PurchaseItemAdminService service;
    private final PurchaseItemConverter converter;





}
