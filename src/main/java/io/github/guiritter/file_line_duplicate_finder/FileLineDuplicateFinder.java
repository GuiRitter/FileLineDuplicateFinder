package io.github.guiritter.file_line_duplicate_finder;

import static java.lang.System.out;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.FILES_ONLY;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

public final class FileLineDuplicateFinder {

	private static File inputFile;

	private static Map<String, Long> map = new HashMap<>();

	private static boolean byMoreThan1(Map.Entry<String, Long> e) 
	{
		return e.getValue() > 1;
	}

	private static void treatEntry(Map.Entry<String, Long> entry) 
	{
		if (entry.getValue() > 1) {
			out.println(entry.getValue() + "×\n" + entry.getKey() + "\n");
		}
	}

	private static final void treatLine(String line) {
		var count = map.get(line);

		if (count == null) {
			count = 1L;
		} else {
			count++;
		}

		map.put(line, count);
	}

	public static void main(String args[]) throws IOException {
		if (args.length > 0) {
			inputFile = new File(args[0]);
		} else {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(FILES_ONLY);
			chooser.setDialogTitle("Choose the text file containing lines");
			if (chooser.showOpenDialog(null) != APPROVE_OPTION) {
				return;
			}
			inputFile = chooser.getSelectedFile();
			if (inputFile == null) {
				return;
			}
		}

		Files.readAllLines(inputFile.toPath(), StandardCharsets.ISO_8859_1).stream()
				.forEach(FileLineDuplicateFinder::treatLine);

		map.entrySet().stream().filter(FileLineDuplicateFinder::byMoreThan1).forEach(FileLineDuplicateFinder::treatEntry);
	}
}
