package  ma.zyn.app.ws.converter.money;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;




import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.money.PurchaseState;
import ma.zyn.app.ws.dto.money.PurchaseStateDto;

@Component
public class PurchaseStateConverter {



    public PurchaseState toItem(PurchaseStateDto dto) {
        if (dto == null) {
            return null;
        } else {
        PurchaseState item = new PurchaseState();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getCode()))
                item.setCode(dto.getCode());
            if(StringUtil.isNotEmpty(dto.getLibelle()))
                item.setLibelle(dto.getLibelle());
            if(StringUtil.isNotEmpty(dto.getStyle()))
                item.setStyle(dto.getStyle());



        return item;
        }
    }


    public PurchaseStateDto toDto(PurchaseState item) {
        if (item == null) {
            return null;
        } else {
            PurchaseStateDto dto = new PurchaseStateDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getCode()))
                dto.setCode(item.getCode());
            if(StringUtil.isNotEmpty(item.getLibelle()))
                dto.setLibelle(item.getLibelle());
            if(StringUtil.isNotEmpty(item.getStyle()))
                dto.setStyle(item.getStyle());


        return dto;
        }
    }


	
    public List<PurchaseState> toItem(List<PurchaseStateDto> dtos) {
        List<PurchaseState> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (PurchaseStateDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<PurchaseStateDto> toDto(List<PurchaseState> items) {
        List<PurchaseStateDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (PurchaseState item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(PurchaseStateDto dto, PurchaseState t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<PurchaseState> copy(List<PurchaseStateDto> dtos) {
        List<PurchaseState> result = new ArrayList<>();
        if (dtos != null) {
            for (PurchaseStateDto dto : dtos) {
                PurchaseState instance = new PurchaseState();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
