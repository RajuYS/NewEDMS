package com.lmh;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmh.postpaid.PostpaidIndexFileConversion;
import com.lmh.prepaid.PrepaidInputFile;
import com.lmh.util.Utilities;

/**
 *
 * @author RajuY
 */
public class PreProcess {
	private static final Logger LOGGER = LoggerFactory.getLogger(PreProcess.class);

	private final WatchService watcher;

	private final Map<WatchKey, Path> keys;

	/**
	 * Creates a WatchService and registers the given directory
	 */
	PreProcess(Path dir) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();

		walkAndRegisterDirectories(dir);
	}

	/**
	 * Register the given directory with the WatchService; This function will be
	 * called by FileVisitor
	 */
	private void registerDirectory(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private void walkAndRegisterDirectories(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerDirectory(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Process all events for keys queued to the watcher
	 * 
	 * @throws IOException
	 */
	void processEvents() throws IOException {
		for (;;) {

			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				LOGGER.info("WatchKey not recognized!!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				@SuppressWarnings("rawtypes")
				WatchEvent.Kind kind = event.kind();

				// Context for directory entry event is the file name of entry
				@SuppressWarnings("unchecked")
				Path name = ((WatchEvent<Path>) event).context();
				 if (name == null) {
	                    break;
	                }
				Path child = dir.resolve(name);

				if (event.kind().name().equals("ENTRY_CREATE")) {
					if (dir.getName(1).toString().equals("postpaid") && dir.getName(3).toString().equals("Index")) {
						PostpaidIndexFileConversion pfc = new PostpaidIndexFileConversion();
						pfc.postpaid();
						break;
					} else if (dir.getName(1).toString().equals("prepaid")
							&& dir.getName(3).toString().equals("feed")) {
						PrepaidInputFile pif = new PrepaidInputFile();
						pif.prepaid();
						break;
					}
				}

				// if directory is created, and watching recursively, then register it and its
				// sub-directories
				if (kind == ENTRY_CREATE) {
					try {
						if (Files.isDirectory(child)) {
							walkAndRegisterDirectories(child);
						}
					} catch (IOException x) {
						LOGGER.error("ERROR|{}",x);
					}
				}
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}

	public void main() throws IOException {
		String watchDirectoryPath = Utilities.getProperty("watch.dir.path");
		LOGGER.info("IngestionModule|Application watch directory path is:{}", watchDirectoryPath);
		Path dir = Paths.get(watchDirectoryPath);
		new PreProcess(dir).processEvents();
	}
}