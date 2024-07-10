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
        if (root != null) {
            drawTree(g, root, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    private void drawTree(Graphics g, HuffmanNode node, int x, int y, int xOffset) {
        if (node != null) {
            g.drawOval(x - 15, y - 15, 30, 30);
            g.drawString(String.valueOf(node.c), x - 5, y + 5);
            g.drawString(String.valueOf(node.frequency), x - 5, y + 20);
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

            JFrame frame = new JFrame("Huffman Tree");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new HuffmanTreePanel(root, huffmanCodes));
            frame.setVisible(true);
        }
    }
}