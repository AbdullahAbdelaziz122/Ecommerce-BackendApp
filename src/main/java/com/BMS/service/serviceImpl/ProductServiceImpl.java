package com.BMS.service.serviceImpl;

import com.BMS.exceptions.ResourceAlreadyExist;
import com.BMS.exceptions.ResourceNotFoundException;
import com.BMS.model.Category;
import com.BMS.model.Product;
import com.BMS.model.Tag;
import com.BMS.payloads.ProductDTO;
import com.BMS.payloads.ProductResponse;
import com.BMS.repository.CategoryRepository;
import com.BMS.repository.ProductRepository;
import com.BMS.repository.TagRepository;
import com.BMS.service.ProductService;
import com.BMS.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    private final CategoryRepository categoryRepository;


    private final ModelMapper modelMapper;

    private final TagRepository tagRepository;

    // Create
    @Override
    public ProductDTO addProduct(Long categoryID, List<String> tags, Product product){
        Category category = categoryRepository.findById(categoryID).orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
        // todo : being able to add tags when making a product.


        // check if all tags given does exist
        for(String tag : tags){
            tagRepository.findTagByName(tag).orElseThrow(() -> new ResourceNotFoundException("There is not tag with name: " + tag));
        }
        List<Tag> tagList = new ArrayList<>();
        for(String tag: tags){
            tagList.add(tagRepository.findTagByName(tag).orElse(null));
        }
        boolean isProductPresent = false;

        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getName().equals(product.getName()) && value.getDescription().equals(product.getDescription())) {
                isProductPresent = true;
                break;
            }
        }

        if (!isProductPresent){
            product.setImage("/images/default.png");
            product.setCategory(category);
            product.setTags(tagList);

            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setPrice(specialPrice);

            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        }else {
            throw new ResourceAlreadyExist("Product Already exist");
        }

    }


    @Override
    public ProductResponse searchProducts(Integer pageNumber, Integer pageLimit,String name, String brand, String category, List<String> tags){

        // pagination
        Pageable pageable = PageRequest.of(pageNumber, pageLimit, Sort.unsorted());
        Page<Product> productPage;

        // filtering process
        Specification<Product> spec = Specification
                .where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasBrand(brand))
                .and(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.hasTags(tags));

        productPage = productRepository.findAll(spec, pageable);

        List<Product> products = productPage.getContent();

        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        // Response construction
        ProductResponse productResponse = new ProductResponse();
        productResponse.setData(productDTOS);
        productResponse.setPageLimit(productPage.getSize());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());

        return productResponse;
    }


//public ProductDTO updateProduct(Product product, Long productId) {
//    Product existingProduct = productRepository.findById(productId)
//            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//
//    if (product.getName() != null) {
//        existingProduct.setName(product.getName());
//    }
//    if (product.getDescription() != null) {
//        existingProduct.setDescription(product.getDescription());
//    }
//    if (product.getBrand() != null) {
//        existingProduct.setBrand(product.getBrand());
//    }
//    if (product.getImage() != null) {
//        existingProduct.setImage(product.getImage());
//    }
//    if (product.getPrice() != 0) {
//        existingProduct.setPrice(product.getPrice());
//    }
//    if (product.getDiscount() != 0) {
//        existingProduct.setDiscount(product.getDiscount());
//    }
//    if (product.getQuantity() != null) {
//        existingProduct.setQuantity(product.getQuantity());
//    }
//    if (product.getTags() != null) {
//        existingProduct.setTags(product.getTags());
//    }
//
//    Product updatedProduct = productRepository.save(existingProduct);
//    return modelMapper.map(updatedProduct, ProductDTO.class);
//}


    @Override
    public ProductDTO updateProduct(Product product, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Copy non-null properties from the input product to the existing product
        BeanUtils.copyProperties(product, existingProduct, getNullPropertyNames(product));

        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    private String[] getNullPropertyNames(Product source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        List<String> emptyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}

    // Update
    // Delete
    // todo: deleting a product will Lead to deleting a project from each cart as it's not there.
    // todo: This method will be implemented after the cartService being finished.


