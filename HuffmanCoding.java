import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.BufferedWriter;
import java.io.FileWriter;


// کلاس برای آپلود فایل
class FileUploader {
    public static String uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Select a text file");
        fileChooser.setApproveButtonText("Upload");
        fileChooser.setApproveButtonToolTipText("Click to upload the selected text file");

        // تنظیمات پیش‌فرض برای دایرکتوری
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Lenovo\\Desktop"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return readFile(selectedFile);
        }
        return null;
    }

    private static String readFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}

// کلاس برای تحلیل متن
class TextAnalyzer {
    public static Map<Character, Integer> analyzeText(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            if (c != '\r' && c != '\n') { // نادیده گرفتن کاراکترهای خط جدید
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
        }
        return frequencyMap;
    }
}

// کلاس برای نودهای درخت هافمن
class HuffmanNode implements Comparable<HuffmanNode> {
    char c;
    int frequency;
    HuffmanNode left, right;

    HuffmanNode(char c, int frequency) {
        this.c = c;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}

// کلاس برای ساخت درخت هافمن
class HuffmanTree {
    public static HuffmanNode buildTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            queue.add(parent);
        }
        return queue.poll();
    }
}

// کلاس برای تولید کدهای هافمن
class HuffmanCode {
    public static Map<Character, String> generateCodes(HuffmanNode root) {
        Map<Character, String> codeMap = new HashMap<>();
        generateCodesRecursive(root, "", codeMap);
        return codeMap;
    }

    private static void generateCodesRecursive(HuffmanNode node, String code, Map<Character, String> codeMap) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            codeMap.put(node.c, code);
        }
        generateCodesRecursive(node.left, code + "0", codeMap);
        generateCodesRecursive(node.right, code + "1", codeMap);
    }

    public static String encodeText(String text, Map<Character, String> huffmanCodes) {
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (huffmanCodes.containsKey(c)) {
                encodedText.append(huffmanCodes.get(c));
            }
        }
        return encodedText.toString();
    }
}

// کلاس برای نمایش جدول تبدیل
class HuffmanTablePanel extends JPanel {
    private Map<Character, String> huffmanCodes;

    public HuffmanTablePanel(Map<Character, String> huffmanCodes) {
        this.huffmanCodes = huffmanCodes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        int y = 20;
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            g.drawString(entry.getKey() + ": " + entry.getValue(), 10, y);
            y += 20;
        }
    }
}

// کلاس برای رسم درخت هافمن
class HuffmanTreePanel extends JPanel {
    private HuffmanNode root;
    private Map<Character, String> huffmanCodes;

    public HuffmanTreePanel(HuffmanNode root, Map<Character, String> huffmanCodes) {
        this.root = root;
        this.huffmanCodes = huffmanCodes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.CYAN);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2)); // تنظیم ضخامت خطوط به 2 پیکسل

        if (root != null) {
            drawTree(g2d, root, getWidth() / 2, 30, getWidth() / 4);
            drawFrequencyAndCodes(g2d, root, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    private void drawFrequencyAndCodes(Graphics g, HuffmanNode node, int x, int y, int xOffset) {
        if (node != null) {
            if (node.left != null) {
                int xLeft = x - xOffset;
                int yLeft = y + 50;
                g.drawLine(x, y, xLeft, yLeft);
                drawFrequencyAndCodes(g, node.left, xLeft, yLeft, xOffset / 2);
            }
            if (node.right != null) {
                int xRight = x + xOffset;
                int yRight = y + 50;
                g.drawLine(x, y, xRight, yRight);
                drawFrequencyAndCodes(g, node.right, xRight, yRight, xOffset / 2);
            }

            // نمایش فراوانی و کد هافمن در زیر هر نود
            if (node.left == null && node.right == null) {
                g.drawString("Code: " + huffmanCodes.get(node.c), x - 15, y + 100);
                g.drawString("Freq: " + node.frequency, x - 15, y + 80);
            }
        }
    }

    private void drawTree(Graphics g, HuffmanNode node, int x, int y, int xOffset) {
        if (node != null) {
            g.drawOval(x - 15, y - 15, 30, 30);
            g.setColor(Color.white);
            g.fillOval(x - 15, y - 15, 30, 30);
            g.setColor(Color.black);
            g.drawString(String.valueOf(node.c), x - 5, y + 5);
            g.drawString(String.valueOf(node.frequency), x - 5, y + 20);

            // تنظیم فونت با اندازه بزرگتر
            Font originalFont = g.getFont();
            Font largeFont = originalFont.deriveFont(Font.ITALIC, 14); // ساخت یک فونت جدید با اندازه 14 پیکسل و روشنایی
                                                                       // باکس
            g.setFont(largeFont);
            g.setColor(Color.BLUE);

            if (node.left == null && node.right == null) {
                g.drawString(huffmanCodes.get(node.c), x - 15, y + 35);
            }
            if (node.left != null) {
                g.drawLine(x, y, x - xOffset, y + 50);
                drawTree(g, node.left, x - xOffset, y + 50, xOffset / 2);
            }
            if (node.right != null) {
                g.drawLine(x, y, x + xOffset, y + 50);
                drawTree(g, node.right, x + xOffset, y + 50, xOffset / 2);
            }
        }
    }
}

// کلاس برای نمایش متن کدگذاری شده
class EncodedTextPanel extends JPanel {
    private String encodedText;

    public EncodedTextPanel(String encodedText) {
        this.encodedText = encodedText;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Encoded Text:", 10, 20);
        g.drawString(encodedText, 10, 50);
    }
}

class FileSaver {
    public static void saveEncodedText(String encodedText, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(encodedText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveHuffmanCodes(Map<Character, String> huffmanCodes, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// کلاس اصلی برای اجرای برنامه
public class HuffmanCoding {
    public static void main(String[] args) {
        String text = FileUploader.uploadFile();

        if (text != null) {
            Map<Character, Integer> frequencyMap = TextAnalyzer.analyzeText(text);
            System.out.println("Character Frequencies:");
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                System.out.println("'" + entry.getKey() + "': " + entry.getValue());
            }

            HuffmanNode root = HuffmanTree.buildTree(frequencyMap);
            Map<Character, String> huffmanCodes = HuffmanCode.generateCodes(root);

            System.out.println("\nHuffman Codes:");
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                System.out.println("'" + entry.getKey() + "': " + entry.getValue());
            }

            String encodedText = HuffmanCode.encodeText(text, huffmanCodes);
            System.out.println("\nEncoded Text:");
            System.out.println(encodedText);

            // ذخیره متن کدگذاری شده و جدول تبدیل در فایل‌های متنی
            FileSaver.saveEncodedText(encodedText, "encoded_text.txt");
            FileSaver.saveHuffmanCodes(huffmanCodes, "huffman_codes.txt");

            JFrame frame = new JFrame("Huffman Coding");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1690, 900);

            // افزودن پنل‌های درخت، جدول تبدیل و متن کدگذاری شده
            JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

            upperSplitPane.setLeftComponent(new HuffmanTreePanel(root, huffmanCodes));
            upperSplitPane.setRightComponent(new HuffmanTablePanel(huffmanCodes));
            upperSplitPane.setDividerLocation(1400);

            mainSplitPane.setTopComponent(upperSplitPane);
            mainSplitPane.setBottomComponent(new EncodedTextPanel(encodedText));
            mainSplitPane.setDividerLocation(600);

            frame.add(mainSplitPane);
            frame.setVisible(true);
        }
    }
}