package com.example.lisener;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.util.StringUtils;
import com.example.entity.WebInfo;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;



@Slf4j
public class SearchActionListener implements ActionListener {

    private JFrame frame;
    private JTextField numberText;
    private JTextField resultText;
    private JLabel processLabel;
    private List<WebInfo> list;

    private final static String DOWNLOAD_TYPE = "3";
    private final static String STAMP_FLAG = "1";
    private final static String MATCH_KEY_WORD = "odd";

    public SearchActionListener() {

    }

    public SearchActionListener(JFrame frame, JTextField numberText, JTextField resultText, JLabel processLabel, List<WebInfo> list) {
        this.frame = frame;
        this.numberText = numberText;
        this.resultText = resultText;
        this.processLabel = processLabel;
        this.list = list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = numberText.getText();
        processLabel.setText("进度:0%");
        if (StringUtils.isBlank(name)) {
            resultText.setText(StringUtils.EMPTY);
            JOptionPane.showMessageDialog(frame, "检索内容不能为空");
            return;
        }

        // case:数字,支持自动检索自增100
        if (NumberUtil.isNumber(name)) {
            search4Numeric(name);
        } else {
        // case:文本
            search4Text(name);
        }


    }

    private void search4Text(String name) {
        boolean existFlg = false;
        // 构建表单参数
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("entry_id", name);
        paramMap.put("downloadType", DOWNLOAD_TYPE);
        paramMap.put("stampFlag", STAMP_FLAG);

        for (WebInfo item : list) {
            String targetUrl = item.getUrl();

            // 校验是否为URL链接
            if (!HttpUtil.isHttp(targetUrl)) {
                continue;
            }

            String result = HttpUtil.post(targetUrl, paramMap);
            // 是否符合检索条件
            boolean match = result.contains(MATCH_KEY_WORD);
            if (match) {
                existFlg = true;
                resultText.setText(targetUrl);
                JOptionPane.showMessageDialog(frame, targetUrl);
                // 打开网页
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + targetUrl +"?entry_id=" + name);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            }
        }

        processLabel.setText("进度:100%");
        if (existFlg) {
            JOptionPane.showMessageDialog(frame, "检索完成,查询结果详见网页");
        } else {
            // 检索后不存在符合条件的网址
            resultText.setText(StringUtils.EMPTY);
            JOptionPane.showMessageDialog(frame, "检索完成,查询结果不存在");
        }
    }

    private void search4Numeric(String name) {
        boolean existFlg = false;
        BigDecimal firstNumber = new BigDecimal(name);

        for (int i = 0; i < 100; i++) {
            BigDecimal number = firstNumber.add(new BigDecimal(i));

            // 构建表单参数
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("entry_id", number.toString());
            paramMap.put("downloadType", DOWNLOAD_TYPE);
            paramMap.put("stampFlag", STAMP_FLAG);

            for (WebInfo item : list) {
                String targetUrl = item.getUrl();

                // 校验是否为URL链接
                if (!HttpUtil.isHttp(targetUrl)) {
                    continue;
                }

                String result = HttpUtil.post(targetUrl, paramMap);
                // 是否符合检索条件
                boolean match = result.contains(MATCH_KEY_WORD);
                if (match) {
                    existFlg = true;
                    numberText.setText(number.toString());
                    resultText.setText(targetUrl);
                    //JOptionPane.showMessageDialog(frame, targetUrl);
                    // 打开网页
                    try {
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + targetUrl + "?entry_id=" + number.toString());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error("线程睡眠报错, ", e);
                    }
                }
            }

            processLabel.setText("进度:" + i  + "%");
            //JOptionPane.showMessageDialog(frame, "继续检索检索下一条:" + number + ",当前进度:" + i  + "%");
        }

        processLabel.setText("进度:100%");
        if (existFlg) {
            JOptionPane.showMessageDialog(frame, "检索完成,查询结果详见网页");
        } else {
            // 检索后不存在符合条件的网址
            resultText.setText(StringUtils.EMPTY);
            JOptionPane.showMessageDialog(frame, "检索完成,查询结果不存在");
        }
    }
}
