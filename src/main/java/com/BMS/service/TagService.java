package com.BMS.service;

import com.BMS.model.Tag;
import com.BMS.payloads.TagDTO;
import com.BMS.payloads.TagResponse;

public interface TagService {

    // Create
    TagDTO creatTag(Tag tag);

    TagResponse getAllTags(int pageNo, int pageSize);


    TagDTO updateTag(Tag tag, Long tagId);

    String deleteTag(Long tagId);
}
