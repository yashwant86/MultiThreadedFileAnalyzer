import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

public class MultiThreadedFileAnalyzerConcurrencyTest {

	@Test
	void testConcurrentExecution() throws Exception {
		Path tempDir = Files.createTempDirectory("mtfaDir");
		tempDir.toFile().deleteOnExit();
		int fileCount = 5;
		String keyword = "keyword";
		List<File> files = new ArrayList<>();

		for (int i = 0; i < fileCount; i++) {
			File f = tempDir.resolve("file" + i + ".txt").toFile();
			f.deleteOnExit();
			try (FileWriter writer = new FileWriter(f)) {
				writer.write("hello world " + keyword + " test " + keyword + " hello");
			}
			files.add(f);
		}

		ExecutorService executor = Executors.newFixedThreadPool(fileCount);
		CountDownLatch ready = new CountDownLatch(fileCount);
		CountDownLatch start = new CountDownLatch(1);
		List<Long> startTimes = Collections.synchronizedList(new ArrayList<>());
		List<String> threadNames = Collections.synchronizedList(new ArrayList<>());
		List<Future<FileAnalysisResult>> futures = new ArrayList<>();

		for (File f : files) {
			futures.add(executor.submit(() -> {
				ready.countDown();
				start.await();
				startTimes.add(System.nanoTime());
				threadNames.add(Thread.currentThread().getName());
				return MultiThreadedFileAnalyzer.analyzeFile(f, keyword);
			}));
		}

		ready.await();
		start.countDown();

		List<FileAnalysisResult> results = new ArrayList<>();
		for (Future<FileAnalysisResult> future : futures) {
			results.add(future.get(5, TimeUnit.SECONDS));
		}
		executor.shutdown();

		long earliest = Collections.min(startTimes);
		long latest = Collections.max(startTimes);
		assertTrue(latest - earliest < TimeUnit.MILLISECONDS.toNanos(100), "Tasks did not start in parallel");
		assertTrue(threadNames.stream().distinct().count() > 1, "Tasks ran on a single thread");

		assertEquals(fileCount, results.size());
		for (int i = 0; i < fileCount; i++) {
			FileAnalysisResult res = results.get(i);
			assertEquals(files.get(i).getName(), res.getFileName());
			assertEquals(6, res.getWordCount());
			assertEquals(2, res.getKeywordOccurrences());
			assertEquals(keyword, res.getLongestWord());
		}
	}
}