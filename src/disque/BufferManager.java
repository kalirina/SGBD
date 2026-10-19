package disque;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.HashMap;

import disque.IBufferManager.ReplacementPolicy;

public class BufferManager implements IBufferManager {
	private int pageSize;
	private int bufferCount;
	private ReplacementPolicy policy;
	private IDiskManager diskManager;
	
	// metadata pour chaque frame/buffer
	// all arrays have buffercount elements
	private ByteBuffer[] buffers;
	private IPageId[] pageIds;
	private int[] pinCounts;
	private int[] dirtyFlags;
	private int[] lastAccess;
	
	private Map<IPageId, Integer> pageTable;
	
	public void init(
            int pageSize,
            int bufferCount,
            ReplacementPolicy policy,
            IDiskManager diskManager
    ) throws DBMSException {
		
		if (pageSize <= 0 || bufferCount <= 0 || policy == null || diskManager == null)
			throw new DBMSException("Invalid BufferManager configuration");
		
		this.pageSize = pageSize;
		this.bufferCount = bufferCount;
		this.policy = policy;
		this.diskManager = diskManager;
		
		this.buffers = new ByteBuffer[bufferCount];
		for (int i = 0; i < bufferCount; i++)
			buffers[i] = ByteBuffer.allocate(pageSize);
		
		this.pageIds = new IPageId[bufferCount];
		this.pinCounts = new int[bufferCount];
		this.dirtyFlags = new int[bufferCount];
		this.lastAccess = new int[bufferCount];
		
		this.pageTable = new HashMap<>();
	}
}
