package com.innervix.model3d.domain.model.mapper;


import com.innervix.model3d.domain.model.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface ModelMapper {
    List<ModelAsset> findAll(@Param("q") String q,
                             @Param("category") String category,
                             @Param("format") String format,
                             @Param("elementTypeId") String elementTypeId,
                             @Param("sort") String sort);

    Optional<ModelAsset> findById(@Param("id") String id);

    int insert(@Param("model") ModelAsset model);

    int update(@Param("id") String id, @Param("request") ModelUpdateRequest request);

    int incrementView(@Param("id") String id);

    int incrementDownload(@Param("id") String id);

    boolean delete(@Param("id") String id);
}
