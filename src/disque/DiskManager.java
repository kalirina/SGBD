package disque;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class DiskManager implements IDiskManager {
	private String dmDir;
	private int pageSize;
	private int pageCount;
	private RandomAccessFile file;
	private List<Integer> freePages = new ArrayList<>();
	private boolean initActivated = false;
	
	public DiskManager(){
		
	}
	
	public void Init(String dmDir, int pageSize) throws DiskManagerException {
		
		if (initActivated) {
			throw new DiskManagerException("Init a déjà été appelé sur ce DiskManager");
		}
		
		if (pageSize <= 0) {
	        throw new DiskManagerException("pageSize invalide : doit être strictement positif (reçu " + pageSize + ")");
	    }
		
		this.dmDir = dmDir;
		
		File metaFile = new File(dmDir + "/metadata.bin");
		
		if(metaFile.exists()) {
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(metaFile));
				int savedPageSize = in.readInt();
				
				if(savedPageSize != pageSize) {
					in.close();
					throw new DiskManagerException("page incompatible: attendu " + savedPageSize);
				}
				
				this.pageSize = savedPageSize;
				this.pageCount = in.readInt();
				
				int freeCount = in.readInt();
				for(int i=0; i<freeCount; i++) {
					freePages.add(in.readInt());
				}
				in.close();
			}
			catch(IOException e) {
				throw new DiskManagerException(e);
			}	
			
		} else {
			this.pageSize = pageSize;
			this.pageCount = 0;
		}
		
		try {
			file = new RandomAccessFile(dmDir + "/data", "rw");
		}
		catch(IOException e) {
			throw new DiskManagerException(e);
		}
		initActivated = true;
	}
	
	public void Save() throws DiskManagerException { // sauvegarder les infos sur metadata.bin pour des prochaines utilisations
		this.CheckInitStatus();
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(dmDir + "/metadata.bin"));
			out.writeInt(pageSize);
			out.writeInt(pageCount);
			out.writeInt(freePages.size());
			for (Integer page : freePages)
				out.writeInt(page);
			out.close();
		}
		catch (IOException e) {
			throw new DiskManagerException(e);
		}
	}
	public IPageId AllocPage() throws DiskManagerException {
		this.CheckInitStatus();
		try {
			int page;
			// S'il existe une page libre, on la réutilise
			if (!freePages.isEmpty())
				page = freePages.remove(0); // la première page
			// Sinon, on crée une nouvelle pqge à la fin fu fichier
			else {
				page = pageCount;
				pageCount++;
				file.setLength((long) pageCount * pageSize); // on aggrandit le fichier
			}
			return new PageId(page);
		}
		catch (IOException e) {
			throw new DiskManagerException(e);
		}
	}
	public void ReadPage(IPageId ipid, ByteBuffer buffer) throws DiskManagerException { //bytebuffer needs exception buffer == null(null pointer exc.) and buffer.capacity() != pageSize (BufferPvergowException)
		this.CheckInitStatus();
		this.CheckBuffer(buffer);
		
		PageId id = (PageId) ipid;
		this.CheckPageAllocated(id.pageId);
		
		try {
			// verifier le cas si page n'existe pas
			file.seek((long)id.pageId * this.pageSize);
			
			byte[] data = new byte[pageSize];
			file.readFully(data);
			
			buffer.clear();
			buffer.put(data);
			buffer.flip();
		}
		catch (IOException e) {
			throw new DiskManagerException(e);
		}
	}
	public void WritePage(IPageId ipid, ByteBuffer buffer) throws DiskManagerException { //bytebuffer needs exception
		this.CheckInitStatus();
		this.CheckBuffer(buffer);
		
		PageId id = (PageId) ipid;
		this.CheckPageAllocated(id.pageId);
		
		try {
			file.seek((long)id.pageId * this.pageSize);
			
			byte[] data = new byte[pageSize];
			buffer.rewind();
			buffer.get(data);
			file.write(data);
		}
		catch (IOException e) {
			throw new DiskManagerException(e);
		}
	}
	public void DeallocPage(IPageId ipid) throws DiskManagerException { // second dealloc exception 
		this.CheckInitStatus();
		
		PageId id = (PageId) ipid;
		this.CheckPageAllocated(id.pageId);
		
		freePages.add(id.pageId);
	}
	
	public int GetActivePageCount(){ // nombre des pages déjà allouées 
		return this.pageCount - this.freePages.size();
	}
	
	public int GetPageSize() {
		return this.pageSize;
	}
	
	private void CheckInitStatus() throws DiskManagerException {
		if (!initActivated) {
			throw new DiskManagerException("Init n'a pas encore été appelé !");
		}
	}
	
	private void CheckBuffer(ByteBuffer buffer) throws DiskManagerException{
		if (buffer == null) {
			throw new DiskManagerException("Le buffer ne peut pas être null !");
		}
		if (buffer.capacity() != pageSize) {
			throw new DiskManagerException("Le buffer doit être de même taille que page size ! Attendu : " + pageSize + ", reçu : " + buffer.capacity());
		}
	}
	
	private void CheckPageAllocated(int pageId) throws DiskManagerException {
		if (freePages.contains(pageId)) {
			throw new DiskManagerException("Page désalloué ou inexistante : " + pageId);
		}
	}
}
