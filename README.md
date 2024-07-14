<h1>Huffman Encoding and Decoding in Java</h1>
<br/>
<p>This project implements Huffman Encoding and Decoding in Java, complete with a graphical user interface (GUI) to visualize the Huffman Tree, Huffman Codes, character frequencies, encoded text, and decoded text. The application also supports file upload and saving encoded and decoded text.</p>

<h3>Features</h3>
<ol>
        <li>File Upload: Upload a text file to be encoded.</li>
        <li>Character Frequency Calculation: Analyze the uploaded text to calculate the frequency of each character.</li>
        <li>Huffman Tree Construction: Build a Huffman Tree based on the character frequencies.</li>
        <li>Huffman Code Generation: Generate Huffman codes for each character from the Huffman Tree.</li>
        <li>Encoding: Encode the text using the generated Huffman codes.</li>
        <li>Decoding: Decode the encoded text back to its original form using the Huffman Tree.</li>
        <li>File Saving: Save the encoded text, Huffman codes, and decoded text to files.</li>
        <li>Graphical Display: Display the character frequencies, Huffman Tree, Huffman codes, encoded text, and decoded text in a GUI.</li>
    </ol>
    <h2>How It Works</h2>
    <ul>
        <li><strong>FileUploader</strong>: Provides functionality to select and upload a text file.</li>
        <li><strong>TextAnalyzer</strong>: Analyzes the uploaded text to calculate character frequencies.</li>
        <li><strong>HuffmanNode</strong>: Represents a node in the Huffman Tree.</li>
        <li><strong>HuffmanTreeBuilder</strong>: Builds the Huffman Tree from the character frequencies.</li>
        <li><strong>HuffmanCode</strong>: Generates Huffman codes from the Huffman Tree and encodes the text.</li>
        <li><strong>HuffmanDecoder</strong>: Decodes the encoded text back to the original text using the Huffman Tree.</li>
        <li><strong>FileSaver</strong>: Saves the encoded text, Huffman codes, and decoded text to files.</li>
        <li><strong>GUI Components</strong>: Various panels (HuffmanTreePanel, HuffmanTablePanel, EncodedTextPanel, FrequencyPanel, DecodedTextPanel) to display the data.</li>
    </ul>
      <h2>Getting Started</h2>
    <h3>Prerequisites</h3>
    <p>Java Development Kit (JDK) 8 or higher</p>
    <h3>Installing</h3>
    <p>Clone the repository:</p>
    <pre><code>git clone https://github.com/yourusername/huffman-encoding.git
cd huffman-encoding
</code></pre>
    <p>Open the project in your IDE and build it.</p>
     <h2>Usage</h2>
    <ol>
        <li>Run the <code>HuffmanEncodingApplication</code> main class.</li>
          <pre><code>Javac HuffmanEncodingApplication.java
Java HuffmanEncodingApplication</code>  </pre>
        <li>Upload a text file when prompted.</li>
        <li>The application will display the character frequencies, Huffman Tree, Huffman codes, encoded text, and decoded text in separate tabs.</li>
        <li>Save the encoded text, Huffman codes, and decoded text using the file save options.</li>
    </ol>
    <h2>Classes and Methods</h2>
    <h3>FileUploader</h3>
    <ul>
        <li><code>uploadFile()</code>: Opens a file chooser to select a text file.</li>
        <li><code>readFile(File file)</code>: Reads the contents of a file and returns it as a string.</li>
    </ul>
    <h3>TextAnalyzer</h3>
    <ul>
        <li><code>analyzeText(String text)</code>: Analyzes the text to calculate character frequencies.</li>
    </ul>
    <h3>HuffmanNode</h3>
    <p>Represents a node in the Huffman Tree.</p>
    <h3>HuffmanTreeBuilder</h3>
    <ul>
        <li><code>buildTree(Map&lt;Character, Integer&gt; frequencyMap)</code>: Builds the Huffman Tree from the character frequencies.</li>
    </ul>
    <h3>HuffmanCode</h3>
    <ul>
        <li><code>generateCodes(HuffmanNode root)</code>: Generates Huffman codes from the Huffman Tree.</li>
        <li><code>encodeText(String text, Map&lt;Character, String&gt; huffmanCodes)</code>: Encodes the text using the Huffman codes.</li>
    </ul>
    <h3>HuffmanDecoder</h3>
    <ul>
        <li><code>decodeText(String encodedText, HuffmanNode root)</code>: Decodes the encoded text back to the original text using the Huffman Tree.</li>
    </ul>
    <h3>FileSaver</h3>
    <ul>
        <li><code>saveEncodedText(String encodedText)</code>: Saves the encoded text to a file.</li>
        <li><code>saveHuffmanCodes(Map&lt;Character, String&gt; huffmanCodes)</code>: Saves the Huffman codes to a file.</li>
        <li><code>saveDecodedText(String decodedText)</code>: Saves the decoded text to a file.</li>
    </ul>
    <h3>GUI Components</h3>
    <ul>
        <li><code>FrequencyPanel</code>: Displays character frequencies.</li>
        <li><code>HuffmanTreePanel</code>: Displays the Huffman Tree.</li>
        <li><code>HuffmanTablePanel</code>: Displays Huffman codes.</li>
        <li><code>EncodedTextPanel</code>: Displays the encoded text.</li>
        <li><code>DecodedTextPanel</code>: Displays the decoded text.</li>
    </ul>
    <h2>Contributing</h2>
    <p>Contributions are welcome! If you have suggestions for improvements, please submit a pull request or open an issue.</p>
    <p>Made with ❤️ by <b>FARZINzx</b></p>

