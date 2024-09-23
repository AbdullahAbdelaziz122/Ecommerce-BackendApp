package com.BMS.Controller;

import com.BMS.util.AppConstants;
import com.BMS.model.Tag;
import com.BMS.payloads.TagDTO;
import com.BMS.payloads.TagResponse;
import com.BMS.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TagController {


    private final TagService tagService;


    @PostMapping("/admin/tag/create")
    public ResponseEntity<TagDTO> createTag(
           @Valid @RequestBody Tag tag
    ){

        return new ResponseEntity<>(tagService.creatTag(tag), HttpStatus.CREATED);
    }


    @GetMapping("/admin/tag/search")
    public ResponseEntity<TagResponse> getAllTags(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ){
        return new ResponseEntity<TagResponse>(tagService.getAllTags(pageNumber, pageSize), HttpStatus.OK);
    }


    @PutMapping("/admin/tag/update")
    public ResponseEntity<TagDTO> updateTag(
            @RequestBody Tag tag,
            @RequestParam(value = "tagId", required = true) Long tagId
    )
    {
        return new ResponseEntity<TagDTO>(tagService.updateTag(tag, tagId), HttpStatus.OK);
    }


   @DeleteMapping("/admin/tag/delete")
   public ResponseEntity<String> deleteTag(@RequestParam(value = "tagId", required = true) Long tagId){
        return new ResponseEntity<>(tagService.deleteTag(tagId), HttpStatus.OK);
   }


}
