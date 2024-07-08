import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.io.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import javax.swing.filechooser.FileNameExtensionFilter;

// کلاس برای آپلود فایل
class FileUploader {
    public static String uploadFile() {
        //
        JFileChooser fileChooser = new JFileChooser();
        //
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        //
        fileChooser.setDialogTitle("Select a text file");
        fileChooser.setApproveButtonText("Upload");
        fileChooser.setApproveButtonToolTipText("Click to upload the selected text file");

        // تنظیمات پیش‌فرض برای دایرکتوری
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Lenovo\\Desktop"));
        // حالت انتخاب فقط فایل‌ها
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //
        // تغییر رنگ پس‌زمینه و اجزای داخلی
        setFileChooserColors(fileChooser);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return readFile(selectedFile);
        }
        return null;
    }

    private static void setFileChooserColors(JFileChooser fileChooser) {
        // تغییر رنگ پس‌زمینه و اجزای داخلی

        // دستیابی به اجزای داخلی و تغییر رنگ آن‌ها
        setColorsRecursively(fileChooser, Color.LIGHT_GRAY, Color.WHITE);
    }

    private static void setColorsRecursively(Component component, Color bg, Color fg) {
        component.setBackground(bg);

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setColorsRecursively(child, bg, fg);
            }
        }
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
            if (c != '\r' && c !='\n') { // نادیده گرفتن کاراکترهای خط جدید
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

// کلاس اصلی برای اجرای برنامه
public class HuffmanCoding {
    public static void main(String[] args) {
        String text = FileUploader.uploadFile();

        if (text != null) {
            Map<Character, Integer> frequencyMap = TextAnalyzer.analyzeText(text);
            System.out.println(text);
            System.out.println("Character Frequencies:");
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                System.out.println("'" + entry.getKey() + "': " + entry.getValue());
            }
            // System.out.println(frequencyMap);
            // HuffmanNode root = HuffmanTree.buildTree(frequencyMap);
            // Map<Character, String> huffmanCodes = HuffmanCode.generateCodes(root);

            // System.out.println("Character Frequencies:");
            // for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            // System.out.println("'" + entry.getKey() + "': " + entry.getValue());
            // }

            // System.out.println("\nHuffman Codes:");
            // for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            // System.out.println("'" + entry.getKey() + "': " + entry.getValue());
            // }
        }
    }
}
