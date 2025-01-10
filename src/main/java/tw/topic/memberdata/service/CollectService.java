package tw.topic.memberdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.topic.memberdata.model.Collect;

@Service
public class CollectService {
	// TODO 收藏的反應，要有點擊後+1，以及再次點擊後-1
	// TODO 收藏後，在收藏頁輸出被收藏的貼文
	@Autowired
	private Collect collect;
	
	
}