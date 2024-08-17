import com.alibaba.excel.EasyExcel;
import com.example.entity.WebInfo;
import com.example.lisener.ExitActionListener;
import com.example.lisener.SearchActionListener;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.List;


@Slf4j
public class Main {
    private static JFrame frame = new JFrame("报关检索");
    private static JPanel mainPanel = new JPanel();
    private static JLabel titleLabel = new JLabel("报关检索");
    private static JLabel numberLabel = new JLabel("编号");
    private static JTextField numberText = new JTextField();

    private static JLabel resultLabel = new JLabel("查询结果");

    private static JTextField resultText = new JTextField();
    private static JButton searchBtn = new JButton("检索");
    private static JButton exitBtn = new JButton("退出");

    private static JLabel processLabel = new JLabel("进度:0%");

    private static String fileName = "D:/报关代理有限公司.xls";

    public static void main(String[] args) {
        // 渲染界面
        initFrame();

        // 预加载文件
        List<WebInfo> list = EasyExcel.read(fileName).head(WebInfo.class).sheet().doReadSync();

        // 设置事件
        searchBtn.addActionListener(new SearchActionListener(frame, numberText, resultText, processLabel,list));
        exitBtn.addActionListener(new ExitActionListener(frame));
    }

    private static void initFrame() {
        frame.add(mainPanel);
        mainPanel.add(titleLabel);
        mainPanel.add(numberLabel);
        mainPanel.add(numberText);
        mainPanel.add(resultLabel);
        mainPanel.add(resultText);
        mainPanel.add(searchBtn);
        mainPanel.add(exitBtn);
        mainPanel.add(processLabel);

        //自定义组件大小和放置位置
        mainPanel.setLayout(null);
        titleLabel.setBounds(120, 40, 340, 35);
        //设置字体，参数：字蔟，状态，大小
        titleLabel.setFont(new Font("黑体", Font.BOLD, 34));

        processLabel.setBounds(400,40,150,30);
        processLabel.setFont(new Font("黑体", Font.BOLD, 20));

        numberLabel.setBounds(94,124,90,30);
        numberLabel.setFont(new Font("黑体", Font.BOLD, 20));

        resultLabel.setBounds(94,174,90,30);
        resultLabel.setFont(new Font("黑体",Font.BOLD,20));

        numberText.setBounds(204, 124, 260, 30);
        numberText.setFont(new Font("黑体", Font.BOLD, 20));

        resultText.setBounds(204,174,260,30);
        resultText.setFont(new Font("黑体",Font.BOLD,20));

        searchBtn.setBounds(157, 232, 100, 30);
        searchBtn.setFont(new Font("黑体", Font.BOLD, 22));

        exitBtn.setBounds(304, 232, 100, 30);
        exitBtn.setFont(new Font("黑体", Font.BOLD, 22));

        //设置窗体背景色
        mainPanel.setBackground(Color.WHITE);

        frame.setBounds(400, 280, 560, 340);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
