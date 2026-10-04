package disque;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;

public class DiskManager {
	private String dmDir;
	private int pageSize;
	private RandomAccessFile file;
	private ArrayList<Integer> freePages;
	
	void Init(String dmDir, int pageSize) {
		this.dmDir = dmDir;
		this.pageSize = pageSize;
		try {
			file = new RandomAccessFile(dmDir + "/data", "rw");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		freePages = new ArrayList<>();
		File f = new File(dmDir + "/freePages.txt");
		if (f.exists()) {
			try {
				Scanner sc = new Scanner(f);
				while (sc.hasNextInt())
					freePages.add(sc.nextInt());
				sc.close();
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	void Save() {
		// sauvegarder les infos sur freePages pour des prochaines utilisations
		try {
			FileWriter wr = new FileWriter(dmDir + "/freePages.txt");
			for (Integer page : freePages)
				wr.write(page + "\n");
			wr.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	IPageId AllocPage() {
		try {
			int page;
			// S'il existe une page libre, on la réutilise
			if (!freePages.isEmpty())
				page = freePages.remove(0); // la première page
			// Sinon, on crée une nouvelle pqge à la fin fu fichier
			else {
				page = (int)(file.length() / pageSize);
				file.setLength(file.length() + pageSize); // on aggrandit le fichier
			}
			return new PageId(page);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	void ReadPage(IPageId ipid, ByteBuffer buffer) {
		try {
			// verifier le cas si page n'existe pas
			PageId id = (PageId) ipid;
			file.seek(id.pageId * this.pageSize);
			
			byte[] data = new byte[pageSize];
			file.readFully(data);
			
			buffer.clear();
			buffer.put(data);
			buffer.flip();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	void WritePage(IPageId ipid, ByteBuffer buffer) {
		try {
			PageId id = (PageId) ipid;
			file.seek(id.pageId * this.pageSize);
			
			byte[] data = new byte[pageSize];
			buffer.rewind();
			buffer.get(data);
			
			file.write(data);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	void DeallocPage(IPageId ipid) {
		PageId id = (PageId) ipid;
		if (!freePages.contains(id.pageId))
			freePages.add(id.pageId);
	}
}
