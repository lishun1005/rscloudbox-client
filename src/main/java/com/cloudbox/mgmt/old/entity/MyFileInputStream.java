package com.cloudbox.mgmt.old.entity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class MyFileInputStream extends FileInputStream {

	private long readbytes;
	private long allbytes;

	public MyFileInputStream(File file) throws IOException {
		super(file);
		allbytes = this.getChannel().size();
	}

	public MyFileInputStream(FileDescriptor fdObj) throws IOException {
		super(fdObj);
		allbytes = this.getChannel().size();
	}

	public MyFileInputStream(String name) throws IOException {
		super(name);
		allbytes = this.getChannel().size();
	}

	@Override
	public int read() throws IOException {
		readbytes += this.getChannel().size();
		return super.read();
	}

	@Override
	public int read(byte b[]) throws IOException {
		readbytes += b.length;
		return super.read(b);
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		// System.out.println(this.getChannel().size());
		readbytes += b.length;
		return super.read(b, off, len);
	}

	public synchronized long getReadbytes() {
		return readbytes;
	}

	public synchronized long getAllbytes() {
		return allbytes;
	}
	
}
