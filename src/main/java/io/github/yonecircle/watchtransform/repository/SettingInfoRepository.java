package io.github.yonecircle.watchtransform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.yonecircle.watchtransform.entity.SettingInfo;

public interface SettingInfoRepository 
    extends JpaRepository<SettingInfo, Long>{
        
}
