import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> wordMap = new HashMap<>();
    private File pdfsDir;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        this.pdfsDir = pdfsDir;

        for (File file : pdfsDir.listFiles()) {

            var doc = new PdfDocument(new PdfReader(file));
            int numberOfPages = doc.getNumberOfPages();

            for (int i = 1; i <= numberOfPages; i++) {

                PdfPage page = doc.getPage(i);
                String text = PdfTextExtractor.getTextFromPage(page);
                String[] words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(),
                            freqs.getOrDefault(word, 0) + 1);
                }

                for (Map.Entry<String, Integer> freq : freqs.entrySet()) {
                    List<PageEntry> pageEntryList = new ArrayList<>();
                    if (wordMap.containsKey(freq.getKey())) {
                        pageEntryList = wordMap.get(freq.getKey());
                    }
                    PageEntry pageEntry = new PageEntry(file.getName(), i, freq.getValue());
                    pageEntryList.add(pageEntry);
                    Collections.sort(pageEntryList);
                    wordMap.put(freq.getKey(), pageEntryList);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (wordMap.containsKey(word)) {
            return wordMap.get(word);
        }
        return Collections.emptyList();
    }
}


