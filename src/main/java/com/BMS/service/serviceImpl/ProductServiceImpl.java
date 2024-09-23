package com.BMS.service.serviceImpl;

import com.BMS.exceptions.ResourceAlreadyExist;
import com.BMS.exceptions.ResourceNotFoundException;
import com.BMS.model.CartItem;
import com.BMS.model.Category;
import com.BMS.model.Product;
import com.BMS.model.Tag;
import com.BMS.payloads.ProductDTO;
import com.BMS.payloads.ProductResponse;
import com.BMS.repository.CartItemRepository;
import com.BMS.repository.CategoryRepository;
import com.BMS.repository.ProductRepository;
import com.BMS.repository.TagRepository;
import com.BMS.service.ProductService;
import com.BMS.specification.ProductSpecification;
import com.BMS.util.BeanUtilsHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    private final CategoryRepository categoryRepository;

    private final CartItemRepository cartItemRepository;

    private final ModelMapper modelMapper;

    private final TagRepository tagRepository;


       /**
     * Retrieves the names of properties that are null in the given Product object.
     * This method is useful for selectively copying non-null properties from one object to another.
     *
     * @param source the Product object to inspect for null properties
     * @return an array of property names that are null in the source object
     */
    private String[] getNullPropertyNames(Product source) {
        return BeanUtilsHelper.getNullPropertyNames(source);
    }


    // Create
    @Override
    public ProductDTO addProduct(Long categoryID, List<String> tags, Product product) {
        Category category = categoryRepository.findById(categoryID)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        // Initialize tagList, which will store the valid tags
        List<Tag> tagList = new ArrayList<>();

        // If tags are provided, check if all tags exist
        if (tags != null && !tags.isEmpty()) {
            // Check if all tags given exist
            for (String tag : tags) {
                tagRepository.findTagByName(tag)
                        .orElseThrow(() -> new ResourceNotFoundException("There is not a tag with name: " + tag));
                // Add valid tag to the tagList
                tagList.add(tagRepository.findTagByName(tag).orElse(null));
            }
        }

        boolean isProductPresent = false;
        List<Product> products = category.getProducts();

        // Check if the product already exists in the category
        for (Product value : products) {
            if (value.getName().equals(product.getName()) && value.getDescription().equals(product.getDescription())) {
                isProductPresent = true;
                break;
            }
        }

        if (!isProductPresent) {
            product.setImage("/images/default.png");
            product.setCategory(category);
            product.setTags(tagList); // Set tags (can be empty)

            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setPrice(specialPrice);

            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new ResourceAlreadyExist("Product Already exists");
        }
    }


    // read
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



    // Update
    @Override
    public ProductDTO updateProduct(Product product, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Copy non-null properties from the input product to the existing product
        BeanUtils.copyProperties(product, existingProduct, getNullPropertyNames(product));

        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }



    // delete
    @Override
    public void deleteProduct(Long productId){
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // delete every Cart item contains product
        List<CartItem> cartItems = cartItemRepository.findCartItemsByProductId(productId).orElse(null);
        if(cartItems != null && !cartItems.isEmpty()){
            cartItemRepository.deleteAll(cartItems);
        }

        productRepository.deleteById(productId);
    }


}


