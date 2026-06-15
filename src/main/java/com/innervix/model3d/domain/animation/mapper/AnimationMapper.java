package com.innervix.model3d.domain.animation.mapper;


import com.innervix.model3d.domain.animation.model.AnimationAsset;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnimationMapper {
    List<AnimationAsset> findAll();

    AnimationAsset findById(@Param("id") String id);

    int insert(@Param("asset") AnimationAsset asset);
}
