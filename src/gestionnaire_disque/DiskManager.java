package gestionnaire_disque;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class DiskManager {
	private String dmDir;
	private int pageSize;
	private RandomAccessFile file;
	
	void Init(String dmDir, int pageSize) {
		this.dmDir = dmDir;
		this.pageSize = pageSize;
		try {
			file = new RandomAccessFile(dmDir + "/data", "rw");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	void Save() {

	}
	void ReadPage(IPageID ipid, ByteBuffer buffer) {
		try {
			PageID id = (PageID) ipid;
			file.seek(id.pageID * this.pageSize);
			byte[] data = new byte[pageSize];
			file.readFully(data);
			buffer.clear();
			buffer.put(data);
			buffer.flip();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	void WritePage(IPageID ipid, ByteBuffer buffer) {
		try {
			PageID id = (PageID) ipid;
			file.seek(id.pageID * this.pageSize);
			byte[] data = new byte[pageSize];
			buffer.rewind();
			buffer.get(data);
			file.write(data);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	void DeallocPage(IPageID ipid) {
		
	}
}
