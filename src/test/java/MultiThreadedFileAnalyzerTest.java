import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MultiThreadedFileAnalyzerTest {

	@Test
	void testAnalyzeFile_withContent() throws IOException {
		File tempFile = File.createTempFile("testFile", ".txt");
		tempFile.deleteOnExit();

		try (FileWriter writer = new FileWriter(tempFile)) {
			writer.write("hello world keyword test keyword hello");
		}

		FileAnalysisResult result = MultiThreadedFileAnalyzer.analyzeFile(tempFile, "keyword");

		assertEquals(tempFile.getName(), result.getFileName());
		assertEquals(6, result.getWordCount());
		assertEquals(2, result.getKeywordOccurrences());
		assertEquals("keyword", result.getLongestWord());
	}

	@Test
	void testAnalyzeFile_emptyFile() throws IOException {
		File emptyFile = File.createTempFile("emptyFile", ".txt");
		emptyFile.deleteOnExit();

		FileAnalysisResult result = MultiThreadedFileAnalyzer.analyzeFile(emptyFile, "anything");

		assertEquals(emptyFile.getName(), result.getFileName());
		assertEquals(0, result.getWordCount());
		assertEquals(0, result.getKeywordOccurrences());
		assertEquals("", result.getLongestWord());
	}

	@Test
	void testAnalyzeFile_punctuationAndSpaces() throws IOException {
		File tempFile = File.createTempFile("punctuation", ".txt");
		tempFile.deleteOnExit();

		try (FileWriter writer = new FileWriter(tempFile)) {
			writer.write("hello,  world!!   keyword keyword!  ");
		}

		FileAnalysisResult result = MultiThreadedFileAnalyzer.analyzeFile(tempFile, "keyword");

		assertEquals(4, result.getWordCount());
		assertEquals(1, result.getKeywordOccurrences());
		assertEquals("keyword!", result.getLongestWord());
	}

	@Test
	void testAnalyzeFile_caseInsensitiveKeyword() throws IOException {
		File tempFile = File.createTempFile("caseInsensitive", ".txt");
		tempFile.deleteOnExit();

		try (FileWriter writer = new FileWriter(tempFile)) {
			writer.write("Keyword keyword KEYWORD Keyword");
		}

		FileAnalysisResult result = MultiThreadedFileAnalyzer.analyzeFile(tempFile, "keyword");

		assertEquals(4, result.getWordCount());
		assertEquals(4, result.getKeywordOccurrences());
		assertEquals("Keyword", result.getLongestWord());
	}

	@Test
	void testAnalyzeFile_leadingTrailingWhitespace() throws IOException {
		File tempFile = File.createTempFile("whitespace", ".txt");
		tempFile.deleteOnExit();

		try (FileWriter writer = new FileWriter(tempFile)) {
			writer.write("   keyword  test \n  another  line   ");
		}

		FileAnalysisResult result = MultiThreadedFileAnalyzer.analyzeFile(tempFile, "keyword");

		assertEquals(4, result.getWordCount());
		assertEquals(1, result.getKeywordOccurrences());
		assertEquals("keyword", result.getLongestWord());
	}
}
