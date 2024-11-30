Description
MultiThreadedFileAnalyzer is a Java-based tool designed to analyze multiple text files concurrently using multithreading. It demonstrates efficient file processing by leveraging parallelism and thread-safe operations. The tool calculates key metrics for each file, such as:

Total word count
Number of occurrences of a user-specified keyword
Longest word in the file
This project showcases skills in:

Multithreading with ExecutorService
File I/O using BufferedReader
Concurrent programming and result aggregation using Future
Features
Processes multiple files in parallel for faster analysis.
Reads and analyzes text files line by line to handle large files efficiently.
User-friendly interface: input the folder path and the keyword to search.
Modular design for easy extension and reuse.
Usage
Specify a directory containing .txt files.
Enter the keyword to analyze.
View the analysis results for each file, including word count, keyword occurrences, and the longest word.
