package  ma.zyn.app.ws.converter.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;




import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.catalog.Product;
import ma.zyn.app.ws.dto.catalog.ProductDto;

@Component
public class ProductConverter {



    public Product toItem(ProductDto dto) {
        if (dto == null) {
            return null;
        } else {
        Product item = new Product();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getCode()))
                item.setCode(dto.getCode());
            if(StringUtil.isNotEmpty(dto.getReference()))
                item.setReference(dto.getReference());



        return item;
        }
    }


    public ProductDto toDto(Product item) {
        if (item == null) {
            return null;
        } else {
            ProductDto dto = new ProductDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getCode()))
                dto.setCode(item.getCode());
            if(StringUtil.isNotEmpty(item.getReference()))
                dto.setReference(item.getReference());


        return dto;
        }
    }


	
    public List<Product> toItem(List<ProductDto> dtos) {
        List<Product> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ProductDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ProductDto> toDto(List<Product> items) {
        List<ProductDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Product item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ProductDto dto, Product t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<Product> copy(List<ProductDto> dtos) {
        List<Product> result = new ArrayList<>();
        if (dtos != null) {
            for (ProductDto dto : dtos) {
                Product instance = new Product();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
