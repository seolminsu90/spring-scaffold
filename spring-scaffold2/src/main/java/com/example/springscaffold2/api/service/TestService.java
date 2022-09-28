package com.example.springscaffold2.api.service;

import com.example.springscaffold2.api.dao.TestMapper;
import com.example.springscaffold2.api.jpas.root.entity.TestRoot;
import com.example.springscaffold2.api.jpas.root.repository.TestRootRepository;
import com.example.springscaffold2.api.jpas.sub.entity.TestSub;
import com.example.springscaffold2.api.jpas.sub.repository.TestSubRepository;
import com.example.springscaffold2.common.model.ThreadLocalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestService {
    /**
        DB example
        ---------------
        root -> test
        sub -> test2
        router -> test, test2, test3
    */
    private final TestRootRepository testRootRepository; // 단일
    private final TestSubRepository testSubRepository; // 단일
    private final TestMapper testMapper; // 다중 (Route)

    private final PropagationTestService testService;

    public List<Object> getNoTran() {
        return this.selectOtherDBs();
    }

    @Transactional(value = "multiTxManager", propagation = Propagation.REQUIRED) //default
    public List<Object> getTran() {
        return this.selectOtherDBs();
    }

    private List<Object> selectOtherDBs() {
        List<Object> results = new ArrayList<>();
        log.info("====================================");
        results.addAll(testSubRepository.findAll());
        results.addAll(testRootRepository.findAll());

        ThreadLocalContext.set("test3");
        results.addAll(testMapper.findAll());
        log.info("====================================");

        return results;
    }

    public int getNoTranRb() {
        this.rollbackTest();
        return 0;
    }

    @Transactional(value = "multiTxManager", propagation = Propagation.REQUIRED) //default
    public int getTranRb() {
        // XA 전파 테스트 - 잘 됨
        testService.imNeedParent();
        testService.imSolo();
        this.rollbackTest();
        return 0;
    }

    private void rollbackTest() {
        TestRoot test = testRootRepository.findById(1).orElse(null);
        if (test != null) test.setName(String.valueOf(Math.random()));
        testRootRepository.save(test);

        TestSub t2 = new TestSub();
        t2.setName("생성된 2번 (Notran)");
        testSubRepository.save(t2);

        ThreadLocalContext.set("test3");
        testMapper.save("3번에 마이마티스로 넣어요");

        if (true) throw new RuntimeException("Test");

        TestSub test2 = testSubRepository.findById(1).orElse(null);
        if (test2 != null) test2.setName(String.valueOf(Math.random()));
        testSubRepository.save(test2);
    }
}
