// Helper class to store analysis results

public class FileAnalysisResult {
	private final String fileName;
	private final int wordCount;
	private final int keywordOccurrences;
	private final String longestWord;

	public FileAnalysisResult(String fileName, int wordCount, int keywordOccurrences, String longestWord) {
		this.fileName = fileName;
		this.wordCount = wordCount;
		this.keywordOccurrences = keywordOccurrences;
		this.longestWord = longestWord;
	}

	public String getFileName() {
		return fileName;
	}

	public int getWordCount() {
		return wordCount;
	}

	public int getKeywordOccurrences() {
		return keywordOccurrences;
	}

	public String getLongestWord() {
		return longestWord;
	}
}
