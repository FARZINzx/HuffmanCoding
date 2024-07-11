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

            Font originalFont = g.getFont();
            Font largeFont = originalFont.deriveFont(Font.ITALIC, 14);
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
        setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setText(encodedText);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}

class FrequencyPanel extends JPanel {
    Map<Character, Integer> frequencyMap;

    public FrequencyPanel(Map<Character, Integer> frequencyMap) {
        this.frequencyMap = frequencyMap;
        setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            sb.append("'").append(entry.getKey()).append("': ").append(entry.getValue()).append("\n");
            ;
        }
        textArea.setText(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}

class FileSaver {
    public static void saveEncodedText(String encodedText) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Encoded Text");
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Lenovo\\Desktop"));
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(encodedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveHuffmanCodes(Map<Character, String> huffmanCodes) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Huffman Codes");
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Lenovo\\Desktop"));
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                    writer.write(entry.getKey() + ": " + entry.getValue());
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveDecodedText(String decodedText) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Decoded Text");
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Lenovo\\Desktop"));
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(decodedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

// کلاس برای کدگشایی متن
class HuffmanDecoder {
    public static String decodeText(String encodedText, HuffmanNode root) {
        StringBuilder decodedText = new StringBuilder();
        HuffmanNode currentNode = root;
        for (char bit : encodedText.toCharArray()) {
            if (bit == '0') {
                currentNode = currentNode.left;
            } else if (bit == '1') {
                currentNode = currentNode.right;
            }

            if (currentNode.left == null && currentNode.right == null) {
                decodedText.append(currentNode.c);
                currentNode = root;
            }
        }
        return decodedText.toString();
    }
}

class DecodedTextPanel extends JPanel {
    private String decodedText;

    public DecodedTextPanel(String decodedText) {
        this.decodedText = decodedText;
        setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setText(decodedText);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}

// کلاس اصلی برای اجرای برنامه
public class HuffmanCoding {
    public static void main(String[] args) {
        String text = FileUploader.uploadFile();

        if (text != null) {
            Map<Character, Integer> frequencyMap = TextAnalyzer.analyzeText(text);
            HuffmanNode root = HuffmanTree.buildTree(frequencyMap);
            Map<Character, String> huffmanCodes = HuffmanCode.generateCodes(root);
            String encodedText = HuffmanCode.encodeText(text, huffmanCodes);
            String decodedText = HuffmanDecoder.decodeText(encodedText, root);

            // ذخیره متن کدگذاری شده و جدول تبدیل در فایل‌های متنی
            FileSaver.saveEncodedText(encodedText);
            FileSaver.saveHuffmanCodes(huffmanCodes);
            FileSaver.saveDecodedText(decodedText);

            JFrame frame = new JFrame("Huffman Coding");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1600, 900);

            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("Character Frequencies", new FrequencyPanel(frequencyMap));
            tabbedPane.addTab("Huffman Tree", new HuffmanTreePanel(root, huffmanCodes));
            tabbedPane.addTab("Huffman Codes", new HuffmanTablePanel(huffmanCodes));
            tabbedPane.addTab("Encoded Text", new EncodedTextPanel(encodedText));
            tabbedPane.addTab("Decoded Text", new DecodedTextPanel(decodedText));

            frame.add(tabbedPane);
            frame.setVisible(true);
        }
    }
}
