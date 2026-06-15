package com.innervix.model3d.domain.element.mapper;


import com.innervix.model3d.domain.element.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface ElementTypeMapper {
    List<ElementType> findActive();

    Optional<ElementType> findById(@Param("id") String id);

    Integer nextSortOrder();

    int insert(@Param("id") String id, @Param("request") ElementTypeRequest request, @Param("sortOrder") Integer sortOrder);
}
