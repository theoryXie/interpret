package whu.se.interpret.service;

import org.springframework.stereotype.Service;
import whu.se.interpret.po.token;
import whu.se.interpret.service.impl.wordAnalysisImpl;

import java.util.List;

/**
 * @author xsy
 * @description: 词法分析方法的实现类
 * @date 2019/9/2121:30
 */

@Service
public class wordAnalysis implements wordAnalysisImpl {

    @Override
    public List<token> wordAnalysis(String code) {
        return null;
    }
}
