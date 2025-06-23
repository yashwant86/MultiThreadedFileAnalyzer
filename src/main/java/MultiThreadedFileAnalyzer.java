
import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class MultiThreadedFileAnalyzer {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Input directory path
		System.out.println("Enter the directory path:");
		String directoryPath = scanner.nextLine();

		// Input keyword to search
		System.out.println("Enter the keyword to search:");
		String keyword = scanner.nextLine();

		File folder = new File(directoryPath);
		if (!folder.exists() || !folder.isDirectory()) {
			System.out.println("Invalid directory path.");
			return;
		}

		File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
		if (files == null || files.length == 0) {
			System.out.println("No text files found in the directory.");
			return;
		}

		ExecutorService executorService = Executors.newFixedThreadPool(files.length);
		List<Future<FileAnalysisResult>> results = new ArrayList<>();

		for (File file : files) {
			results.add(executorService.submit(() -> analyzeFile(file, keyword)));
		}

		executorService.shutdown();

		// Aggregating results
		System.out.println("\nAnalysis Results:");
		for (Future<FileAnalysisResult> result : results) {
			try {
				FileAnalysisResult analysis = result.get();
				System.out.println("File: " + analysis.getFileName());
				System.out.println("Word Count: " + analysis.getWordCount());
				System.out.println("Keyword Occurrences: " + analysis.getKeywordOccurrences());
				System.out.println("Longest Word: " + analysis.getLongestWord());
				System.out.println("---------------------------------");
			} catch (Exception e) {
				System.out.println("Error processing file: " + e.getMessage());
			}
		}
	}

	public static FileAnalysisResult analyzeFile(File file, String keyword) throws IOException {
		int wordCount = 0;
		int keywordOccurrences = 0;
		String longestWord = "";

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] words = line.split("\\s+");
				wordCount += words.length;

				for (String word : words) {
					if (word.equalsIgnoreCase(keyword)) {
						keywordOccurrences++;
					}
					if (word.length() > longestWord.length()) {
						longestWord = word;
					}
				}
			}
		}

		return new FileAnalysisResult(file.getName(), wordCount, keywordOccurrences, longestWord);
	}
}

