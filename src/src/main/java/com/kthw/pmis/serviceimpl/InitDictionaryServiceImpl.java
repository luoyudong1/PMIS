package com.kthw.pmis.serviceimpl;


import org.springframework.stereotype.Service;

import com.kthw.common.base.AppCache;
import com.kthw.pmis.service.InitDictionaryService;

@Service
public class InitDictionaryServiceImpl implements InitDictionaryService {
	
//	@Autowired
//	DeviceMapper deviceMapper;
	
	public AppCache createAppChche(){
		AppCache appcache = new AppCache();
//		List<LuJu> bureauList = deviceMapper.getLujuList();
//		if(bureauList != null){
//			appcache.setBureauList(bureauList);
//		}	
		return appcache;
	}
	
}
