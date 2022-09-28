package com.example.springscaffold2.api.service;

import com.example.springscaffold2.api.dao.TestMapper;
import com.example.springscaffold2.api.jpas.sub.entity.TestSub;
import com.example.springscaffold2.api.jpas.sub.repository.TestSubRepository;
import com.example.springscaffold2.common.model.ThreadLocalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropagationTestService {
    private final TestSubRepository testSubRepository; // 단일
    private final TestMapper testMapper; // 다중 (Route)
    
    
    @Transactional(value="multiTxManager", propagation = Propagation.REQUIRES_NEW)
    public void imSolo(){
        log.info("Active Tx : {}", String.valueOf(TransactionSynchronizationManager.isActualTransactionActive()));
        log.info("TxName : {}", TransactionSynchronizationManager.getCurrentTransactionName());
        TestSub t2 = new TestSub();
        t2.setName("저는 롤백되지 않아요");
        testSubRepository.save(t2);

        ThreadLocalContext.set("test2");
        testMapper.save("저도 같이 롤백되지 않아야해요.");
    }

    @Transactional(value="multiTxManager", propagation = Propagation.REQUIRED)
    public void imNeedParent(){
        log.info("Active Tx : {}", String.valueOf(TransactionSynchronizationManager.isActualTransactionActive()));
        log.info("TxName : {}", TransactionSynchronizationManager.getCurrentTransactionName());
        TestSub t2 = new TestSub();
        t2.setName("저는 롤백될거에요");
        testSubRepository.save(t2);

        ThreadLocalContext.set("test2");
        testMapper.save("저도 같이 롤백되야해요.");
    }
}
