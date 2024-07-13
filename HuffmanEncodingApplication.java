//Farzin Hamzehi 40117023172
//import JAVAX.SWING.LIBRARIES
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
//import JAVA.AWT.LIBRARIES
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
//import JAVA.IO.LIBRARIES
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
//import JAVA.UTIL.LIBRARIES
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// Class that provides functionality for selecting and uploading a text file
class FileUploader {
    // Method to upload a file using a file chooser
    public static String uploadFile() {
        try {
            // Set LookAndFeel to Nimbus to allow some appearance settings
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Change color and appearance settings
        UIManager.put("FileChooser.background", new Color(60, 63, 65));
        UIManager.put("FileChooser.foreground", new Color(187, 187, 187));
        UIManager.put("FileChooser.panelBackground", new Color(60, 63, 65));
        UIManager.put("FileChooser.panelForeground", new Color(187, 187, 187));
        UIManager.put("FileChooser.buttonBackground", new Color(75, 110, 175));
        UIManager.put("FileChooser.buttonForeground", new Color(255, 255, 255));
        // Create a file chooser with custom filter
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Select text file!");
        fileChooser.setApproveButtonText("Submit");
        fileChooser.setApproveButtonToolTipText("Click to Submit the selected text file");

        // Setting Default repository
        fileChooser.setCurrentDirectory(new File("E:\\Programming\\HuffmanEncoding"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return readFile(selectedFile);
        }
        return null;
    }

    // Method for reading the contents of a file and returning it as a String
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

// Class for analyzing text to calculate character frequencies
class TextAnalyzer {
    public static Map<Character, Integer> analyzeText(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            // Ignore carriage return and newline characters
            if (c != '\r' && c != '\n') {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
        }
        return frequencyMap;
    }
}

// Class for represente a node in the Huffman tree
class HuffmanNode implements Comparable<HuffmanNode> {
    char c;
    int frequency;
    HuffmanNode left, right;

    HuffmanNode(char c, int frequency) {
        this.c = c;
        this.frequency = frequency;
    }

    //// ??//
    @Override
    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}

// Class for building the Huffman tree by HuffmanNode
class HuffmanTreeBuilder {
    public static HuffmanNode buildTree(Map<Character, Integer> frequencyMap) {
        // Create a priority queue(Min-heap) to hold the nodes, sorted by frequency
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        // Add each character and its frequency to the queue as a nod
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // Build the huffman tree , Combime two nodes with the lowest frequency
        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            // Create a new parent of two parent
            HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            queue.add(parent);
        }
        // The last remaining node is the root of the Huffman tree
        return queue.poll();
    }
}

// Class for generating Huffman codes
class HuffmanCode {
    public static Map<Character, String> generateCodes(HuffmanNode root) {
        Map<Character, String> codeMap = new HashMap<>();
        // Recursively generate the codes and store them in the map
        generateCodesRecursive(root, "", codeMap);
        return codeMap;
    }

    // Recursive method to generate Huffman codes
    private static void generateCodesRecursive(HuffmanNode node, String code, Map<Character, String> codeMap) {
        if (node == null) {
            return;
        }
        // if the node is leaf
        if (node.left == null && node.right == null) {
            codeMap.put(node.c, code);
        }
        // Recursively generate codes for the left and right children
        generateCodesRecursive(node.left, code + "0", codeMap);
        generateCodesRecursive(node.right, code + "1", codeMap);
    }

    // Method to encode the text using the generated Huffman codes
    public static String encodeText(String text, Map<Character, String> huffmanCodes) {
        StringBuilder encodedText = new StringBuilder();
        // Iterate through each character in the text and add its Huffman code to the
        // result
        for (char c : text.toCharArray()) {
            if (huffmanCodes.containsKey(c)) {
                encodedText.append(huffmanCodes.get(c));
            }
        }
        return encodedText.toString();
    }
}

// Class for displaying the Huffman codes in a table format
class HuffmanTablePanel extends JPanel {
    private Map<Character, String> huffmanCodes;

    public HuffmanTablePanel(Map<Character, String> huffmanCodes) {
        this.huffmanCodes = huffmanCodes;
    }

    // Override paintComponent
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

// Class for drawing the Huffman tree
class HuffmanTreePanel extends JPanel {
    private HuffmanNode root;
    private Map<Character, String> huffmanCodes;

    public HuffmanTreePanel(HuffmanNode root, Map<Character, String> huffmanCodes) {
        this.root = root;
        this.huffmanCodes = huffmanCodes;
    }

    // Override paintComponent
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.CYAN);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        if (root != null) {
            // Draw the Huffman tree starting from the root
            drawTree(g2d, root, getWidth() / 2, 30, getWidth() / 4);
            // Draw the frequencies and Huffman codes
            drawFrequencyAndCodes(g2d, root, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    // Method to draw the frequencies and Huffman codes for each node
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

            if (node.left == null && node.right == null) {
                g.drawString("Code: " + huffmanCodes.get(node.c), x - 15, y + 100);
                g.drawString("Freq: " + node.frequency, x - 15, y + 80);
            }
        }
    }

    // Method to draw the Huffman tree
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
            // Draw the left child
            if (node.left != null) {
                g.drawLine(x, y, x - xOffset, y + 50);
                drawTree(g, node.left, x - xOffset, y + 50, xOffset / 2);
            }
            // Draw the right child
            if (node.right != null) {
                g.drawLine(x, y, x + xOffset, y + 50);
                drawTree(g, node.right, x + xOffset, y + 50, xOffset / 2);
            }
        }
    }
}

// Class for drawing the Encoded Text
class EncodedTextPanel extends JPanel {
    @SuppressWarnings("unused")
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

// Class for drawing the Frequency
class FrequencyPanel extends JPanel {
    Map<Character, Integer> frequencyMap;

    public FrequencyPanel(Map<Character, Integer> frequencyMap) {
        this.frequencyMap = frequencyMap;
        setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            sb.append("'").append(entry.getKey()).append("': ").append(entry.getValue()).append("\n");
        }
        textArea.setText(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}

// Class for drawing the Decoded Text
class DecodedTextPanel extends JPanel {
    @SuppressWarnings("unused")
    private String decodedText;

    public DecodedTextPanel(String decodedText) {
        this.decodedText = decodedText;
        setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setText(decodedText);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}

// class for save file in custom location
class FileSaver {
    // method that save Encoded text in custom location
    public static void saveEncodedText(String encodedText) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Encoded Text");
        fileChooser.setCurrentDirectory(new File("E:\\Programming\\HuffmanEncoding"));
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

    // method that save Encoded text in custom location
    public static void saveHuffmanCodes(Map<Character, String> huffmanCodes) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Huffman Codes");
        fileChooser.setCurrentDirectory(new File("E:\\Programming\\HuffmanEncoding"));
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

    // method that save Encoded text in custom location
    public static void saveDecodedText(String decodedText) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Decoded Text");
        fileChooser.setCurrentDirectory(new File("E:\\Programming\\HuffmanEncoding"));
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

// class for decoding Huffman encoded Text
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

// Main class for the Huffman Encoding application
public class HuffmanEncodingApplication {
    public static void main(String[] args) {
        // Upload the file and read its contents
        String text = FileUploader.uploadFile();

        if (text != null) {
            // Analyze the text to calculate character frequencies
            Map<Character, Integer> frequencyMap = TextAnalyzer.analyzeText(text);

            // Build the Huffman tree from the frequency map
            HuffmanNode root = HuffmanTreeBuilder.buildTree(frequencyMap);

            // Generate Huffman codes from the Huffman tree
            Map<Character, String> huffmanCodes = HuffmanCode.generateCodes(root);

            // Generate EncodedText and DecodedText
            String encodedText = HuffmanCode.encodeText(text, huffmanCodes);
            String decodedText = HuffmanDecoder.decodeText(encodedText, root);

            // Save EncodedText & HuffmanCode & DeacodedText in custom location
            FileSaver.saveEncodedText(encodedText);
            FileSaver.saveHuffmanCodes(huffmanCodes);
            FileSaver.saveDecodedText(decodedText);

            // Create a Frame to show Data and Set size to it
            JFrame frame = new JFrame("Huffman Coding");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set frame to full screen

            // create a JTabbedPane for set layout for the frame
            JTabbedPane tabbedPane = new JTabbedPane();

            // Add FrequencyPanel to JTabbedPane
            tabbedPane.addTab("Character Frequencies", new FrequencyPanel(frequencyMap));

            // Add HuffmanTreePanel to JTabbedPane
            tabbedPane.addTab("Huffman Tree", new HuffmanTreePanel(root, huffmanCodes));

            // Add HuffmanTablePanel to JTabbedPane
            tabbedPane.addTab("Huffman Codes", new HuffmanTablePanel(huffmanCodes));

            // Add EncodedTextPanel to JTabbedPane
            tabbedPane.addTab("Encoded Text", new EncodedTextPanel(encodedText));

            // Add DecodedTextPanel to JTabbedPane
            tabbedPane.addTab("Decoded Text", new DecodedTextPanel(decodedText));

            // Add JTabbedPane to Frame
            frame.add(tabbedPane);
            frame.setVisible(true);
        }
    }
}
