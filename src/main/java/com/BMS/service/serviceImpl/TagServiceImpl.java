package com.BMS.service.serviceImpl;

import com.BMS.exceptions.ResourceAlreadyExist;
import com.BMS.exceptions.ResourceNotFoundException;
import com.BMS.model.Product;
import com.BMS.model.Tag;
import com.BMS.payloads.TagDTO;
import com.BMS.payloads.TagResponse;
import com.BMS.repository.ProductRepository;
import com.BMS.repository.TagRepository;
import com.BMS.service.TagService;
import com.BMS.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {


    private final TagRepository tagRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public TagDTO creatTag(Tag tag) {
        boolean isTagExist = tagRepository.findTagByName(tag.getName()).isPresent();
        if(isTagExist){
            throw new ResourceAlreadyExist("Tag with with name: " + tag.getName()+ " already exists!");
        }

        Tag savedTag = tagRepository.save(tag);

        return modelMapper.map(savedTag, TagDTO.class);
    }


    @Override
    public TagResponse getAllTags(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Tag> pageTags = tagRepository.findAll(pageable);

        List<Tag> tags = pageTags.getContent();

        if(tags.isEmpty()){
            throw new ResourceNotFoundException("No Tags Found");
        }

        List<TagDTO> tagsDTO = tags.stream().map((tag) -> modelMapper.map(tag, TagDTO.class)).toList();

        TagResponse tagResponse = new TagResponse();

        tagResponse.setData(tags);
        tagResponse.setPageNumber(pageTags.getNumber());
        tagResponse.setPageLimit(pageTags.getSize());
        tagResponse.setTotalElements(pageTags.getTotalElements());
        tagResponse.setTotalPages(pageTags.getTotalPages());

        return tagResponse;
    }

    @Override
    public TagDTO updateTag(Tag tag,Long tagId) {
        Tag savedTag = tagRepository.findById(tagId).orElseThrow(()->  new ResourceNotFoundException("Tag "));

        tag.setId(tagId);
        savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag, TagDTO.class);

    }


    @Override
    public String deleteTag(Long tagId) {
    Tag savedTag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("No tag is found with this Id " + tagId));

    // Retrieve all products associated with the tag
    Specification<Product> spec = Specification
            .where(ProductSpecification.hasTags(List.of(savedTag.getName())));

    List<Product> products = productRepository.findAll(spec);


    // Remove the tag from each product's list of tags
    for (Product product : products) {
        product.getTags().remove(savedTag);
        productRepository.save(product);
    }

    // Delete the tag from the database
    tagRepository.deleteById(tagId);
    return "Tag has been successfully deleted";
}
}
